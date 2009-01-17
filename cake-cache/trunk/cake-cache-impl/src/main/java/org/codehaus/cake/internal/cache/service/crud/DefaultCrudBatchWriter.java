package org.codehaus.cake.internal.cache.service.crud;

import java.util.Map;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.crud.CrudBatchWriter;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.ops.Ops.Op;

public class DefaultCrudBatchWriter<K, V, R> implements CrudBatchWriter<K, V, R> {

    private final CacheRequestFactory<K, V> factory;
    private final Op<CacheEntry<K, V>, R> newEntry;
    private final Op<CacheEntry<K, V>, R> previous;
    private final CacheProcessor<K, V> processor;

    public DefaultCrudBatchWriter(CacheRequestFactory<K, V> factory, CacheProcessor<K, V> processor,
            Op<CacheEntry<K, V>, R> previous, Op<CacheEntry<K, V>, R> newEntry) {
        this.factory = factory;
        this.processor = processor;
        this.previous = previous;
        this.newEntry = newEntry;
    }

    public R putAll(Map<? extends K, ? extends V> t) {
        return putAll(t, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public R putAll(Map<? extends K, ? extends V> t, AttributeMap attributes) {
        AddEntriesRequest<K, V> r = factory.createEntries(t, attributes);
        processor.process(r);
        return null;
    }

    public static <K, V> DefaultCrudBatchWriter<K, V, Void> returnVoid(CacheRequestFactory<K, V> factory,
            CacheProcessor<K, V> processor) {
        return new DefaultCrudBatchWriter<K, V, Void>(factory, processor, null, null);
    }

    public R removeAll(Iterable<? extends K> keys) {
        RemoveEntriesRequest<K, V> r = factory.removeAll(keys);
        processor.process(r);
        return null;
    }

    public R removeAll() {
        return null;
    }
}
