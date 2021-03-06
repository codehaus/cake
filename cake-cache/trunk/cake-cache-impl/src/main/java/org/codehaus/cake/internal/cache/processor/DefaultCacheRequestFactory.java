package org.codehaus.cake.internal.cache.processor;

import java.util.Comparator;
import java.util.Map;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.defaults.DefaultAddAllRequest;
import org.codehaus.cake.internal.cache.processor.defaults.DefaultCreateUpdateRequest;
import org.codehaus.cake.internal.cache.processor.defaults.DefaultCreateUpdateWithFactoryRequest;
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
import org.codehaus.cake.util.Pair;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;

public class DefaultCacheRequestFactory<K, V> implements CacheRequestFactory<K, V> {

    private final Predicate<CacheEntry<K,V>> predicate;

    public DefaultCacheRequestFactory() {
        this(null);
    }
    public DefaultCacheRequestFactory(Predicate<CacheEntry<K, V>> predicate) {
        this.predicate = predicate;
    }

    public ClearCacheRequest<K, V> createClear() {
        return new InternalCacheClearEvent();
    }

    public AddEntriesRequest<K, V> createEntries(Map<? extends K, ? extends V> t, AttributeMap attributes) {
        return new DefaultAddAllRequest<K, V>(t, attributes);
    }

    public TrimToSizeRequest<K, V> createTrimToSizeRequest(long trimToSize,
            Comparator<? extends CacheEntry<K, V>> comparator) {
        return new DefaultTrimToSizeRequest<K, V>(trimToSize, comparator);
    }

    public TrimToVolumeRequest<K, V> createTrimToVolumeRequest(long volume,
            Comparator<? extends CacheEntry<K, V>> comparator) {
        return new DefaultTrimToVolumeRequest<K, V>(volume, comparator);
    }

    public AddEntryRequest<K, V> createUpdate(K key, AttributeMap attributes, Object value,
            Predicate<? extends CacheEntry<K, V>> updatePredicate, Op<CacheEntry<K, V>, ?> previousEntryUpdate,
            Op<CacheEntry<K, V>, ?> nextEntryUpdate) {
        return new DefaultCreateUpdateRequest<K, V>(key, attributes, value, updatePredicate, previousEntryUpdate,
                nextEntryUpdate);
    }

    public AddEntryRequest<K, V> createUpdate(K key, Predicate<? extends CacheEntry<K, V>> updatePredicate,
            Op<? extends K, Pair<V, AttributeMap>> factory, Op<CacheEntry<K, V>, ?> previousEntryUpdate,
            Op<CacheEntry<K, V>, ?> nextEntryUpdate) {
        return new DefaultCreateUpdateWithFactoryRequest<K, V>(key,factory, updatePredicate, previousEntryUpdate,
                nextEntryUpdate);
    }

    public AddEntryRequest<K, V> loaded(K key, V value, AttributeMap attributes) {
        return new DefaultCreateUpdateRequest<K, V>(key, attributes, value, null, null, null);
    }

    public RemoveEntryRequest<K, V> remove(K key, Predicate<? extends CacheEntry<K, V>> removePredicate,
            Op<CacheEntry<K, V>, ?> previousEntryUpdate) {
        return new DefaultRemoveRequest<K, V>(key, removePredicate);
    }

    public RemoveEntriesRequest<K, V> removeAll(Iterable<? extends K> keys) {
        if (keys == null) {
            throw new NullPointerException("collection is null");
        }
        CollectionUtils.checkCollectionForNulls(keys);
        return new DefaultRemoveEntriesRequest<K, V>(keys);
    }

}
