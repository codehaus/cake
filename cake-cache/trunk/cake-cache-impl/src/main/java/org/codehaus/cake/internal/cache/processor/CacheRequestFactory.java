package org.codehaus.cake.internal.cache.processor;

import java.util.Comparator;
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
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.util.Pair;

public interface CacheRequestFactory<K, V> {
    TrimToSizeRequest<K, V> createTrimToSizeRequest(int trimToSize, Comparator<? extends CacheEntry<K, V>> comparator);

    TrimToVolumeRequest<K, V> createTrimToVolumeRequest(long volume, Comparator<? extends CacheEntry<K, V>> comparator);

    AddEntryRequest<K, V> loaded(K key, V value, AttributeMap attributes);

    AddEntryRequest<K, V> createUpdate(K key, AttributeMap attributes, Object value,
            Predicate<? extends CacheEntry<K, V>> updatePredicate, Op<CacheEntry<K, V>, ?> previousEntryUpdate,
            Op<CacheEntry<K, V>, ?> nextEntryUpdate);

    AddEntryRequest<K, V> createUpdate(K key, Predicate<? extends CacheEntry<K, V>> updatePredicate,
            Op<? extends K, Pair<V,AttributeMap>> factory, Op<CacheEntry<K, V>, ?> previousEntryUpdate,
            Op<CacheEntry<K, V>, ?> nextEntryUpdate);

    AddEntriesRequest<K, V> createEntries(Map<? extends K, ? extends V> t, AttributeMap attributes);

    RemoveEntryRequest<K, V> remove(K key, Predicate<? extends CacheEntry<K, V>> removePredicate,
            Op<CacheEntry<K, V>, ?> previousEntryUpdate);

    RemoveEntriesRequest<K, V> removeAll(Iterable<? extends K> keys);

    ClearCacheRequest<K, V> createClear();
}
