/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.attribute;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * The default implementation of an {@link MutableAttributeMap}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class DefaultAttributeMap implements MutableAttributeMap, Serializable {

    /** The HashMap that is storing the attribute value pairs. */
    private final Map<Attribute, Object> map = new HashMap<Attribute, Object>();

    /** Creates a new empty DefaultAttributeMap. */
    public DefaultAttributeMap() {
    }

    /**
     * Creates a new DefaultAttributeMap copying the existing attributes from the specified map.
     * 
     * @param copyFrom
     *            the attributemap to copy existing attributes from
     */
    public DefaultAttributeMap(AttributeMap copyFrom) {
        if (copyFrom != Attributes.EMPTY_ATTRIBUTE_MAP) {
            for (Map.Entry<Attribute, Object> e : copyFrom.entrySet()) {
                put(e.getKey(), e.getValue());
            }
        }
    }

    /** {@inheritDoc} */
    public Set<Attribute> attributes() {
        return (Set) map.keySet();
    }

    /** {@inheritDoc} */
    public void clear() {
        map.clear();
    }

    /** {@inheritDoc} */
    public boolean contains(Attribute<?> attribute) {
        return map.containsKey(attribute);
    }

    /** {@inheritDoc} */
    public Set<Entry<Attribute, Object>> entrySet() {
        return map.entrySet();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AttributeMap))
            return false;
        AttributeMap m = (AttributeMap) o;
        if (m.size() != size())
            return false;
        return m.entrySet().equals(entrySet());
    }

    /** {@inheritDoc} */
    public <T> T get(Attribute<T> key) {
        if (map.containsKey(key)) {
            return (T) map.get(key);
        } else {
            return key.getDefault();
        }
    }

    /** {@inheritDoc} */
    public <T> T get(Attribute<T> key, T defaultValue) {
        if (map.containsKey(key)) {
            return (T) map.get(key);
        } else {
            return defaultValue;
        }
    }

    /** {@inheritDoc} */
    public boolean get(BooleanAttribute key) {
        if (map.containsKey(key)) {
            return (Boolean) map.get(key);
        } else {
            return key.getDefault();
        }
    }

    /** {@inheritDoc} */
    public boolean get(BooleanAttribute key, boolean defaultValue) {
        if (map.containsKey(key)) {
            return (Boolean) map.get(key);
        } else {
            return defaultValue;
        }
    }

    /** {@inheritDoc} */
    public byte get(ByteAttribute key) {
        if (map.containsKey(key)) {
            return (Byte) map.get(key);
        } else {
            return key.getDefault();
        }
    }

    /** {@inheritDoc} */
    public byte get(ByteAttribute key, byte defaultValue) {
        if (map.containsKey(key)) {
            return (Byte) map.get(key);
        } else {
            return defaultValue;
        }
    }

    /** {@inheritDoc} */
    public char get(CharAttribute key) {
        if (map.containsKey(key)) {
            return (Character) map.get(key);
        } else {
            return key.getDefault();
        }
    }

    /** {@inheritDoc} */
    public char get(CharAttribute key, char defaultValue) {
        if (map.containsKey(key)) {
            return (Character) map.get(key);
        } else {
            return defaultValue;
        }
    }

    /** {@inheritDoc} */
    public double get(DoubleAttribute key) {
        return get(key, key.getDefaultValue());
    }

    /** {@inheritDoc} */
    public double get(DoubleAttribute key, double defaultValue) {
        if (map.containsKey(key)) {
            return (Double) map.get(key);
        } else {
            return defaultValue;
        }
    }

    /** {@inheritDoc} */
    public float get(FloatAttribute key) {
        if (map.containsKey(key)) {
            return (Float) map.get(key);
        } else {
            return (Float) ((Attribute) key).getDefault();
        }
    }

    /** {@inheritDoc} */
    public float get(FloatAttribute key, float defaultValue) {
        if (map.containsKey(key)) {
            return (Float) map.get(key);
        } else {
            return defaultValue;
        }
    }

    /** {@inheritDoc} */
    public int get(IntAttribute key) {
        return get(key, key.getDefaultValue());
    }

    /** {@inheritDoc} */
    public int get(IntAttribute key, int defaultValue) {
        if (map.containsKey(key)) {
            return (Integer) map.get(key);
        } else {
            return defaultValue;
        }
    }

    /** {@inheritDoc} */
    public long get(LongAttribute key) {
        return get(key, key.getDefaultValue());
    }

    /** {@inheritDoc} */
    public long get(LongAttribute key, long defaultValue) {
        if (map.containsKey(key)) {
            return (Long) map.get(key);
        } else {
            return defaultValue;
        }
    }

    /** {@inheritDoc} */
    public short get(ShortAttribute key) {
        if (map.containsKey(key)) {
            return (Short) map.get(key);
        } else {
            return (Short) ((Attribute) key).getDefault();
        }
    }

    /** {@inheritDoc} */
    public short get(ShortAttribute key, short defaultValue) {
        if (map.containsKey(key)) {
            return (Short) map.get(key);
        } else {
            return defaultValue;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return map.hashCode();
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /** {@inheritDoc} */
    public <T> T put(Attribute<T> key, T value) {
        if (map.containsKey(key)) {
            return (T) map.put(key, value);
        } else {
            map.put(key, value);
            return key.getDefault();
        }
    }

    /** {@inheritDoc} */
    public boolean put(BooleanAttribute key, boolean value) {
        Object prev = map.put(key, value);
        return prev == null ? key.getDefault() : (Boolean) prev;
    }

    /** {@inheritDoc} */
    public byte put(ByteAttribute key, byte value) {
        Object prev = map.put(key, value);
        return prev == null ? key.getDefault() : (Byte) prev;

    }

    /** {@inheritDoc} */
    public char put(CharAttribute key, char value) {
        Object prev = map.put(key, value);
        return prev == null ? key.getDefault() : (Character) prev;
    }

    /** {@inheritDoc} */
    public double put(DoubleAttribute key, double value) {
        Object prev = map.put(key, value);
        return prev == null ? key.getDefaultValue() : (Double) prev;

    }

    /** {@inheritDoc} */
    public float put(FloatAttribute key, float value) {
        Object prev = map.put(key, value);
        return prev == null ? key.getDefault() : (Float) prev;
    }

    /** {@inheritDoc} */
    public int put(IntAttribute key, int value) {
        Object prev = map.put(key, value);
        return prev == null ? key.getDefaultValue() : (Integer) prev;
    }

    /** {@inheritDoc} */
    public long put(LongAttribute key, long value) {
        Object prev = map.put(key, value);
        return prev == null ? key.getDefaultValue() : (Long) prev;
    }

    /** {@inheritDoc} */
    public short put(ShortAttribute key, short value) {
        Object prev = map.put(key, value);
        return prev == null ? key.getDefault() : (Short) prev;

    }

    /** {@inheritDoc} */
    public <T> T remove(Attribute<T> key) {
        if (map.containsKey(key)) {
            return (T) map.remove(key);
        } else {
            return key.getDefault();
        }
    }

    /** {@inheritDoc} */
    public boolean remove(BooleanAttribute key) {
        Object prev = map.remove(key);
        return prev == null ? key.getDefaultValue() : (Boolean) prev;
    }

    /** {@inheritDoc} */
    public byte remove(ByteAttribute key) {
        Object prev = map.remove(key);
        return prev == null ? key.getDefaultValue() : (Byte) prev;

    }

    /** {@inheritDoc} */
    public char remove(CharAttribute key) {
        Object prev = map.remove(key);
        return prev == null ? key.getDefaultValue() : (Character) prev;
    }

    /** {@inheritDoc} */
    public double remove(DoubleAttribute key) {
        Object prev = map.remove(key);
        return prev == null ? key.getDefaultValue() : (Double) prev;
    }

    /** {@inheritDoc} */
    public float remove(FloatAttribute key) {
        Object prev = map.remove(key);
        return prev == null ? key.getDefaultValue() : (Float) prev;
    }

    /** {@inheritDoc} */
    public int remove(IntAttribute key) {
        Object prev = map.remove(key);
        return prev == null ? key.getDefaultValue() : (Integer) prev;
    }

    /** {@inheritDoc} */
    public long remove(LongAttribute key) {
        Object prev = map.remove(key);
        return prev == null ? key.getDefaultValue() : (Long) prev;
    }

    /** {@inheritDoc} */
    public short remove(ShortAttribute key) {
        Object prev = map.remove(key);
        return prev == null ? key.getDefaultValue() : (Short) prev;
    }

    /** {@inheritDoc} */
    public int size() {
        return map.size();
    }

    /** {@inheritDoc} */
    public String toString() {
        return map.toString();
    }

    /** {@inheritDoc} */
    public Collection<Object> values() {
        return map.values();
    }

    /** {@inheritDoc} */
    public void putAll(AttributeMap attributes) {
        for (Map.Entry<Attribute, Object> m : attributes.entrySet()) {
            put(m.getKey(), m.getValue());
        }
    }
}
