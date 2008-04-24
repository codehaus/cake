/* Copyright 2004 - 2007 Kasper Nielsen <kasper@codehaus.org> Licensed under 
 * the Apache 2.0 License, see http://coconut.codehaus.org/license.
 */
package org.codehaus.cake.cache.test.operations;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.ops.Ops.Generator;

public abstract class AbstractOperation<K, V> implements Runnable {

    Cache<K, V> cache;

    Generator<K> keyGenerator;

    CacheLoadingService loadingService;

    MemoryStoreService evictionService;

    public void setCache(Cache<K, V> cache) {
        this.cache = cache;
        evictionService = cache.with().memoryStore();
        loadingService = cache.with().loading();
    }

    public void setKeyGenerator(Generator<K> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

}
