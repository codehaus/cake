/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.internal.util.CollectionUtils;

/**
 * Various Factory and utility methods.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheServices.java 469 2007-11-17 14:32:25Z kasper $
 */
public final class Caches {

    /**
     * The empty cache (immutable). This cache is serializable.
     * 
     * @see #emptyCache()
     */
    final static Cache EMPTY_CACHE = new EmptyCache();

    /**
     * Returns the empty cache (immutable). This cache is serializable.
     * <p>
     * This example illustrates the type-safe way to obtain an empty cache:
     * 
     * <pre>
     * Cache&lt;Integer, String&gt; c = Caches.emptyCache();
     * </pre>
     * 
     * Implementation note: Implementations of this method need not create a separate <tt>Cache</tt>
     * object for each call. Using this method is likely to have comparable cost to using the
     * like-named field. (Unlike this method, the field does not provide type safety.)
     * 
     * @see #EMPTY_CACHE
     */
    public static <K, V> Cache<K, V> emptyCache() {
        return EMPTY_CACHE;
    }

    /**
     * Returns a Runnable that when executed will call the clear method on the specified cache.
     * <p>
     * The following example shows how this can be used to clear the cache every hour.
     * 
     * <pre>
     * Cache c;
     * ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
     * ses.scheduleAtFixedRate(Caches.runClear(c), 0, 60 * 60, TimeUnit.SECONDS);
     * </pre>
     * 
     * @param cache
     *            the cache on which to call evict
     * @return a runnable where invocation of the run method will clear the specified cache
     * @throws NullPointerException
     *             if the specified cache is <tt>null</tt>.
     */
    public static Runnable clearAsRunnable(Cache<?, ?> cache) {
        return new ClearRunnable(cache);
    }

    // /CLOVER:OFF
    /** Cannot instantiate. */
    private Caches() {}

    // /CLOVER:ON

    /**
     * A runnable used for calling clear on a cache.
     */
    static class ClearRunnable implements Runnable {

        /** The cache to call clear on. */
        private final Cache<?, ?> cache;

        /**
         * Creates a new ClearRunnable.
         * 
         * @param cache
         *            the cache to call clear on
         */
        ClearRunnable(Cache<?, ?> cache) {
            if (cache == null) {
                throw new NullPointerException("cache is null");
            }
            this.cache = cache;
        }

        /** {@inheritDoc} */
        public void run() {
            cache.clear();
        }
    }

    /**
     * The empty cache. 
     */
    static class EmptyCache<K, V> extends AbstractMap<K, V> implements Cache<K, V>, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = -5245003832315997155L;

        /** {@inheritDoc} */
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }

        /** {@inheritDoc} */
        @Override
        public Set<java.util.Map.Entry<K, V>> entrySet() {
            return Collections.EMPTY_MAP.entrySet();
        }

        /** {@inheritDoc} */
        public Map<K, V> getAll(Collection<? extends K> keys) {
            CollectionUtils.checkCollectionForNulls(keys);
            Map<K, V> result = new HashMap<K, V>(keys.size());
            for (K key : keys) {
                result.put(key, null);
            }
            return result;
        }

        /** {@inheritDoc} */
        public Map<Class<?>, Object> getAllServices() {
            return Collections.emptyMap();
        }

        /** {@inheritDoc} */
        public CacheEntry<K, V> getEntry(K key) {
            return null;
        }

        /** {@inheritDoc} */
        public String getName() {
            return "emptymap";
        }

        /** {@inheritDoc} */
        public <T> T getService(Class<T> serviceType) {
            if (serviceType == null) {
                throw new NullPointerException("serviceType is null");
            }
            T t = (T) getAllServices().get(serviceType);
            if (t == null) {
                throw new IllegalArgumentException("Unknown service " + serviceType);
            }
            return t;
        }

        /** {@inheritDoc} */
        public boolean hasService(Class<?> serviceType) {
            return getAllServices().containsKey(serviceType);
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
        public V peek(K key) {
            return null;
        }

        /** {@inheritDoc} */
        public CacheEntry<K, V> peekEntry(K key) {
            return null;
        }

        /** {@inheritDoc} */
        public V putIfAbsent(K key, V value) {
            throw new UnsupportedOperationException();
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return EMPTY_CACHE;
        }

        /** {@inheritDoc} */
        public boolean remove(Object key, Object value) {
            return false;
        }

        /** {@inheritDoc} */
        public void removeAll(Collection<? extends K> keys) {

        }

        /** {@inheritDoc} */
        public V replace(K key, V value) {
            throw new UnsupportedOperationException();// ??
        }

        /** {@inheritDoc} */
        public boolean replace(K key, V oldValue, V newValue) {
            return false;
        }

        public CacheServices<K, V> services() {
            return new CacheServices<K, V>(this);
        }

        /** {@inheritDoc} */
        public void shutdown() {

        }

        /** {@inheritDoc} */
        public void shutdownNow() {

        }

        /** {@inheritDoc} */
        public long volume() {
            return 0;
        }

        /** {@inheritDoc} */
        public CacheServices<K, V> with() {
            return new CacheServices<K, V>(this);
        }
    }
}
