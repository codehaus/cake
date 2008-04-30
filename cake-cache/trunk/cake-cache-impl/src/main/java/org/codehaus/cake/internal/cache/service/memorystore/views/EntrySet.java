package org.codehaus.cake.internal.cache.service.memorystore.views;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.InternalCache;
import org.codehaus.cake.internal.util.CollectionUtils;

 class EntrySet<K, V> extends AbstractSetView<Map.Entry<K, V>> {

    EntrySet(InternalCache<K, V> cache) {
        super(cache);
    }

    public final boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException("o is null");
        }
        if (!(o instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
        CacheEntry<K, V> ce =(CacheEntry) cache.peekEntry(e.getKey());
        return ce != null && ce.getValue().equals(e.getValue());
    }
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (o == null) {
                throw new NullPointerException();
            }
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<K, V> e = (Map.Entry<K, V>) o;
            Map.Entry<K, V> candidate = (CacheEntry) cache.peekEntry(e.getKey());
            if (candidate == null || !e.getValue().equals(candidate.getValue())) {
                return false;
            }
        }
        return true;
    }
    public final boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("o is null");
        }
        if (!(o instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
        return cache.remove(e.getKey(), e.getValue());
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
    public Iterator<Map.Entry<K, V>> iterator() {
        final Iterator<CacheEntry<K, V>> iter = cache.iterator();
        return new Iterator<Map.Entry<K, V>>() {

            public boolean hasNext() {
                return iter.hasNext();
            }

            public Map.Entry<K, V> next() {
                CacheEntry<K, V> current = iter.next();
                return current;
            }

            public void remove() {
                iter.remove();
            }
        };
    }
}
