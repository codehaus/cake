package org.codehaus.cake.internal.cache.processor;

import java.util.Map;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;
import org.codehaus.cake.ops.Ops.Op;

public interface CacheProcessor<K, V> {

    void process(AddEntriesRequest<K, V> r);

    void process(AddEntryRequest<K, V> r);

    void process(ClearCacheRequest<K, V> r);

    void process(RemoveEntriesRequest<K, V> r);

    void process(RemoveEntryRequest<K, V> r);

    void process(TrimToSizeRequest<K, V> r);

    void process(TrimToVolumeRequest<K, V> r);

    <T> T get(K key, Op<CacheEntry<K, V>, T> extractor, boolean isPeek);

    <T> Map<K, T> getAll(Iterable<? extends K> key, Op<CacheEntry<K, V>, T> extractor, boolean isPeek);
}
