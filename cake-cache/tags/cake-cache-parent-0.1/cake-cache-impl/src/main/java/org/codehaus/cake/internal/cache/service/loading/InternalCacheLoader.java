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

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;

public interface InternalCacheLoader<K, V> {

    void loadAsync(K key, AttributeMap attributes);

    void loadAsync(Map<? extends K, AttributeMap> map);

    // throws errors /runtime exceptions
    /**
     * <p>
     * This method can be called without holding a lock.
     * 
     * @param key
     * @param attributes
     * @return
     */
    CacheEntry<K, V> load(K key, AttributeMap attributes);
}
