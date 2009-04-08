/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerShutdownException;

/**
 * A <tt>cache</tt> is a collection of data duplicating original values stored elsewhere or computed earlier, where
 * the original data are expensive (usually in terms of access time) to fetch or compute relative to reading from the
 * cache. Once the data are stored in the cache, future use can be made by accessing the cached copy rather than
 * refetching or recomputing the original data, so that the average access time is lowered.
 * <p>
 * Currently two implementations exist: {@link org.codehaus.cake.cache.UnsynchronizedCache} and
 * {@link org.codehaus.cake.cache.SynchronizedCache}.
 * <p>
 * The three collection views, which allow a cache's contents to be viewed as a set of keys, collection of values, or
 * set of key-value mappings only shows values contained in the actual cache. Furthermore, the cache will <tt>not</tt>
 * check whether or not an entry has expired when calling methods on any of the collection views. As a result the cache
 * might return values that have expired.
 * <p>
 * All general-purpose <tt>Cache</tt> implementation classes should provide two "standard" constructors: a void (no
 * arguments) constructor, which creates an empty cache with default settings, and a constructor with a single argument
 * of type {@link CacheConfiguration}. There is no way to enforce this recommendation (as interfaces cannot contain
 * constructors) but all of the general-purpose cache implementations available in Cake Cache comply. Unlike its super
 * class {@link java.util.Map}, a constructor taking a single map is not required.
 * <p>
 * Generally, Cache implementations do not define element-based versions of the <tt>equals</tt> and <tt>hashCode</tt>
 * methods, but instead inherit the identity-based versions from class <tt>Object</tt>. Nore, are they generally
 * serializable.
 * <p>
 * Unlike {@link java.util.HashMap}, <tt>Cache</tt> implementations does NOT allow <tt>null</tt> to be used as a
 * key or value. It is the authors belief that allowing null values (or keys) does more harm then good, by masking what
 * are almost always usage errors. If nulls are absolutely needed the <a
 * href="http://today.java.net/today/2004/12/10/refactor.pdf">Null Object Pattern</a> can be used as an alternative.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */
public interface Cache<K, V> extends ConcurrentMap<K, V>, Container, Iterable<CacheEntry<K, V>> {

    // drop concurrentMap, can be optained by calling write()?
    // get() <- CacheReader
    // R get(K key, Op<CacheEntry<K,V>,R> mapper);
    // <-basically
    // getAttribute(K, Attribute A);
    // getAttribute(K, AttributeMap AM A);
    //
    // view() <-CacheView with defaultSettings
    // withView() returns CacheViewFactory
    //
    // write() <-CacheWriter (was CrudWriter)
    // writeBatch() <-CacheWriter (was CrudWriter)
    // withWriter() <-CacheWriterFactory();
    //    
    /**
     * Removes all entries from this cache. The cache will be empty after this call returns.
     * <p>
     * If this method is used for periodically removing stale data. A better solution, if the cache has a cache loader
     * defined, might be to call <tt>cache.with().loadingForced().loadAll()</tt> which will forcefully reload all
     * entries in the cache. In this way users of the cache does not pay the cost upfront for repopulating an empty
     * cache. Instead fresh data will be loaded in the background by the cache.
     * <p>
     * If the cache has been shutdown calls to this method are ignored (the cache is already empty).
     * 
     * @throws UnsupportedOperationException
     *             if the <tt>clear</tt> operation is not supported by this cache
     */
    void clear();

    /**
     * Returns <tt>true</tt> if this cache contains a mapping for the specified key. More formally, returns
     * <tt>true</tt> if and only if this cache contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>. (There can be at most one such mapping.)
     * <p>
     * If this cache has been shutdown this method returns <tt>false</tt>.
     * 
     * @param key
     *            key whose presence in this cache is to be tested
     * @return <tt>true</tt> if this cache contains a mapping for the specified key, otherwise <tt>false</tt>
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified key is null
     */
    boolean containsKey(Object key);

    /**
     * Returns <tt>true</tt> if this cache maps one or more keys to the specified value. More formally, returns
     * <tt>true</tt> if and only if this cache contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>. This operation will require time linear in the cache size
     * for most implementations.
     * <p>
     * If this cache has been shutdown this method returns <tt>false</tt>.
     * 
     * @param value
     *            value whose presence in this cache is to be tested
     * @return <tt>true</tt> if this cache maps one or more keys to the specified value
     * @throws ClassCastException
     *             if the value is of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified value is null
     */
    boolean containsValue(Object value);

