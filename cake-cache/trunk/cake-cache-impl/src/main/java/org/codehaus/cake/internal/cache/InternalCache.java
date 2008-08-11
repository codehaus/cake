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
package org.codehaus.cake.internal.cache;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;

public interface InternalCache<K, V> extends Cache<K, V>, Iterable<CacheEntry<K, V>> {

    void putAllWithAttributes(Map<K, Map.Entry<V, AttributeMap>> data);

    // boolean removeEntries(Collection<?> entries);

    /**
     * Called by a cache loader whenever a value has been loaded.
     * 
     * @param key
     * @param value
     * @param map
     * @return
     */
    CacheEntry<K, V> valueLoaded(K key, V value, AttributeMap map);
}
