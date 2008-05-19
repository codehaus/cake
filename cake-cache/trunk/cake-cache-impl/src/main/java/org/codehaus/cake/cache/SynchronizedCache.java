/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.CacheServices;
import org.codehaus.cake.cache.loading.CacheLoadingService;
import org.codehaus.cake.cache.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.SynchronizedInternalCache;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ServiceManager;
import org.codehaus.cake.service.executor.ExecutorsService;

/**
 * A <tt>synchronized</tt> {@link Cache} implementation.
 * <p>
 * It is imperative that the user manually synchronize on the cache when iterating over any of its collection views:
 * 
 * <pre>
 *  Cache c = new SynchronizedCache();
 *      ...
 *  Set s = c.keySet();  // Needn't be in synchronized block
 *      ...
 *  synchronized(c) {  // Synchronizing on c, not s!
 *      Iterator i = s.iterator(); // Must be in synchronized block
 *      while (i.hasNext())
 *          foo(i.next());
 *  }
 * </pre>
 * 
 * Failure to follow this advice may result in non-deterministic behavior.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: SynchronizedCache.java 560 2008-01-09 16:58:56Z kasper $
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */
@Container.SupportedServices( { MemoryStoreService.class, CacheLoadingService.class, ServiceManager.class,
        ExecutorsService.class, Manageable.class })
public class SynchronizedCache<K, V> extends AbstractMap<K, V> implements Cache<K, V> {
    private final SynchronizedInternalCache<K, V> cache;

    /**
     * Creates a new UnsynchronizedCache with a default configuration.
     */
    public SynchronizedCache() {
        this(CacheConfiguration.<K, V> newConfiguration());
    }

    /**
     * Creates a new UnsynchronizedCache from the specified configuration.
     * 
     * @param conf
     *            the configuration to create the cache from
     * @throws NullPointerException
     *             if the specified configuration is <code>null</code>
     */
    public SynchronizedCache(CacheConfiguration<K, V> conf) {
        this.cache = SynchronizedInternalCache.create(conf, this);
    }

    /** {@inheritDoc} */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return cache.awaitTermination(timeout, unit);
    }

    /** {@inheritDoc} */
    public void clear() {
        cache.clear();
    }

    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        return cache.containsValue(value);
    }

    /** {@inheritDoc} */
    public Set<Entry<K, V>> entrySet() {
        return cache.entrySet();
    }


    /** {@inheritDoc} */
    public V get(Object key) {
        return cache.get(key);
    }

    /** {@inheritDoc} */
    public Map<K, V> getAll(Collection<? extends K> keys) {
        return cache.getAll(keys);
    }

    /** {@inheritDoc} */
    public Map<Class<?>, Object> getAllServices() {
        return cache.getAllServices();
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> getEntry(K key) {
        return cache.getEntry(key);
    }

    /** {@inheritDoc} */
    public String getName() {
        return cache.getName();
    }

    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType) {
        return cache.getService(serviceType);
    }

    /** {@inheritDoc} */
    public boolean hasService(Class<?> serviceType) {
        return cache.hasService(serviceType);
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    /** {@inheritDoc} */
    public boolean isShutdown() {
        return cache.isShutdown();
    }

    /** {@inheritDoc} */
    public boolean isStarted() {
        return cache.isStarted();
    }

    /** {@inheritDoc} */
    public boolean isTerminated() {
        return cache.isTerminated();
    }

    /** {@inheritDoc} */
    public Set<K> keySet() {
        return cache.keySet();
    }

    /** {@inheritDoc} */
    public V peek(K key) {
        return cache.peek(key);
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> peekEntry(K key) {
        return cache.peekEntry(key);
    }

    /** {@inheritDoc} */
    public void prestart() {
        cache.prestart();
    }

    /** {@inheritDoc} */
    public V put(K key, V value) {
        return cache.put(key, value);
    }

    /** {@inheritDoc} */
    public void putAll(Map<? extends K, ? extends V> t) {
        cache.putAll(t);
    }

    /** {@inheritDoc} */
    public V putIfAbsent(K key, V value) {
        return cache.putIfAbsent(key, value);
    }

    /** {@inheritDoc} */
    public V remove(Object key) {
        return cache.remove(key);
    }

    /** {@inheritDoc} */
    public boolean remove(Object key, Object value) {
        return cache.remove(key, value);
    }

    /** {@inheritDoc} */
    public void removeAll(Collection<? extends K> keys) {
        cache.removeAll(keys);
    }

    /** {@inheritDoc} */
    public V replace(K key, V value) {
        return cache.replace(key, value);
    }

    /** {@inheritDoc} */
    public boolean replace(K key, V oldValue, V newValue) {
        return cache.replace(key, oldValue, newValue);
    }

    /** {@inheritDoc} */
    public void shutdown() {
        cache.shutdown();
    }

    /** {@inheritDoc} */
    public void shutdownNow() {
        cache.shutdownNow();
    }

    /** {@inheritDoc} */
    public int size() {
        return cache.size();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized String toString() {
        return super.toString();
    }

    /** {@inheritDoc} */
    public Collection<V> values() {
        return cache.values();
    }

    /** {@inheritDoc} */
    public CacheServices<K, V> with() {
        return cache.with();
    }
}
