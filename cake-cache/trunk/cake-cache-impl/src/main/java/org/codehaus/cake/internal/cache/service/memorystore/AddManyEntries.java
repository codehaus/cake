package org.codehaus.cake.internal.cache.service.memorystore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.CacheEntry;

public class AddManyEntries<K, V> {
    private final Type type;
    private final List<AddSingleEntry<K, V>> entries;
    private Iterable<CacheEntry<K, V>> trimmed = Collections.EMPTY_LIST;

    private AddManyEntries(Type type, List<AddSingleEntry<K, V>> entries) {
        this.type = type;
        this.entries = entries;
    }

    public Iterable<CacheEntry<K, V>> getTrimmed() {
        return trimmed;
    }

    public void setTrimmed(Iterable<CacheEntry<K, V>> trimmed) {
        this.trimmed = trimmed;
    }

    public Type getType() {
        return type;
    }

    public List<AddSingleEntry<K, V>> getEntries() {
        return entries;
    }

    public static <K, V> AddManyEntries<K, V> putAll(Map<? extends K, ? extends V> t) {
        return putAll(t, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public static <K, V> AddManyEntries<K, V> putAll(Map<? extends K, ? extends V> t, AttributeMap attributes) {
        List<AddSingleEntry<K, V>> entries = new ArrayList<AddSingleEntry<K, V>>();
        for (Map.Entry<? extends K, ? extends V> entry : t.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            AddSingleEntry<K, V> e = AddSingleEntry.put(key, value, attributes);
            entries.add(e);
        }
        return new AddManyEntries<K, V>(Type.PUT, entries);
    }

    public static enum Type {
        PUT
    }
}
