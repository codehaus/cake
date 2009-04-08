package org.codehaus.cake.internal.codegen.attribute;

import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;

import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.BooleanAttribute;
import org.codehaus.cake.util.attribute.ByteAttribute;
import org.codehaus.cake.util.attribute.CharAttribute;
import org.codehaus.cake.util.attribute.DoubleAttribute;
import org.codehaus.cake.util.attribute.FloatAttribute;
import org.codehaus.cake.util.attribute.IntAttribute;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.codehaus.cake.util.attribute.ShortAttribute;

public class SingleElement implements AttributeMap {

   private final Object o;

    public SingleElement() {
        this.o = 123;
    }

    public SingleElement(Object o) {
        if (o == null) {
            throw new NullPointerException("o is null");
        }
        this.o = o;
    }

    public Set<Attribute> attributes() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Attribute<?> attribute) {
        throw new UnsupportedOperationException();
    }

    public Set<Entry<Attribute, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public <T> T get(Attribute<T> attribute) {
        throw new UnsupportedOperationException();
    }

    public <T> T get(Attribute<T> attribute, T defaultValue) {
        throw new UnsupportedOperationException();
    }

    public boolean get(BooleanAttribute attribute) {
        throw new UnsupportedOperationException();
    }

    public boolean get(BooleanAttribute attribute, boolean defaultValue) {
        throw new UnsupportedOperationException();
    }

    public byte get(ByteAttribute attribute) {
        throw new UnsupportedOperationException();
    }

    public byte get(ByteAttribute attribute, byte defaultValue) {
        throw new UnsupportedOperationException();
    }

    public char get(CharAttribute attribute) {
        throw new UnsupportedOperationException();
    }

    public char get(CharAttribute attribute, char defaultValue) {
        throw new UnsupportedOperationException();
    }

    public double get(DoubleAttribute attribute) {
        throw new UnsupportedOperationException();
    }

    public double get(DoubleAttribute attribute, double defaultValue) {
        throw new UnsupportedOperationException();
    }

    public float get(FloatAttribute attribute) {
        throw new UnsupportedOperationException();
    }

    public float get(FloatAttribute attribute, float defaultValue) {
        throw new UnsupportedOperationException();
    }

    public int get(IntAttribute attribute) {
        throw new UnsupportedOperationException();
    }

    public int get(IntAttribute attribute, int defaultValue) {
        throw new UnsupportedOperationException();
    }

    public long get(LongAttribute attribute) {
        throw new UnsupportedOperationException();
    }

    public long get(LongAttribute attribute, long defaultValue) {
        throw new UnsupportedOperationException();
    }

    public short get(ShortAttribute attribute) {
        throw new UnsupportedOperationException();
    }

    public short get(ShortAttribute attribute, short defaultValue) {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public int size() {
        throw new UnsupportedOperationException();
    }

    public Collection<Object> values() {
        throw new UnsupportedOperationException();
    }

}
