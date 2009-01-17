package org.codehaus.cake.internal.cache.service.memorystore;

import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.ByteAttribute;
import org.codehaus.cake.attribute.CharAttribute;
import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.FloatAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.ShortAttribute;

public class TmpOpenAdressingEntry<K, V> extends OpenAdressingEntry<K, V> {

    private final MutableAttributeMap attributes;

    public TmpOpenAdressingEntry(K key, int hash, V value, MutableAttributeMap attributes) {
        super(key, hash, value);
        this.attributes = attributes;
    }

    public MutableAttributeMap getAttributes() {
        return attributes;
    }

    public <T> T get(Attribute<T> attribute, T defaultValue) {
        return attributes.get(attribute, defaultValue);
    }

    public <T> T get(Attribute<T> attribute) {
        return attributes.get(attribute);
    }

    public boolean get(BooleanAttribute attribute, boolean defaultValue) {
        return attributes.get(attribute, defaultValue);
    }

    public boolean get(BooleanAttribute attribute) {
        return attributes.get(attribute);
    }

    public byte get(ByteAttribute attribute, byte defaultValue) {
        return attributes.get(attribute, defaultValue);
    }

    public byte get(ByteAttribute attribute) {
        return attributes.get(attribute);
    }

    public char get(CharAttribute attribute, char defaultValue) {
        return attributes.get(attribute, defaultValue);
    }

    public char get(CharAttribute attribute) {
        return attributes.get(attribute);
    }

    public double get(DoubleAttribute attribute, double defaultValue) {
        return attributes.get(attribute, defaultValue);
    }

    public double get(DoubleAttribute attribute) {
        return attributes.get(attribute);
    }

    public float get(FloatAttribute attribute, float defaultValue) {
        return attributes.get(attribute, defaultValue);
    }

    public float get(FloatAttribute attribute) {
        return attributes.get(attribute);
    }

    public int get(IntAttribute attribute, int defaultValue) {
        return attributes.get(attribute, defaultValue);
    }

    public int get(IntAttribute attribute) {
        return attributes.get(attribute);
    }

    public long get(LongAttribute attribute, long defaultValue) {
        return attributes.get(attribute, defaultValue);
    }

    public long get(LongAttribute attribute) {
        return attributes.get(attribute);
    }

    public short get(ShortAttribute attribute, short defaultValue) {
        return attributes.get(attribute, defaultValue);
    }

    public short get(ShortAttribute attribute) {
        return attributes.get(attribute);
    }

    public boolean contains(Attribute<?> attribute) {
        return attributes.contains(attribute);
    }

    public int size() {
        return attributes.size();
    }

    public Set<Attribute> attributes() {
        return attributes.attributes();
    }

    public Set<Entry<Attribute, Object>> entrySet() {
        return attributes.entrySet();
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    public Collection<Object> values() {
        return attributes.values();
    }
}
