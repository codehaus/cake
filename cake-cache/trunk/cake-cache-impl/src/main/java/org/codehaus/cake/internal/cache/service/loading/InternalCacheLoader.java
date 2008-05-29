package org.codehaus.cake.internal.cache.service.loading;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;

public interface InternalCacheLoader<K, V> {

    void loadAsync(K key, AttributeMap attributes);

    void loadAsync(Map<? extends K, AttributeMap> map);

    // throws errors /runtime exceptions
    /**
     * <p>
     * This method can be called without holding a lock.
     * 
     * @param key
     * @param attributes
     * @return
     */
    CacheEntry<K, V> load(K key, AttributeMap attributes);
}
