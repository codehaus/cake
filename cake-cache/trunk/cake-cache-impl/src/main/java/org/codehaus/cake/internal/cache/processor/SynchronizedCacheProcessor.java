package org.codehaus.cake.internal.cache.processor;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.Cache;
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
import org.codehaus.cake.ops.Ops.Predicate;

public class SynchronizedCacheProcessor<K, V> implements CacheProcessor<K, V> {
    private final MemoryStore<K, V> memoryStore;
    private final RunState runState;
    private final InternalCacheLoadingService<K, V> loading;
    private final Cache<?, ?> mutex;

    public SynchronizedCacheProcessor(Cache<?, ?> mutex, RunState runState, MemoryStore<K, V> memoryStore) {
        this(mutex, runState, memoryStore, null);
    }

    public SynchronizedCacheProcessor(Cache<?, ?> mutex, RunState runState, MemoryStore<K, V> memoryStore,
            InternalCacheLoadingService<K, V> loading) {
        this.mutex = mutex;
        this.runState = runState;
        this.memoryStore = memoryStore;
        this.loading = loading;
    }

    public void process(AddEntryRequest<K, V> r) {
        synchronized (mutex) {
            runState.isRunningLazyStart(true);
            memoryStore.process(r);
        }
    }

    public <T> T get(Predicate<CacheEntry<K, V>> selector, K key, Op<CacheEntry<K, V>, T> extractor) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        CacheEntry<K, V> entry;
        boolean dontLoad = false;
        synchronized (mutex) {
            runState.isRunningLazyStart(true);
            entry = memoryStore.get(key);
            if (entry != null || loading == null) {
                if (entry != null && selector != null && !selector.op(entry)) {
                    entry = null;
                    dontLoad = true;
                }
                if (entry != null) {
                    memoryStore.touch(entry);
                }
            }
        }
        if (dontLoad || entry != null || loading == null) {
            return extractor.op(entry);
        }

        entry = loading.load(key, new DefaultAttributeMap());

        if (entry == null) {
            return extractor.op(entry);
        }
        synchronized (mutex) {
            if (selector == null || selector.op(entry)) {
                memoryStore.touch(entry);
            } else {
                entry = null;
            }
        }
        return extractor.op(entry);
    }

    public <T> Map<K, T> getAll(Predicate<CacheEntry<K, V>> selector, Iterable<? extends K> keys,
            Op<CacheEntry<K, V>, T> extractor) {
        if (keys == null) {
            throw new NullPointerException("keys is null");
        }
        HashMap<K, T> result = new HashMap<K, T>();

        synchronized (mutex) {
            runState.isRunningLazyStart(true);
            for (K key : keys) {
                result.put(key, get(selector, key, extractor));
            }
        }
        return result;
    }

    public void process(AddEntriesRequest<K, V> r) {
        synchronized (mutex) {
            runState.isRunningLazyStart(true);
            memoryStore.process(r);
        }
    }

    public void process(Predicate<CacheEntry<K, V>> filter, ClearCacheRequest<K, V> r) {
        synchronized (mutex) {
            runState.isRunningLazyStart(false);
            memoryStore.process(filter, r);
        }
    }

    public void process(RemoveEntriesRequest<K, V> r) {
        synchronized (mutex) {
            runState.isRunningLazyStart(false);
            memoryStore.process(r);
        }
    }

    public void process(RemoveEntryRequest<K, V> r) {
        synchronized (mutex) {
            runState.isRunningLazyStart(false);
            memoryStore.process(r);
        }
    }

    public void process(TrimToSizeRequest<K, V> r) {
        synchronized (mutex) {
            runState.isRunningLazyStart(false);
            memoryStore.process(r);
        }
    }

    public void process(TrimToVolumeRequest<K, V> r) {
        synchronized (mutex) {
            runState.isRunningLazyStart(false);
            memoryStore.process(r);
        }
    }
}
