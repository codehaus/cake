package org.codehaus.cake.internal.cache.service.listener;

import java.util.Collection;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.InternalCacheEntry;

public class DefaultCacheListener<K, V> implements InternalCacheListener<K, V> {

    public void afterCacheClear(long timestamp, Collection<? extends CacheEntry<K, V>> entries) {
        // TODO Auto-generated method stub
        
    }

    public void afterPut(long started, Collection<? extends CacheEntry<K, V>> evictedEntries,
            InternalCacheEntry<K, V> oldEntry, InternalCacheEntry<K, V> newEntry, boolean fromLoader) {
        // TODO Auto-generated method stub
        
    }

    public void afterPutAll(long started, Collection<? extends CacheEntry<K, V>> evictedEntries,
            Map<InternalCacheEntry<K, V>, InternalCacheEntry<K, V>> newPrevEntries, boolean fromLoader) {
        // TODO Auto-generated method stub
        
    }

    public void afterRemove(long started, CacheEntry<K, V> entry) {
        // TODO Auto-generated method stub
        
    }

    public void afterRemoveAll(long start, Collection<? extends K> keys, Collection<CacheEntry<K, V>> removed) {
        // TODO Auto-generated method stub
        
    }

    public long beforeCacheClear() {
        // TODO Auto-generated method stub
        return 0;
    }

    public long beforePut(K key, V value, boolean fromLoader) {
        // TODO Auto-generated method stub
        return 0;
    }

    public long beforePutAll(Map<? extends K, ? extends V> map, Map<? extends K, AttributeMap> attributes,
            boolean fromLoader) {
        // TODO Auto-generated method stub
        return 0;
    }

    public long beforeRemove(Object key, Object value) {
        // TODO Auto-generated method stub
        return 0;
    }

    public long beforeRemoveAll(Collection<? extends K> keys) {
        // TODO Auto-generated method stub
        return 0;
    }

}