    /**
     * Returns a {@link Set} view of the mappings contained in this cache. The set is backed by the cache, so changes to
     * the cache are reflected in the set, and vice-versa. If the cache is modified while an iteration over the set is
     * in progress (except through the iterator's own <tt>remove</tt> operation, or through the <tt>setValue</tt>
     * operation on a map entry returned by the iterator) the results of the iteration are undefined. The set supports
     * element removal, which removes the corresponding mapping from the cache, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt> operations. It does not
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     * <p>
     * If the cache has been shutdown calls to <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt> and <tt>retainAll</tt> operation will throw an IllegalStateException.
     * 
     * @return a set view of the mappings contained in this map
     * @see #view()
     */
    Set<Map.Entry<K, V>> entrySet();

    /**
     * Returns a cache selector that can be used to create a filtered view of the mappings contained in this cache. The
     * filtered view is backed by the cache, so changes to the cache are reflected in the view, and vice-versa. This is
     * useful for operating on subsets of the data in a cache. For example, for removing all entries where the value is
     * of a particular type.
     * 
     * Some cautions should apply, for example the following methods will not work.
     * {@link Container#awaitTermination(long, java.util.concurrent.TimeUnit)}, {@link Container#shutdown()},{@link Container#shutdownNow()},
     * insertion??
     * <p>
     * When operating on a filtered view some operations might be slower. For example, to calculate the
     * {@link Cache#size()} of a filtered view, the cache will need to evaluate all elements to see if they match the
     * specified filter.
     * <p>
     * <b>Sample usages.</b> The following snippet will create a new filtered cache view that contains only those
     * entries where the type of the value is Integer.
     * 
     * <pre>
     * Cache&lt;String, Number&gt; c = ...;
     * Cache&lt;String, Integer&gt; onlyInts = c.filter().onValueType(Integer.class);
     * </pre>
     * 
     * The next snippet will reload all cache entries that have not been modified doing the last hour (Assuming the
     * cache has been configured to keep a modication timestamp using
     * {@link CacheConfiguration#addEntryAttributes(org.codehaus.cake.attribute.Attribute...)}.
     * 
     * <pre>
     * long oneHourAgo = new Date().getTime() - 60 * 60 * 1000;
     * c.filter().on(CacheEntry.TIME_MODIFIED, PrimitivePredicates.lessThen(oneHourAgo)).with().loadingForced().loadAll();
     * </pre>
     * 
     * <p>
     * If the cache has been shutdown all cache views returned by the selector will be empty
     * 
     * @throws UnsupportedOperationException
     *             if the <tt>filter</tt> operation is not supported by this cache
     */
    CacheSelector<K, V> filter();

    /**
     * Works as {@link Map#get(Object)} with the following modifications.
     * <p>
     * If the cache has a configured backend (cache loader) and no mapping exists for the specified key. The cache will
     * attempt to transparently load a value for the specified key through the backend.
     * <p>
     * The number of cache hits will increase by 1 if a valid mapping is present. Otherwise the number of cache misses
     * will be increased by 1.
     * 
     * @param key
     *            key whose associated value is to be returned.
     * @return the value to which this cache maps the specified key
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional).
     * @throws NullPointerException
     *             if the specified key is <tt>null</tt>
     * @throws IllegalStateException
     *             if the cache has been shutdown
     * @see Map#get(Object)
     * @see org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration#setLoader(org.codehaus.cake.cache.service.loading.SimpleCacheLoader))
     */
    V get(Object key);

    Map<K, V> getAllOld(Iterable<? extends K> keys);

