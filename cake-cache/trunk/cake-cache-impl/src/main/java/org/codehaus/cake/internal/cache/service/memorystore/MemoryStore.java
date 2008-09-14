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

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;

/**
 * MemoryStore is an in-memory store of data.
 * 
 * @param <K>
 * @param <V>
 */
public interface MemoryStore<K, V> extends Iterable<CacheEntry<K, V>> {

    /**
     * Returns the CacheEntry for the specified key, or <code>null</code> if it doesn't exist.
     * 
     * @param key
     * @return
     */
    CacheEntry<K, V> get(K key);

    // will most likely have getValue(), getAttribute(Attribute a)
    // if we start storing, the value, key, attributes in separate arrays.

    int getSize();

    long getVolume();

    CacheEntry<K, V> peek(K key);

    void process(AddEntriesRequest<K, V> r);

    void process(AddEntryRequest<K, V> r);

    void process(ClearCacheRequest<K, V> r);

    void process(RemoveEntriesRequest<K, V> r);

    void process(RemoveEntryRequest<K, V> r);

    void process(TrimToSizeRequest<K, V> r);

    void process(TrimToVolumeRequest<K, V> r);

    void updateConfiguration(AttributeMap attributes);

}
