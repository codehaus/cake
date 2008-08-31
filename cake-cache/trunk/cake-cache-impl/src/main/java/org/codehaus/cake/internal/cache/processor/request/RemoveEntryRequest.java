package org.codehaus.cake.internal.cache.processor.request;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public interface RemoveEntryRequest<K, V> {
    K getKey();

    Predicate<CacheEntry<K, V>> getUpdatePredicate();

    Op<CacheEntry<K, V>, ?> getPreviousEntryExtractor();

    boolean wasRemoved();

    void setPreviousEntry(CacheEntry<K,V> value);

    CacheEntry<K,V> getPreviousEntry();
    
    V getPreviousAsValue();
}
