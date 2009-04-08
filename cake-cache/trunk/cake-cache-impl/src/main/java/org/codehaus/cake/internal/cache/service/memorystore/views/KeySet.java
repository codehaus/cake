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
package org.codehaus.cake.internal.cache.service.memorystore.views;

import java.util.Collection;
import java.util.Iterator;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.util.CollectionUtils;

class KeySet<K, V> extends AbstractSetView<K> {

    KeySet(Cache<K, V> cache) {
        super(cache);
    }

    public final boolean contains(Object o) {
        return cache.containsKey(o);
    }

    public final boolean remove(Object o) {
        return cache.remove(o) != null;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c == null) {
            throw new NullPointerException("c is null");
        }
        CollectionUtils.checkCollectionForNulls(c);
        return super.removeAll(c);
    }

    @Override
    public Iterator<K> iterator() {
        final Iterator<CacheEntry<K, V>> iter = cache.iterator();
        return new Iterator<K>() {
            public boolean hasNext() {
                return iter.hasNext();
            }

            public K next() {
                CacheEntry<K, V> current = iter.next();
                return current.getKey();
            }

            public void remove() {
                iter.remove();
            }
        };
    }
}
