package org.codehaus.cake.internal.cache.memorystore.openadressing;

import static org.codehaus.cake.cache.CacheEntry.SIZE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.cache.service.memorystore.IsCacheablePredicate;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.concurrent.collection.ParallelArray;
import org.codehaus.cake.internal.cache.CachePredicates;
import org.codehaus.cake.internal.cache.memorystore.MemoryStore;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;
import org.codehaus.cake.internal.cache.processor.request.Trimable;
import org.codehaus.cake.internal.cache.service.attribute.InternalAttributeService;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;
import org.codehaus.cake.internal.cache.service.memorystore.MemoryStoreAttributes;
import org.codehaus.cake.internal.service.configuration.RuntimeConfigurableService;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.Procedure;
import org.codehaus.cake.service.OnShutdown;

/**
 * An implementation of {@link MemoryStore} using open adressing.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java 239 2008-12-23 20:29:21Z kasper $
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */
public class OpenAdressingMemoryStore<K, V> implements MemoryStore<K, V>, CompositeService, RuntimeConfigurableService {
    private static final int REPROBE_LIMIT = 10; // Too many reprobes then force a table-resize

    final static OpenAdressingEntry TOMBSTONE = new OpenAdressingEntry(null, 0, null);

    private final InternalAttributeService<K, V> attributeService;

    /** Used for evicting entries if no eviction policy is set. */
    private int clock;

    private final Procedure<MemoryStoreService<K, V>> evictor;
    private final InternalCacheExceptionService<K, V> ies;
    private final IsCacheablePredicate<? super K, ? super V> isCacheable;

    /** Whether or not the cache is disabled. */
    private boolean isDisabled;
    /** Maximum number of entries in the cache. */
    private int maximumSize;
    /** Maximum volume of the cache. */
    private long maximumVolume;

    /** The replacement policy used in the cache, null is allowed. */
    private final ReplacementPolicy<CacheEntry<K, V>> policy;
    /** Current number of entries in the cache. */
    private int size;
    /** Array holding all cached entries. */
    OpenAdressingEntry<K, V>[] table;
    /** Current volume of the cache. */
    private long volume;
    /** Whether or not we keep track of the volume in the cache. */
    private final boolean volumeEnabled;

    public OpenAdressingMemoryStore(CacheConfiguration cacheConfiguration,
            MemoryStoreConfiguration<K, V> storeConfiguration, InternalAttributeService<K, V> attributeService,
            InternalCacheExceptionService<K, V> ies) {
        volumeEnabled = cacheConfiguration.getAllEntryAttributes().contains(CacheEntry.SIZE);
        this.attributeService = attributeService;
        this.ies = ies;
        Class<? extends ReplacementPolicy> policy = storeConfiguration.getPolicy();
        this.policy = policy == null ? null : Policies.create(policy);
        isCacheable = storeConfiguration.getIsCacheableFilter();
        updateConfiguration(storeConfiguration.getAttributes());
        evictor = storeConfiguration.getEvictor();
        table = newElementArray(16);
    }

    public long getVolume(Predicate<CacheEntry<K, V>> filter) {
        if (!volumeEnabled) {
            return size(filter);
        }
        if (filter != null) {
            OpenAdressingEntry<K, V>[] table = this.table;
            int len = table.length;
            long volume = 0;
            for (int i = 0; i < len; i++) {
                OpenAdressingEntry<K, V> e = table[i];
                if (e != null && e != TOMBSTONE && filter.op(e)) {
                    volume += e.get(CacheEntry.SIZE);
                }
            }
            return volume;
        }
        return volume;

    }

