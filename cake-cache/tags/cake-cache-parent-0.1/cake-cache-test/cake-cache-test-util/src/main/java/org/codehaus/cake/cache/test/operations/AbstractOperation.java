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