    /**
     * Creates a virtual view of the entries of all of the mappings for the specified keys. The effect of this call is
     * equivalent to that of calling {@link #getEntry(Object)} once for each of the specified keys and then extracting
     * needed data for each entry. However, in most cases it is faster to load several cache items at once, for example,
     * if the cache must fetch the values from a remote host.
     * <p>
     * <b>Sample usages.</b>
     * 
     * Given a cache with person names (String) as keys and the age of each person as a value, the following shows some
     * examples.
     * 
     * <pre>
     * Cache&lt;String, Integer&gt; cache = ...;
     * Iterable&lt;String&gt; persons = Arrays.asList(&quot;Peter&quot;, &quot;Joe&quot;, &quot;Hahnah&quot;);
     * 
     * Returns a map with the name of the person mapping to the age of the person
     * Map&lt;String, Integer&gt; personAge = cache.getAll(persons).keyValues().toMap();
     * 
     * Returns a sorted map (iterators will return entries in sorted order) with the name of the person mapping to the age of the person
     * Map&lt;String, Integer&gt; personAgeSorted = cache.getAll(persons).orderByValuesMax().keyValues().toMap();
     * 
     * Returns the maximum age of any of the 3 persons 
     * Integer maximumAge = cache.getAll(persons).values().max();
     * 
     * Returns the minimum age of any of the 3 persons
     * Integer minimumAge = cache.getAll(persons).values().max();
     * </pre>
     * 
     * <p>
     * The behavior of this operation is unspecified if the specified iterable is modified while the operation is in
     * progress.
     * 
     * @param keys
     *            a iterable of keys whose associated values are to be returned.
     * @return a virtual cache view with mappings from each key to the corresponding value, or to <tt>null</tt> if no
     *         mapping for this key exists
     * @throws ClassCastException
     *             if any of the keys in the specified iterable are of an inappropriate type for this cache (optional).
     * @throws NullPointerException
     *             if the specified iterable of keys is <tt>null</tt> or the specified iterable contains a
     *             <tt>null</tt>
     * @throws ContainerShutdownException
     *             if the cache has been shutdown
     */
    CacheView<K, V> getAll(Iterable<? extends K> keys);

    // CacheView<K, V> getAll(K... keys);
    // CacheView<K, V> getAllIn(Iterable<? extends K> keys);

    /**
     * Analogous to {@link #get(Object)} except that it returns an immutable {@link CacheEntry}.
     * 
     * @param key
     *            whose associated cache entry is to be returned.
     * @return the cache entry to which this cache maps the specified key, or <tt>null</tt> if the cache contains no
     *         mapping for this key.
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional).
     * @throws NullPointerException
     *             if the specified key is <tt>null</tt>
     * @throws ContainerShutdownException
     *             if the cache has been shutdown
     */
    CacheEntry<K, V> getEntry(K key);

    /**
     * Returns <tt>true</tt> if this cache contains no elements or the cache has been shutdown, otherwise
     * <tt>false</tt>.
     * 
     * @return <tt>true</tt> if this cache contains no elements or has been shutdown, otherwise <tt>false</tt>
     */
    boolean isEmpty();

    /**
     * Returns a {@link Set} view of the keys contained in this cache. The set is backed by the cache, so changes to the
     * cache are reflected in the set, and vice-versa. If the cache is modified while an iteration over the set is in
     * progress (except through the iterator's own <tt>remove</tt> operation), the results of the iteration are
     * undefined. The set supports element removal, which removes the corresponding mapping from the cache, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and
     * <tt>clear</tt> operations. It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     * <p>
     * If the cache has been shutdown calls to <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt> operation will throw an
     * {@link ContainerShutdownException}.
     * 
     * @return a set view of the keys contained in this cache
     * @see #view()
     */
    Set<K> keySet();

    /**
     * This method works analogues to the {@link #get(Object)} method with the following modifications.
     * <p>
     * It will not try to fetch missing items, through the use of cache loaders. It will only return a value if it
     * actually exists in the cache. All implementations of this method should take care to assure that a call to peek
     * does not have any observable side effects. For example, modifying cache usage statistics.
     * <p>
     * If this cache has been shutdown this method returns <tt>null</tt>.
     * 
     * @param key
     *            key whose associated value is to be returned.
     * @return the value to which this cache maps the specified key, or <tt>null</tt> if the cache contains no mapping
     *         for this key.
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional).
     * @throws NullPointerException
     *             if the specified key is <tt>null</tt>
     */
    V peek(K key);

    /**
     * Analogous to {@link #get(Object)} except that it returns an immutable {@link CacheEntry}.
     * 
     * @param key
     *            key whose associated cache entry is to be returned.
     * @return the cache entry to which this cache maps the specified key, or <tt>null</tt> if the cache contains no
     *         mapping for this key.
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional).
     * @throws NullPointerException
     *             if the specified key is <tt>null</tt>
     */
    CacheEntry<K, V> peekEntry(K key);