    private OpenAdressingEntry<K, V> add(AddEntryRequest<K, V> request, K key, int hash,
            OpenAdressingEntry<K, V> existing) {
        // Check any update predicate, to see if we cannot insert the new entry
        Predicate<CacheEntry<K, V>> updatePredicate = request.getUpdatePredicate();
        if (updatePredicate != null) {
            if (existing == null) {
                if (updatePredicate == Predicates.IS_NOT_NULL) {
                    return null;
                }
            } else {
                if (updatePredicate == Predicates.IS_NULL) {
                    request.setPreviousEntry(existing);
                    return existing;
                } else if (updatePredicate instanceof CachePredicates.CacheValueEquals && !updatePredicate.op(existing)) {
                    return existing;
                }
            }
        }

        // Creates the attributemap for the new entry
        V value = request.getValue();
        final OpenAdressingEntry<K, V> ne;
        if (existing == null) {
            MutableAttributeMap atr = attributeService.create(key, value, request.getAttributes());
            ne = newEntry(key, hash, value, atr);
        } else {
            MutableAttributeMap atr = attributeService.update(key, value, request.getAttributes(),
                    ((TmpOpenAdressingEntry<K, V>) existing).getAttributes());
            ne = newEntry(key, hash, value, atr);
        }
        boolean evicted = false;
        if (existing == null) {
            boolean doCache = isCacheable == null ? true : isCacheable.add((CacheEntry) ne);
            if (doCache) {
                if (policy != null) {
                    policy.add(ne);
                }
                if (volumeEnabled) {
                    volume += ne.get(SIZE);
                }
                size++;
                request.setNewEntry(ne);
                return ne;
            }
        } else {
            CacheEntry<K, V> replacing = isCacheable == null ? ne : (CacheEntry) isCacheable.replace(
                    (CacheEntry) existing, (CacheEntry) ne);
            if (replacing == null) {
                size--;
                if (policy != null) {
                    policy.remove(existing);
                }
                if (volumeEnabled) {
                    long existingVolume = existing.get(SIZE);
                    volume -= existingVolume;
                }
                return null;
            } else if (replacing == ne) { // Insert new entry
                if (volumeEnabled) {
                    long existingVolume = existing.get(SIZE);
                    volume -= existingVolume;
                    volume += ne.get(SIZE);
                }
                if (policy != null) {
                    policy.replace(existing, ne);
                }

                request.setPreviousEntry(existing);
                request.setNewEntry(ne);
                return ne;
            } else if (replacing == existing) {
                return existing;
            }
        }
        return null;
        // Check to see if the entry can be cached

        // if (isCacheable != null) {
        // try {
        // keepNew = isCacheable.op(ne);
        // } catch (RuntimeException e) {
        // ies.error("IsCacheable predicate failed to validate, entry was not cached", e);
        // keepNew = false;
        // }
        // }

    }

