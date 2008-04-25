/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.internal.cache.service.loading;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;

/**
 * Various utilities used for the loading service.
 * <p>
 * NOTICE: This is an internal class and should not be directly referred. No guarantee is made to
 * the compatibility of this class between different releases of Cake Cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LoadingUtils.java 526 2007-12-27 01:32:16Z kasper $
 */
public final class LoadingUtils {

    /** Cannot instantiate. */
    // /CLOVER:OFF
    private LoadingUtils() {}

    // /CLOVER:ON

    /**
     * Wraps a CacheLoadingService implementation such that only methods from the
     * CacheLoadingService interface is exposed.
     * 
     * @param service
     *            the CacheLoadingService to wrap
     * @return a wrapped service that only exposes CacheLoadingService methods
     * @param <K>
     *            the type of keys maintained by this service
     * @param <V>
     *            the type of mapped values
     */
    public static <K, V> CacheLoadingService<K, V> wrapService(CacheLoadingService<K, V> service) {
        return new DelegatedCacheLoadingService<K, V>(service);
    }

    /**
     * A wrapper class that exposes only the CacheLoadingService methods of a CacheLoadingService
     * implementation.
     */
    public static final class DelegatedCacheLoadingService<K, V> implements
            CacheLoadingService<K, V> {

        /** The CacheLoadingService that is wrapped. */
        private final CacheLoadingService<K, V> delegate;

        /**
         * Creates a wrapped CacheLoadingService from the specified implementation.
         * 
         * @param service
         *            the CacheLoadingService to wrap
         */
        public DelegatedCacheLoadingService(CacheLoadingService<K, V> service) {
            if (service == null) {
                throw new NullPointerException("service is null");
            }
            this.delegate = service;
        }

        public WithLoad<V> withAll() {
            return delegate.withAll();
        }

        public WithLoad<V> withAll(AttributeMap attributes) {
            return delegate.withAll(attributes);
        }

        public WithLoad<V> withKey(K key, AttributeMap attributes) {
            return delegate.withKey(key, attributes);
        }

        public WithLoad<V> withKey(K key) {
            return delegate.withKey(key);
        }

        public WithLoad<V> withKeys(Iterable<? extends K> keys, AttributeMap attributes) {
            return delegate.withKeys(keys, attributes);
        }

        public WithLoad<V> withKeys(Iterable<? extends K> keys) {
            return delegate.withKeys(keys);
        }
    }
}
