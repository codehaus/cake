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

import org.codehaus.cake.internal.cache.InternalCache;
import org.codehaus.cake.service.Container;

public class SynchronizedCollectionViews<K, V> implements CollectionViews<K, V> {
    private Set<Map.Entry<K, V>> entrySet;

    private Set<K> keySet;
    private Collection<V> values;
    private final InternalCache<K, V> cache;
    private final Object mutex;

    public SynchronizedCollectionViews(Container mutex, InternalCache<K, V> cache) {
        this.cache = cache;
        this.mutex = mutex;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = entrySet;
        return (es != null) ? es : (entrySet = new SynchronizedEntrySet<K, V>(mutex, cache));
    }

    public Set<K> keySet() {
        Set<K> ks = keySet;
        return (ks != null) ? ks : (keySet = new SynchronizedKeySet<K, V>(mutex, cache));
    }

    public Collection<V> values() {
        Collection<V> vs = values;
        return (vs != null) ? vs : (values = new SynchronizedValues<K, V>(mutex, cache));
    }
}
