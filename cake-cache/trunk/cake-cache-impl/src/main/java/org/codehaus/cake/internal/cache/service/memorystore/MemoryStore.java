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

import java.util.Collection;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.forkjoin.collections.ParallelArray;

public interface MemoryStore<K, V> extends Iterable<CacheEntry<K, V>> {

    CacheEntry<K, V> get(K key);

    CacheEntry<K, V> peek(K key);

    CacheEntry<K, V> remove(Object key);

    CacheEntry<K, V> remove(Object key, Object value);

    ParallelArray<CacheEntry<K, V>> removeAll(Collection entries);

    ParallelArray<CacheEntry<K, V>> trim();

    void add(AddSingleEntry<K, V> entry);
    
    SingleEntryUpdate<K, V> put(K key, V value, AttributeMap attributes, boolean OnlyIfAbsent);

    SingleEntryUpdate<K, V> replace(K key, V oldValue, V newValue, AttributeMap attributes);

    Map<CacheEntry<K, V>, CacheEntry<K, V>> putAllWithAttributes(Map<K, Map.Entry<V, AttributeMap>> data);

    ParallelArray<CacheEntry<K, V>> removeAll();

    int getSize();

    // CacheEntry<K, V> removeAny(Predicate<? super CacheEntry<K, V>> selector);

    // ParallelArray<CacheEntry<K, V>> removeEntries(Collection entries);

    // ParallelArray<CacheEntry<K, V>> removeValues(Collection entries);

}
