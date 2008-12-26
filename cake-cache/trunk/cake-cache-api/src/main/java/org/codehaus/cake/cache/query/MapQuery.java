package org.codehaus.cake.cache.query;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.ops.Ops.Op;

public interface MapQuery<K, V> extends Iterable<Map.Entry<K,V>> {
    /** @return the query result(s) as a {@link List} */
    List<Map.Entry<K, V>> asList();

    <E> Query<E> to(Op<Map.Entry<K, V>, E> transformer);

    /** @return the query result(s) as a {@link Map} */
    Map<K, V> asMap();

    <E> MapQuery<E, V> keyTo(Op<K, E> transformer);

    <E> MapQuery<K, E> valueTo(Op<V, E> transformer);

    /** @return the query result(s) as a {@link List} of keys */
    Query<K> keys();

    MapQuery<K, V> orderBy(Comparator<? super Map.Entry<K, V>> comparator);

    MapQuery<K, V> orderByKeys(Comparator<K> comparator);

    /**
     * Orders the keys
     * 
     * @return this query
     */
    MapQuery<K, V> orderByKeysMax();

    MapQuery<K, V> orderByKeysMin();

    MapQuery<K, V> orderByValues(Comparator<V> comparator);

    MapQuery<K, V> orderByValuesMax();

    MapQuery<K, V> orderByValuesMin();

    /**
     * Limits the
     * 
     * @param maxresults
     * @return
     */
    MapQuery<K, V> setLimit(int maxresults);

    /**
     * Executes the query and returns the result as a list of values.
     * 
     * @return the resulting values
     */
    Query<V> values();
}
