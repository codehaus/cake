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
package org.codehaus.cake.internal.cache.service.memorystore;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.internal.cache.service.attribute.InternalAttributeService;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;

public class SynchronizedHashMapMemoryStore<K, V> extends HashMapMemoryStore<K, V> {

    private final Object mutex;

    public SynchronizedHashMapMemoryStore(Cache<K, V> cache, MemoryStoreConfiguration<K, V> storeConfiguration,
            InternalAttributeService<K, V> attributeService, InternalCacheExceptionService<K, V> ies) {
        super(storeConfiguration, attributeService, ies);
        this.mutex = cache;
    }

    public int getSize() {
        synchronized(mutex) {
            return super.getSize();
        }
    }

    public long getVolume() {
        synchronized (mutex) {
            return super.getVolume();
        }
    }
}
