package org.codehaus.cake.internal.cache.processor.request;

import org.codehaus.cake.attribute.GetAttributer;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public interface AddEntryRequest<K, V> extends Trimable<K, V> {
    GetAttributer getAttributes();

    K getKey();

    Op<CacheEntry<K, V>, ?> getNewEntryExtractor();

    CacheEntry<K, V> getPreviousAsEntry();

    V getPreviousAsValue();

    Op<CacheEntry<K, V>, ?> getPreviousEntryExtractor();

    Predicate<CacheEntry<K, V>> getUpdatePredicate();

    V getValue();

    CacheEntry<K, V> getNewEntry();

    void setNewEntry(CacheEntry<K, V> entry);

    void setPreviousEntry(CacheEntry<K, V> entry);
}
