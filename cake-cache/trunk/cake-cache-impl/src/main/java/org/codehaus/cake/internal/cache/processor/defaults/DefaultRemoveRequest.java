package org.codehaus.cake.internal.cache.processor.defaults;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.CachePredicates;
import org.codehaus.cake.internal.cache.processor.request.RemoveEntryRequest;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

public class DefaultRemoveRequest<K, V> implements RemoveEntryRequest<K, V> {

    private final K key;

    private CacheEntry<K, V> previousEntry;

    private final V value;
    private final Predicate<? extends CacheEntry<K, V>> removePredicate;

    public DefaultRemoveRequest(K key, V value) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        this.key = key;
        this.value = value;
        removePredicate = value == null ? null : new CachePredicates.CacheValueEquals(value);
    }

    public DefaultRemoveRequest(K key, Predicate<? extends CacheEntry<K, V>> removePredicate) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        this.key = key;
        this.value = null;
        this.removePredicate = removePredicate;
    }

    public K getKey() {
        return key;
    }

    public Op<CacheEntry<K, V>, ?> getPreviousEntryExtractor() {
        return null;
    }

    public Predicate<CacheEntry<K, V>> getUpdatePredicate() {
        return (Predicate) removePredicate;
    }

    public void setPreviousEntry(CacheEntry<K, V> entry) {
        previousEntry = entry;
    }

    public CacheEntry<K, V> getPreviousEntry() {
        return previousEntry;
    }

    public V getPreviousAsValue() {
        return previousEntry == null ? null : previousEntry.getValue();
    }

    public boolean wasRemoved() {
        return previousEntry != null;
    }

}