    /**
     * Associates the specified value with the specified key in this cache (optional operation). If the cache previously
     * contained a mapping for this key, the old value is replaced by the specified value. (A cache <tt>c</tt> is said
     * to contain a mapping for a key <tt>k</tt> if and only if {@link #containsKey(Object) c.containsKey(k)} would
     * return <tt>true</tt>.))
     * <p>
     * In most usage scenarios is more effective to specify a cache loader that implicitly loads values, rather then
     * explicitly adding them to cache using <tt>put</tt>, see
     * {@link CacheLoadingConfiguration#setLoader(org.codehaus.cake.cache.service.loading.BlockingCacheLoader)}.
     * 
     * @param key
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @return previous value associated with specified key, or <tt>null</tt> if there was no mapping for key.
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this cache.
     * @throws ClassCastException
     *             if the class of the specified key or value prevents it from being stored in this cache.
     * @throws IllegalArgumentException
     *             if some aspect of this key or value prevents it from being stored in this cache.
     * @throws ContainerShutdownException
     *             if the cache has been shutdown
     * @throws NullPointerException
     *             if the specified key or value is <tt>null</tt>.
     * 
     */
    V put(K key, V value);

    /**
     * Copies all of the mappings from the specified map to this cache (optional operation). The effect of this call is
     * equivalent to that of calling {@link #put(Object,Object) put(k, v)} on this cache once for each mapping from key
     * <tt>k</tt> to value <tt>v</tt> in the specified map. The behavior of this operation is undefined if the
     * specified map is modified while the operation is in progress.
     * 
     * @param m
     *            mappings to be stored in this cache
     * @throws UnsupportedOperationException
     *             if the <tt>putAll</tt> operation is not supported by this cache
     * @throws ClassCastException
     *             if the class of a key or value in the specified map prevents it from being stored in this cache
     * @throws NullPointerException
     *             if the specified map is null or if the specified map contain null keys or values
     * @throws ContainerShutdownException
     *             if the cache has been shutdown
     * @throws IllegalArgumentException
     *             if some property of a key or value in the specified map prevents it from being stored in this cache
     */
    void putAll(Map<? extends K, ? extends V> m);

    /**
     * If the specified key is not already associated with a value, associate it with the given value. This is
     * equivalent to
     * 
     * <pre>
     * if (!cache.containsKey(key))
     *     return cache.put(key, value);
     * else
     *     return cache.get(key);
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     * @return the previous value associated with the specified key, or <tt>null</tt> if there was no mapping for the
     *         key.
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this cache
     * @throws ClassCastException
     *             if the class of the specified key or value prevents it from being stored in this cache
     * @throws IllegalStateException
     *             if the cache has been shutdown
     * @throws NullPointerException
     *             if the specified key or value is null
     * @throws IllegalArgumentException
     *             if some property of the specified key or value prevents it from being stored in this cache
     */
    V putIfAbsent(K key, V value);

    /**
     * Removes the mapping for a key from this cache if it is present (optional operation). More formally, if this cache
     * contains a mapping from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping is removed. (The cache can contain at most
     * one such mapping.)
     * <p>
     * Returns the value to which this cache previously associated the key, or <tt>null</tt> if the cache contained no
     * mapping for the key.
     * <p>
     * The cache will not contain a mapping for the specified key once the call returns.
     * 
     * @param key
     *            key whose mapping is to be removed from the cache
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for
     *         <tt>key</tt>.
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this cache
     * @throws ContainerShutdownException
     *             if the cache has been shutdown
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified key is null
     */
    V remove(Object key);

    /**
     * Removes the entry for a key only if currently mapped to a given value. This is equivalent to
     * 
     * <pre>
     * if (cache.containsKey(key) &amp;&amp; cache.get(key).equals(value)) {
     *     cache.remove(key);
     *     return true;
     * } else
     *     return false;
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param key
     *            key with which the specified value is associated
     * @param value
     *            value expected to be associated with the specified key
     * @return <tt>true</tt> if the value was removed
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this cache
     * @throws ContainerShutdownException
     *             if the cache has been shutdown
     * @throws ClassCastException
     *             if the key or value is of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified key or value is null
     */
    boolean remove(Object key, Object value);

