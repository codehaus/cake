package org.codehaus.cake.cache.service.store;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;

public interface SimpleCacheStore<K, V> extends SimpleCacheLoader<K, V> {

    /**
     * <p>
     * If this method fails by throwing an exception the cache will assume that the value has not been erased.
     * 
     * @param key
     * @param value
     * @throws Exception
     */
    void erase(K key, V value) throws Exception;

    /**
     * <p>
     * If this method fails by throwing an exception the cache will assume that the value has not been stored.
     * 
     * @param entry
     *            the cacheentry to store
     * @throws Exception
     */
    void store(CacheEntry<K, V> entry) throws Exception;
}
