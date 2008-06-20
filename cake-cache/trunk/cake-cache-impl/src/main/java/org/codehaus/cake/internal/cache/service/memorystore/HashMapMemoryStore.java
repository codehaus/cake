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

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.forkjoin.collections.ParallelArray;
import org.codehaus.cake.internal.cache.InternalCacheEntry;
import org.codehaus.cake.internal.cache.service.attribute.InternalAttributeService;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;
import org.codehaus.cake.internal.cache.util.EntryPair;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.internal.util.CollectionUtils;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.Procedure;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;

public class HashMapMemoryStore<K, V> extends AbstractMemoryStore<K, V> implements MemoryStoreService<K, V>,
        CompositeService {

    private Map<K, DefaultEntry<K, V>> map = new HashMap<K, DefaultEntry<K, V>>();
    private final InternalCacheExceptionService<K, V> ies;
    private final InternalAttributeService<K, V> attributeService;
    private int maximumSize;
    private long maximumVolume;
    private long volume;
    private final ReplacementPolicy<K, V> policy;
    private final Predicate isCacheable;
    private boolean isDisabled;
    private Procedure<MemoryStoreService<K, V>> evictor;

    public HashMapMemoryStore(MemoryStoreConfiguration<K, V> storeConfiguration,
            InternalAttributeService<K, V> attributeService, InternalCacheExceptionService<K, V> ies) {
        this.attributeService = attributeService;
        this.ies = ies;

        maximumSize = initializeMaximumSize(storeConfiguration);
        maximumVolume = initializeMaximumVolume(storeConfiguration);
        policy = storeConfiguration.getPolicy();
        isCacheable = storeConfiguration.getIsCacheableFilter();
        isDisabled = storeConfiguration.isDisabled();
        evictor = storeConfiguration.getEvictor();
    }

    // public CacheEntry<K, V> any() {
    // return map.size() == 0 ? null : map.values().iterator().next();
    // }

    public CacheEntry<K, V> get(Object key) {
        CacheEntry<K, V> entry = map.get(key);
        if (entry != null) {
            attributeService.access(entry.getAttributes());
            if (policy != null) {
                policy.touch(entry);
            }
        }
        return entry;
    }

    public CacheEntry<K, V> peek(Object key) {
        return map.get(key);
    }

    public EntryPair<K, V> put(K key, V value, AttributeMap attributes, boolean isAbsent) {
        final DefaultEntry<K, V> prev = map.get(key);
        if (isDisabled || isAbsent && prev != null) {
            return new EntryPair<K, V>(prev, null);
        }

        final AttributeMap atr;
        if (prev == null) {
            atr = attributeService.create(key, value, attributes);
        } else {
            atr = attributeService.update(key, value, attributes, prev.getAttributes());
        }

        final DefaultEntry<K, V> entry = new DefaultEntry<K, V>(key, value, atr);
        boolean keepNew = true;
        if (isCacheable != null) {
            try {
                keepNew = isCacheable.op(entry);
            } catch (RuntimeException e) {
                ies.fatal("IsCacheable predicate failed to validate, entry was not cached", e);
                keepNew = false;
            }
        }

        boolean keepExisting = false;
        boolean evicted = false;
        if (keepNew && policy != null) {
            evicted = true;
            if (prev == null) {
                keepNew = policy.add(entry);
            } else {
                CacheEntry<K, V> e = policy.replace(prev, entry);
                keepExisting = e == prev;
                keepNew = e == entry;
            }
        }
        if (prev != null && !keepExisting) {
            removeEntry(prev, evicted);
            if (!keepNew) {
                map.remove(key);
            }
        }
        if (keepNew) {
            volume += SIZE.get(entry);
            map.put(key, entry);
        }
        return new EntryPair<K, V>(prev, keepNew ? entry : null);
    }

    public Map<CacheEntry<K, V>, CacheEntry<K, V>> putAllWithAttributes(Map<K, Entry<V, AttributeMap>> data) {
        HashMap result = new HashMap<CacheEntry<K, V>, CacheEntry<K, V>>();
        for (Map.Entry<K, Entry<V, AttributeMap>> s : data.entrySet()) {
            EntryPair<K, V> ss = put(s.getKey(), s.getValue().getKey(), s.getValue().getValue(), false);
            result.put(ss.getPrevious(), ss.getNew());
        }
        return result;
    }

    public DefaultEntry<K, V> remove(Object key, Object value) {
        if (value == null) {
            DefaultEntry<K, V> entry = map.remove(key);
            removeEntry(entry, false);
            return entry;
        } else {
            DefaultEntry<K, V> entry = map.get(key);
            if (entry == null) {
                return null;
            }
            if (entry.getValue().equals(value)) {
                map.remove(key);
                removeEntry(entry, false);
                return entry;
            } else {
                return null;
            }
        }
    }

    public ParallelArray<CacheEntry<K, V>> removeAll(Collection entries) {
        ParallelArray<CacheEntry<K, V>> pa = ParallelArray.create(entries.size(), CacheEntry.class, ParallelArray
                .defaultExecutor());
        for (Object o : entries) {
            DefaultEntry<K, V> entry = remove(o, null);
            if (entry != null) {
                removeEntry(entry, false);
                pa.asList().add(entry);
            }
        }
        return pa;
    }

    private void clearEntries() {
        volume = 0;
        if (policy != null) {
            policy.clear();
        }
    }

    private void removeEntry(DefaultEntry<K, V> entry, boolean isEvicted) {
        if (entry != null) {
            volume -= SIZE.get(entry);
            if (!isEvicted && policy != null) {
                policy.remove(entry);
            }
        }
    }

    public ParallelArray<CacheEntry<K, V>> trim() {
        ParallelArray<CacheEntry<K, V>> pa = ParallelArray.create(0, CacheEntry.class, ParallelArray.defaultExecutor());
        while (map.size() > maximumSize || volume > maximumVolume) {
            if (evictor == null) {
                pa.asList().add(evictNext());
            } else {
                Trim t = new Trim();
                evictor.op(t);
                if (t.volume != null) {
                    trimToVolume(pa, t.volume, t.comparator);
                }
                if (t.size != null) {
                    trimToSize(pa, t.size, t.comparator);
                }
                if (pa.size() == 0) {
                    ies.warning("Custom Evictor failed to reduce the size of the cache, manually removing 1 element");
                    pa.asList().add(evictNext());
                }
            }
        }
        return pa;
    }

    class Trim implements MemoryStoreService<K, V> {
        Long volume;
        Integer size;
        Comparator comparator;

        public int getMaximumSize() {
            return HashMapMemoryStore.this.getMaximumSize();
        }

        public long getMaximumVolume() {
            return HashMapMemoryStore.this.getMaximumVolume();
        }

        public int getSize() {
            return HashMapMemoryStore.this.getSize();
        }

        public long getVolume() {
            return HashMapMemoryStore.this.getVolume();
        }

        public boolean isDisabled() {
            return HashMapMemoryStore.this.isDisabled();
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
            this.size = size;
        }

        public void trimToVolume(long volume) {
            this.volume = volume;
        }

        public void trimToSize(int size, Comparator<? extends CacheEntry<K, V>> comparator) {
            this.size = size;
            this.comparator = comparator;
        }

        public void trimToVolume(long volume, Comparator<? extends CacheEntry<K, V>> comparator) {
            this.volume = volume;
            this.comparator = comparator;
        }
    }

    private CacheEntry<K, V> evictNext() {
        if (policy != null) {
            DefaultEntry<K, V> entry = (DefaultEntry<K, V>) policy.evictNext();
            map.remove(entry.getKey());
            removeEntry(entry, true);
            return entry;
        }
        Iterator<DefaultEntry<K, V>> iter = map.values().iterator();
        DefaultEntry<K, V> entry = iter.next();
        iter.remove();
        removeEntry(entry, false);
        return entry;
    }

    public ParallelArray<CacheEntry<K, V>> removeAll() {
        ParallelArray<CacheEntry<K, V>> array = (ParallelArray) ParallelArray.createUsingHandoff(map.values().toArray(
                new DefaultEntry[0]), ParallelArray.defaultExecutor());
        map.clear();
        clearEntries();
        return array;
    }

    static class DefaultEntry<K, V> implements CacheEntry<K, V>, InternalCacheEntry<K, V> {

        private final AttributeMap attributes;
        /** The key of the entry. */
        private final K key;

        /** The value of the entry. */
        private final V value;

        public DefaultEntry(K key, V value, AttributeMap attributes) {
            this.key = key;
            this.value = value;
            this.attributes = attributes;
        }

        /** {@inheritDoc} */

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry) o;
            return CollectionUtils.eq(key, e.getKey()) && CollectionUtils.eq(value, e.getValue());
        }

        /** {@inheritDoc} */
        public K getKey() {
            return key;
        }

        /** {@inheritDoc} */
        public V getValue() {
            return value;
        }

        /** {@inheritDoc} */

        public int hashCode() {
            return (key.hashCode()) ^ value.hashCode();
            // return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        /** {@inheritDoc} */
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        public AttributeMap getAttributes() {
            return attributes;
        }

        /** {@inheritDoc} */
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(key);
            sb.append("=");
            sb.append(value);
            sb.append(" [");

            Iterator<Map.Entry<Attribute, Object>> i = attributes.entrySet().iterator();
            if (!i.hasNext()) {
                return sb.append("]").toString();
            }
            for (;;) {
                Map.Entry<Attribute, Object> e = i.next();
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                if (!i.hasNext())
                    return sb.append(']').toString();
                sb.append(", ");
            }
        }
    }

    public Iterator<CacheEntry<K, V>> iterator() {
        final Iterator<DefaultEntry<K, V>> iter = map.values().iterator();
        return new Iterator<CacheEntry<K, V>>() {
            DefaultEntry<K, V> current;

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

    public EntryPair<K, V> replace(K key, V oldValue, V newValue, AttributeMap attributes) {
        CacheEntry<K, V> prev = map.get(key);
        if (oldValue == null && prev != null || oldValue != null && prev != null && oldValue.equals(prev.getValue())) {
            return put(key, newValue, attributes, false);
        }
        return new EntryPair<K, V>(prev, null);
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    @Startable
    public void start(ContainerConfiguration<?> configuration, ServiceRegistrant serviceRegistrant) throws Exception {
        serviceRegistrant.registerService(MemoryStoreService.class, this);
    }

    public long getMaximumVolume() {
        return maximumVolume;
    }

    public int getSize() {
        return map.size();
    }

    public long getVolume() {
        return volume;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public void setMaximumSize(int maximumSize) {
        if (maximumSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.maximumSize = maximumSize;
    }

    public void setMaximumVolume(long maximumVolume) {
        if (maximumVolume <= 0) {
            throw new IllegalArgumentException();
        }
        this.maximumVolume = maximumVolume;
    }

    public void trimToSize(int size) {
        ParallelArray<CacheEntry<K, V>> pa = ParallelArray.create(0, CacheEntry.class, ParallelArray.defaultExecutor());
        trimToSize(pa, size, null);
    }

    public void trimToVolume(long volume) {
        ParallelArray<CacheEntry<K, V>> pa = ParallelArray.create(0, CacheEntry.class, ParallelArray.defaultExecutor());
        trimToVolume(pa, volume, null);
    }

    public void trimToSize(int size, Comparator<? extends CacheEntry<K, V>> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        ParallelArray<CacheEntry<K, V>> pa = ParallelArray.create(0, CacheEntry.class, ParallelArray.defaultExecutor());
        trimToSize(pa, size, (Comparator) comparator);
    }

    ParallelArray<CacheEntry<K, V>> all() {
        return (ParallelArray) ParallelArray.createFromCopy(map.values().toArray(new CacheEntry[map.size()]),
                ParallelArray.defaultExecutor());
    }

    private void trimToVolume(ParallelArray<CacheEntry<K, V>> pa, long volume, Comparator comparator) {
        long currentVolume = this.volume;
        long trimTo = volume >= 0 ? volume : Math.max(0, currentVolume + volume);
        if (comparator == null) {
            while (this.volume > trimTo) {
                pa.asList().add(evictNext());
            }
        } else {
            ParallelArray sorter = all();
            sorter.sort((Comparator) comparator);
            int i = 0;
            while (this.volume > trimTo) {
                DefaultEntry<K, V> e = (DefaultEntry<K, V>) sorter.get(i++);
                removeEntry(e, false);
                map.remove(e.getKey());
                pa.asList().add(e);
            }
        }
    }

    private void trimToSize(ParallelArray<CacheEntry<K, V>> pa, int size, Comparator comparator) {
        int currentSize = map.size();
        int trimSize = size >= 0 ? currentSize - size : Math.min(currentSize, -size);
        if (size == Integer.MIN_VALUE) {
            trimSize = currentSize; // Math.abs(Integer.MIN_VALUE)==Integer.MIN_VALUE
        }
        if (trimSize > 0) {
            if (comparator == null) {
                while (trimSize-- > 0) {
                    pa.asList().add(evictNext());
                }
            } else {
                ParallelArray sorter = all();
                sorter.sort((Comparator) comparator);
                for (int i = 0; i < trimSize; i++) {
                    DefaultEntry<K, V> e = (DefaultEntry<K, V>) sorter.get(i);
                    removeEntry(e, false);
                    map.remove(e.getKey());
                    pa.asList().add(e);
                }
            }
        }
    }

    public void trimToVolume(long volume, Comparator<? extends CacheEntry<K, V>> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        ParallelArray<CacheEntry<K, V>> pa = ParallelArray.create(0, CacheEntry.class, ParallelArray.defaultExecutor());
        trimToVolume(pa, volume, (Comparator) comparator);
    }

    public Collection<?> getChildServices() {
        return Arrays.asList(policy);
    }
}
