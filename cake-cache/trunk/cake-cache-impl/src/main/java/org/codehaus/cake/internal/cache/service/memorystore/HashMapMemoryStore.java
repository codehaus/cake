/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.internal.cache.service.memorystore;

import static org.codehaus.cake.cache.CacheEntry.SIZE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.Caches;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.cache.service.attribute.CacheAttributeConfiguration;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.forkjoin.collections.ParallelArray;
import org.codehaus.cake.internal.cache.CachePredicates;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;
import org.codehaus.cake.internal.cache.processor.request.Trimable;
import org.codehaus.cake.internal.cache.service.attribute.InternalAttributeService;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;
import org.codehaus.cake.internal.cache.service.memorystore.CacheMap.HashEntry;
import org.codehaus.cake.internal.service.configuration.RuntimeConfigurableService;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.Procedure;
import org.codehaus.cake.service.annotation.Stoppable;

/**
 * The default Implementaiton of {@link MemoryStore}. Storing entries in an HashMap.
 * 
 * @param <K>
 * @param <V>
 */
public class HashMapMemoryStore<K, V> extends CacheMap<K, V> implements MemoryStore<K, V>, CompositeService,
        RuntimeConfigurableService {

    private final InternalAttributeService<K, V> attributeService;
    /** Used for evicting entries if no eviction policy is set. */
    private int clock;
    private final Procedure<MemoryStoreService<K, V>> evictor;
    private final InternalCacheExceptionService<K, V> ies;
    private final Predicate<CacheEntry<K, V>> isCacheable;

    private boolean isDisabled;
    private int maximumSize;
    private long maximumVolume;
    private final ReplacementPolicy<K, V> policy;

    private final boolean volumeEnabled;
    /** The total sizes of all elements in the cache. */
    private long volume;

    public HashMapMemoryStore(CacheAttributeConfiguration attributeConfiguration,
            MemoryStoreConfiguration<K, V> storeConfiguration, InternalAttributeService<K, V> attributeService,
            InternalCacheExceptionService<K, V> ies) {
        volumeEnabled = attributeConfiguration.getAllAttributes().contains(CacheEntry.SIZE);
        this.attributeService = attributeService;
        this.ies = ies;
        policy = storeConfiguration.getPolicy();
        isCacheable = (Predicate) storeConfiguration.getIsCacheableFilter();
        updateConfiguration(storeConfiguration.getAttributes());
        evictor = storeConfiguration.getEvictor();
    }

    //
    // private void print() {
    // System.out.println("---- " + size + "---------");
    // for (int i = 0; i < table.length; i++) {
    // HashEntry he = table[i];
    // String s = "";
    // while (he != null) {
    // s += he.getKey() + ",";
    // he = he.next;
    // }
    // System.out.println(i + ":" + s);
    // }
    // }

    private ParallelArray<CacheEntry<K, V>> all() {
        return (ParallelArray) ParallelArray.createUsingHandoff(allAsArray(), ParallelArray.defaultExecutor());
    }

    private CacheEntry<K, V> evictNext() {
        assert size != 0;
        if (policy != null) {
            CacheEntry<K, V> entry = (CacheEntry<K, V>) policy.evictNext();
            // print();
            if (remove(entry.getKey()) == null) {
                throw new AssertionError();
            }
            removeEntry(entry, true);
            return entry;
        }
        // No replacement policy defined by the user we try to remove a random element
        // The following uses a primitive clock algorithm that gives precedens to recently inserted items
        // This algorithm is rather slow if the table size is very big, but the amount of entries are small

        int tableLength = table.length - 1;
        int clock = this.clock; // TODO we might want to initialize it to this.clock+1;
        // so we don't keep hammering the same slot the whole time
        HashEntry<K, V> e = table[clock];
        while (e == null) {
            if (++clock > tableLength) {
                clock = 0;
            }
            e = table[clock];
        }
        ++modCount;
        table[clock] = e.next;
        size--;
        if (volumeEnabled) {
            volume -= SIZE.get(e);
        }
        this.clock = clock;
        return e;
    }

    public Collection<?> getChildServices() {
        return Arrays.asList(policy);
    }

    public Set<Attribute<?>> getRuntimeConfigurableAttributes() {
        return new HashSet<Attribute<?>>(Arrays.<Attribute<?>> asList(MemoryStoreAttributes.IS_DISABLED,
                MemoryStoreAttributes.MAX_SIZE, MemoryStoreAttributes.MAX_VOLUME));
    }

    public long getVolume(Predicate<CacheEntry<K, V>> filter) {
        if (!volumeEnabled) {
            return size(filter);
        }
        if (filter == null || filter == Predicates.TRUE) {
            return volume;
        } else {
            HashEntry<K, V>[] tab = table;
            long count = 0;
            int len = tab.length;
            for (int i = 0; i < len; i++) {
                for (HashEntry<K, V> e = tab[i]; e != null; e = e.next) {
                    if (filter.op(e)) {
                        count += e.getAttributes().get(CacheEntry.SIZE);
                    }
                }
            }
            return count;
        }
    }

    public Iterator<CacheEntry<K, V>> iterator(Predicate<CacheEntry<K, V>> predicate) {
        final Iterator<CacheEntry<K, V>> iter = newEntrySetIterator(predicate);
        return new Iterator<CacheEntry<K, V>>() {
            CacheEntry<K, V> current;

            public boolean hasNext() {
                return iter.hasNext();
            }

            public CacheEntry<K, V> next() {
                current = iter.next();
                return current;
            }

            public void remove() {
                iter.remove();
                removeEntry(current, false);
            }
        };
    }

    public void process(AddEntriesRequest<K, V> r) {
        if (isDisabled) {
            return;
        }
        // prepare
        for (AddEntryRequest<K, V> e : r.adds()) {
            CacheEntry<K, V> previous = get(null, e.getKey());
            e.setPreviousEntry(previous);
        }

        for (AddEntryRequest<K, V> entry : r.adds()) {
            CacheEntry<K, V> previous = entry.getPreviousAsEntry();
            final AttributeMap atr;
            if (previous == null) {
                atr = attributeService.create(entry.getKey(), entry.getValue(), entry.getAttributes());
            } else {
                atr = attributeService.update(entry.getKey(), entry.getValue(), entry.getAttributes(), previous
                        .getAttributes());
            }

            final CacheEntry<K, V> newEntry = Caches.newEntry(entry.getKey(), entry.getValue(), atr);
            boolean keepNew = true;
            if (isCacheable != null) {
                try {
                    keepNew = isCacheable.op(newEntry);
                } catch (RuntimeException e) {
                    ies.fatal("IsCacheable predicate failed to validate, entry was not cached", e);
                    keepNew = false;
                }
            }

            boolean keepExisting = false;
            boolean evicted = false;
            if (keepNew && policy != null) {
                evicted = true;
                if (previous == null) {
                    keepNew = policy.add(newEntry);
                } else {
                    CacheEntry<K, V> e = policy.replace(previous, newEntry);
                    keepExisting = e == previous;
                    keepNew = e == newEntry;
                }
            }
            if (previous != null && !keepExisting) {
                removeEntry(previous, evicted);
                if (!keepNew) {
                    remove(entry.getKey());
                }
            }
            if (keepNew) {
                if (volumeEnabled) {
                    volume += SIZE.get(newEntry);
                }
                put(entry.getKey(), newEntry.getValue(), newEntry.getAttributes());
            }
            if (keepNew) {
                entry.setNewEntry(newEntry);
            }
        }
        trim(r);
    }

    public void process(AddEntryRequest<K, V> entry) {
        if (isDisabled) {
            return;
        }
        if (size + 1 > threshold) {
            // ensure capacity
            rehash();
        }
        HashEntry<K, V>[] tab = table;
        K key = entry.getKey();
        int hash = hash(key.hashCode());
        int index = hash & (tab.length - 1);
        HashEntry<K, V> first = tab[index];
        HashEntry<K, V> existing = first;
        HashEntry<K, V> prev = first;

        // Find previous entry
        while (existing != null && (existing.hash != hash || !key.equals(existing.key))) {
            prev = existing;
            existing = existing.next;
        }

        // See if there are any conditional inserts
        Predicate<CacheEntry<K, V>> updatePredicate = entry.getUpdatePredicate();
        if (updatePredicate != null) {
            if (existing == null) {
                if (updatePredicate == Predicates.IS_NOT_NULL) {
                    return;
                }
            } else {
                if (updatePredicate == Predicates.IS_NULL) {
                    entry.setPreviousEntry(existing);
                    return;
                }
                if (updatePredicate instanceof CachePredicates.CacheValueEquals && !updatePredicate.op(existing)) {
                    return;
                }
            }
        }

        // Create the attributemap for the new entry
        V value = entry.getValue();
        final HashEntry<K, V> ne;
        if (existing == null) {
            AttributeMap atr = attributeService.create(key, value, entry.getAttributes());
            ne = newEntry(key, hash, first, value, atr);
        } else {
            AttributeMap atr = attributeService.update(key, value, entry.getAttributes(), existing.getAttributes());
            ne = newEntry(key, hash, existing.next, value, atr);
        }

        boolean keepNew = true;
        if (isCacheable != null) {
            try {
                keepNew = isCacheable.op(ne);
            } catch (RuntimeException e) {
                ies.error("IsCacheable predicate failed to validate, entry was not cached", e);
                keepNew = false;
            }
        }

        boolean keepExisting = false;
        boolean evicted = false;
        if (keepNew && policy != null) {
            evicted = true;
            if (existing == null) {
                keepNew = policy.add(ne);
            } else {
                CacheEntry<K, V> e = policy.replace(existing, ne);
                keepExisting = e == existing;
                keepNew = e == ne;
            }
        }
        if (existing != null && !keepExisting) {
            boolean trim = false;
            if (volumeEnabled) {
                long existingVolume = SIZE.get(existing);
                trim = existingVolume < 0;
                volume -= existingVolume;
            }
            if (!evicted && policy != null) {
                policy.remove(existing);
            }
            if (!keepNew) { // Remove in table
                ++modCount;
                if (prev == existing) { // first entry
                    tab[index] = existing.next;
                } else {
                    prev.next = existing.next;
                }
                existing.entryRemoved(this);
                size--;
                if (trim) {// In the rare cases where we have nagative volume
                    trim(entry);
                }
                return;
            }
        }
        if (keepNew) {
            if (volumeEnabled) {
                volume += SIZE.get(ne);
            }
            modCount++;
            if (existing == null) {// Insert
                tab[index] = ne;
                size++;
            } else { // Replace
                if (prev == existing) { // first entry
                    tab[index] = ne;
                } else {
                    prev.next = ne;
                }
            }
            entry.setPreviousEntry(existing);
            entry.setNewEntry(ne);
            trim(entry);
        }
    }

    public void process(Predicate<CacheEntry<K, V>> filter, ClearCacheRequest<K, V> r) {
        if (filter == null || filter == Predicates.TRUE) {
            if (size != 0) {
                modCount++;
                HashEntry<K, V>[] tab = table;
                for (int i = 0; i < tab.length; i++) {
                    tab[i] = null;
                }
                size = 0;
                volume = 0;
                if (policy != null) {
                    policy.clear();
                }
            }
        } else {
            modCount++;
            HashEntry<K, V>[] tab = table;
            int len = tab.length;
            for (int i = 0; i < len; i++) {
                HashEntry<K, V> e = tab[i];
                HashEntry<K, V> prev = e;
                while (e != null) {
                    if (filter.op(e)) {// remove it
                        if (prev == e) { // first entry
                            tab[i] = e.next;
                            prev = e.next;
                        } else {
                            prev.next = e.next;
                        }
                        size--;
                        removeEntry(e, false);
                    } else {
                        prev = e;
                    }
                    e = e.next;
                }
            }
        }
    }

    public void process(RemoveEntriesRequest<K, V> r) {
        for (RemoveEntryRequest<K, V> e : r.removes()) {
            process(e);
        }
    }

    public void process(RemoveEntryRequest<K, V> r) {
        K key = r.getKey();
        int hash = hash(key.hashCode());
        HashEntry<K, V>[] tab = table;
        int index = hash & (tab.length - 1);
        HashEntry<K, V> e = tab[index];
        HashEntry<K, V> prev = e;
        while (e != null && (e.hash != hash || !key.equals(e.key))) {
            prev = e;
            e = e.next;
        }
        Predicate<CacheEntry<K, V>> p = r.getUpdatePredicate();
        if (e != null && (p == null || p.op(e))) {
            ++modCount;
            if (prev == e) { // first entry
                tab[index] = e.next;
            } else {
                prev.next = e.next;
            }
            e.entryRemoved(this);
            size--;
            if (volumeEnabled) {
                long existingVolume = SIZE.get(e);
                volume -= existingVolume;
            }
            if (policy != null) {
                policy.remove(e);
            }
            // TODO we should trim if existingVolume<0
            r.setPreviousEntry(e);
        }
    }

    public void process(TrimToSizeRequest<K, V> r) {
        int size = r.getSizeToTrimTo();
        Comparator<CacheEntry<K, V>> comparator = r.getComparator();
        trimToSize(r, comparator, size);
    }

    public void process(TrimToVolumeRequest<K, V> r) {
        long volume = r.getVolumeToTrimTo();
        Comparator<CacheEntry<K, V>> comparator = r.getComparator();
        trimToVolume(r, comparator, volume);
    }

    private void removeEntry(CacheEntry<K, V> entry, boolean isEvicted) {
        if (volumeEnabled) {
            volume -= SIZE.get(entry);
        }
        if (!isEvicted && policy != null) {
            policy.remove(entry);
        }
    }

    @Stoppable
    public final void stop() {
        modCount++;
        size = 0;
        volume = 0;
        if (policy != null) {
            policy.clear();
        }
        table = HashEntry.newArray(1);
    }

    public void touch(CacheEntry<K, V> entry) {
        // Perhaps we can move .access to outer loop
        attributeService.access(entry.getAttributes());
        if (policy != null) {
            policy.touch(entry);
        }
    }

    public void trim(Trimable<K, V> trimable) {
        while (size(null) > maximumSize || (volumeEnabled ? this.volume : this.size) > maximumVolume) {
            List<CacheEntry<K, V>> trimmed = trimable.getTrimmed();
            if (trimmed == null) {
                trimmed = new ArrayList<CacheEntry<K, V>>();
                trimable.setTrimmed(trimmed);
            }
            if (evictor == null) {
                trimmed.add(evictNext());
            } else {
                Trim t = new Trim();
                evictor.op(t);
                if (t.newVolume != null) {
                    trimToVolume(trimable, t.comparator, t.newVolume);
                }
                Integer newSize = t.newSize;
                if (newSize != null) {
                    trimToSize(trimable, t.comparator, newSize);
                }
                if (trimmed.size() == 0) {
                    ies.warning("Custom Evictor failed to reduce the size of the cache, manually removing 1 element");
                    trimmed.add(evictNext());
                }
            }
        }
    }

    private void trimToSize(Trimable<K, V> trimable, Comparator comparator, int size) {

        int currentSize = size(null);
        int trimSize = size >= 0 ? currentSize - size : Math.min(currentSize, -size);
        if (size == Integer.MIN_VALUE) {
            trimSize = currentSize; // Math.abs(Integer.MIN_VALUE)==Integer.
            // MIN_VALUE
        }
        if (trimSize > 0) {
            List<CacheEntry<K, V>> trimmed = trimable.getTrimmed();
            if (trimmed == null) {
                trimmed = new ArrayList<CacheEntry<K, V>>(trimSize);
                trimable.setTrimmed(trimmed);
            }
            if (comparator == null) {
                while (trimSize-- > 0) {
                    trimmed.add(evictNext());
                }
            } else {
                ParallelArray<CacheEntry<K, V>> sorter = all();
                sorter.sort(comparator);
                for (int i = 0; i < trimSize; i++) {
                    CacheEntry<K, V> e = (CacheEntry<K, V>) sorter.get(i);
                    removeEntry(e, false);
                    remove(e.getKey());
                    trimmed.add(e);
                }
            }
        }
    }

    private void trimToVolume(Trimable<K, V> trimable, Comparator<CacheEntry<K, V>> comparator, long trimToVolume) {
        long currentVolume = volumeEnabled ? this.volume : this.size;
        long trimTo = trimToVolume >= 0 ? trimToVolume : Math.max(0, currentVolume + trimToVolume);
        List<CacheEntry<K, V>> trimmed = trimable.getTrimmed();
        if (trimmed == null) {
            trimmed = new ArrayList<CacheEntry<K, V>>();
            trimable.setTrimmed(trimmed);
        }
        if (comparator == null) {
            while ((volumeEnabled ? this.volume : this.size) > trimTo) {
                trimmed.add(evictNext());
            }
        } else {
            ParallelArray sorter = all();
            sorter.sort((Comparator) comparator);
            int i = 0;
            while (this.volume > trimTo) {
                CacheEntry<K, V> e = (CacheEntry<K, V>) sorter.get(i++);
                removeEntry(e, false);
                remove(e.getKey());
                trimmed.add(e);
            }
        }
    }

    public void updateConfiguration(AttributeMap attributes) {
        isDisabled = attributes.get(MemoryStoreAttributes.IS_DISABLED, isDisabled);
        maximumSize = attributes.get(MemoryStoreAttributes.MAX_SIZE, maximumSize);
        if (maximumSize == 0) {
            maximumSize = Integer.MAX_VALUE;
        }
        maximumVolume = attributes.get(MemoryStoreAttributes.MAX_VOLUME, maximumVolume);
        if (maximumVolume == 0) {
            maximumVolume = Long.MAX_VALUE;
        }
    }

    class Trim implements MemoryStoreService<K, V> {
        Comparator comparator;
        Integer newSize;
        Long newVolume;

        public int getMaximumSize() {
            return maximumSize;
        }

        public long getMaximumVolume() {
            return maximumVolume;
        }

        public int getSize() {
            return size;
        }

        public long getVolume() {
            return volumeEnabled ? volume : size;
        }

        public boolean isDisabled() {
            return isDisabled;
        }

        public void setDisabled(boolean isDisabled) {
            throw new UnsupportedOperationException("cannot call this method from here");
        }

        public void setMaximumSize(int maximumSize) {
            throw new UnsupportedOperationException("cannot call this method from here");
        }

        public void setMaximumVolume(long maximumVolume) {
            throw new UnsupportedOperationException("cannot call this method from here");
        }

        public void trimToSize(int size) {
            this.newSize = size;
        }

        public void trimToSize(int size, Comparator<? extends CacheEntry<K, V>> comparator) {
            this.newSize = size;
            this.comparator = comparator;
        }

        public void trimToVolume(long volume) {
            this.newVolume = volume;
        }

        public void trimToVolume(long volume, Comparator<? extends CacheEntry<K, V>> comparator) {
            this.newVolume = volume;
            this.comparator = comparator;
        }
    }
}
