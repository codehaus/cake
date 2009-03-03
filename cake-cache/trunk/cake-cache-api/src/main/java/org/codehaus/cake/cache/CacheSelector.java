/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache;

import java.util.Map.Entry;

import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.BooleanAttribute;
import org.codehaus.cake.util.attribute.ByteAttribute;
import org.codehaus.cake.util.attribute.CharAttribute;
import org.codehaus.cake.util.attribute.DoubleAttribute;
import org.codehaus.cake.util.attribute.FloatAttribute;
import org.codehaus.cake.util.attribute.IntAttribute;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.codehaus.cake.util.attribute.ShortAttribute;
import org.codehaus.cake.util.ops.Ops.BinaryPredicate;
import org.codehaus.cake.util.ops.Ops.BytePredicate;
import org.codehaus.cake.util.ops.Ops.CharPredicate;
import org.codehaus.cake.util.ops.Ops.DoublePredicate;
import org.codehaus.cake.util.ops.Ops.FloatPredicate;
import org.codehaus.cake.util.ops.Ops.IntPredicate;
import org.codehaus.cake.util.ops.Ops.LongPredicate;
import org.codehaus.cake.util.ops.Ops.Predicate;
import org.codehaus.cake.util.ops.Ops.ShortPredicate;

/**
 * A CacheSelector is used for creating filtered views of a cache. A CacheSelector is normally acquire by calling
 * {@link Cache#filter()}
 * <p>
 * There are a number of limitations on the operations that can invoked on a filtered cache view. in general is not
 * allowed to modify entries in a filtered view. There are two exceptions, it is allowed to remove entries from a view,
 * and it is allowed to bulk replace entries in a view. Only bulk?
 * 
 * Inserting entries into a filtered cache view is not allowed.
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */

public interface CacheSelector<K, V> {

    /**
     * Returns a new filtered cache view containing only those entries where the value of the specified attribute is
     * accepted by the specified filter. The filtered cache view is backed by the cache, so changes to the cache are
     * reflected in the filtered cache view, and vice-versa.
     * <p>
     * <b>Sample usages.</b>
     * 
     * Given a cache with Strings keys and Integer values, the following example will create a filtered cache view that
     * contains only those mappings where the key starts with <tt>https://</tt>
     * 
     * <pre>
     * Cache&lt;String, Integer&gt; cache = null;
     * Cache&lt;String, Integer&gt; secureCache = cache.select().onKey(StringOps.startsWith(&quot;https://&quot;));
     * </pre>
     * 
     * @param attribute
     *            the attribute used for filtering
     * @param filter
     *            the filter used for evaluation
     * @return a new filtered cache view where the specified attribute is accepted by the specified filter
     * @throws NullPointerException
     *             if the specified attribute or predicate is null
     */
    <T> Cache<K, V> on(Attribute<T> attribute, Predicate<T> filter);

    Cache<K, V> on(BinaryPredicate<? super K, ? super V> filter);

    /**
     * Equivalent to {@link #on(Attribute, Predicate)} accept taking a primitive attribute and the value that the
     * attribute must match.
     * 
     * @param attribute
     *            the attribute used for filtering
     * @param value
     *            the value that the attribute must match
     * @return a new filtered cache view where the specified attribute matches the specified value
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    Cache<K, V> on(BooleanAttribute attribute, boolean value);

    /**
     * Equivalent to {@link #on(Attribute, Predicate)} accept taking a primitive attribute and filter.
     * 
     * @param attribute
     *            the attribute used for filtering
     * @param filter
     *            the filter used for evaluation
     * @return a new filtered cache view where the specified attribute is accepted by the specified filter
     * @throws NullPointerException
     *             if the specified attribute or predicate is null
     */
    Cache<K, V> on(ByteAttribute attribute, BytePredicate filter);

    /**
     * Equivalent to {@link #on(Attribute, Predicate)} accept taking a primitive attribute and filter.
     * 
     * @param attribute
     *            the attribute used for filtering
     * @param filter
     *            the filter used for evaluation
     * @return a new filtered cache view where the specified attribute is accepted by the specified filter
     * @throws NullPointerException
     *             if the specified attribute or predicate is null
     */
    Cache<K, V> on(CharAttribute attribute, CharPredicate filter);

    /**
     * Equivalent to {@link #on(Attribute, Predicate)} accept taking a primitive attribute and filter.
     * 
     * @param attribute
     *            the attribute used for filtering
     * @param filter
     *            the filter used for evaluation
     * @return a new filtered cache view where the specified attribute is accepted by the specified filter
     * @throws NullPointerException
     *             if the specified attribute or predicate is null
     */
    Cache<K, V> on(DoubleAttribute attribute, DoublePredicate filter);

    /**
     * Equivalent to {@link #on(Attribute, Predicate)} accept taking a primitive attribute and filter.
     * 
     * @param attribute
     *            the attribute used for filtering
     * @param filter
     *            the filter used for evaluation
     * @return a new filtered cache view where the specified attribute is accepted by the specified filter
     * @throws NullPointerException
     *             if the specified attribute or predicate is null
     */
    Cache<K, V> on(FloatAttribute attribute, FloatPredicate filter);

