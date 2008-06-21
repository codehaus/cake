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

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;
import org.codehaus.cake.internal.cache.InternalCache;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;

public class UnsynchronizedCacheLoader<K, V> extends AbstractCacheLoader<K, V> {

    private final InternalCache<K, V> cache;

    private final SimpleCacheLoader<K, V> loader;

    public UnsynchronizedCacheLoader(InternalCache<K, V> cache, CacheLoadingConfiguration<K, V> conf,
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
