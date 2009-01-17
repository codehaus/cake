package org.codehaus.cake.internal.cache.service.crud;

import java.util.Map;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.crud.CrudReader;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public class DefaultCrudReader<K, V, R> implements CrudReader<K, R> {
    private final Predicate<CacheEntry<K, V>> filter;

    private final CacheProcessor<K, V> processor;
    private final Op<CacheEntry<K, V>, R> extractor;

    public DefaultCrudReader(Predicate<CacheEntry<K, V>> filter, CacheProcessor<K, V> processor,
            Op<CacheEntry<K, V>, R> extractor) {
        this.filter = filter;
        this.processor = processor;
        this.extractor = extractor;
    }

    public R get(K key) {
        return processor.get(filter, key, Attributes.EMPTY_ATTRIBUTE_MAP, extractor);
    }

    public R get(K key, AttributeMap attributes) {
        return processor.get(filter, key, attributes, extractor);
    }

    public Map<K, R> getAll(Iterable<? extends K> keys) {
        return processor.getAll(filter, keys, extractor);
    }

}
