package org.codehaus.cake.internal.cache.service.memorystore;

import java.util.Collections;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.CacheEntry;

public final class AddSingleEntry<K, V> {
    private final AttributeMap attributes;
    private final K key;
    private final Type type;
    private CacheEntry<K, V> newEntry;

    private CacheEntry<K, V> previousEntry;

    private Object replace;

    public Object getReplace() {
        return replace;
    }

    private Iterable<CacheEntry<K, V>> trimmed = Collections.EMPTY_LIST;
    private final V value;

    private AddSingleEntry(K key, V value, Type type) {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (value == null) {
            throw new NullPointerException("value is null");
        }
        this.key = key;
        this.value = value;
        this.attributes = Attributes.EMPTY_ATTRIBUTE_MAP;
        this.type = type;
    }

    private AddSingleEntry(K key, V value, AttributeMap attributes, Type type) {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (value == null) {
            throw new NullPointerException("value is null");
        } else if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        this.key = key;
        this.value = value;
        this.attributes = attributes;
        this.type = type;
    }

    public AttributeMap getAttributes() {
        return attributes;
    }

    public K getKey() {
        return key;
    }

    public CacheEntry<K, V> getNewEntry() {
        return newEntry;
    }

    public CacheEntry<K, V> getPreviousEntry() {
        return previousEntry;
    }

    public Iterable<CacheEntry<K, V>> getTrimmed() {
        return trimmed;
    }

    public V getValue() {
        return value;
    }

    public void setNewEntry(CacheEntry<K, V> newEntry) {
        this.newEntry = newEntry;
    }

    public void setPreviousEntry(CacheEntry<K, V> previousEntry) {
        this.previousEntry = previousEntry;
    }

    public void setTrimmed(Iterable<CacheEntry<K, V>> trimmed) {
        this.trimmed = trimmed;
    }

    public static <K, V> AddSingleEntry<K, V> put(K key, V value) {
        return new AddSingleEntry<K, V>(key, value, Type.PUT);
    }

    public static <K, V> AddSingleEntry<K, V> put(K key, V value, AttributeMap attributes) {
        return new AddSingleEntry<K, V>(key, value, attributes, Type.PUT);
    }

    public static <K, V> AddSingleEntry<K, V> replace(K key, V value) {
        return new AddSingleEntry<K, V>(key, value, Type.REPLACE);
    }

    public static <K, V> AddSingleEntry<K, V> replace(K key, V prevValue, V newValue) {
        AddSingleEntry<K, V> e = new AddSingleEntry<K, V>(key, newValue, Type.REPLACE_IF_VALUE);
        if (prevValue == null) {
            throw new NullPointerException("prevValue is null");
        }
        e.replace = prevValue;
        return e;
    }

    public static <K, V> AddSingleEntry<K, V> putIfAbsent(K key, V value) {
        return new AddSingleEntry<K, V>(key, value, Type.PUT_IF_ABSENT);
    }

    public static <K, V> AddSingleEntry<K, V> loadPut(K key, V value, AttributeMap attributes) {
        return new AddSingleEntry<K, V>(key, value, attributes, Type.LOADED);
    }

    public boolean onlyIfAbsent() {
        return type == Type.PUT_IF_ABSENT;
    }

    public Type getType() {
        return type;
    }

    public static enum Type {
        PUT_IF_ABSENT, PUT, REPLACE, REPLACE_IF_VALUE, LOADED
    }
}
