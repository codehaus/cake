/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.internal.cache;

import java.util.Collection;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;

public interface InternalCache<K, V> extends Cache<K, V>, Iterable<CacheEntry<K, V>> {

    CacheEntry<K, V> put(K key, V value, AttributeMap attributes);

    void putAllWithAttributes(Map<K, Map.Entry<V, AttributeMap>> data);

    boolean removeEntries(Collection<?> entries);

    CacheEntry<K, V> valueLoaded(K key, V value, AttributeMap map);
}
