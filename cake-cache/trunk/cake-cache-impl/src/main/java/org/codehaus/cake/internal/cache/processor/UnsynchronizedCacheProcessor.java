package org.codehaus.cake.internal.cache.processor;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.request.AddEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.ClearCacheRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntriesRequest;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;
import org.codehaus.cake.internal.cache.service.loading.InternalCacheLoadingService;
import org.codehaus.cake.internal.cache.service.memorystore.MemoryStore;
import org.codehaus.cake.internal.service.RunState;
import org.codehaus.cake.ops.Ops.Op;

public class UnsynchronizedCacheProcessor<K, V> implements CacheProcessor<K, V> {
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

    public <T> T get(K key, Op<CacheEntry<K, V>, T> extractor, boolean isPeek) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        CacheEntry<K, V> entry;
        if (isPeek) {
            runState.isRunningLazyStart(false);
            entry = memoryStore.peek(key);
        } else {
            runState.isRunningLazyStart(true);
            entry = memoryStore.get(key);
            if (entry == null && loading != null) {
                entry = loading.load(key, new DefaultAttributeMap());
            }
        }
        if (entry != null) {
            return extractor.op(entry);
        }
        return null;
    }

    public <T> Map<K, T> getAll(Iterable<? extends K> keys, Op<CacheEntry<K, V>, T> extractor, boolean isPeek) {
        if (keys == null) {
            throw new NullPointerException("keys is null");
        }
        runState.isRunningLazyStart(true);
        HashMap<K, T> result = new HashMap<K, T>();
        for (K key : keys) {
            result.put(key, get(key, extractor, isPeek));
        }
        return result;
    }

    public void process(AddEntriesRequest<K, V> r) {
        runState.isRunningLazyStart(true);
        memoryStore.process(r);

    }

    public void process(ClearCacheRequest<K, V> r) {
        runState.isRunningLazyStart(false);
        memoryStore.process(r);
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
}
