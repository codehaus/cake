package org.codehaus.cake.internal.cache.processor;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.defaults.DefaultAddAllRequest;
import org.codehaus.cake.internal.cache.processor.defaults.DefaultCreateUpdateRequest;
import org.codehaus.cake.internal.cache.processor.defaults.DefaultRemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.defaults.DefaultRemoveRequest;
import org.codehaus.cake.internal.cache.processor.defaults.DefaultTrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.defaults.DefaultTrimToVolumeRequest;
import org.codehaus.cake.internal.cache.processor.defaults.InternalCacheClearEvent;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;
import org.codehaus.cake.internal.util.CollectionUtils;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public class DefaultCacheRequestFactory<K, V> implements CacheRequestFactory<K, V> {

    public TrimToSizeRequest<K, V> createTrimToSizeRequest(int trimToSize,
            Comparator<? extends CacheEntry<K, V>> comparator) {
        return new DefaultTrimToSizeRequest<K, V>(trimToSize, comparator);
    }

    public TrimToVolumeRequest<K, V> createTrimToVolumeRequest(long volume,
            Comparator<? extends CacheEntry<K, V>> comparator) {
        return new DefaultTrimToVolumeRequest<K, V>(volume, comparator);
    }

    public ClearCacheRequest<K, V> createClear() {
        return new InternalCacheClearEvent();
    }

    public AddEntryRequest<K, V> createUpdate(K key, AttributeMap attributes, Object value,
            Predicate<? extends CacheEntry<K, V>> updatePredicate, Op<CacheEntry<K, V>, ?> previousEntryUpdate,
            Op<CacheEntry<K, V>, ?> nextEntryUpdate) {
        return new DefaultCreateUpdateRequest<K, V>(key, attributes, value, updatePredicate, previousEntryUpdate,
                nextEntryUpdate);
    }

    public RemoveEntryRequest<K, V> remove(K key, Predicate<? extends CacheEntry<K, V>> removePredicate,
            Op<CacheEntry<K, V>, ?> previousEntryUpdate) {
        return new DefaultRemoveRequest<K, V>(key, removePredicate);
    }

    public RemoveEntriesRequest<K, V> removeAll(Collection<? extends K> keys) {
        if (keys == null) {
            throw new NullPointerException("collection is null");
        }
        CollectionUtils.checkCollectionForNulls(keys);
        return new DefaultRemoveEntriesRequest<K, V>(keys);
    }

    public AddEntriesRequest<K, V> createEntries(Map<? extends K, ? extends V> t) {
        return new DefaultAddAllRequest<K, V>(t);
    }

    public AddEntryRequest<K, V> loaded(K key, V value, AttributeMap attributes) {
        return new DefaultCreateUpdateRequest<K, V>(key, attributes, value, null, null, null);
    }

}
