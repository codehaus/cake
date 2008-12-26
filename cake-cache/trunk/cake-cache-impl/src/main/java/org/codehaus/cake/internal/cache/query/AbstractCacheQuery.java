package org.codehaus.cake.internal.cache.query;

import java.util.Comparator;
import java.util.Iterator;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.GetAttributer;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.query.CacheQuery;
import org.codehaus.cake.ops.CollectionOps;
import org.codehaus.cake.ops.Comparators;
import org.codehaus.cake.ops.Ops.Op;

public abstract class AbstractCacheQuery<K, V> implements CacheQuery<K, V> {

    public Iterator<CacheEntry<K, V>> iterator() {
        return asList().iterator();
    }

    public CacheQuery<K, V> orderByKeys(Comparator<K> comparator) {
        return orderBy(Comparators.mappedComparator(CollectionOps.MAP_ENTRY_TO_KEY_OP, comparator));
    }

    public CacheQuery<K, V> orderByKeysMax() {
        return orderBy(Comparators.mappedComparator(CollectionOps.MAP_ENTRY_TO_KEY_OP, Comparators.NATURAL_COMPARATOR));
    }

    public CacheQuery<K, V> orderByKeysMin() {
        return orderBy(Comparators.mappedComparator(CollectionOps.MAP_ENTRY_TO_KEY_OP,
                Comparators.NATURAL_REVERSE_COMPARATOR));
    }

    public CacheQuery<K, V> orderByMax(Attribute<?> attribute) {
        return orderBy(Attributes.maxComparator(attribute));
    }

    public CacheQuery<K, V> orderByMin(Attribute<?> attribute) {
        return orderBy(Attributes.minComparator(attribute));
    }

    public CacheQuery<K, V> orderByValues(Comparator<V> comparator) {
        return orderBy(Comparators.mappedComparator(CollectionOps.MAP_ENTRY_TO_VALUE_OP, comparator));
    }

    public CacheQuery<K, V> orderByValuesMax() {
        return orderBy(Comparators
                .mappedComparator(CollectionOps.MAP_ENTRY_TO_VALUE_OP, Comparators.NATURAL_COMPARATOR));
    }

    public CacheQuery<K, V> orderByValuesMin() {
        return orderBy(Comparators.mappedComparator(CollectionOps.MAP_ENTRY_TO_VALUE_OP,
                Comparators.NATURAL_REVERSE_COMPARATOR));
    }

    static class ExtractAttribute<T> implements Op<GetAttributer, T> {

        private final Attribute<T> attribute;

        ExtractAttribute(Attribute<T> attribute) {
            if (attribute == null) {
                throw new NullPointerException("attribute is null");
            }
            this.attribute = attribute;
        }

        public T op(GetAttributer a) {
            return a.get(attribute);
        }

    }
}
