package org.codehaus.cake.internal.cache.service.loading;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.loading.SimpleCacheLoader;
import org.codehaus.cake.internal.cache.InternalCache;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;

public class UnsynchronizedCacheLoader<K, V> extends AbstractCacheLoader<K, V> {

    private final InternalCache<K, V> cache;

    private final SimpleCacheLoader<K, V> loader;

    public UnsynchronizedCacheLoader(InternalCache<K, V> cache,
            CacheLoadingConfiguration<K, V> conf,
            InternalCacheExceptionService<K, V> exceptionHandler) {
        super(exceptionHandler);
        loader = getSimpleLoader(conf);
        this.cache = cache;
    }

    public CacheEntry<K, V> load(K key, AttributeMap map) {
        map = new DefaultAttributeMap(map);
        V value = doLoad(loader, key, map);
        return cache.valueLoaded(key, value, map);
    }

    public void loadAsync(K key, AttributeMap map) {
        load(key, map);
    }
}
