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
import org.codehaus.cake.internal.cache.service.memorystore.SynchronizedHashMapMemoryStore;
import org.codehaus.cake.internal.cache.service.memorystore.views.SynchronizedCollectionViews;
import org.codehaus.cake.internal.cache.util.EntryPair;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.UnsynchronizedRunState;
import org.codehaus.cake.internal.service.executor.DefaultExecutorsService;
import org.codehaus.cake.internal.service.management.DefaultManagementService;
import org.codehaus.cake.internal.util.CollectionUtils;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ServiceManager;
import org.codehaus.cake.service.executor.ExecutorsService;

@Container.SupportedServices( { MemoryStoreService.class, CacheLoadingService.class, ServiceManager.class,
        ExecutorsService.class, Manageable.class })
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

    public Map<K, V> getAll(Collection<? extends K> keys) {
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

    public CacheEntry<K, V> put(K key, V value, AttributeMap attributes) {
        checkPut(key, value, attributes);
        return put(key, value, attributes, false);
    }

    private CacheEntry<K, V> put(K key, V value, AttributeMap attributes, boolean OnlyIfAbsent) {
        // TODO sync
        lazyStartFailIfShutdown();
        long started = listener.beforePut(key, value, false);

        EntryPair<K, V> prev = memoryCache.put(key, value, attributes, OnlyIfAbsent);
        ParallelArray<CacheEntry<K, V>> trimmed = memoryCache.trim();

        listener.afterPut(started, trimmed.asList(), (InternalCacheEntry) prev.getPrevious(), (InternalCacheEntry) prev
                .getNew(), false);
        return prev.getPrevious();
    }

    public void putAllWithAttributes(Map<K, Map.Entry<V, AttributeMap>> data) {
        // TODO sync
        long started = listener.beforePutAll(null, null, false);

        lazyStartFailIfShutdown();
        for (Map.Entry<K, Map.Entry<V, AttributeMap>> entry : data.entrySet()) {
            if (entry.getKey() == null) {
                throw new NullPointerException();
            }
            if (entry.getValue().getKey() == null) {
                throw new NullPointerException();
            }
        }
        Map<CacheEntry<K, V>, CacheEntry<K, V>> result = memoryCache.putAllWithAttributes(data);
        ParallelArray<CacheEntry<K, V>> trimmed = memoryCache.trim();

        listener.afterPutAll(started, trimmed.asList(), (Map) result, false);
    }

    public V putIfAbsent(K key, V value) {
        checkKeyValue(key, value);
        CacheEntry<K, V> prev = put(key, value, Attributes.EMPTY_ATTRIBUTE_MAP, true);
        return prev == null ? null : prev.getValue();
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
        long started = listener.beforeRemoveAll((Collection) keys);

        lazyStart();
        ParallelArray<CacheEntry<K, V>> list = memoryCache.removeAll(keys);

        listener.afterRemoveAll(started, (Collection) keys, list.asList());

        // return list.size() > 0;
    }

    /** {@inheritDoc} */
    private CacheEntry<K, V> removeByKey(Object key, Object value) {
        long started = listener.beforeRemove(key, value);
        // TODO sync
        lazyStart();
        CacheEntry<K, V> e = memoryCache.remove(key, value);

        listener.afterRemove(started, e);
        return e;
    }

    // public boolean removeEntries(Collection<?> entries) {
    // if (entries == null) {
    // throw new NullPointerException("collection is null");
    // }
    // // TODO sync
    // CollectionUtils.checkCollectionForNulls(entries);
    // long started = listener.beforeRemoveAll((Collection) entries);
    //
    // lazyStart();
    // ParallelArray<CacheEntry<K, V>> list = memoryCache.removeEntries(entries);
    //
    // listener.afterRemoveAll(started, (Collection) entries, list.asList());
    //
    // return list.size() > 0;
    // }
    //
    // public boolean removeKeys(Collection<?> keys) {
    // if (keys == null) {
    // throw new NullPointerException("collection is null");
    // }
    // // TODO sync
    // CollectionUtils.checkCollectionForNulls(keys);
    // long started = listener.beforeRemoveAll((Collection) keys);
    //
    // lazyStart();
    // ParallelArray<CacheEntry<K, V>> list = memoryCache.removeAll(keys);
    //
    // listener.afterRemoveAll(started, (Collection) keys, list.asList());
    //
    // return list.size() > 0;
    // }
    //
    // public boolean removeValue(Object value) {
    // long started = listener.beforeRemove(null, value);
    // // TODO sync
    // lazyStart();
    // CacheEntry<K, V> e = memoryCache.removeAny(Predicates.mapAndEvaluate(CollectionOps.MAP_ENTRY_TO_VALUE_OP,
    // Predicates.equalsTo(value)));
    //
    // listener.afterRemove(started, e);
    // return e != null;
    // }
    //
    // public boolean removeValues(Collection<?> values) {
    // if (values == null) {
    // throw new NullPointerException("collection is null");
    // }
    // // TODO sync
    // CollectionUtils.checkCollectionForNulls(values);
    // long started = listener.beforeRemoveAll((Collection) values);
    //
    // lazyStart();
    // ParallelArray<CacheEntry<K, V>> list = memoryCache.removeValues(values);
    //
    // listener.afterRemoveAll(started, (Collection) values, list.asList());
    //
    // return list.size() > 0;
    // }

    public V replace(K key, V value) {
        checkKeyValue(key, value);
        CacheEntry<K, V> prev = replace(key, null, value, Attributes.EMPTY_ATTRIBUTE_MAP).getPrevious();
        return prev == null ? null : prev.getValue();
    }

    public boolean replace(K key, V oldValue, V newValue) {
        checkReplace(key, oldValue, newValue);
        CacheEntry<K, V> newEntry = replace(key, oldValue, newValue, Attributes.EMPTY_ATTRIBUTE_MAP).getNew();
        return newEntry != null;
    }

    private EntryPair<K, V> replace(K key, V oldValue, V newValue, AttributeMap attributes) {
        synchronized (mutex) {
            lazyStartFailIfShutdown();
            EntryPair pair = memoryCache.replace(key, oldValue, newValue, attributes);
            return pair;
        }
    }

    public int size() {
        lazyStart();
        synchronized (mutex) {
            return memoryCache.getSize();
        }
    }

    public CacheEntry<K, V> valueLoaded(K key, V value, AttributeMap map) {
        // TODO sync
        if (value != null) {
            long started = listener.beforePut(key, value, false);

            EntryPair<K, V> prev = memoryCache.put(key, value, map, false);
            ParallelArray<CacheEntry<K, V>> trimmed = memoryCache.trim();

            listener.afterPut(started, trimmed.asList(), (InternalCacheEntry) prev.getPrevious(),
                    (InternalCacheEntry) prev.getNew(), false);
            return prev.getNew();
        }
        return null;
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
        composer.registerImplementation(DefaultExecutorsService.class);
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
