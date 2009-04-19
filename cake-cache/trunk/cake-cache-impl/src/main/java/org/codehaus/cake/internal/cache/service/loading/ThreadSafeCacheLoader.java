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
package org.codehaus.cake.internal.cache.service.loading;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.loading.CacheLoadingConfiguration;
import org.codehaus.cake.internal.cache.memorystore.MemoryStore;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;
import org.codehaus.cake.service.AfterStart;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.OnShutdown;
import org.codehaus.cake.service.OnTermination;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.DefaultAttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

public class ThreadSafeCacheLoader<K, V> extends AbstractCacheLoader<K, V> {

    private final Cache cache;
    private final ConcurrentHashMap<K, LoadableFutureTask<K, V>> futures = new ConcurrentHashMap<K, LoadableFutureTask<K, V>>();
    private final BlockingCacheLoader<K, V> loader;
    private final CacheRequestFactory<K, V> requestFactory;
    private final MemoryStore<K, V> store;
    /** The Executor responsible for doing the actual load. */
    private volatile Executor loadExecutor;

    public ThreadSafeCacheLoader(MemoryStore<K, V> store, CacheLoadingConfiguration<K, V> conf, Cache<K, V> cache,
            InternalCacheExceptionService<K, V> exceptionHandler, CacheRequestFactory<K, V> requestFactory) {
        super(exceptionHandler);
        this.cache = cache;
        this.loader = getSimpleLoader(conf);
        this.requestFactory = requestFactory;
        this.store = store;
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

            MutableAttributeMap map = new DefaultAttributeMap(attributes);
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
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        loadExecutor.execute(createFuture(key, attributes));
    }

    CacheEntry<K, V> loadFromFuture(K key, MutableAttributeMap map) {
        try {
            V value = doLoad(loader, key, map);
            // TODO cache = weak reference?, for ill behaving loaders?
            if (value != null) {
                AddEntryRequest<K, V> loaded = requestFactory.loaded(key, value, map);
                synchronized (cache) {
                    store.process(loaded);
                }
                return loaded.getNewEntry();
            }
            return null;
        } finally {
            futures.remove(key);
        }
    }

    @OnTermination
    public void dispose() {
        futures.clear();
    }

    @OnShutdown
    public void stop() {
        for (Future<?> f : futures.values()) {
            f.cancel(false);
        }
    }
}
