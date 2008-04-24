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

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public V next() {
                CacheEntry<K, V> current = iter.next();
                return current.getValue();
            }

            @Override
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
