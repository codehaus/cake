package org.codehaus.cake.internal.cache.service.memorystore.views;

import java.util.Collection;

import org.codehaus.cake.internal.cache.InternalCache;

final class SynchronizedValues<K, V> extends Values<K, V> {
    private final Object mutex;

    SynchronizedValues(Object mutex, InternalCache<K, V> cache) {
        super(cache);
        this.mutex = mutex;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        synchronized (mutex) {
            return super.containsAll(c);
        }
    }

    @Override
    public Object[] toArray() {
        synchronized (mutex) {
            return super.toArray();
        }
    }

    @Override
    public <T> T[] toArray(T[] a) {
        synchronized (mutex) {
            return super.toArray(a);
        }
    }

    @Override
    public String toString() {
        synchronized (mutex) {
            return toString();
        }
    }
}
