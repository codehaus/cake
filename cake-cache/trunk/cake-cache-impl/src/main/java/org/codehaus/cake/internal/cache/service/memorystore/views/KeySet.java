package org.codehaus.cake.internal.cache.service.memorystore.views;

import java.util.Collection;
import java.util.Iterator;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.InternalCache;
import org.codehaus.cake.internal.util.CollectionUtils;

class KeySet<K, V> extends AbstractSetView<K> {

     KeySet(InternalCache<K, V> cache) {
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
