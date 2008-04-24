package org.codehaus.cake.internal.cache.service.loading;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;
import org.codehaus.cake.container.lifecycle.DisposableService;
import org.codehaus.cake.container.lifecycle.Stoppable;
import org.codehaus.cake.internal.cache.InternalCache;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;
import org.codehaus.cake.service.executor.ExecutorsService;

public class ThreadSafeCacheLoader<K, V> extends AbstractCacheLoader<K, V> implements
        DisposableService {

    private final InternalCache<K, V> cache;

    private final ConcurrentHashMap<K, LoadableFutureTask<K, V>> futures = new ConcurrentHashMap<K, LoadableFutureTask<K, V>>();
    private final SimpleCacheLoader<K, V> loader;

    /** The Executor responsible for doing the actual load. */
    private final Executor loadExecutor;

    public ThreadSafeCacheLoader(InternalCache<K, V> cache, CacheLoadingConfiguration<K, V> conf,
            InternalCacheExceptionService<K, V> exceptionHandler, ExecutorsService executorsService) {
        super(exceptionHandler);
        this.loader = getSimpleLoader(conf);
        this.loadExecutor = executorsService.getExecutorService(CacheLoadingService.class);
        this.cache = cache;
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

    @Override
    public CacheEntry<K, V> load(K key, AttributeMap attributes) {
        LoadableFutureTask<K, V> ft = createFuture(key, attributes);
        ft.run();
        return ft.getBlocking();
    }

    /** {@inheritDoc} */
    public void loadAsync(K key, AttributeMap attributes) {
        loadExecutor.execute(createFuture(key, attributes));
    }

    CacheEntry<K, V> loadFromFuture(K key, AttributeMap map) {
        try {
            V value = doLoad(loader, key, map);
            // TODO cache = weak reference?, for ill behaving loaders?
            return cache.valueLoaded(key, value, map);
        } finally {
            futures.remove(key);
        }
    }

    @Override
    public void dispose() {
        futures.clear();
    }

    @Stoppable
    public void stop() throws Exception {
        for (Future<?> f : futures.values()) {
            f.cancel(false);
        }
    }

}
