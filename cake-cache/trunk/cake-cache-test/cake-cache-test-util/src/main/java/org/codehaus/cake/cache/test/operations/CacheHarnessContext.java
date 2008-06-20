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
