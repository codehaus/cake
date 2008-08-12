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
package org.codehaus.cake.internal.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.forkjoin.collections.ParallelArray;
import org.codehaus.cake.internal.cache.service.attribute.MemorySparseAttributeService;
import org.codehaus.cake.internal.cache.service.loading.DefaultCacheLoadingService;
import org.codehaus.cake.internal.cache.service.loading.InternalCacheLoader;
import org.codehaus.cake.internal.cache.service.loading.ThreadSafeCacheLoader;
import org.codehaus.cake.internal.cache.service.memorystore.AddManyEntries;
import org.codehaus.cake.internal.cache.service.memorystore.AddSingleEntry;
import org.codehaus.cake.internal.cache.service.memorystore.SynchronizedHashMapMemoryStore;
import org.codehaus.cake.internal.cache.service.memorystore.views.SynchronizedCollectionViews;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.UnsynchronizedRunState;
import org.codehaus.cake.internal.service.executor.DefaultExecutorService;
import org.codehaus.cake.internal.service.executor.DefaultForkJoinPool;
import org.codehaus.cake.internal.service.executor.DefaultScheduledExecutorService;
import org.codehaus.cake.internal.service.management.DefaultManagementService;
import org.codehaus.cake.internal.util.CollectionUtils;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ServiceManager;

@Container.SupportedServices( { ExecutorService.class, MemoryStoreService.class, CacheLoadingService.class,
        ServiceManager.class, Manageable.class })
public class SynchronizedInternalCache<K, V> extends AbstractInternalCache<K, V> {

    InternalCacheLoader<K, V> loader;

    private final Object mutex;

    /**
     * Creates a new UnsynchronizedInternalCache with default configuration.
     */
    public SynchronizedInternalCache() {
        this(CacheConfiguration.<K, V> newConfiguration());
    }

    public SynchronizedInternalCache(CacheConfiguration<K, V> configuration) {
        this(createComposer(configuration), null);
    }

    public SynchronizedInternalCache(CacheConfiguration<K, V> configuration, Cache<K, V> wrapper) {
        this(createComposer(configuration, wrapper), wrapper);
    }

    private SynchronizedInternalCache(Composer composer, Object mutex) {
        super(composer);
        this.mutex = mutex == null ? this : mutex;
        loader = composer.getIfAvailable(InternalCacheLoader.class);
    }

    public void clear() {
        lazyStart();
        synchronized (mutex) {
            long started = listener.beforeCacheClear();
            ParallelArray<CacheEntry<K, V>> list = memoryCache.removeAll();

            listener.afterCacheClear(started, list.asList());
        }
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        lazyStart();
        synchronized (mutex) {
            for (CacheEntry<K, V> entry : memoryCache) {
                if (entry.getValue().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<K, V> getAll(Iterable<? extends K> keys) {
        HashMap<K, V> result = new HashMap<K, V>();
        for (K key : keys) {
            result.put(key, get(key));
        }
        return result;
    }

    public CacheEntry<K, V> getEntry(K key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        CacheEntry<K, V> entry = null;
        synchronized (mutex) {
            lazyStartFailIfShutdown();
            entry = memoryCache.get(key);
        }
        if (entry == null && loader != null) {
            entry = loader.load(key, Attributes.EMPTY_ATTRIBUTE_MAP);
        }
        return entry;
    }

    public Iterator<CacheEntry<K, V>> iterator() {
        lazyStart();
        return memoryCache.iterator();
    }

    public CacheEntry<K, V> peekEntry(K key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        lazyStart();
        synchronized (mutex) {
            return memoryCache.peek(key);
        }
    }

    public V remove(Object key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        CacheEntry<K, V> removed = null;
        synchronized (mutex) {
            removed = removeByKey(key, null);
        }
        return removed == null ? null : removed.getValue();
    }

    public boolean remove(Object key, Object value) {
        checkKeyValue(key, value);
        return removeByKey(key, value) != null;
    }

    public void removeAll(Collection<? extends K> keys) {
        if (keys == null) {
            throw new NullPointerException("collection is null");
        }
        CollectionUtils.checkCollectionForNulls(keys);
        // TODO sync
        long started = listener.beforeRemoveAll(keys);
        final Iterable<CacheEntry<K, V>> list;
        synchronized (mutex) {
            lazyStart();
            list = memoryCache.removeAll(keys);
        }

        listener.afterRemoveAll(started, keys, list);

        // return list.size() > 0;
    }

    /** {@inheritDoc} */
    private CacheEntry<K, V> removeByKey(Object key, Object value) {
        long started = listener.beforeRemove(key, value);
        // TODO sync
        final CacheEntry<K, V> e;
        synchronized (mutex) {
            lazyStart();
            if (value == null) {
                e = memoryCache.remove(key);
            } else {
                e = memoryCache.remove(key, value);
            }

        }
        listener.afterRemove(started, e);
        return e;
    }

    public void process(AddSingleEntry<K, V> entry) {
        listener.beforePut(entry);
        synchronized (mutex) {
            lazyStartFailIfShutdown();
            memoryCache.add(entry);
        }
        listener.afterPut(entry);
    }

    public void process(AddManyEntries<K, V> entry) {

        synchronized (mutex) {
            lazyStartFailIfShutdown();
            // listener.beforePut(entry);
            memoryCache.add(entry);
        }
        // listener.afterPut(entry);
    }

    public int size() {
        lazyStart();
        synchronized (mutex) {
            return memoryCache.getSize();
        }
    }

    private static Composer createComposer(CacheConfiguration<?, ?> configuration) {
        Composer composer = newComposer(configuration);
        composer.registerImplementation(SynchronizedHashMapMemoryStore.class);

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

    private static Composer createComposer(CacheConfiguration<?, ?> configuration, Cache<?, ?> cache) {
        Composer composer = createComposer(configuration);
        composer.registerInstance(Cache.class, cache);
        composer.registerInstance(Container.class, cache);
        return composer;
    }
}
