package org.codehaus.cake.internal.cache.processor.defaults;

import java.util.List;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;

public class DefaultCreateUpdateRequest<K, V> implements AddEntryRequest<K, V> {
    private final K key;
    private Object value;
    private final AttributeMap attributes;
    private final Predicate<? extends CacheEntry<K, V>> updatePredicate;

    private CacheEntry<K, V> previousEntry;
    private CacheEntry<K, V> newEntry;


    public DefaultCreateUpdateRequest(K key, AttributeMap attributes, Object value,
            Predicate<? extends CacheEntry<K, V>> updatePredicate, Op<CacheEntry<K, V>, ?> previousEntryUpdate,
            Op<CacheEntry<K, V>, ?> nextEntryUpdate) {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (value == null) {
            throw new NullPointerException("value is null");
        } else if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        this.key = key;
        this.attributes = attributes;
        this.value = value;
        this.updatePredicate = updatePredicate;
    }

    public boolean doTrim() {
        return false;
    }

    public AttributeMap getAttributes() {
        return attributes;
    }

    public K getKey() {
        return key;
    }

    public Op<CacheEntry<K, V>, ?> getNewEntryExtractor() {
        return null;
    }

    public CacheEntry<K, V> getPreviousAsEntry() {
        return previousEntry;
    }

    public V getPreviousAsValue() {
        if (previousEntry == null) {
            return null;
        }
        return previousEntry.getValue();
    }

    public Op<CacheEntry<K, V>, ?> getPreviousEntryExtractor() {
        return null;
    }

    public Predicate<CacheEntry<K, V>> getUpdatePredicate() {
        return (Predicate) updatePredicate;
    }

    public V getValue() {
        return (V) value;
    }

    public void setPreviousEntry(CacheEntry<K, V> entry) {
        this.previousEntry = entry;
    }

    public void setNewEntry(CacheEntry<K, V> entry) {
        this.newEntry = entry;
    }
    private List<CacheEntry<K, V>> trimmed;

    public List<CacheEntry<K, V>> getTrimmed() {
        return trimmed;
    }

    public void setTrimmed(List<CacheEntry<K, V>> trimmed) {
        this.trimmed = trimmed;
    }
    public CacheEntry<K, V> getNewEntry() {
        return newEntry;
    }

}
