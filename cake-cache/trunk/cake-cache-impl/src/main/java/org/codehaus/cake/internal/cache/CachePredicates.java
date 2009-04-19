package org.codehaus.cake.internal.cache;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.util.ops.Ops.Predicate;

public class CachePredicates {

    public static <K, V> Predicate<CacheEntry<K, V>> valueEquals(V value) {
        return new CacheValueEquals<K, V>(value);
    }

    public static class CacheValueEquals<K, V> implements Predicate<CacheEntry<K, V>> {
        private final V value;

        public CacheValueEquals(V value) {
            if (value == null) {
                throw new NullPointerException("value is null");
            }
            this.value = value;
        }

        public boolean op(CacheEntry<K, V> a) {
            return value.equals(a.getValue());
        }

    }
}
