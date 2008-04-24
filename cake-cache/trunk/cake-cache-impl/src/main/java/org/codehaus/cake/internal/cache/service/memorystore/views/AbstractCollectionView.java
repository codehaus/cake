package org.codehaus.cake.internal.cache.service.memorystore.views;

import java.util.AbstractCollection;
import java.util.Collection;

import org.codehaus.cake.internal.cache.InternalCache;

abstract class AbstractCollectionView<E> extends AbstractCollection<E> {

    final InternalCache cache;

    AbstractCollectionView(InternalCache cache) {
        this.cache = cache;
    }

    public final boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    public final void clear() {
        cache.clear();
    }

    public final boolean isEmpty() {
        return cache.isEmpty();
    }

    public final int size() {
        return cache.size();
    }
}
