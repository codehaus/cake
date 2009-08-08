package org.codehaus.cake.util;

import org.codehaus.cake.internal.util.EquivalenceUtil;

/**
 * An object performing equality comparisons, along with a hash function consistent with this comparison. The type
 * signatures of the methods of this interface reflect those of {@link java.util.Map}: While only elements of
 * <code>K</code> may be entered into a Map, any <code>Object</code> may be tested for membership. Note that the
 * performance of hash maps is heavily dependent on the quality of hash functions.
 */
public interface Equivalence<K> {

    /**
     * An Equivalence object performing {@link Object#equals} based comparisons and using {@link Object#hashCode} for
     * hashing.
     */
    Equivalence<Object> EQUALS = EquivalenceUtil.EQUALS;

    /**
     * An Equivalence object performing identity-based comparisons and using {@link System#identityHashCode} for
     * hashing.
     */
    Equivalence<Object> IDENTITY = EquivalenceUtil.IDENTITY;

    /**
     * Returns true if the given objects are considered equal. This function must obey an equivalence relation: equal(a,
     * a) is always true, equal(a, b) implies equal(b, a), and (equal(a, b) &amp;&amp; equal(b, c) implies equal(a, c).
     * Note that the second argument need not be known to have the same declared type as the first.
     * 
     * @param key
     *            a key in, or being placed in, the map
     * @param x
     *            an object queried for membership
     * @return true if considered equal
     */
    boolean equal(K key, Object x);

    /**
     * Returns a hash value such that equal(a, b) implies hash(a)==hash(b).
     * 
     * @param x
     *            the object the hash should be calculated for
     * @return a hash value
     */
    int hash(Object x);
}
