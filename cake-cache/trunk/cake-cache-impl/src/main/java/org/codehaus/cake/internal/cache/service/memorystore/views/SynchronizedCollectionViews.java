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
    public SynchronizedCollectionViews(Container mutex,InternalCache<K, V> cache) {
        this.cache = cache;
        this.mutex=mutex;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = entrySet;
        return (es != null) ? es : (entrySet = new EntrySet<K, V>(cache));
    }

    public Set<K> keySet() {
        Set<K> ks = keySet;
        return (ks != null) ? ks : (keySet = new KeySet<K, V>(cache));
    }

    public Collection<V> values() {
        Collection<V> vs = values;
        return (vs != null) ? vs : (values = new Values<K, V>(cache));
    }
}