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

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.loading.CacheLoadingConfiguration;
import org.codehaus.cake.internal.cache.memorystore.MemoryStore;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.DefaultAttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

public class UnsynchronizedCacheLoader<K, V> extends AbstractCacheLoader<K, V> {

    private final BlockingCacheLoader<K, V> loader;

    private final MemoryStore<K, V> store;
    private final CacheRequestFactory<K, V> requestFactory;

    public UnsynchronizedCacheLoader(MemoryStore<K, V> store, CacheLoadingConfiguration<K, V> conf,
            InternalCacheExceptionService<K, V> exceptionHandler, CacheRequestFactory<K, V> requestFactory) {
        super(exceptionHandler);
        loader = getSimpleLoader(conf);
        this.store = store;
        this.requestFactory = requestFactory;
    }

    public CacheEntry<K, V> load(K key, AttributeMap map) {
        MutableAttributeMap m = new DefaultAttributeMap(map);
        V value = doLoad(loader, key, m);
        if (value != null) {
            AddEntryRequest<K, V> loaded = requestFactory.loaded(key, value, m);
            store.process(loaded);
            return loaded.getNewEntry();
        }
        return null;
    }

    public void loadAsync(K key, AttributeMap map) {
        load(key, map);
    }
}
