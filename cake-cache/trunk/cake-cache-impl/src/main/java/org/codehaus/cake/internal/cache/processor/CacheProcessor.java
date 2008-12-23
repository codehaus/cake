package org.codehaus.cake.internal.cache.processor;

import java.util.List;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;
import org.codehaus.cake.internal.cache.query.DefaultQuery;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public interface CacheProcessor<K, V> {

    void process(AddEntriesRequest<K, V> r);

    void process(AddEntryRequest<K, V> r);

    void process(Predicate<CacheEntry<K, V>> filter, ClearCacheRequest<K, V> r);

    void process(RemoveEntriesRequest<K, V> r);

    void process(RemoveEntryRequest<K, V> r);

    void process(TrimToSizeRequest<K, V> r);

    void process(TrimToVolumeRequest<K, V> r);

    List<CacheEntry<K, V>> process(Predicate<CacheEntry<K, V>> filter,DefaultQuery<K, V> query);

    <T> T get(Predicate<CacheEntry<K, V>> selector, K key, AttributeMap attributes, Op<CacheEntry<K, V>, T> extractor);

    <T> Map<K, T> getAll(Predicate<CacheEntry<K, V>> selector, Iterable<? extends K> key,
            Op<CacheEntry<K, V>, T> extractor);

    // Iterable<K> getKeys(Predicate<CacheEntry<K, V>> predicate);

}
