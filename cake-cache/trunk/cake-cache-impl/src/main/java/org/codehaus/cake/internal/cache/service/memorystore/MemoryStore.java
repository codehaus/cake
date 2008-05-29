package org.codehaus.cake.internal.cache.service.memorystore;

import java.util.Collection;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.forkjoin.collections.ParallelArray;
import org.codehaus.cake.internal.cache.util.EntryPair;
import org.codehaus.cake.ops.Ops.Predicate;

public interface MemoryStore<K, V> extends Iterable<CacheEntry<K, V>> {

    /**
     * This method should be called while holding a lock.
     * 
     * @param key
     * @return
     */
    CacheEntry<K, V> get(Object key);

    CacheEntry<K, V> peek(Object key);

    CacheEntry<K, V> remove(Object key, Object value);

    ParallelArray<CacheEntry<K, V>> removeAll(Collection entries);

    ParallelArray<CacheEntry<K, V>> trim();

    CacheEntry<K, V> removeAny(Predicate<? super CacheEntry<K, V>> selector);

    ParallelArray<CacheEntry<K, V>> removeEntries(Collection entries);

    EntryPair put(K key, V value, AttributeMap attributes, boolean OnlyIfAbsent);

    EntryPair replace(K key, V oldValue, V newValue, AttributeMap attributes);

    Map<CacheEntry<K, V>, CacheEntry<K, V>> putAllWithAttributes(Map<K, Map.Entry<V, AttributeMap>> data);

    ParallelArray<CacheEntry<K, V>> removeValues(Collection entries);

    ParallelArray<CacheEntry<K, V>> removeAll();

    int getSize();
}
