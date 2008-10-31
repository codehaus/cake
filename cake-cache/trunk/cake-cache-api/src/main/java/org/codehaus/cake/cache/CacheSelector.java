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
 * to {@link Cache#select()}
 * 
 * @param <K>
 * @param <V>
 */
public interface CacheSelector<K, V> {
    <T> Cache<K, V> on(Attribute<T> a, Predicate<T> p);

    Cache<K, V> on(BinaryPredicate<? super K, ? super V> selector);

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

    Cache<K, V> on(Predicate<CacheEntry<K, V>> filter);

    Cache<K, V> on(ShortAttribute attribute, ShortPredicate filter);

    /**
     * Returns a cache view where the all the keys of elements is accepted by the specified predicate.
     * 
     * @param p
     *            the predicate that keys should be tested against
     * @return
     */
    Cache<K, V> onKey(Predicate<K> filter);

    /**
     * Returns a filtered view containing only those elements where the elements key is of the specified type.
     * 
     * @param <T>
     *            the type of the keys that should be visible in the view
     * @param c
     *            the type of the keys that should be visible in the view
     * @return a cache view where all the keys are of the specified type
     */
    <T extends K> Cache<T, V> onKeyType(Class<T> c);

    Cache<K, V> onValue(Predicate<V> filter);

    /**
     * Returns a filtered view containing only those elements where the elements value is of the specified type.
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    <T extends V> Cache<K, T> onValueType(Class<T> clazz);
}