    /**
     * Equivalent to {@link #on(Attribute, Predicate)} accept taking a primitive attribute and filter.
     * 
     * @param attribute
     *            the attribute used for filtering
     * @param filter
     *            the filter used for evaluation
     * @return a new filtered cache view where the specified attribute is accepted by the specified filter
     * @throws NullPointerException
     *             if the specified attribute or predicate is null
     */
    Cache<K, V> on(IntAttribute attribute, IntPredicate filter);

    /**
     * Equivalent to {@link #on(Attribute, Predicate)} accept taking a primitive attribute and filter.
     * 
     * @param attribute
     *            the attribute used for filtering
     * @param filter
     *            the filter used for evaluation
     * @return a new filtered cache view where the specified attribute is accepted by the specified filter
     * @throws NullPointerException
     *             if the specified attribute or predicate is null
     */
    Cache<K, V> on(LongAttribute attribute, LongPredicate filter);

    Cache<K, V> on(Predicate<CacheEntry<K, V>> filter);

    /**
     * Equivalent to {@link #on(Attribute, Predicate)} accept taking a primitive attribute and filter.
     * 
     * @param attribute
     *            the attribute used for filtering
     * @param filter
     *            the filter used for evaluation
     * @return a new filtered cache view where the specified attribute is accepted by the specified filter
     * @throws NullPointerException
     *             if the specified attribute or predicate is null
     */
    Cache<K, V> on(ShortAttribute attribute, ShortPredicate filter);

    /**
     * Returns a new filtered cache view containing only those entries where the {@link Entry#getKey() key} is accepted
     * by the specified filter. The filtered cache view is backed by the cache, so changes to the cache are reflected in
     * the filtered cache view, and vice-versa.
     * <p>
     * <b>Sample usages.</b>
     * 
     * Given a cache with String keys and Integer values, the following example creates a filtered cache view containing
     * only those mappings where the key starts with <tt>https://</tt>.
     * 
     * <pre>
     * Cache&lt;String, Integer&gt; cache = null;
     * Cache&lt;String, Integer&gt; secureCache = cache.select().onKey(StringOps.startsWith(&quot;https://&quot;));
     * </pre>
     * 
     * @param filter
     *            the filter used for evaluation
     * @return a new filtered cache view where all keys are accepted by the specified filter
     * @throws NullPointerException
     *             if the specified predicate is null
     */
    Cache<K, V> onKey(Predicate<? super K> filter);

    /**
     * Returns a new filtered cache view containing only those entries where the {@link Entry#getKey() key} is of the
     * specified type. The filtered cache view is backed by the cache, so changes to the cache are reflected in the
     * filtered cache view, and vice-versa.
     * <p>
     * <b>Sample usages.</b>
     * 
     * Given a cache with Number keys and String values, the following example creates a filtered cache view containing
     * only those mappings where the key is of type Double.
     * 
     * <pre>
     * Cache&lt;Number, String&gt; cache = somecache;
     * Cache&lt;Double, String&gt; doubleCache = cache.select().onKeyType(Double.class);
     * </pre>
     * 
     * @param <T>
     *            the type of keys that are accepted
     * @param clazz
     *            the type of keys that are accepted
     * @return a new filtered cache view where all keys are of the specified type
     * @throws NullPointerException
     *             if the specified class is null
     */
    <T extends K> Cache<T, V> onKeyType(Class<T> c);

    /**
     * Returns a new filtered cache view containing only those entries where the {@link Entry#getValue() value} is
     * accepted by the specified filter. The filtered cache view is backed by the cache, so changes to the cache are
     * reflected in the filtered cache view, and vice-versa.
     * <p>
     * <b>Sample usages.</b>
     * 
     * Given a cache with Number keys and String values, the following example creates a filtered cache view containing
     * only those mappings where the length of the String value is greater then 5.
     * 
     * <pre>
     * Cache&lt;Integer, String&gt; cache = somecache;
     * Cache&lt;Integer, String&gt; longStringCache = cache.select().onValue(new Predicate&lt;String&gt;() {
     *     public boolean op(String a) {
     *         return a.length() &gt; 5;
     *     }
     * });
     * </pre>
     * 
     * @param filter
     *            the filter used for evaluation
     * @return a new filtered cache view where all values are accepted by the specified filter
     * @throws NullPointerException
     *             if the specified predicate is null
     */
    Cache<K, V> onValue(Predicate<? super V> filter);

    /**
     * Returns a new filtered cache view containing only those entries where the {@link Entry#getValue() value} is of
     * the specified type. The filtered cache view is backed by the cache, so changes to the cache are reflected in the
     * filtered cache view, and vice-versa.
     * <p>
     * <b>Sample usages.</b>
     * 
     * Given a cache with String keys and Number values, the following example creates a filtered cache view containing
     * only those mappings where the value is of type Integer.
     * 
     * <pre>
     * Cache&lt;String, Number&gt; cache = somecache;
     * Cache&lt;String, Integer&gt; intCache = cache.select().onValueType(Integer.class);
     * </pre>
     * 
     * @param <T>
     *            the type of values that are accepted
     * @param clazz
     *            the type of values that are accepted
     * @return a new filtered cache view where all values are of the specified type
     * @throws NullPointerException
     *             if the specified class is null
     */
    <T extends V> Cache<K, T> onValueType(Class<T> clazz);
}

// Cache<K, V> onValue(String method, Predicate<?> filter);
// <E> Cache<K, V> onValue(String method, Class<E> resultType, Predicate<? super E> filter);
