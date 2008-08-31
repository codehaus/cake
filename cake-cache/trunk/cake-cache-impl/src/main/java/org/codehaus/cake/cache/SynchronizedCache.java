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
import java.util.concurrent.ExecutorService;

import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.processor.DefaultCacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.SynchronizedCacheProcessor;
import org.codehaus.cake.internal.cache.service.attribute.MemorySparseAttributeService;
import org.codehaus.cake.internal.cache.service.loading.DefaultCacheLoadingService;
import org.codehaus.cake.internal.cache.service.loading.ThreadSafeCacheLoader;
import org.codehaus.cake.internal.cache.service.memorystore.ExportedMemoryStoreService;
import org.codehaus.cake.internal.cache.service.memorystore.SynchronizedHashMapMemoryStore;
import org.codehaus.cake.internal.cache.service.memorystore.views.SynchronizedCollectionViews;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.UnsynchronizedRunState;
import org.codehaus.cake.internal.service.configuration.SynchronizedConfigurationService;
import org.codehaus.cake.internal.service.executor.DefaultExecutorService;
import org.codehaus.cake.internal.service.executor.DefaultForkJoinPool;
import org.codehaus.cake.internal.service.executor.DefaultScheduledExecutorService;
import org.codehaus.cake.internal.service.management.DefaultManagementService;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ServiceManager;

/**
 * A <tt>synchronized</tt> {@link Cache} implementation.
 * <p>
 * It is imperative that the user manually synchronize on the cache when
 * iterating over any of its collection views:
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
@Container.SupportedServices( { ExecutorService.class, MemoryStoreService.class, CacheLoadingService.class,
        ServiceManager.class, Manageable.class })
public class SynchronizedCache<K, V> extends AbstractCache<K, V> {

    /** Creates a new SynchronizedCache with default configuration. */
    public SynchronizedCache() {
        this(CacheConfiguration.<K, V> newConfiguration());
    }
    /**
     * Creates a new SynchronizedCache with the specified configuration.
     * 
     * @param configuration
     *            the configuration
     */
    public SynchronizedCache(CacheConfiguration<K, V> configuration) {
        super(createComposer(configuration));
    }

    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        synchronized (this) {
            lazyStart();
            for ( V v : values()) {
                if (v.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    public Iterator<CacheEntry<K, V>> iterator() {
        lazyStart();
        return memoryCache.iterator();
    }

    private static Composer createComposer(CacheConfiguration<?, ?> configuration) {
        Composer composer = newComposer(configuration);
        composer.registerImplementation(SynchronizedHashMapMemoryStore.class);
        composer.registerImplementation(SynchronizedConfigurationService.class);
        composer.registerImplementation(DefaultCacheRequestFactory.class);
        composer.registerImplementation(SynchronizedCacheProcessor.class);
        composer.registerImplementation(ExportedMemoryStoreService.class);

        // Common components
        composer.registerImplementation(UnsynchronizedRunState.class);
        if (configuration.withManagement().isEnabled()) {
            composer.registerImplementation(DefaultManagementService.class);
        }

        // Cache components
        composer.registerImplementation(SynchronizedCollectionViews.class);
        // composer.registerImplementation(DefaultAttributeService.class);
        composer.registerImplementation(DefaultExecutorService.class);
        composer.registerImplementation(DefaultScheduledExecutorService.class);
        composer.registerImplementation(DefaultForkJoinPool.class);

        composer.registerImplementation(MemorySparseAttributeService.class);
        if (configuration.withLoading().getLoader() != null) {
            composer.registerImplementation(ThreadSafeCacheLoader.class);
            composer.registerImplementation(DefaultCacheLoadingService.class);
        }

        return composer;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        synchronized (this) {
            return super.toString();
        }
    }
}
