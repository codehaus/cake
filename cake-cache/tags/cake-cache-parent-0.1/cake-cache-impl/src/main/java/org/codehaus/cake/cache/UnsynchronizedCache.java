/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.UnsynchronizedInternalCache;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ServiceManager;

/**
 * An <tt>unsynchronized</tt> {@link Cache} implementation.
 * <p>
 * If multiple threads access this cache concurrently, and at least one of the threads modifies the cache structurally,
 * it <i>must</i> be synchronized externally. (A structural modification is any operation that adds, deletes or changes
 * one or more mappings.) This is typically accomplished by synchronizing on some object that naturally encapsulates the
 * cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: UnsynchronizedCache.java 560 2008-01-09 16:58:56Z kasper $
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */
@Container.SupportedServices( { MemoryStoreService.class, CacheLoadingService.class, ServiceManager.class })
public class UnsynchronizedCache<K, V> implements Cache<K, V> {
    private final UnsynchronizedInternalCache<K, V> cache;

    /** Creates a new UnsynchronizedCache with a default configuration. */
    public UnsynchronizedCache() {
        this(CacheConfiguration.<K, V> newConfiguration());
    }

    /**
     * Creates a new UnsynchronizedCache from the specified configuration. The behavior of this constructor is undefined
     * if the specified configuration is modified while the construction is in progress.
     * 
     * @param conf
     *            the configuration to create the cache from
     * @throws NullPointerException
     *             if the specified configuration is <code>null</code>
     */
    public UnsynchronizedCache(CacheConfiguration<K, V> conf) {
        this.cache = new UnsynchronizedInternalCache<K, V>(conf, this);
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
    public String toString() {
        return cache.toString();
    }

    /** {@inheritDoc} */
    public Collection<V> values() {
        return cache.values();
    }

    /** {@inheritDoc} */
    public CacheServices<K, V> with() {
        return cache.with();
    }

    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType, AttributeMap attributes) {
        return cache.getService(serviceType, attributes);
    }

    public Set<Class<?>> serviceKeySet() {
        return cache.serviceKeySet();
    }
}
