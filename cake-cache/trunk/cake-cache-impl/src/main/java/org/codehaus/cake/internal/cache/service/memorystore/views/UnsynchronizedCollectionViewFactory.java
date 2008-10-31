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
package org.codehaus.cake.internal.cache.service.memorystore.views;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.codehaus.cake.cache.Cache;

public class UnsynchronizedCollectionViewFactory<K, V> implements CollectionViewFactory<K, V> {

    public Set<Map.Entry<K, V>> entrySet(Cache<K,V> cache) {
        return new EntrySet<K, V>(cache);
    }

    public Set<K> keySet(Cache<K,V> cache) {
        return new KeySet<K, V>(cache);
    }

    public Collection<V> values(Cache<K,V> cache) {
        return new Values<K, V>(cache);
    }
}
