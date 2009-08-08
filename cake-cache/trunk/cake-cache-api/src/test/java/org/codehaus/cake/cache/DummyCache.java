/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.view.MapView;

/**
 * A dummy implementation of a {@link Cache}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */
public class DummyCache<K, V> implements Cache<K, V> {

    /** The name of the cache. */
    private final String name;

    /** A list of service for the cache. */
    private final HashMap<Class, Object> services = new HashMap<Class, Object>();

    /**
     * Creates a new DummyCache with the default constructor.
     */
    public DummyCache() {
        this(CacheConfiguration.newConfiguration());
    }

    /**
     * Creates a new DummyCache.
     * 
     * @param configuration
     *            the cache configuration
     */
    public DummyCache(CacheConfiguration<?, ?> configuration) {
        this.name = configuration.getName();
    }

    /**
     * Adds a service that can later be retrieved from {@link #getService(Class)}.
     * 
     * @param key
     *            the key of the service
     * @param service
     *            the service to add
     */
    public void addService(Class key, Object service) {
        services.put(key, service);
    }

    /** {@inheritDoc} */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /** {@inheritDoc} */
    public void clear() {}

    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return false;
    }

    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        return false;
    }

    /** {@inheritDoc} */
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return null;
    }

    /** {@inheritDoc} */
    public V get(Object key) {
        return null;
    }

    /** {@inheritDoc} */
    public Map<K, V> getAllOld(Iterable<? extends K> keys) {
        return null;
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> getEntry(K key) {
        return null;
    }

    /** {@inheritDoc} */
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType) {
        return (T) services.get(serviceType);
    }

    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType, AttributeMap attributes) {
        return (T) services.get(serviceType);
    }

    /** {@inheritDoc} */
    public long volume() {
        return 0;
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean isShutdown() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean isStarted() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean isTerminated() {
        return false;
    }

    /** {@inheritDoc} */
    public Set<K> keySet() {
        return null;
    }

    /** {@inheritDoc} */
    public V peek(K key) {
        return null;
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> peekEntry(K key) {
        return null;
    }

    /** {@inheritDoc} */
    public V put(K key, V value) {
        return null;
    }

    /** {@inheritDoc} */
    public void putAll(Map<? extends K, ? extends V> t) {}

    /** {@inheritDoc} */
    public V putIfAbsent(K key, V value) {
        return null;
    }

    /** {@inheritDoc} */
    public V remove(Object key) {
        return null;
    }

    /** {@inheritDoc} */
    public boolean remove(Object key, Object value) {
        return false;
    }

    /** {@inheritDoc} */
    public V replace(K key, V value) {
        return null;
    }

    /** {@inheritDoc} */
    public boolean replace(K key, V oldValue, V newValue) {
        return false;
    }

    /** {@inheritDoc} */
    public void shutdown() {}

    /** {@inheritDoc} */
    public void shutdownNow() {}

    /** {@inheritDoc} */
    public long size() {
        return 0;
    }

    /** {@inheritDoc} */
    public Collection<V> values() {
        return null;
    }
    /** {@inheritDoc} */
    public CacheCrud<K, V> withCrud() {
        return new CacheCrud<K, V>(this);
    }
    
    public Iterator<CacheEntry<K, V>> iterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    /**
     * A Cache that is abstract.
     */
    public static abstract class CannotInstantiateAbstractCache extends DummyCache {

        /**
         * Create a new CannotInstantiateAbstractCache.
         * 
         * @param configuration
         *            the cache configuration
         */
        public CannotInstantiateAbstractCache(CacheConfiguration configuration) {
            super(configuration);
        }
    }

    /**
     * A Cache that throws an {@link ArithmeticException} in the constructor.
     */
    public static class ConstructorRuntimeThrowingCache extends DummyCache {

        /**
         * Create a new ConstructorThrowingCache.
         * 
         * @param configuration
         *            the cache configuration
         */
        public ConstructorRuntimeThrowingCache(CacheConfiguration configuration) {
            super(configuration);
            throw new ArithmeticException();
        }
    }

    /**
     * A Cache that throws an {@link ArithmeticException} in the constructor.
     */
    public static class ConstructorErrorThrowingCache extends DummyCache {

        /**
         * Create a new ConstructorThrowingCache.
         * 
         * @param configuration
         *            the cache configuration
         */
        public ConstructorErrorThrowingCache(CacheConfiguration configuration) {
            super(configuration);
            throw new AbstractMethodError();
        }
    }

    /**
     * A Cache that throws an {@link ArithmeticException} in the constructor.
     */
    public static class ConstructorExceptionThrowingCache extends DummyCache {

        /**
         * Create a new ConstructorThrowingCache.
         * 
         * @param configuration
         *            the cache configuration
         * @throws Exception
         *             construction failed
         */
        public ConstructorExceptionThrowingCache(CacheConfiguration configuration) throws Exception {
            super(configuration);
            throw new IOException();
        }
    }

    /**
     * A Cache that has a private constructor.
     */
    public static final class PrivateConstructorCache extends DummyCache {

        /**
         * Create a new PrivateConstructorCache.
         * 
         * @param configuration
         *            the cache configuration
         */
        private PrivateConstructorCache(CacheConfiguration configuration) {
            super(configuration);
        }
    }

    public CacheServices<K, V> services() {
        return new CacheServices<K, V>(this);
    }

    public CacheServices<K, V> with() {
        return new CacheServices<K, V>(this);
    }

    public Map<Class<?>, Object> getAllServices() {
        return Collections.EMPTY_MAP;
    }

    public boolean hasService(Class<?> serviceType) {
        return false;
    }

    public Set<Class<?>> serviceKeySet() {
        return Collections.emptySet();
    }

    public CacheSelector<K, V> filter() {
        return Caches.EMPTY_CACHE.filter();
    }


    public CacheView<K, V> view() {
        return Caches.EMPTY_CACHE.view();
    }

    public CacheView<K, V> getAll(Iterable<? extends K> keys) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    public ConcurrentMap<K, V> asMap() {
        return null;
    }

    public State getState() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean awaitState(State state, long timeout, TimeUnit unit) throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }
}
