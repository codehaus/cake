package org.codehaus.cake.internal.cache.service.crud;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheDataExtractor;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.CacheWriter;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.util.Pair;

public class DefaultCrudWriter<K, V, R> extends AbstractCrudWriter<K, V, R> {

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
        return previous == null ? null : previous.op(r.getPreviousAsEntry());
    }

    public R putIf(Predicate<CacheEntry<K, V>> condition, K key, V value, AttributeMap attributes) {
        if (condition == null) {
            throw new NullPointerException("condition is null");
        }
        return put(factory.createUpdate(key, attributes, value, condition, null, null));
    }

    public R putIfLazy(Predicate<CacheEntry<K, V>> condition, K key, Op<? extends K, Pair<V, AttributeMap>> factory) {
        if (condition == null) {
            throw new NullPointerException("condition is null");
        } else if (factory == null) {
            throw new NullPointerException("factory is null");
        }
        return put(this.factory.createUpdate(key, condition, factory, null, null));
    }

    public R removeIf(Predicate<CacheEntry<K, V>> condition, K key) {
        if (condition == null) {
            throw new NullPointerException("condition is null");
        }
        RemoveEntryRequest<K, V> r = factory.remove(key, condition, previous);
        processor.process(r);
        return previous == null ? null : previous.op(r.getPreviousEntry());
    }

    public static <K, V> CacheWriter<K, V, Boolean> previousNotNull(CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor) {
        return new DefaultCrudWriter<K, V, Boolean>(factory, processor, CacheDataExtractor.IS_NOT_NULL, null);
    }

    public static <K, V> CacheWriter<K, V, V> returnPreviousValue(CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor) {
        return new DefaultCrudWriter<K, V, V>(factory, processor, CacheDataExtractor.ONLY_VALUE, null);
    }

    public static <K, V, R> CacheWriter<K, V, R> withPrevious(CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor, Op<CacheEntry<K, V>, R> previous) {
        return new DefaultCrudWriter<K, V, R>(factory, processor, previous, null);
    }
}
