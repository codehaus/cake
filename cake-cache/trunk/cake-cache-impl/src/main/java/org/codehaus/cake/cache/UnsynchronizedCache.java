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

import java.util.Iterator;

import org.codehaus.cake.cache.SynchronizedCache.SelectedCache;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.processor.DefaultCacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.UnsynchronizedCacheProcessor;
import org.codehaus.cake.internal.cache.service.attribute.MemorySparseAttributeService;
import org.codehaus.cake.internal.cache.service.loading.DefaultCacheLoadingService;
import org.codehaus.cake.internal.cache.service.loading.UnsynchronizedCacheLoader;
import org.codehaus.cake.internal.cache.service.memorystore.ExportedMemoryStoreService;
import org.codehaus.cake.internal.cache.service.memorystore.HashMapMemoryStore;
import org.codehaus.cake.internal.cache.service.memorystore.views.UnsynchronizedCollectionViewFactory;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.UnsynchronizedRunState;
import org.codehaus.cake.internal.service.configuration.SynchronizedConfigurationService;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.Container;

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
@Container.SupportedServices( { MemoryStoreService.class, CacheLoadingService.class})
public class UnsynchronizedCache<K, V> extends AbstractCache<K, V> {

    /** Creates a new UnsynchronizedCache with default configuration. */
    public UnsynchronizedCache() {
        this(CacheConfiguration.<K, V> newConfiguration());
    }

    /**
     * Creates a new UnsynchronizedCache with the specified configuration.
     * 
     * @param configuration
     *            the configuration
     */
    public UnsynchronizedCache(CacheConfiguration<K, V> configuration) {
        super(createComposer(configuration));
    }

    /**
     * Creates a new UnsynchronizedCache from the specified configuration.
     * 
     * @param configuration
     *            the cache configuration to create the cache from
     * @param <K>
     *            the types of key maintained by the cache
     * @param <V>
     *            the types of values maintained by the cache
     * @return a new UnsynchronizedCache
     */
    public static <K, V> UnsynchronizedCache<K, V> from(CacheConfiguration<K, V> configuration) {
        return new UnsynchronizedCache<K, V>(configuration);
    }

    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        for (V v : values()) {
            if (v.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    public Iterator<CacheEntry<K, V>> iterator() {
        lazyStart();
        return memoryCache.iterator(null);
    }

    private static Composer createComposer(CacheConfiguration<?, ?> configuration) {
        Composer composer = newComposer(configuration);

        // Common components
        composer.registerImplementation(UnsynchronizedRunState.class);
        if (configuration.withManagement().isEnabled()) {
            throw new IllegalArgumentException("Cache does not support Management");
        }

        // Cache components
        composer.registerImplementation(ExportedMemoryStoreService.class);
        composer.registerImplementation(UnsynchronizedCollectionViewFactory.class);
        composer.registerImplementation(SynchronizedConfigurationService.class);
        composer.registerImplementation(DefaultCacheRequestFactory.class);
        composer.registerImplementation(UnsynchronizedCacheProcessor.class);

        // composer.registerImplementation(DefaultAttributeService.class);
        composer.registerImplementation(MemorySparseAttributeService.class);

        if (configuration.withLoading().getLoader() != null) {
            composer.registerImplementation(UnsynchronizedCacheLoader.class);
            composer.registerImplementation(DefaultCacheLoadingService.class);
        }
        return composer;
    }
    

    public CacheSelector<K, V> select() {
        return new AbstractCacheSelector<K, V>() {
            public Cache<K, V> on(Predicate<CacheEntry<K, V>> filter) {
                return new SelectedCache(filter);
            }
        };
    }

    @SuppressWarnings("unchecked")
    class SelectedCache extends AbstractCache.AbstractSelectedCache {
        SelectedCache(Predicate<CacheEntry<K, V>> filter) {
            super(filter);
        }

        public CacheSelector<K, V> select() {
            return new AbstractCacheSelector<K, V>() {
                public Cache<K, V> on(Predicate<CacheEntry<K, V>> selector) {
                    return new SelectedCache(Predicates.and(filter,selector));
                }
            };
        }
    }
}
