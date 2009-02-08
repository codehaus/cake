package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.internal.cache.CachePredicates;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.util.Pair;

public abstract class AbstractCrudWriter<K, V, R> implements CrudWriter<K, V, R> {

    /** {@inheritDoc} */
    public final R putIf(Predicate<CacheEntry<K, V>> predicate, K key, V value) {
        return putIf(predicate, key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public final R put(K key, V value) {
        return put(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public final R put(K key, V value, AttributeMap attributes) {
        return putIf(Predicates.TRUE, key, value, attributes);
    }

    /** {@inheritDoc} */
    public final R putIfAbsent(K key, V value) {
        return putIfAbsent(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public final R putIfAbsent(K key, V value, AttributeMap attributes) {
        return putIf(Predicates.IS_NULL, key, value, attributes);
    }

    /** {@inheritDoc} */
    public final R putIfAbsentLazy(K key, Op<? extends K, Pair<V, AttributeMap>> factory) {
        return putIfLazy(Predicates.IS_NULL, key, factory);
    }

    /** {@inheritDoc} */
    public final R remove(K key) {
        return removeIf(Predicates.TRUE, key);
    }

    /** {@inheritDoc} */
    public final R remove(K key, V value) {
        return removeIf(CachePredicates.<K, V> valueEquals(value), key);
    }

    /** {@inheritDoc} */
    public final R replace(K key, V value) {
        return replace(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public final R replace(K key, V value, AttributeMap attributes) {
        return putIf(Predicates.IS_NOT_NULL, key, value, attributes);
    }

    /** {@inheritDoc} */
    public final R replace(K key, V oldValue, V newValue) {
        return replace(key, oldValue, newValue, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public final R replace(K key, V oldValue, V newValue, AttributeMap attributes) {
        return putIf(CachePredicates.<K, V> valueEquals(oldValue), key, newValue, attributes);
    }
}
