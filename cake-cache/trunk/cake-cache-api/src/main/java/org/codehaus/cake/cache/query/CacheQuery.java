package org.codehaus.cake.cache.query;

import java.util.Comparator;
import java.util.List;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.attribute.common.ComparableObjectAttribute;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.ops.Ops.Op;

/**
 * A CacheQuery is normally obtained by calling {@link Cache#query()} can be used to extract data from a {@link Cache},
 * allowing both limiting the number of results (using {@link #setLimit(int)}) and specifying the order that the result
 * should be returned in.
 * <p>
 * A CacheQuery can be reused. The executing methods can safely be used between multiple threads. However, the various
 * order methods and {@link #setLimit(int)} must be from one thread only.
 * 
 * @param <K>
 * @param <V>
 */
public interface CacheQuery<K, V> extends Iterable<CacheEntry<K, V>> {

    /** @return the query result(s) as a {@link List} */
    List<CacheEntry<K, V>> asList();

    <E> Query<E> to(Op<CacheEntry<K, V>, E> transformer);

    /** @return a new MapQuery mapping keys to values, ignoring attributes */
    MapQuery<K, V> map();

    /** @return a new MapQuery mapping keys to values, ignoring attributes */
    /**
     * Creates a new MapQuery mapping keys to specified attribute, ignoring other attributes and values.
     * 
     * @param <T>
     *            the type of the attribute
     * @param attribute
     *            the attribute to map to
     * @return the new MapQuery
     */
    <T> MapQuery<K, T> map(Attribute<T> attribute);

    /**
     * Creates a new Query returning only the specified attributes value.
     * 
     * @param <T>
     *            the type of the attribute
     * @param attribute
     *            the attribute to map to
     * @return the new query
     */
    <T> Query<T> attribute(Attribute<T> attribute);

    /**
     * Creates a new CacheQuery where all keys are mapped according to the specified mapper.
     * 
     * @param <E>
     *            the type of keys the mapper maps to
     * @param mapper
     * @return the new query
     */
    <E> CacheQuery<E, V> keyTo(Op<? super K, ? extends E> mapper);

    <E> CacheQuery<K, E> valueTo(Op<? super V, ? extends E> transformer);

    /** @return the query result(s) as a {@link List} of keys */
    Query<K> keys();

    CacheQuery<K, V> orderBy(Comparator<? super CacheEntry<K, V>> comparator);

    CacheQuery<K, V> orderByKeys(Comparator<K> comparator);

    /**
     * Orders the keys
     * 
     * @return this query
     */
    CacheQuery<K, V> orderByKeysMax();

    CacheQuery<K, V> orderByKeysMin();

    /**
     * Orders the results according to the value of the specified attribute.
     * 
     * @param attribute
     *            the attribute
     * @throws IllegalArgumentException
     *             if the specified attribute is a subclass of {@link ObjectAttribute} but not
     *             {@link ComparableObjectAttribute}
     * @return this query
     */
    CacheQuery<K, V> orderByMax(Attribute<?> attribute);

    CacheQuery<K, V> orderByMin(Attribute<?> attribute);

    /**
     * Creates a new CacheQuery where the result is ordered accordingly to the specified Comparator.
     * 
     * @throws ClassCastException
     *             if any value is not Comparable.
     * @return a new query
     */
    CacheQuery<K, V> orderByValues(Comparator<V> comparator);

    /**
     * Creates a new CacheQuery where the result is ordered according to the values natural ordering, assuming all
     * values are Comparable.
     * 
     * @throws ClassCastException
     *             if any value is not Comparable.
     * @return a new query
     */
    CacheQuery<K, V> orderByValuesMax();

    /**
     * Creates a new CacheQuery where the result is ordered according to the values natural ordering, assuming all
     * values are Comparable.
     * 
     * @throws ClassCastException
     *             if any value is not Comparable.
     * @return a new query
     */
    CacheQuery<K, V> orderByValuesMin();

    /**
     * Executes the query and inserts the result into the specified Cache.
     * 
     * @param cache
     *            the cache to insert the result into
     */
    void putInto(Cache<K, V> cache);

    // <K1, V1> void putInto(Cache<K1, V1> cache, Op<CacheEntry<K, V>, CacheEntry<K1, V1>> transformer);

    /**
     * Creates a new CacheQuery which limits the number of results returned by this query.
     * 
     * @param limit
     *            the maximum number of results to return
     * @return a new query
     */
    CacheQuery<K, V> setLimit(int limit);

    /**
     * Creates a new Query returning only the value part of a CacheEntry.
     * 
     * @return the new query
     */
    Query<V> values();
}
