package org.codehaus.cake.cache.test.tck.service.memorystore;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.memorystore.IsCacheablePredicate;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.ops.Ops.BinaryPredicate;
import org.codehaus.cake.util.ops.Ops.Predicate;

/**
 * Temporary here, until it comes out of the sandbox*
 */
class IsCacheablePredicates {
    
    //Lav det som en builder???
    
    //FailFast
    //KeepExistingOnReplace
    //IsB
    private IsCacheablePredicates() {
    };

    // Predicate on key
    // Predicate on value
    // Predicate on attribute

    public static <K, V> IsCacheablePredicate<K, V> predicateOn(BinaryPredicate<? super K, ? super V> predicate,
            boolean keepExisting) {
        return null;
    }

    public static <K, V> Cache<K, V> on(Predicate<CacheEntry<K, V>> filter) {
        return null;
    }

    public static <K, V, T> IsCacheablePredicate<K, V> predicateOn(Attribute<T> attribute, Predicate<T> predicate) {
        return null;
    }

    public static <K, V> IsCacheablePredicate<K, V> predicateOnKey(Predicate<? super K> predicate, boolean keepExisting) {
        return new PredicateOnKey<K, V>(predicate, keepExisting);
    }

    public static <K, V> IsCacheablePredicate<K, V> predicateOnValue(Predicate<? super V> predicate,
            boolean keepExisting) {
        return new PredicateOnValue<K, V>(predicate, keepExisting);
    }

    public static enum OnReplace {
        KEEP_EXISTING
    }

    static class PredicateOnKey<K, V> implements IsCacheablePredicate<K, V> {
        private final Predicate<? super K> predicate;
        private final boolean keepExisting;

        public PredicateOnKey(Predicate<? super K> predicate, boolean keepExisting) {
            if (predicate == null) {
                throw new NullPointerException("predicate is null");
            }
            this.predicate = predicate;
            this.keepExisting = keepExisting;
        }

        public boolean add(CacheEntry<K, V> entry) {
            return predicate.op(entry.getKey());
        }

        public CacheEntry<K, V> replace(CacheEntry<K, V> existing, CacheEntry<K, V> entry) {
            if (predicate.op(entry.getKey())) {
                return entry;
            }
            return keepExisting ? existing : null;
        }
    }

    static class PredicateOnValue<K, V> implements IsCacheablePredicate<K, V> {
        private final Predicate<? super V> predicate;
        private final boolean keepExisting;

        public PredicateOnValue(Predicate<? super V> predicate, boolean keepExisting) {
            if (predicate == null) {
                throw new NullPointerException("predicate is null");
            }
            this.predicate = predicate;
            this.keepExisting = keepExisting;
        }

        public boolean add(CacheEntry<K, V> entry) {
            return predicate.op(entry.getValue());
        }

        public CacheEntry<K, V> replace(CacheEntry<K, V> existing, CacheEntry<K, V> entry) {
            if (predicate.op(entry.getValue())) {
                return entry;
            }
            return keepExisting ? existing : null;
        }
    }
}
