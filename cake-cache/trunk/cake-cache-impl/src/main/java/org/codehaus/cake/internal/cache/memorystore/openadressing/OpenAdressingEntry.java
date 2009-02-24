package org.codehaus.cake.internal.cache.memorystore.openadressing;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.ByteAttribute;
import org.codehaus.cake.attribute.CharAttribute;
import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.FloatAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.ShortAttribute;
import org.codehaus.cake.cache.CacheEntry;

public class OpenAdressingEntry<K, V> implements CacheEntry<K, V> {

    final static OpenAdressingEntry[] EMPTY_ARRAY = new OpenAdressingEntry[0];
    /** The hash of this entry's key. */
    final int hash;
    /** The key of the entry. */
    final K key;
    /** The value of the entry. */
    final V value;

    public OpenAdressingEntry(K key, int hash, V value) {
        this.key = key;
        this.value = value;
        this.hash = hash;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Map.Entry))
            return false;
        Map.Entry e = (Map.Entry) o;
        Object key1 = getKey();
        Object key2 = e.getKey();
        if (key1 == key2 || (key1 != null && key1.equals(key2))) {
            Object value1 = getValue();
            Object value2 = e.getValue();
            if (value1 == value2 || (value1 != null && value1.equals(value2)))
                return true;
        }
        return false;
    }

    public Set<Attribute> attributes() {
        return Collections.EMPTY_SET;
    }

    public boolean contains(Attribute<?> attribute) {
        return false;
    }

    public Set<Entry<Attribute, Object>> entrySet() {
        return Collections.EMPTY_SET;
    }

    public <T> T get(Attribute<T> attribute) {
        return attribute.getDefault();
    }

    public <T> T get(Attribute<T> attribute, T defaultValue) {
        return defaultValue;
    }

    public boolean get(BooleanAttribute attribute) {
        return attribute.getDefaultValue();
    }

    public boolean get(BooleanAttribute attribute, boolean defaultValue) {
        return defaultValue;
    }

    public byte get(ByteAttribute attribute) {
        return attribute.getDefaultValue();
    }

    public byte get(ByteAttribute attribute, byte defaultValue) {
        return defaultValue;
    }

    public char get(CharAttribute attribute) {
        return attribute.getDefaultValue();
    }

    public char get(CharAttribute attribute, char defaultValue) {
        return defaultValue;
    }

    public double get(DoubleAttribute attribute) {
        return attribute.getDefaultValue();
    }

    public double get(DoubleAttribute attribute, double defaultValue) {
        return defaultValue;
    }

    public float get(FloatAttribute attribute) {
        return attribute.getDefaultValue();
    }

    public float get(FloatAttribute attribute, float defaultValue) {
        return defaultValue;
    }

    public int get(IntAttribute attribute) {
        return attribute.getDefaultValue();
    }

    public int get(IntAttribute attribute, int defaultValue) {
        return defaultValue;
    }

    public long get(LongAttribute attribute) {
        return attribute.getDefaultValue();
    }

    public long get(LongAttribute attribute, long defaultValue) {
        return defaultValue;
    }

    public short get(ShortAttribute attribute) {
        return attribute.getDefaultValue();
    }

    public short get(ShortAttribute attribute, short defaultValue) {
        return defaultValue;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }

    public boolean isEmpty() {
        return true;
    }

    public V setValue(V newValue) {
        throw new UnsupportedOperationException("setValue not supported");
    }

    public int size() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        sb.append(value);
        sb.append(" [");

        Iterator<Map.Entry<Attribute, Object>> i = entrySet().iterator();
        if (!i.hasNext()) {
            return sb.append("]").toString();
        }
        for (;;) {
            Map.Entry<Attribute, Object> e = i.next();
            sb.append(e.getKey());
            sb.append("=");
            sb.append(e.getValue());
            if (!i.hasNext())
                return sb.append(']').toString();
            sb.append(", ");
        }
    }

    public Collection<Object> values() {
        return Collections.EMPTY_LIST;
    }
}
