package org.codehaus.cake.cache.query;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
public interface CacheQuery<K, V>  extends Iterable<CacheEntry<K,V>>{

    /** @return the query result(s) as a {@link List} */
    List<CacheEntry<K, V>> asList();

    <E> Query<E> to(Op<CacheEntry<K, V>, E> transformer);
    /** @return the query result(s) as a {@link Map} */
    
    MapQuery<K, V> map();
    
    <T> MapQuery<K, T> map(Attribute<T> attribute);
    
    <T> Query<T> attribute(Attribute<T> attribute);

    <E> CacheQuery<E, V> keyTo(Op<K, E> transformer);

    <E> CacheQuery<K, E> valueTo(Op<V, E> transformer);

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

    CacheQuery<K, V> orderByValues(Comparator<V> comparator);

    CacheQuery<K, V> orderByValuesMax();

    CacheQuery<K, V> orderByValuesMin();

    void putInto(Cache<K, V> cache);

    //<K1, V1> void putInto(Cache<K1, V1> cache, Op<CacheEntry<K, V>, CacheEntry<K1, V1>> transformer);

    /**
     * Limits the
     * 
     * @param maxresults
     * @return
     */
    CacheQuery<K, V> setLimit(int maxresults);

    /**
     * Executes the query and returns the result as a list of values.
     * 
     * @return the resulting values
     */
    Query<V> values();
}