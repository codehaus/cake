package org.codehaus.cake.internal.cache;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.ops.Ops.Predicate;

public class CachePredicates {

    public static <K, V> Predicate<CacheEntry<K, V>> valueEquals(V value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        return new CacheValueEquals<K, V>(value);
    }

    public static class CacheValueEquals<K, V> implements Predicate<CacheEntry<K, V>> {
        private final V value;

        public CacheValueEquals(V value) {
            this.value = value;
        }

        public boolean op(CacheEntry<K, V> a) {
            V v = a.getValue();
            return value.equals(v);
        }

    }
}
