package org.codehaus.cake.cache.service.memorystore;

import org.codehaus.cake.cache.CacheEntry;

public interface CacheInsertPredicate<K, V> {
    boolean add(CacheEntry<K, V> entry);

    CacheEntry<K, V> replace(CacheEntry<K, V> entry);
}
