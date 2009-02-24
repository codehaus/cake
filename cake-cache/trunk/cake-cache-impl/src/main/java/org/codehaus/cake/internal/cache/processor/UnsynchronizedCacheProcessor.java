package org.codehaus.cake.internal.cache.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.CacheView;
import org.codehaus.cake.internal.cache.memorystore.MemoryStore;
import org.codehaus.cake.internal.cache.memorystore.openadressing.OpenAdressingEntry;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;
import org.codehaus.cake.internal.cache.service.loading.InternalCacheLoadingService;
import org.codehaus.cake.internal.cache.view.AbstractView;
import org.codehaus.cake.internal.cache.view.query.HashEntryArrayProcessor;
import org.codehaus.cake.internal.cache.view.util.QueryStack;
import org.codehaus.cake.internal.service.RunState;
import org.codehaus.cake.internal.util.ArrayUtils;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public class UnsynchronizedCacheProcessor<K, V>  implements CacheProcessor<K, V> {
    private final MemoryStore<K, V> memoryStore;
    private final RunState runState;
    private final InternalCacheLoadingService<K, V> loading;

    public UnsynchronizedCacheProcessor(RunState runState, MemoryStore<K, V> memoryStore) {
        this(runState, memoryStore, null);
    }

    public UnsynchronizedCacheProcessor(RunState runState, MemoryStore<K, V> memoryStore,
            InternalCacheLoadingService<K, V> loading) {
        this.runState = runState;
        this.memoryStore = memoryStore;
        this.loading = loading;
    }

    public void process(AddEntryRequest<K, V> r) {
        runState.isRunningLazyStart(true);
        memoryStore.process(r);
    }

    public <T> T get(Predicate<CacheEntry<K, V>> selector, K key, AttributeMap attributes,
            Op<CacheEntry<K, V>, T> extractor) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        runState.isRunningLazyStart(true);
        CacheEntry<K, V> entry = memoryStore.get(null, key);
        if (entry != null && selector != null && !selector.op(entry)) {
            return extractor.op(null);
        }
        if (entry == null && loading != null) {
            entry = loading.load(key, new DefaultAttributeMap(attributes));
        }
        if (entry == null) {
            return extractor.op(entry);
        }
        if (selector == null || selector.op(entry)) {
            memoryStore.touch(entry);
            return extractor.op(entry);
        }
        return extractor.op(null);

    }

    public <T> CacheView<K, T> getAll(Predicate<CacheEntry<K, V>> selector, Iterable<? extends K> keys,
            Op<CacheEntry<K, V>, T> extractor) {
        if (keys == null) {
            throw new NullPointerException("keys is null");
        }
        runState.isRunningLazyStart(true);
        HashMap<K, T> result = new HashMap<K, T>();
        for (K key : keys) {
            result.put(key, get(selector, key, Attributes.EMPTY_ATTRIBUTE_MAP, extractor));
        }
        return null; // result;
    }

    public void process(AddEntriesRequest<K, V> r) {
        runState.isRunningLazyStart(true);
        memoryStore.process(r);
    }

    public void process(Predicate<CacheEntry<K, V>> filter, ClearCacheRequest<K, V> r) {
        runState.isRunningLazyStart(false);
        memoryStore.clear(filter);
    }

    public void process(RemoveEntriesRequest<K, V> r) {
        runState.isRunningLazyStart(false);
        memoryStore.process(r);
    }

    public void process(RemoveEntryRequest<K, V> r) {
        runState.isRunningLazyStart(false);
        memoryStore.process(r);
    }

    public void process(TrimToSizeRequest<K, V> r) {
        runState.isRunningLazyStart(false);
        memoryStore.process(r);
    }

    public void process(TrimToVolumeRequest<K, V> r) {
        runState.isRunningLazyStart(false);
        memoryStore.process(r);
    }

    public <T> List<T> process(Predicate<CacheEntry<K, V>> filter, Comparator<CacheEntry<K, V>> comparator,
            Op<CacheEntry<K, V>, T> mapper, int limit) {
        runState.isRunningLazyStart(false);
        CacheEntry<K, V>[] entries = memoryStore.get(filter, comparator == null ? limit : Integer.MAX_VALUE);

        if (comparator != null) {
            entries = ArrayUtils.sort(entries, comparator, limit);
        } else if (entries.length > limit) {
            CacheEntry<K, V>[] old = entries;
            entries = new CacheEntry[limit];
            System.arraycopy(old, 0, entries, 0, limit);
        }
        if (mapper == null) {
            return (List<T>) Arrays.asList(entries);
        }
        ArrayList<T> result = new ArrayList<T>(entries.length);
        for (CacheEntry<K, V> entry : entries) {
            T mapped = mapper.op(entry);
            result.add(mapped);
        }
        return result;
    }

    public Object executeView(Predicate<CacheEntry<K, V>> filter, AbstractView view, QueryStack stack) {
        runState.isRunningLazyStart(false);
        CacheEntry<K, V>[] entries = memoryStore.get(filter, Integer.MAX_VALUE);
        HashEntryArrayProcessor p = new HashEntryArrayProcessor<K, V>((OpenAdressingEntry<K, V>[]) entries,
                (Predicate) filter);
        return p.execute(stack);
    }


}
