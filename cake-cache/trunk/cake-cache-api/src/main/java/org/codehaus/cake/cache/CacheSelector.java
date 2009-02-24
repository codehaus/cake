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

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.ByteAttribute;
import org.codehaus.cake.attribute.CharAttribute;
import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.FloatAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.ShortAttribute;
import org.codehaus.cake.ops.Ops.BinaryPredicate;
import org.codehaus.cake.ops.Ops.BytePredicate;
import org.codehaus.cake.ops.Ops.CharPredicate;
import org.codehaus.cake.ops.Ops.DoublePredicate;
import org.codehaus.cake.ops.Ops.FloatPredicate;
import org.codehaus.cake.ops.Ops.IntPredicate;
import org.codehaus.cake.ops.Ops.LongPredicate;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.ops.Ops.ShortPredicate;

/**
 * A CacheSelector is used for creating views of a cache where the ret A CacheSelector is normally acquire through calls
 * to {@link Cache#filter()}
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */

public interface CacheSelector<K, V>  {

    /**
     * Returns a cache view where the specified attribute.
     * 
     * @param a
     * @param value
     * @return
     */
    Cache<K, V> on(BooleanAttribute attribute, boolean value);

    Cache<K, V> on(ByteAttribute attribute, BytePredicate filter);

    Cache<K, V> on(CharAttribute attribute, CharPredicate filter);

    Cache<K, V> on(DoubleAttribute attribute, DoublePredicate filter);

    Cache<K, V> on(FloatAttribute attribute, FloatPredicate filter);

    Cache<K, V> on(IntAttribute attribute, IntPredicate filter);

    Cache<K, V> on(LongAttribute attribute, LongPredicate filter);

    Cache<K, V> on(ShortAttribute attribute, ShortPredicate filter);

    <T> Cache<K, V> on(Attribute<T> a, Predicate<T> p);

    Cache<K, V> on(BinaryPredicate<? super K, ? super V> selector);

    // /**
    // * Returns a cache view where the specified attribute.
    // *
    // * @param a
    // * @param value
    // * @return
    // */
    // Cache<K, V> on(BooleanAttribute attribute, boolean value);
    //
    // Cache<K, V> on(ByteAttribute attribute, BytePredicate filter);
    //
    // Cache<K, V> on(CharAttribute attribute, CharPredicate filter);
    //
    // Cache<K, V> on(DoubleAttribute attribute, DoublePredicate filter);
    //
    // Cache<K, V> on(FloatAttribute attribute, FloatPredicate filter);
    //
    // Cache<K, V> on(IntAttribute attribute, IntPredicate filter);
    //
    // Cache<K, V> on(LongAttribute attribute, LongPredicate filter);
    //
    Cache<K, V> on(Predicate<CacheEntry<K, V>> filter);

    //
    // Cache<K, V> on(ShortAttribute attribute, ShortPredicate filter);

    /**
     * Returns a filtered view containing only those elements key that are accepted by the specified filter. Given a
     * cache full of Strings mapping to an Integer the following example will create a cache view that contains only
     * those mappings where the key starts with <tt>https://</tt>
     * 
     * <pre>
     * Cache&lt;String, Integer&gt; cache = null;
     * Cache&lt;String, Integer&gt; longStrings = cache.select().onKey(StringOps.startsWith(&quot;https://&quot;));
     * </pre>
     * 
     * @param filter
     *            the filter used to evaluate whether a key should be included in the view
     * @return a cache view where all keys are accepted by the specified filter
     */
    Cache<K, V> onKey(Predicate<? super K> filter);

    /**
     * Returns a filtered view containing only those elements where the elements key is of the specified type. Given a
     * cache full of {@link Number}s mapping to a String the following will create a cache view that contains only
     * those mappings where the value is of type {@link Double}.
     * 
     * <pre>
     * Cache&lt;Number, String&gt; cache = somecache;
     * Cache&lt;Double, String&gt; onlyDoubles = cache.select().onKeyType(Double.class);
     * </pre>
     * 
     * @param <T>
     *            the type of keys that are accepted
     * @param clazz
     *            the type of keys that are accepted
     * @return a cache view with only the specific type of keys
     */
    <T extends K> Cache<T, V> onKeyType(Class<T> c);

    /**
     * Returns a filtered view containing only those entries there the entrys value are accepted by the specified
     * filter.
     * <p>
     * Given a cache with {@link String} to {@link Number} mappings, the following example will create a new view that
     * contains only those mappings where the length of the String is greater then 5.
     * 
     * <pre>
     * Cache&lt;Integer, String&gt; cache = somecache;
     * Cache&lt;Integer, String&gt; longStrings = cache.select().onValue(new Predicate&lt;String&gt;() {
     *     public boolean op(String a) {
     *         return a.length() &gt; 5;
     *     }
     * });
     * </pre>
     * 
     * @param filter
     *            the filter used to evaluate whether a value should be included in the view
     * @throws NullPointerException
     *             if the specified predicate is null
     * @return a cache view where all values are accepted by the specified filter
     */
    Cache<K, V> onValue(Predicate<? super V> filter);

    // Cache<K, V> onValue(String method, Predicate<?> filter);
    // <E> Cache<K, V> onValue(String method, Class<E> resultType, Predicate<? super E> filter);

    /**
     * Returns a filtered view containing only those entries where the entrys value is of the specified type.
     * <p>
     * Given a cache with {@link String} to {@link Number} mappings, the following will create a new view that contains
     * only those mappings where the value is of type {@link Integer}.
     * 
     * <pre>
     * Cache&lt;String, Number&gt; cache = somecache;
     * Cache&lt;String, Integer&gt; allInts = cache.select().onValueType(Integer.class);
     * </pre>
     * 
     * @param <T>
     *            the type of values that are accepted
     * @param clazz
     *            the type of values that are accepted
     * @throws NullPointerException
     *             if the specified class is null
     * @return a cache view with only the specific type of values
     */
    <T extends V> Cache<K, T> onValueType(Class<T> clazz);
}
