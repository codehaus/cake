/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.operations;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.test.util.LoopHelpers;

public class CacheHarnessContext<K, V> {

    final LoopHelpers.SimpleRandom random = new LoopHelpers.SimpleRandom();

    private final Cache<K, V> cache;

    private final MemoryStoreService<K, V> evictionService;

    CacheHarnessContext(Cache<K, V> cache) {
        this.cache = cache;
        evictionService = cache.with().memoryStore();
    }

    final LoopHelpers.SimpleRandom rng = new LoopHelpers.SimpleRandom();

    public Cache<K, V> cache() {
        return cache;
    }

    public MemoryStoreService<K, V> eviction() {
        return evictionService;
    }

    public Integer randomKey() {
        return rng.next() % 1000;
    }

    public int randomInt() {
        return random.next();
    }

    public int randomInt(int n) {
        return random.nextInt(n);
    }
}
