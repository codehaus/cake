package org.codehaus.cake.internal.cache.service.crud;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.CacheDataExtractor;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.crud.WriteService;
import org.codehaus.cake.internal.cache.CachePredicates;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.ops.Predicates;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public class DefaultWriteService<K, V, R> implements WriteService<K, V, R> {

    public static Op NOT_NULL = new Op<CacheEntry<?, ?>, Boolean>() {
        public Boolean op(CacheEntry<?, ?> a) {
            return a != null;
        }
    };

    private final CacheRequestFactory<K, V> factory;

    private final Op<CacheEntry<K, V>, R> newEntry;
    private final Op<CacheEntry<K, V>, R> previous;
    private final CacheProcessor<K, V> processor;
    public DefaultWriteService(CacheRequestFactory<K, V> factory, CacheProcessor<K, V> processor,
            Op<CacheEntry<K, V>, R> previous, Op<CacheEntry<K, V>, R> newEntry) {
        this.factory = factory;
        this.processor = processor;
        this.previous = previous;
        this.newEntry = newEntry;
    }

    private R add(K key, V value, AttributeMap attributes, Predicate updatePredicate) {
        AddEntryRequest<K, V> r = factory.createUpdate(key, attributes, value, updatePredicate, null, null);
        processor.process(r);

        if (previous == null) {
            return null;
        }
        CacheEntry<K, V> e = r.getPreviousAsEntry();
        if (previous == NOT_NULL) {
            return e == null ? (R) Boolean.FALSE : previous.op(e);
        }
        return e == null ? null : previous.op(e);
    }

    private R add(K key, V value, Predicate updatePredicate) {
        return add(key, value, Attributes.EMPTY_ATTRIBUTE_MAP, updatePredicate);
    }

    /** {@inheritDoc} */
    public R put(K key, V value) {
        return put(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public R put(K key, V value, AttributeMap attributes) {
        return add(key, value, attributes, null);
    }

    /** {@inheritDoc} */
    public void putAll(Map<? extends K, ? extends V> t) {
        AddEntriesRequest<K, V> r = factory.createEntries(t);
        processor.process(r);
    }

    /** {@inheritDoc} */
    public R putIfAbsent(K key, V value) {
        return add(key, value, Predicates.IS_NULL);
    }

    /** {@inheritDoc} */
    public R putIfAbsent(K key, V value, AttributeMap attributes) {
        return add(key, value, attributes, Predicates.IS_NULL);
    }

    /** {@inheritDoc} */

    public R remove(K key) {
        return remove0(key, null);
    }

    /** {@inheritDoc} */

    public R remove(K key, V value) {
        return remove0(key, CachePredicates.valueEquals(value));
    }

    private R remove0(K key, Predicate updatePredicate) {
        RemoveEntryRequest<K, V> r = factory.remove(key, updatePredicate, previous);
        processor.process(r);
        if (previous == null) {
            return null;
        }
        CacheEntry<K, V> e = r.getPreviousEntry();
        if (previous == NOT_NULL) {
            return e == null ? (R) Boolean.FALSE : previous.op(e);
        }
        return e == null ? null : previous.op(e);
    }

    /** {@inheritDoc} */
    public R replace(K key, V value) {
        return add(key, value, Predicates.IS_NOT_NULL);
    }

    /** {@inheritDoc} */
    public R replace(K key, V value, AttributeMap attributes) {
        return add(key, value, attributes, Predicates.IS_NOT_NULL);
    }

    /** {@inheritDoc} */
    public R replace(K key, V oldValue, V newValue) {
        return add(key, newValue, CachePredicates.valueEquals(oldValue));
    }

    public static <K, V> WriteService<K, V, Boolean> previousNotNull(CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor) {
        return new DefaultWriteService<K, V, Boolean>(factory, processor, (Op) NOT_NULL, null);
    }

    public static <K, V> WriteService<K, V, V> returnPreviousValue(CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor) {
        return new DefaultWriteService<K, V, V>(factory, processor, CacheDataExtractor.ONLY_VALUE, null);
    }

    public static <K, V, R> WriteService<K, V, R> withPrevious(CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor, Op<CacheEntry<K, V>, R> previous) {
        return new DefaultWriteService<K, V, R>(factory, processor, previous, null);
    }
}
