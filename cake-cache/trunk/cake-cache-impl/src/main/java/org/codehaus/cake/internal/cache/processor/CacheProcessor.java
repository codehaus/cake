package org.codehaus.cake.internal.cache.processor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;
import org.codehaus.cake.internal.cache.view.AbstractView;
import org.codehaus.cake.internal.cache.view.util.QueryStack;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;

public interface CacheProcessor<K, V> {

    public static final long SIZE = 123123871263L;

    void process(AddEntriesRequest<K, V> r);

    void process(AddEntryRequest<K, V> r);

    void process(Predicate<CacheEntry<K, V>> filter, ClearCacheRequest<K, V> r);

    void process(RemoveEntriesRequest<K, V> r);

    void process(RemoveEntryRequest<K, V> r);

    void process(TrimToSizeRequest<K, V> r);

    void process(TrimToVolumeRequest<K, V> r);

    Object executeView(Predicate<CacheEntry<K, V>> filter, AbstractView view, QueryStack result);

    <T> List<T> process(Predicate<CacheEntry<K, V>> filter, Comparator<CacheEntry<K, V>> comparator,
            Op<CacheEntry<K, V>, T> mapper, int limit);

    <T> T get(Predicate<CacheEntry<K, V>> selector, K key, AttributeMap attributes, Op<CacheEntry<K, V>, T> extractor);

    <T> Map<K, T> getAll(Predicate<CacheEntry<K, V>> selector, Iterable<? extends K> key,
            Op<CacheEntry<K, V>, T> extractor);

    // Iterable<K> getKeys(Predicate<CacheEntry<K, V>> predicate);

}
