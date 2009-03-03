package org.codehaus.cake.internal.cache.processor.defaults;

import java.util.List;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.internal.cache.processor.request.AddEntryRequest;
import org.codehaus.cake.util.Pair;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.ops.Ops.Op;
import org.codehaus.cake.util.ops.Ops.Predicate;

public class DefaultCreateUpdateWithFactoryRequest<K, V> implements AddEntryRequest<K, V> {
    private AttributeMap attributes;
    private final K key;
    private Object value;
    private Op<K, Pair<V, AttributeMap>> factory;
    private CacheEntry<K, V> previousEntry;
    private CacheEntry<K, V> newEntry;

    private final Predicate<? extends CacheEntry<K, V>> updatePredicate;

    public DefaultCreateUpdateWithFactoryRequest(K key, Op<? extends K, Pair<V, AttributeMap>> factory,
            Predicate<? extends CacheEntry<K, V>> updatePredicate, Op<CacheEntry<K, V>, ?> previousEntryUpdate,
            Op<CacheEntry<K, V>, ?> nextEntryUpdate) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        this.key = key;
        this.factory = (Op) factory;
        this.updatePredicate = updatePredicate;
    }

    public boolean doTrim() {
        return false;
    }

    public AttributeMap getAttributes() {
        lazyCreate();
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
        lazyCreate();
        return (V) value;
    }

    private void lazyCreate() {
        if (factory != null) {
            Pair<V, AttributeMap> entry = factory.op(key);
            if (entry == null) {
                throw new NullPointerException("A null pair was returned");
            }
            value = entry.getFirst();
            if (value == null) {
                throw new NullPointerException("The specified factory created a null value");
            }
            attributes = entry.getSecond();
            if (attributes == null) {
                throw new NullPointerException("The specified factory created a null attributemap");
            }
            factory = null;
        }
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