    /**
     * Replaces the entry for a key only if currently mapped to some value. This is equivalent to
     * 
     * <pre>
     * if (cache.containsKey(key)) {
     *     return cache.put(key, value);
     * } else
     *     return null;
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param key
     *            key with which the specified value is associated
     * @param value
     *            value to be associated with the specified key
     * @return the previous value associated with the specified key, or <tt>null</tt> if there was no mapping for the
     *         key.
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this cache
     * @throws ClassCastException
     *             if the class of the specified key or value prevents it from being stored in this cache
     * @throws ContainerShutdownException
     *             if the cache has been shutdown
     * @throws NullPointerException
     *             if the specified key or value is null
     * @throws IllegalArgumentException
     *             if some property of the specified key or value prevents it from being stored in this cache
     */
    V replace(K key, V value);

    /**
     * Replaces the entry for a key only if currently mapped to a given value. This is equivalent to
     * 
     * <pre>
     * if (cache.containsKey(key) &amp;&amp; cache.get(key).equals(oldValue)) {
     *     cache.put(key, newValue);
     *     return true;
     * } else
     *     return false;
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param key
     *            key with which the specified value is associated
     * @param oldValue
     *            value expected to be associated with the specified key
     * @param newValue
     *            value to be associated with the specified key
     * @return <tt>true</tt> if the value was replaced
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this cache
     * @throws ClassCastException
     *             if the class of a specified key or value prevents it from being stored in this cache
     * @throws NullPointerException
     *             if a specified key or value is null
     * @throws ContainerShutdownException
     *             if the cache has been shutdown
     * @throws IllegalArgumentException
     *             if some property of a specified key or value prevents it from being stored in this cache
     */
    boolean replace(K key, V oldValue, V newValue);

    /**
     * Returns the number of elements in this cache. If the cache contains more than <tt>Integer.MAX_VALUE</tt>
     * elements, returns <tt>Integer.MAX_VALUE</tt>.
     * <p>
     * If this cache has been shutdown this method returns <tt>0</tt>.
     * 
     * @return the number of elements in this cache
     */
    int size();

    /**
     * Returns a {@link Collection} view of the values contained in this cache. The collection is backed by the cache,
     * so changes to the cache are reflected in the collection, and vice-versa. If the cache is modified while an
     * iteration over the collection is in progress (except through the iterator's own <tt>remove</tt> operation), the
     * results of the iteration are undefined. The collection supports element removal, which removes the corresponding
     * mapping from the cache, via the <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support the <tt>add</tt> or <tt>addAll</tt>
     * operations.
     * <p>
     * If this cache has been shutdown this method returns an empty collection.
     * 
     * @return a collection view of the values contained in this cache
     * @see #view()
     */
    Collection<V> values();

    /**
     * Returns a {@link CacheView} view of the mappings contained in this cache, respecting any predicates that have
     * been set using {@link #filter()}. The set is backed by the cache, so changes to the cache are reflected in the
     * set, and vice-versa. The returned view can be used for extracting entries from the cache and performing
     * calculations on the data in the cache.
     * <p>
     * <b>Sample usages.</b>
     * 
     * Given a cache with person names (String) as keys and the age of each person as a value, the following shows some
     * examples.
     * 
     * <pre>
     * Cache&lt;String, Integer&gt; cache = ...;
     * Iterable&lt;String&gt; persons = Arrays.asList(&quot;Peter&quot;, &quot;Joe&quot;, &quot;Hahnah&quot;);
     * 
     * Returns a map with the name of the person mapping to the age of the person
     * Map&lt;String, Integer&gt; personAge = cache.getAll(persons).keyValues().toMap();
     * 
     * Returns a sorted map (iterators will return entries in sorted order) with the name of the person mapping to the age of the person
     * Map&lt;String, Integer&gt; personAgeSorted = cache.getAll(persons).orderByValuesMax().keyValues().toMap();
     * 
     * Returns the maximum age of any of the 3 persons 
     * Integer maximumAge = cache.getAll(persons).values().max();
     * 
     * Returns the minimum age of any of the 3 persons
     * Integer minimumAge = cache.getAll(persons).values().max();
     * </pre>
     * 
     * @return a cache view of the entries contained in this cache
     */
    CacheView<K, V> view();

    /**
     * Returns a {@link CacheServices} instance that can be used for easily accessing a number of cache services.
     * 
     * @return a CacheServices object
     */
    CacheServices<K, V> with();

    /**
     * @return a factory that can be used to create various instances of {@link CacheWriter} and
     *         {@link CacheBatchWriter}
     */
    CacheCrud<K, V> withCrud();
}
