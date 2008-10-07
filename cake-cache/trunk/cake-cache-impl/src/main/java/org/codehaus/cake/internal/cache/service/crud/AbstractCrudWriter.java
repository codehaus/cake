package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.internal.cache.CachePredicates;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public abstract class AbstractCrudWriter<K, V, R> implements CrudWriter<K, V, R> {

    /** {@inheritDoc} */
    public final R put(K key, Predicate<CacheEntry<K, V>> predicate, V value) {
        return put(key, predicate, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public final R put(K key, V value) {
        return put(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public final R put(K key, V value, AttributeMap attributes) {
        return put(key, Predicates.TRUE, value, attributes);
    }

    /** {@inheritDoc} */
    public final R putIfAbsent(K key, V value) {
        return putIfAbsent(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public final R putIfAbsent(K key, V value, AttributeMap attributes) {
        return put(key, Predicates.IS_NULL, value, attributes);
    }

    /** {@inheritDoc} */
    public final R putIfAbsentLazy(K key, Op<? extends K, CacheEntry<K, V>> factory) {
        return putLazy(key, Predicates.IS_NULL, factory);
    }

    /** {@inheritDoc} */
    public final R remove(K key) {
        return remove(key, Predicates.TRUE);
    }

    /** {@inheritDoc} */
    public final R remove(K key, V value) {
        return remove(key, CachePredicates.<K, V> valueEquals(value));
    }

    /** {@inheritDoc} */
    public final R replace(K key, V value) {
        return replace(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public final R replace(K key, V value, AttributeMap attributes) {
        return put(key, Predicates.IS_NOT_NULL, value, attributes);
    }

    /** {@inheritDoc} */
    public final R replace(K key, V oldValue, V newValue) {
        return replace(key, oldValue, newValue, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public final R replace(K key, V oldValue, V newValue, AttributeMap attributes) {
        return put(key, CachePredicates.<K, V> valueEquals(oldValue), newValue, attributes);
    }
}
