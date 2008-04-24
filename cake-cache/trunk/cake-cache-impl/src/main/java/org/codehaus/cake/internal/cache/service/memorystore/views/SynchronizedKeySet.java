package org.codehaus.cake.internal.cache.service.memorystore.views;

import java.util.Collection;

import org.codehaus.cake.internal.cache.InternalCache;

final class SynchronizedKeySet<K,V> extends KeySet<K,V> {
    private final Object mutex;

    SynchronizedKeySet(Object mutex, InternalCache<K, V> cache) {
        super(cache);
        this.mutex = mutex;
    }

    public boolean containsAll(Collection<?> c) {
        synchronized (mutex) {
            return super.containsAll(c);
        }
    }

    @Override
    public boolean equals(Object o) {
        synchronized (mutex) {
            return super.equals(o);
        }
    }

    @Override
    public int hashCode() {
        synchronized (mutex) {
            return super.hashCode();
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