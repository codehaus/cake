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
import java.util.Iterator;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.InternalCache;

class Values<K, V> extends AbstractCollectionView<V> {

    Values(InternalCache<K, V> cache) {
        super(cache);
    }

    public final boolean contains(Object o) {
        return cache.containsValue(o);
    }

    @Override
    public Iterator<V> iterator() {
        final Iterator<CacheEntry<K, V>> iter = cache.iterator();
        return new Iterator<V>() {

            public boolean hasNext() {
                return iter.hasNext();
            }

            public V next() {
                CacheEntry<K, V> current = iter.next();
                return current.getValue();
            }

            public void remove() {
                iter.remove();
            }
        };
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("c is null");
        }
        return super.removeAll(c);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("o is null");
        }
        return super.remove(o);
    }
}
