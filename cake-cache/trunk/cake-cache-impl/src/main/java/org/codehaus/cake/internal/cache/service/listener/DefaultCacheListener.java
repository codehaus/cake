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


    public void afterCachePurge(long start, Collection<? extends CacheEntry<K, V>> purgedEntries,
            int previousSize, int newSize) {
        // TODO Auto-generated method stub
        
    }


    public void afterContainerStart() {
        // TODO Auto-generated method stub
        
    }


    public void afterContainerStop() {
        // TODO Auto-generated method stub
        
    }


    public void afterGetAll(long started, Object[] keys, CacheEntry<K, V>[] entries,
            boolean[] isHit, boolean[] isExpired, Map<K, V> loadedEntries) {
        // TODO Auto-generated method stub
        
    }


    public void afterHit(long started, K key, CacheEntry<K, V> entry) {
        // TODO Auto-generated method stub
        
    }


    public void afterMiss(long started, K key, CacheEntry<K, V> previousEntry,
            CacheEntry<K, V> newEntry, boolean isExpired) {
        // TODO Auto-generated method stub
        
    }


    public void afterPut(long started, Collection<? extends CacheEntry<K, V>> evictedEntries,
            InternalCacheEntry<K, V> oldEntry, InternalCacheEntry<K, V> newEntry) {
        // TODO Auto-generated method stub
        
    }


    public void afterPut(long started, Collection<? extends CacheEntry<K, V>> evictedEntries,
            InternalCacheEntry<K, V> oldEntry, InternalCacheEntry<K, V> newEntry, boolean fromLoader) {
        // TODO Auto-generated method stub
        
    }


    public void afterPutAll(long started, Collection<? extends CacheEntry<K, V>> evictedEntries,
            Map<InternalCacheEntry<K, V>, InternalCacheEntry<K, V>> newPrevEntries,
            boolean fromLoader) {
        // TODO Auto-generated method stub
        
    }


    public void afterRemove(long started, CacheEntry<K, V> entry) {
        // TODO Auto-generated method stub
        
    }


    public void afterRemoveAll(long start, Collection<? extends K> keys,
            Collection<CacheEntry<K, V>> removed) {
        // TODO Auto-generated method stub
        
    }


    public void afterTrimCache(long started, Collection<? extends CacheEntry<K, V>> evictedEntries,
            int previousSize, int newSize) {
        // TODO Auto-generated method stub
        
    }


    public long beforeCacheClear() {
        // TODO Auto-generated method stub
        return 0;
    }


    public long beforeCachePurge() {
        // TODO Auto-generated method stub
        return 0;
    }


    public long beforeGet(K key) {
        // TODO Auto-generated method stub
        return 0;
    }


    public long beforeGetAll(Collection<? extends K> keys) {
        // TODO Auto-generated method stub
        return 0;
    }


    public long beforePut(K key, V value, boolean fromLoader) {
        // TODO Auto-generated method stub
        return 0;
    }


    public long beforePutAll(Map<? extends K, ? extends V> map,
            Map<? extends K, AttributeMap> attributes, boolean fromLoader) {
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


    public long beforeReplace(K key, V value) {
        // TODO Auto-generated method stub
        return 0;
    }


    public long beforeTrim(int newSize) {
        // TODO Auto-generated method stub
        return 0;
    }


    public void dexpired(long started, CacheEntry<K, V> entry) {
        // TODO Auto-generated method stub
        
    }

}
