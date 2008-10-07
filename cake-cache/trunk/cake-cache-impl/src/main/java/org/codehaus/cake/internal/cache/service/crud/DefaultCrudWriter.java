package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheDataExtractor;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public class DefaultCrudWriter<K, V, R> extends AbstractCrudWriter<K, V, R> {

    public static Op NOT_NULL = new Op<CacheEntry<?, ?>, Boolean>() {
        public Boolean op(CacheEntry<?, ?> a) {
            return a != null;
        }
    };

    private final CacheRequestFactory<K, V> factory;
    private final Op<CacheEntry<K, V>, R> newEntry;
    private final Op<CacheEntry<K, V>, R> previous;
    private final CacheProcessor<K, V> processor;

    public DefaultCrudWriter(CacheRequestFactory<K, V> factory, CacheProcessor<K, V> processor,
            Op<CacheEntry<K, V>, R> previous, Op<CacheEntry<K, V>, R> newEntry) {
        this.factory = factory;
        this.processor = processor;
        this.previous = previous;
        this.newEntry = newEntry;
    }

    private R put(AddEntryRequest<K, V> r) {
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

    public R put(K key, Predicate<CacheEntry<K, V>> updatePredicate, V value, AttributeMap attributes) {
        return put(factory.createUpdate(key, attributes, value, updatePredicate, null, null));
    }

    public R putLazy(K key, Predicate<CacheEntry<K, V>> predicate, Op<? extends K, CacheEntry<K, V>> factory) {
        return put(this.factory.createUpdate(key, predicate, factory, null, null));
    }

    public R remove(K key, Predicate<CacheEntry<K, V>> updatePredicate) {
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

    public static <K, V> CrudWriter<K, V, Boolean> previousNotNull(CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor) {
        return new DefaultCrudWriter<K, V, Boolean>(factory, processor, (Op) NOT_NULL, null);
    }

    public static <K, V> CrudWriter<K, V, V> returnPreviousValue(CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor) {
        return new DefaultCrudWriter<K, V, V>(factory, processor, CacheDataExtractor.ONLY_VALUE, null);
    }

    public static <K, V, R> CrudWriter<K, V, R> withPrevious(CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor, Op<CacheEntry<K, V>, R> previous) {
        return new DefaultCrudWriter<K, V, R>(factory, processor, previous, null);
    }
}