    public void clear(Predicate<CacheEntry<K, V>> filter) {
        if (size != 0) {
            if (filter == null) {
                clear();
            } else {
                OpenAdressingEntry<K, V>[] table = this.table;
                int len = table.length;
                for (int i = 0; i < len; i++) {
                    OpenAdressingEntry<K, V> e = table[i];
                    if (e != null && e != TOMBSTONE && filter.op(e)) {
                        table[i] = TOMBSTONE;
                        size--;
                        removeEntry(e, false);
                    }
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
        OpenAdressingEntry<K, V>[] table = this.table;
        int hash = hash(key);
        int mask = table.length - 1;
        int idx = hash & mask;
        OpenAdressingEntry<K, V> e = table[idx];
        while (e != null) {
            if (e != TOMBSTONE) {
                K k = e.key;
                if (k == key || (hash == e.hash && k.equals(key))) {
                    // If next is not null set to tombstone
                    table[idx] = table[(idx + 1) & mask] == null ? null : TOMBSTONE;
                    Predicate<CacheEntry<K, V>> p = r.getUpdatePredicate();
                    if (p == null || p.op(e)) {
                        size--;
                        removeEntry(e, false);
                        r.setPreviousEntry(e);
                        // TODO we should trim if existingVolume<0
                    }
                }
            }
            idx = (idx + 1) & mask; // Reprobe by 1
            e = table[idx];
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

    @OnShutdown
    public final void stop() {
        size = 0;
        volume = 0;
        if (policy != null) {
            policy.clear();
        }
        table = newElementArray(1);
    }

    public void touch(CacheEntry<K, V> entry) {
        // Perhaps we can move .access to outer loop
        attributeService.access(((TmpOpenAdressingEntry) entry).getAttributes());
        if (policy != null) {
            policy.touch(entry);
        }
    }

    private ParallelArray<CacheEntry<K, V>> all() {
        return (ParallelArray) ParallelArray.createUsingHandoff(toArray(), ParallelArray.defaultExecutor());
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
                    OpenAdressingEntry<K, V> e = (OpenAdressingEntry<K, V>) sorter.get(i);
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
                OpenAdressingEntry<K, V> e = (OpenAdressingEntry<K, V>) sorter.get(i++);
                removeEntry(e, false);
                remove(e.getKey());
                trimmed.add(e);
            }
        }
    }

    public void trim(Trimable<K, V> trimable) {
        // if (size>maximumSize) {
        // CacheEntry<K, V> e=evictNext();
        // trimable.setTrimmed(Collections.singletonList(e));
        // }
        // if (true) {
        // return;
        // }
        while (size > maximumSize || (volumeEnabled ? this.volume : size) > maximumVolume) {
            List<CacheEntry<K, V>> trimmed = trimable.getTrimmed();
            if (trimmed == null) {
                trimmed = new ArrayList<CacheEntry<K, V>>(5);
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

    public void process(AddEntriesRequest<K, V> reg) {
        if (isDisabled) {
            return;
        }
        for (AddEntryRequest<K, V> r : reg.adds()) {
            process(r);
        }
    }

    public void clear() {
        OpenAdressingEntry<K, V>[] table = this.table;
        int len = table.length;
        for (int i = 0; i < len; i++) {
            table[i] = null;
        }
        size = 0;
        volume = 0;
        if (policy != null) {
            policy.clear();
        }
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public boolean containsKey(Predicate<CacheEntry<K, V>> filter, Object key) {
        if (filter == null) {
            return containsKey(key);
        }
        OpenAdressingEntry<K, V> e = get(key);
        return e != null && filter.op(e);
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        OpenAdressingEntry<K, V>[] table = this.table;
        for (int i = 0; i < table.length; i++) {
            OpenAdressingEntry<K, V> e = table[i];
            if (e != null && e != TOMBSTONE && e.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(Predicate<CacheEntry<K, V>> filter, Object value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        if (filter == null) {
            return containsValue(value);
        }
        OpenAdressingEntry<K, V>[] table = this.table;
        for (int i = 0; i < table.length; i++) {
            OpenAdressingEntry<K, V> e = table[i];
            if (e != null && e != TOMBSTONE && filter.op(e) && e.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private CacheEntry<K, V> evictNext() {
        assert size != 0;
        if (policy != null) {
            OpenAdressingEntry<K, V> entry = (OpenAdressingEntry<K, V>) policy.evictNext();
            if (remove(entry.getKey()) == null) {
                throw new AssertionError();
            }
            removeEntry(entry, true);
            return entry;
        }
        // No replacement policy defined by the user we try to remove a random element
        // using a primitive clock like algorithm
        OpenAdressingEntry<K, V>[] table = this.table;
        int mask = table.length - 1;
        int idx = this.clock;
        OpenAdressingEntry<K, V> e = table[clock];
        while (e == null || e == TOMBSTONE) {
            e = table[idx = (idx + 1) & mask];
        }
        table[idx] = TOMBSTONE;
        size--;
        removeEntry(e, true);
        this.clock = idx;// Advance one, so we don't keep hitting the same slot
        return e;
    }

    public OpenAdressingEntry<K, V> get(Object key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        OpenAdressingEntry<K, V>[] tab = this.table;
        int hash = hash(key);
        int mask = table.length - 1;
        int idx = hash & mask;
        OpenAdressingEntry<K, V> e = tab[idx];
        int col = 0;
        int tombstoneCount = 0;
        while (e != null) {
            if (e != TOMBSTONE) {
                K k = e.key;
                if (k == key || (hash == e.hash && k.equals(key))) {
                    return e;
                }
            } else {
                if (tombstoneCount++ > 10) { // Many tombstones, cleanup
                    rehash(true);
                }
            }
            idx = (idx + 1) & mask; // Reprobe by 1
            e = tab[idx];
            col++;
        }
        return e;
    }

    public CacheEntry<K, V>[] get(Predicate<CacheEntry<K, V>> filter, int limit) {
        if (size != 0) {
            OpenAdressingEntry<K, V>[] table = this.table;
            int len = table.length;
            if (filter == null) {
                int count = Math.min(limit, size);
                OpenAdressingEntry<K, V>[] result = newElementArray(count--); // count should index in array
                int i = 0;
                while (i++ < len && count != -1) {
                    OpenAdressingEntry<K, V> e = table[i];
                    if (e != null && e != TOMBSTONE) {
                        result[count--] = e;
                    }
                }
                return result;
            } else {
                ArrayList<OpenAdressingEntry<K, V>> list = new ArrayList<OpenAdressingEntry<K, V>>();
                int count = Math.min(limit, size);
                int i = 0;
                while (i < len && count != 0) {
                    OpenAdressingEntry<K, V> e = table[i++];
                    if (e != null && e != TOMBSTONE && filter.op(e)) {
                        list.add(e);
                        count--;
                    }
                }
                return list.toArray(newElementArray(list.size()));
            }
        }
        return OpenAdressingEntry.EMPTY_ARRAY;
    }

    public OpenAdressingEntry<K, V> get(Predicate<CacheEntry<K, V>> filter, Object key) {
        OpenAdressingEntry<K, V> e = get(key);
        return e != null && (filter == null || filter.op(e)) ? e : null;
    }

    public Collection<?> getChildServices() {
        return Arrays.asList(policy);
    }

    public Set<Attribute<?>> getRuntimeConfigurableAttributes() {
        return new HashSet<Attribute<?>>(Arrays.<Attribute<?>> asList(MemoryStoreAttributes.IS_DISABLED,
                MemoryStoreAttributes.MAX_SIZE, MemoryStoreAttributes.MAX_VOLUME));
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isEmpty(final Predicate<CacheEntry<K, V>> filter) {
        if (filter == null) {
            return isEmpty();
        }
        OpenAdressingEntry<K, V>[] table = this.table;
        int len = table.length;
        for (int i = 0; i < len; i++) {
            OpenAdressingEntry<K, V> e = table[i];
            if (e != null && e != TOMBSTONE && filter.op(e)) {
                return false;
            }
        }
        return true;
    }

    public Iterator<CacheEntry<K, V>> iterator(Predicate<CacheEntry<K, V>> predicate) {
        return (Iterator) new BaseIterator(predicate);
    }

    protected void iteratorRemove(OpenAdressingEntry<K, V> entry) {
        size--;
        removeEntry(entry, false);
    }

    /**
     * Create a new element array
     * 
     * @param s
     *            the number of elements
     * @return Reference to the element array
     */
    private OpenAdressingEntry<K, V>[] newElementArray(int s) {
        return new OpenAdressingEntry[s];
    }

    protected OpenAdressingEntry<K, V> newEntry(K key, int hash, V value, MutableAttributeMap attributes) {
        return new TmpOpenAdressingEntry<K, V>(key, hash, value, attributes);
    }

    public void print() {
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                System.out.println(i + " null");
            } else if (table[i] == TOMBSTONE) {
                System.out.println(i + " tombstone");
            } else {
                System.out.println(i + " " + table[i].key);
            }
        }
    }

    public void process(AddEntryRequest<K, V> request) {
        if (isDisabled) {
            return;
        }
        K key = request.getKey();
        OpenAdressingEntry<K, V>[] table = this.table;
        int hash = hash(key);
        int len = table.length;
        if ((size << 1) > len) {
            rehash(false);
            process(request);
            return;
        }
        int mask = len - 1;
        int idx = hash & mask;
        int limit = (idx + reprobeLimit(len)) & mask;
        int firstTombstone = -1;
        OpenAdressingEntry<K, V> e = table[idx];
        int reprobes = 0;
        while (e != null) {
            if (e != TOMBSTONE) {
                K k = e.key;
                if (k == key || (hash == e.hash && k.equals(key))) {
                    OpenAdressingEntry<K, V> add = add(request, key, hash, e);
                    if (add == null) {
                        table[idx] = TOMBSTONE;
                    } else if (add != e) {
                        table[idx] = add;
                        trim(request);
                    }
                    return;
                    // Check reprobe
                }
            } else if (firstTombstone == -1) {
                firstTombstone = idx;
            }

            idx = (idx + 1) & mask; // Reprobe by 1
            e = table[idx];
            if (reprobes == limit) {
                rehash(false);
                process(request);
                return;
            }
            reprobes++;
        }
        if (firstTombstone > -1) {
            idx = firstTombstone;
        }
        OpenAdressingEntry<K, V> add = add(request, key, hash, null);
        if (add != null) {
            table[idx] = add;
            trim(request);
        }
    }

    private void rehash(boolean isCleanup) {
        OpenAdressingEntry<K, V>[] table = this.table;
        int size = this.size; // Get current size
        int newlength = isCleanup ? table.length : table.length << 1;
        if (size >= (table.length >> 1)) {
            newlength = newlength << 1; // Double up again
        }

        OpenAdressingEntry<K, V>[] newtable = newElementArray(newlength);
        int len = newtable.length;

        for (int i = 0; i < table.length; i++) {
            OpenAdressingEntry<K, V> e = table[i];
            if (e != null && e != TOMBSTONE) {
                int hash = e.hash;
                int idx = hash & (len - 1);
                while (newtable[idx] != null) {
                    idx = (idx + 1) & (len - 1); // Reprobe by 1
                }
                newtable[idx] = e;
            }
        }
        this.table = newtable;
    }

    public OpenAdressingEntry<K, V> remove(K key) {
        OpenAdressingEntry<K, V>[] table = this.table;
        int hash = hash(key);
        int mask = table.length - 1;
        int idx = hash & mask;
        OpenAdressingEntry<K, V> e = table[idx];
        while (e != null) {
            if (e != TOMBSTONE) {
                K k = e.key;
                if (k == key || (hash == e.hash && k.equals(key))) {
                    // If next is not null set to tombstone
                    table[idx] = table[(idx + 1) & mask] == null ? null : TOMBSTONE;
                    size--;
                    return e;
                }
            }
            idx = (idx + 1) & mask; // Reprobe by 1
            e = table[idx];
        }
        return null;
    }

    private void removeEntry(OpenAdressingEntry<K, V> entry, boolean isEvicted) {
        if (volumeEnabled) {
            volume -= entry.get(SIZE);
        }
        if (!isEvicted && policy != null) {
            policy.remove(entry);
        }
    }

    protected void resized(OpenAdressingEntry<K, V>[] newEntries) {
    }

    public int size() {
        return size;
    }

    public int size(final Predicate<CacheEntry<K, V>> filter) {
        if (filter == null) {
            return size();
        }
        int size = 0;
        OpenAdressingEntry<K, V>[] table = this.table;
        for (int i = 0; i < table.length; i++) {
            OpenAdressingEntry<K, V> e = table[i];
            if (e != null && e != TOMBSTONE && filter != null && filter.op(e)) {
                size++;
            }
        }
        return size;
    }

    public OpenAdressingEntry<K, V>[] toArray() {
        OpenAdressingEntry<K, V>[] all = newElementArray(size);
        OpenAdressingEntry<K, V>[] table = this.table;
        int count = 0;
        int len = table.length;
        for (int i = 0; i < len; i++) {
            OpenAdressingEntry<K, V> e = table[i];
            if (e != null && e != TOMBSTONE) {
                all[count++] = e;
            }
        }
        return all;
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

    static int hash(Object key) {
        int h = key.hashCode();
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private static final int reprobeLimit(int len) {
        // System.out.println("limit:" +len + " " + (REPROBE_LIMIT + (len >> 2)));
        return REPROBE_LIMIT + (len >> 2);
    }

    class BaseIterator implements Iterator<OpenAdressingEntry<K, V>> {
        private OpenAdressingEntry<K, V> entry;
        private int index;

        private OpenAdressingEntry<K, V> nextEntry;

        private int nextIndex = -1;

        private final Predicate<CacheEntry<K, V>> predicate;

        BaseIterator(Predicate<CacheEntry<K, V>> predicate) {
            this.predicate = predicate;
            if (size > 0) {
                findNextBucket();
            }
        }

        private void findNextBucket() {
            OpenAdressingEntry<K, V>[] table = OpenAdressingMemoryStore.this.table;
            while (++nextIndex < table.length) {
                nextEntry = table[nextIndex];
                if (nextEntry != null && nextEntry != TOMBSTONE && (predicate == null || predicate.op(nextEntry))) {
                    break;
                }
            }
        }

        public boolean hasNext() {
            return nextEntry != null;
        }

        /** @see java.util.Iterator#next() */
        public OpenAdressingEntry<K, V> next() {
            OpenAdressingEntry<K, V> e = nextEntry;
            entry = nextEntry;
            index = nextIndex;
            if (e == null)
                throw new NoSuchElementException();
            nextEntry = null;
            findNextBucket();
            return e;
        }

        /** @see java.util.Iterator#remove() */
        public void remove() {
            if (entry == null)
                throw new IllegalStateException();
            size--;
            removeEntry(entry, false);

            entry = null;
            table[index] = TOMBSTONE;
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
