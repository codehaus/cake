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
package org.codehaus.cake.internal.cache.service.listener;

import java.util.Collection;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.InternalCacheEntry;

public class DefaultCacheListener<K, V> implements InternalCacheListener<K, V> {

    public void afterCacheClear(long timestamp, Iterable<? extends CacheEntry<K, V>> entries) {
    // TODO Auto-generated method stub

    }

    public void afterPut(long started, Iterable<? extends CacheEntry<K, V>> evictedEntries,
            InternalCacheEntry<K, V> oldEntry, InternalCacheEntry<K, V> newEntry, boolean fromLoader) {
    // TODO Auto-generated method stub

    }

    public void afterPutAll(long started, Iterable<? extends CacheEntry<K, V>> evictedEntries,
            Map<InternalCacheEntry<K, V>, InternalCacheEntry<K, V>> newPrevEntries, boolean fromLoader) {
    // TODO Auto-generated method stub

    }

    public void afterRemove(long started, CacheEntry<K, V> entry) {
    // TODO Auto-generated method stub

    }

    public void afterRemoveAll(long start, Iterable<? extends K> keys, Iterable<CacheEntry<K, V>> removed) {
    // TODO Auto-generated method stub

    }

    public long beforeCacheClear() {
        // TODO Auto-generated method stub
        return 0;
    }

    public long beforePut(K key, V value, boolean fromLoader) {
        // TODO Auto-generated method stub
        return 0;
    }

    public long beforePutAll(Map<? extends K, ? extends V> map, Map<? extends K, AttributeMap> attributes,
            boolean fromLoader) {
        // TODO Auto-generated method stub
        return 0;
    }

    public long beforeRemove(Object key, Object value) {
        // TODO Auto-generated method stub
        return 0;
    }

    public long beforeRemoveAll(Iterable<? extends K> keys) {
        // TODO Auto-generated method stub
        return 0;
    }

}
