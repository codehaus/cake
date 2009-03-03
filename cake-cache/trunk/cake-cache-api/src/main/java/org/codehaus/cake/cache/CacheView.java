package org.codehaus.cake.cache;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.ComparableObjectAttribute;
import org.codehaus.cake.util.attribute.ObjectAttribute;
import org.codehaus.cake.util.collection.MapView;
import org.codehaus.cake.util.collection.View;

/**
 * A cache view is a virtual view of a collection of cache entries.
 * <p>
 * On way to obtain i The two primary ways for optaining a cache view it either by calling
 * {@link Cache#getAll(Iterable)} or {@link Cache#view()}.
 * <p>
 * 
 * 
 * 
 * A CacheView is normally obtained by calling {@link Cache#view()} can be used to extract data from a {@link Cache},
 * allowing both limiting the number of results (using {@link #setLimit(int)}) and specifying the order that the result
 * should be returned in.
 * 
 * For example, given a cache with {@link Integer} keys, we can create a list of all entries with odd keys by using:
 * 
 * <pre>
 * Cache&lt;Integer, String&gt; cache =...
 * List&lt;CacheEntry&lt;Integer,String&gt;&gt; list= cache.withFilter(IntegerPredicates.IS_ODD).view().entries().toList();
 * </pre>
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java 239 2008-12-23 20:29:21Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public interface CacheView<K, V> {

    /**
     * Creates a new view with all entries in this view. The following example extract all entries in this view as a
     * {@link List}:
     * 
     * <pre>
     * List&lt;CacheEntry&lt;K, V&gt;&gt; list = cacheview.entries().toList();
     * </pre>
     * 
     * @return the new view
     */
    View<CacheEntry<K, V>> entries();

    /**
     * Creates a new view with the {@link CacheEntry#getKey() key} for each entry. The following example extract all
     * keys in this view as a {@link List}:
     * 
     * <pre>
     * List&lt;K&gt; list = cacheview.keys().toList();
     * </pre>
     * 
     * @return the new view
     */
    View<K> keys();

    /**
     * Creates a new map view with the keys and values of this view. The following example extract all key-value
     * mappings as a {@link Map}:
     * 
     * <pre>
     * Map&lt;K, V&gt; list = cacheview.keysValues().toMap();
     * </pre>
     * 
     * @return the new view
     */
    MapView<K, V> keysValues();

    /**
     * Creates a new view where all entries are ordered accordingly to the specified comparator.
     * 
     * @param comparator
     *            the comparator
     * @throws NullPointerException
     *             if the specified comparator is null
     * @return the new view
     */
    CacheView<K, V> orderBy(Comparator<? super CacheEntry<K, V>> comparator);

    /**
     * Creates a new view where all entries are ordered accordingly to the entries value of the specified attribute.
     * 
     * @param attribute
     *            the attribute used for sorting
     * @param comparator
     *            the comparator
     * @return the new view
     */
    <T> CacheView<K, V> orderByAttribute(Attribute<T> attribute, Comparator<? extends T> comparator);

    /**
     * Creates a new view where all elements are ordered accordingly to the natural order of the attribute.
     * 
     * @param attribute
     *            the attribute
     * @throws IllegalArgumentException
     *             if the specified attribute is a subclass of {@link ObjectAttribute} but not
     *             {@link ComparableObjectAttribute}
     * @return this query
     */
    CacheView<K, V> orderByAttributeMax(Attribute<?> attribute);

    /**
     * Creates a new view where all entries are ordered according to the natural order of their values.
     * 
     * @return the new view
     */
    CacheView<K, V> orderByAttributeMin(Attribute<?> attribute);

    /**
     * Creates a new CacheView where the elements are ordered accordingly to the specified Comparator.
     * 
     * @return a new query
     */
    CacheView<K, V> orderByKeys(Comparator<K> comparator);

    /**
     * Assuming all values are {@link Comparable}, returns a new view of the entries contained in this view. The new
     * view is backed by the old view, so changes to the old view or the datastructure backing the old view are
     * reflected in the new view. The entries in the new view are ordered by the natural order of their
     * {@link CacheEntry#getKey() values}.
     * <p>
     * Usage:
     * 
     * <pre>
     * Given a cache with the following entries [&quot;A&quot;-&gt;1, &quot;B&quot; -&gt;0, &quot;C&quot;-&gt;3, &quot;D&quot; -&gt;4]
     * 
     * cache.view().orderByKeysMax().entries().toList();
     * 
     * will return a list with the following entries [&quot;D&quot;-&gt;4, &quot;C&quot;-&gt;3, &quot;A&quot;-&gt;1, &quot;B&quot;-&gt;0]
     * </pre>
     * 
     * @return the new view
     */
    CacheView<K, V> orderByKeysMax();

    /**
     * Assuming all keys are {@link Comparable}, creates a new view where all entries are ordered according to the
     * natural order of their keys.
     * 
     * @return the new view
     */
    CacheView<K, V> orderByKeysMin();

    /**
     * Creates a new CacheView where the elements are ordered accordingly to the specified Comparator.
     * 
     * @return a new query
     */
    CacheView<K, V> orderByValues(Comparator<V> comparator);

    /**
     * Assuming all values are {@link Comparable}, returns a new view of the entries contained in this view. The new
     * view is backed by the old view, so changes to the old view or the datastructure backing the old view are
     * reflected in the new view. The entries in the new view are ordered by the natural order of their
     * {@link CacheEntry#getValue() values}.
     * <p>
     * Usage:
     * 
     * <pre>
     * Given a cache with the following entries [&quot;A&quot;-&gt;1, &quot;B&quot; -&gt;0, &quot;C&quot;-&gt;3, &quot;D&quot; -&gt;4]
     * 
     * cache.view().orderByKeysMax().entries().toList();
     * 
     * will return a list with the following entries [&quot;D&quot;-&gt;4, &quot;C&quot;-&gt;3, &quot;A&quot;-&gt;1, &quot;B&quot;-&gt;0]
     * </pre>
     * 
     * @return a view of the entries contained in this view.
     */
    CacheView<K, V> orderByValuesMax();

    /**
     * Assuming all values are {@link Comparable}, creates a new view where all entries are ordered according to the
     * natural order of their values.
     * 
     * @return the new view
     */
    CacheView<K, V> orderByValuesMin();

    /**
     * Creates a new cache view which limits the number of mappings in this view. If the mappings in this view are
     * ordered, for example, if this view has been created by calling {@link #orderBy(Comparator)} on an existing view.
     * The elements in the new view will keep this ordering.
     * <p>
     * Usage: Returning a list of the 10 highest numbers in a view containing only integers:
     * 
     * <pre>
     * View&lt;Integer&gt; v=...
     * List&lt;Integer&gt; list = v.orderByMax().setLimit(10).toList();
     * </pre>
     * 
     * @param limit
     *            the maximum number of mappings in the new view
     * @throws IllegalArgumentException
     *             if the limit is not positive (>0)
     * @return a new view
     */
    CacheView<K, V> setLimit(long limit);

    /**
     * Returns an array containing all of the entries in this view. If this view is ordered, the returned array will
     * preserve this ordering.
     * <p>
     * The returned array will be "safe" in that no references to it are maintained by this view. (In other words, this
     * method must allocate a new array even if this view is backed in any way by an array). The caller is thus free to
     * modify the returned array.
     * 
     * @return an array containing all of the entries in this view
     */
    CacheEntry<K, V>[] toArray();

    /**
     * Creates a new view with the {@link CacheEntry#getValue() value} for each entry.
     * <p>
     * The following example extract all values in this view as a {@link List}:
     * 
     * <pre>
     * List&lt;V&gt; list = cacheview.values().toList();
     * </pre>
     * 
     * @return the new view
     */
    View<V> values();
}
