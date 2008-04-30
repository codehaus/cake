/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.internal.cache;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.management.DefaultManagementService;
import org.codehaus.cake.internal.service.spi.GlobalServiceMutex;

public abstract class SynchronizedInternalCache<K, V> extends AbstractInternalCache<K, V> {

    public static <K, V> SynchronizedInternalCache<K, V> create(
            CacheConfiguration<K, V> configuration, Cache<K, V> cache) {
        Composer composer = newComposer(configuration);
        composer.registerInstance(GlobalServiceMutex.class,GlobalServiceMutex.from(cache));

        // composer.registerInternalImplementation(SynchronizedMemoryStoreService.class);
        if (configuration.withManagement().isEnabled()) {
            composer.registerImplementation(DefaultManagementService.class);
        }
        return null; // new SynchronizedInternalCache<K, V>(composer, configuration);
    }

    private final Object mutex;

    SynchronizedInternalCache(Composer composer) {
        super(composer);
        this.mutex = composer.get(GlobalServiceMutex.class).getMutex();
    }

    // public void clear() {
    // long started = listener.beforeCacheClear();
    // Collection<? extends CacheEntry<K, V>> list = Collections.EMPTY_LIST;
    // long volume = 0;
    //
    // synchronized (mutex) {
    // lazyStart();
    // // list = memoryCache.clear();
    // }
    //
    // listener.afterCacheClear(started, list);
    // }
    //
    // public void clearView(Predicate p) {}
    //
    // @Override
    // public boolean containsKey(Object key) {
    // synchronized (mutex) {
    // return super.containsKey(key);
    // }
    // }
    //
    // @Override
    // public boolean containsValue(Object value) {
    // synchronized (mutex) {
    // //return super.containsValue(value);
    // return false;
    // }
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // CacheEntry<K, V> doRemove(Object key, Object value) {
    // long started = listener.beforeRemove(key, value);
    // CacheEntry<K, V> e = null;
    //
    // synchronized (mutex) {
    // lazyStart();
    // e = memoryCache.remove(key, value);
    // }
    //
    // listener.afterRemove(started, e);
    // return e;
    // }
    //
    // public V get(Object key) {
    // return null;
    // }
    //
    // public Map<K, V> getAll(Collection<? extends K> keys) {
    // return null;
    // }
    //
    // public CacheEntry<K, V> getEntry(K key) {
    // return null;
    // }
    //
    // @Override
    // public CacheEntry<K, V> peekEntry(K key) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // CacheEntry<K, V> put(K key, V value, AttributeMap attributes, boolean OnlyIfAbsent) {
    // return null;
    // }
    //
    // public void putAllWithAttributes(Map<K, java.util.Map.Entry<V, AttributeMap>> data) {}
    //
    // public boolean removeEntries(Collection<?> entries) {
    // return false;
    // }
    //
    // public boolean removeKeys(Collection<?> keys) {
    // return false;
    // }
    //
    // public boolean removeValue(Object value) {
    // long started = listener.beforeRemove(null, value);
    // CacheEntry<K, V> e = null;
    //
    // synchronized (mutex) {
    // lazyStart();
    // // e = memoryCache.removeValue(value);
    // }
    //
    // listener.afterRemove(started, e);
    // return e != null;
    // }
    //
    // public boolean removeValues(Collection<?> values) {
    // return false;
    // }
    //
    // public V replace(K key, V value) {
    // return null;
    // }
    //
    // // @Override
    // // public long volume() {
    // // synchronized (mutex) {
    // // return super.volume();
    // // }
    // // }
    //
    // public boolean replace(K key, V oldValue, V newValue) {
    // return false;
    // }
    //
    // public boolean retainAll(Op pre, Collection<?> c) {
    // return false;
    // }
    //
    // @Override
    // public int size() {
    // synchronized (mutex) {
    // return super.size();
    // }
    // }
    //
    // @Override
    // public CacheEntry<K, V> valueLoaded(K key, V value, AttributeMap map) {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // EntryPair<K, V> doReplace(K key, V oldValue, V newValue, AttributeMap attributes) {
    // // TODO Auto-generated method stub
    // return null;
    // }

}
