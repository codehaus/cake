package org.codehaus.cake.internal.cache.query;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.cake.cache.query.MapQuery;
import org.codehaus.cake.ops.CollectionOps;
import org.codehaus.cake.ops.Comparators;

public abstract class AbstractMapQuery<K, V> implements MapQuery<K, V> {

    public MapQuery<K, V> orderByKeysMax() {
        return orderBy(Comparators.mappedComparator(CollectionOps.MAP_ENTRY_TO_KEY_OP, Comparators.NATURAL_COMPARATOR));
    }

    public MapQuery<K, V> orderByKeysMin() {
        return orderBy(Comparators.mappedComparator(CollectionOps.MAP_ENTRY_TO_KEY_OP,
                Comparators.NATURAL_REVERSE_COMPARATOR));
    }

    public MapQuery<K, V> orderByValuesMax() {
        return orderBy(Comparators
                .mappedComparator(CollectionOps.MAP_ENTRY_TO_VALUE_OP, Comparators.NATURAL_COMPARATOR));
    }

    public MapQuery<K, V> orderByValuesMin() {
        return orderBy(Comparators.mappedComparator(CollectionOps.MAP_ENTRY_TO_VALUE_OP,
                Comparators.NATURAL_REVERSE_COMPARATOR));
    }

    public MapQuery<K, V> orderByKeys(Comparator<K> comparator) {
        return orderBy(Comparators.mappedComparator(CollectionOps.MAP_ENTRY_TO_KEY_OP, comparator));
    }

    public MapQuery<K, V> orderByValues(Comparator<V> comparator) {
        return orderBy(Comparators.mappedComparator(CollectionOps.MAP_ENTRY_TO_VALUE_OP, comparator));
    }


    public Iterator<Map.Entry<K, V>> iterator() {
        return asList().iterator();
    }

}
