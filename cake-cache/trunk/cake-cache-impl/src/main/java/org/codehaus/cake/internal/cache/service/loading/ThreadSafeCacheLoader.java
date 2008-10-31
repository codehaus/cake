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
package org.codehaus.cake.internal.cache.service.loading;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.loading.BlockingCacheLoader;
import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;
import org.codehaus.cake.internal.cache.service.memorystore.MemoryStore;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.annotation.AfterStart;
import org.codehaus.cake.service.annotation.Disposable;
import org.codehaus.cake.service.annotation.Stoppable;

public class ThreadSafeCacheLoader<K, V> extends AbstractCacheLoader<K, V> {

    private final ConcurrentHashMap<K, LoadableFutureTask<K, V>> futures = new ConcurrentHashMap<K, LoadableFutureTask<K, V>>();
    private final BlockingCacheLoader<K, V> loader;
    private final MemoryStore<K, V> store;
    private final CacheRequestFactory<K, V> requestFactory;

    /** The Executor responsible for doing the actual load. */
    private volatile Executor loadExecutor;

    public ThreadSafeCacheLoader(MemoryStore<K, V> store, CacheLoadingConfiguration<K, V> conf,
            InternalCacheExceptionService<K, V> exceptionHandler, CacheRequestFactory<K, V> requestFactory) {
        super(exceptionHandler);
        this.loader = getSimpleLoader(conf);
        this.store = store;
        this.requestFactory = requestFactory;
    }

    @AfterStart
    public void setExecutor(Container s) {
        // TODO fix after start
        this.loadExecutor = s.getService(ExecutorService.class);
    }

    private LoadableFutureTask<K, V> createFuture(K key, AttributeMap attributes) {
        LoadableFutureTask<K, V> future = futures.get(key);
        if (future == null) {
            // no load in progress, create new Future for load of key

            AttributeMap map = new DefaultAttributeMap(attributes);
            LoadableFutureTask<K, V> newFuture = new LoadableFutureTask<K, V>(this, key, map);
            // TODO mentally check scenarios
            future = futures.putIfAbsent(key, newFuture);
            // another thread might have created a future in the mean time
            if (future == null) {
                future = newFuture;
            }
        }
        return future;
    }

    public CacheEntry<K, V> load(K key, AttributeMap attributes) {
        LoadableFutureTask<K, V> ft = createFuture(key, attributes);
        ft.run();
        return ft.getBlocking();
    }

    /** {@inheritDoc} */
    public void loadAsync(K key, AttributeMap attributes) {
        if (key==null) {
            throw new NullPointerException("key is null");
        }
        loadExecutor.execute(createFuture(key, attributes));
    }

    CacheEntry<K, V> loadFromFuture(K key, AttributeMap map) {
        try {
            V value = doLoad(loader, key, map);
            // TODO cache = weak reference?, for ill behaving loaders?
            if (value != null) {
                AddEntryRequest<K, V> loaded = requestFactory.loaded(key, value, map);
                store.process(loaded);
                return loaded.getNewEntry();
            }
            return null;
        } finally {
            futures.remove(key);
        }
    }

    @Disposable
    public void dispose() {
        futures.clear();
    }

    @Stoppable
    public void stop() {
        for (Future<?> f : futures.values()) {
            f.cancel(false);
        }
    }
}
