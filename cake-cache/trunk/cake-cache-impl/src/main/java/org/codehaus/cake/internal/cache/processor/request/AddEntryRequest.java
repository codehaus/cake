package org.codehaus.cake.internal.cache.processor.request;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;

public interface AddEntryRequest<K, V> extends Trimable<K, V> {
    AttributeMap getAttributes();

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
