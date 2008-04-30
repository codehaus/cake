/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.internal.cache.service.listener;

import java.util.Collection;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.InternalCacheEntry;
import org.codehaus.cake.internal.service.listener.InternalContainerListener;

/**
 * @param <K>
 * @param <V>
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: InternalCacheListener.java 556 2008-01-09 14:23:11Z kasper $
 */
public interface InternalCacheListener<K, V> extends InternalContainerListener {
    /**
     * Called after the specified cache was cleared.
     * 
     * @param internalCache
     *            the cache that was cleared
     * @param timestamp
     *            the timestamp that was returned by {@link #beforeCacheClear(Cache)}
     * @param entries
     *            the entries that was removed
     * @param previousVolume
     *            the previous volume of the cache
     */
    void afterCacheClear(long timestamp, Collection<? extends CacheEntry<K, V>> entries
            );

    void afterCachePurge(long start, Collection<? extends CacheEntry<K, V>> purgedEntries,
            int previousSize, int newSize );

    void afterGetAll(long started, Object[] keys, CacheEntry<K, V>[] entries, boolean[] isHit,
            boolean[] isExpired, Map<K, V> loadedEntries);

    void afterHit(long started, K key, CacheEntry<K, V> entry);

    void afterMiss(long started, K key, CacheEntry<K, V> previousEntry, CacheEntry<K, V> newEntry,
            boolean isExpired);

    void afterPut(long started, Collection<? extends CacheEntry<K, V>> evictedEntries,
            InternalCacheEntry<K, V> oldEntry, InternalCacheEntry<K, V> newEntry);

    void afterPut(long started, Collection<? extends CacheEntry<K, V>> evictedEntries,
            InternalCacheEntry<K, V> oldEntry, InternalCacheEntry<K, V> newEntry, boolean fromLoader);

    void afterPutAll(long started, Collection<? extends CacheEntry<K, V>> evictedEntries,
            Map<InternalCacheEntry<K, V>, InternalCacheEntry<K, V>> newPrevEntries,
            boolean fromLoader);

    void afterRemove(long started, CacheEntry<K, V> entry);

    void afterRemoveAll(long start, Collection<? extends K> keys,
            Collection<CacheEntry<K, V>> removed);

    void afterContainerStart();

    void afterContainerStop();

    void afterTrimCache(long started, Collection<? extends CacheEntry<K, V>> evictedEntries,
            int previousSize, int newSize);

    /**
     * Called before the cache was cleared.
     * 
     * @param internalCache
     *            the cache that was cleared
     * @return a timestamp
     */
    long beforeCacheClear();

    long beforeCachePurge();

    long beforeGet(K key);

    long beforeGetAll(Collection<? extends K> keys);

    long beforePut(K key, V value, boolean fromLoader);

    long beforePutAll(Map<? extends K, ? extends V> map, Map<? extends K, AttributeMap> attributes,
            boolean fromLoader);

    long beforeRemove(Object key, Object value);

    long beforeRemoveAll(Collection<? extends K> keys);

    long beforeReplace(K key, V value);

    long beforeTrim(int newSize);

    void dexpired(long started, CacheEntry<K, V> entry);

}
