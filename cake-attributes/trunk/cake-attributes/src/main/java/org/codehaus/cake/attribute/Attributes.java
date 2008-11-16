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
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.codehaus.cake.internal.attribute.AttributeHelper;

/**
 * 
 * This class consists exclusively of static methods that operate on or return collections. It contains polymorphic
 * algorithms that operate on collections, "wrappers", which return a new collection backed by a specified collection,
 * and a few other odds and ends.
 * 
 * 
 * Contains various utility methods for a {@link AttributeMap}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: AttributeMaps.java 472 2007-11-19 09:34:26Z kasper $
 */
public final class Attributes {
    /** The empty attribute map (immutable). This attribute map is serializable. */
    public static final AttributeMap EMPTY_ATTRIBUTE_MAP = new EmptyAttributeMap();

    // /CLOVER:OFF
    /** Cannot instantiate. */
    private Attributes() {
    }

    /**
     * Creates a new AttributeMap from the 2 specified attributes and respective values.
     * 
     * @param <T1>
     *            the type of the first attribute
     * @param <T2>
     *            the type of the second attribute
     * @param attribute1
     *            the first attribute
     * @param value1
     *            the value of the first attribute
     * @param attribute2
     *            the second attribute
     * @param value2
     *            the value of the second attribute
     * @return a new AttributeMap from the 2 specified attributes and respective values
     */
    public static <T1, T2> AttributeMap from(Attribute<T1> attribute1, T1 value1, Attribute<T2> attribute2, T2 value2) {
        DefaultAttributeMap map = new DefaultAttributeMap();
        map.put(attribute1, value1);
        map.put(attribute2, value2);
        return map;
    }

    // /CLOVER:ON
    /**
     * Returns an immutable AttributeMap containing only the specified attribute mapping to the specified value.
     * Attempts to modify the returned attribute map, whether direct or via its collection views, result in an
     * <tt>UnsupportedOperationException</tt>.
     * <p>
     * The returned attribute map will be serializable if the specified attribute and the specified value is
     * serializable.
     * 
     * @param attribute
     *            the attribute to map from
     * @param value
     *            the value to map to
     * @return a singleton attribute map
     * @param <T>
     *            the containing type of the attribute
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    public static <T> AttributeMap singleton(Attribute<T> attribute, T value) {
        return new SingletonAttributeMap(attribute, value);
    }

    /**
     * Returns an unmodifiable view of the specified attribute map. This method allows modules to provide users with
     * "read-only" access to internal attribute maps. Query operations on the returned attribute map "read through" to
     * the specified attribute map, and attempts to modify the returned attribute map, whether direct or via its
     * collection views, result in an <tt>UnsupportedOperationException</tt>.
     * <p>
     * The returned attribute map will be serializable if the specified attribute map is serializable.
     * 
     * @param attributes
     *            the attribute map for which an unmodifiable view is to be returned.
     * @return an unmodifiable view of the specified attribute map.
     */
    public static AttributeMap unmodifiableAttributeMap(AttributeMap attributes) {
        return new ImmutableAttributeMap(attributes);
    }

    /**
     * Validates that all entries entries are valid in the specified attribute map according to
     * {@link Attribute#checkValid(Object)}. The method returns a new immutable map with all the entries.
     * 
     * @param attributes
     *            the map to validate
     * @return a new immutable map with validated entries
     * @throws IllegalArgumentException
     *             if the map contains an illegal value for an attribute
     */
    public static AttributeMap validatedAttributeMap(AttributeMap attributes) {
        DefaultAttributeMap result = new DefaultAttributeMap();
        for (Map.Entry<Attribute, Object> e : attributes.entrySet()) {
            Attribute<Object> a = e.getKey();
            Object o = e.getValue();
            a.checkValid(o);
            result.put(a, o);
        }
        return unmodifiableAttributeMap(result);
    }

    /** The default implementation of an immutable empty {@link AttributeMap}. */
    static final class EmptyAttributeMap implements AttributeMap, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = -3037602713439417782L;

        /** {@inheritDoc} */
        public Set<Attribute> attributeSet() {
            return Collections.EMPTY_SET;
        }

        /** {@inheritDoc} */
        public void clear() {
        }

        /** {@inheritDoc} */
        public boolean contains(Attribute<?> attribute) {
            return false;
        }

        /** {@inheritDoc} */
        public Set<Entry<Attribute, Object>> entrySet() {
            return Collections.EMPTY_SET;
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(Object o) {
            return o instanceof AttributeMap && ((AttributeMap) o).size() == 0;
        }

        /** {@inheritDoc} */
        public <T> T get(Attribute<T> key) {
            return key.getDefault();
        }

        /** {@inheritDoc} */
        public <T> T get(Attribute<T> key, T defaultValue) {
            return defaultValue;
        }

        /** {@inheritDoc} */
        public boolean get(BooleanAttribute key) {
            return key.getDefault();
        }

        /** {@inheritDoc} */
        public boolean get(BooleanAttribute key, boolean defaultValue) {
            return defaultValue;
        }

        /** {@inheritDoc} */
        public byte get(ByteAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public byte get(ByteAttribute key, byte defaultValue) {
            return defaultValue;
        }

        /** {@inheritDoc} */
        public char get(CharAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public char get(CharAttribute key, char defaultValue) {
            return defaultValue;
        }

        /** {@inheritDoc} */
        public double get(DoubleAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public double get(DoubleAttribute key, double defaultValue) {
            return defaultValue;
        }

        /** {@inheritDoc} */
        public float get(FloatAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public float get(FloatAttribute key, float defaultValue) {
            return defaultValue;
        }

        /** {@inheritDoc} */
        public int get(IntAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public int get(IntAttribute key, int defaultValue) {
            return defaultValue;
        }

        /** {@inheritDoc} */
        public long get(LongAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public long get(LongAttribute key, long defaultValue) {
            return defaultValue;
        }

        /** {@inheritDoc} */
        public short get(ShortAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public short get(ShortAttribute key, short defaultValue) {
            return defaultValue;
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return 0;
        }

        /** {@inheritDoc} */
        public boolean isEmpty() {
            return true;
        }

        /** {@inheritDoc} */
        public <T> T put(Attribute<T> key, T value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public boolean put(BooleanAttribute key, boolean value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public byte put(ByteAttribute key, byte value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public char put(CharAttribute key, char value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public double put(DoubleAttribute key, double value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public float put(FloatAttribute key, float value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public int put(IntAttribute key, int value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public long put(LongAttribute key, long value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public short put(ShortAttribute key, short value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        public void putAll(AttributeMap attributes) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /**
         * Preserves singleton property.
         * 
         * @return the empty map
         */
        private Object readResolve() {
            return EMPTY_ATTRIBUTE_MAP;
        }

        /** {@inheritDoc} */
        public <T> T remove(Attribute<T> key) {
            return key.getDefault();
        }

        /** {@inheritDoc} */
        public boolean remove(BooleanAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public byte remove(ByteAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public char remove(CharAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public double remove(DoubleAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public float remove(FloatAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public int remove(IntAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public long remove(LongAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public short remove(ShortAttribute key) {
            return key.getDefaultValue();
        }

        /** {@inheritDoc} */
        public int size() {
            return 0;
        }

        /** {@inheritDoc} */
        public String toString() {
            return "{}";
        }

        /** {@inheritDoc} */
        public Collection<Object> values() {
            return Collections.EMPTY_SET;
        }
    }

    /** An unmodifiable view of an attribute map. */
    static class ImmutableAttributeMap implements AttributeMap, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -8792952517961074713L;

        /** The map that is being wrapped. */
        private final AttributeMap map;

        /**
         * Creates a new ImmutableAttributeMap.
         * 
         * @param attributes
         *            the attribute map to wrap
         */
        ImmutableAttributeMap(AttributeMap attributes) {
            if (attributes == null) {
                throw new NullPointerException("attributes is null");
            }
            this.map = attributes;
        }

        /** {@inheritDoc} */
        public Set<Attribute> attributeSet() {
            return Collections.unmodifiableSet(map.attributeSet());
        }

        /** {@inheritDoc} */
        public void clear() {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public boolean contains(Attribute<?> attribute) {
            return map.contains(attribute);
        }

        /** {@inheritDoc} */
        public Set<Entry<Attribute, Object>> entrySet() {
            return Collections.unmodifiableSet(map.entrySet());
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(Object obj) {
            // TODO not sure this is safe
            return map.equals(obj);
        }

        /** {@inheritDoc} */
        public <T> T get(Attribute<T> key) {
            return map.get(key);
        }

        /** {@inheritDoc} */
        public <T> T get(Attribute<T> key, T defaultValue) {
            return map.get(key, defaultValue);
        }

        /** {@inheritDoc} */
        public boolean get(BooleanAttribute key) {
            return map.get(key);
        }

        /** {@inheritDoc} */
        public boolean get(BooleanAttribute key, boolean defaultValue) {
            return map.get(key, defaultValue);
        }

        /** {@inheritDoc} */
        public byte get(ByteAttribute key) {
            return map.get(key);
        }

        /** {@inheritDoc} */
        public byte get(ByteAttribute key, byte defaultValue) {
            return map.get(key, defaultValue);
        }

        /** {@inheritDoc} */
        public char get(CharAttribute key) {
            return map.get(key);
        }

        /** {@inheritDoc} */
        public char get(CharAttribute key, char defaultValue) {
            return map.get(key, defaultValue);
        }

        /** {@inheritDoc} */
        public double get(DoubleAttribute key) {
            return map.get(key);
        }

        /** {@inheritDoc} */
        public double get(DoubleAttribute key, double defaultValue) {
            return map.get(key, defaultValue);
        }

        /** {@inheritDoc} */
        public float get(FloatAttribute key) {
            return map.get(key);
        }

        /** {@inheritDoc} */
        public float get(FloatAttribute key, float defaultValue) {
            return map.get(key, defaultValue);
        }

        /** {@inheritDoc} */
        public int get(IntAttribute key) {
            return map.get(key);
        }

        /** {@inheritDoc} */
        public int get(IntAttribute key, int defaultValue) {
            return map.get(key, defaultValue);
        }

        /** {@inheritDoc} */
        public long get(LongAttribute key) {
            return map.get(key);
        }

        /** {@inheritDoc} */
        public long get(LongAttribute key, long defaultValue) {
            return map.get(key, defaultValue);
        }

        /** {@inheritDoc} */
        public short get(ShortAttribute key) {
            return map.get(key);
        }

        /** {@inheritDoc} */
        public short get(ShortAttribute key, short defaultValue) {
            return map.get(key, defaultValue);
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
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public boolean put(BooleanAttribute key, boolean value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public byte put(ByteAttribute key, byte value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public char put(CharAttribute key, char value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public double put(DoubleAttribute key, double value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public float put(FloatAttribute key, float value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public int put(IntAttribute key, int value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public long put(LongAttribute key, long value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public short put(ShortAttribute key, short value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        public void putAll(AttributeMap attributes) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public <T> T remove(Attribute<T> key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public boolean remove(BooleanAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public byte remove(ByteAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public char remove(CharAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public double remove(DoubleAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public float remove(FloatAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public int remove(IntAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public long remove(LongAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public short remove(ShortAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public int size() {
            return map.size();
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            return map.toString();
        }

        /** {@inheritDoc} */
        public Collection<Object> values() {
            return Collections.unmodifiableCollection(map.values());
        }

    }

    /** A singleton attribute map. */
    static class SingletonAttributeMap implements AttributeMap, Serializable {

        /** serialVersionUID. */
        private static final long serialVersionUID = -6979724477215052911L;

        /** The singleton key. */
        private final Attribute attribute;

        /** The singleton value. */
        private final Object value;

        /**
         * Creates a new SingletonAttributeMap.
         * 
         * @param attribute
         *            the attribute to create the singleton from
         * @param value
         *            the value of the specified attribute
         */
        SingletonAttributeMap(Attribute<?> attribute, Object value) {
            if (attribute == null) {
                throw new NullPointerException("attribute is null");
            }
            this.attribute = attribute;
            this.value = value;
        }

        /** {@inheritDoc} */
        public Set<Attribute> attributeSet() {
            return (Set) Collections.singleton(attribute);
        }

        /** {@inheritDoc} */
        public void clear() {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public boolean contains(Attribute<?> attribute) {
            return this.attribute == attribute;
        }

        /** {@inheritDoc} */
        public Set<Entry<Attribute, Object>> entrySet() {
            return Collections
                    .<Map.Entry<Attribute, Object>> singleton(new AttributeHelper.SimpleImmutableEntry<Attribute, Object>(
                            attribute, value));
        }

        /** {@inheritDoc} */
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof AttributeMap)) {
                return false;
            }
            AttributeMap map = (AttributeMap) o;
            if (map.size() != size()) {
                return false;
            }
            Object other = map.get(attribute);
            return AttributeHelper.eq(value, other)
                    && (!AttributeHelper.eq(value, attribute.getDefault()) || map.contains(attribute));
        }

        /** {@inheritDoc} */
        public <T> T get(Attribute<T> key) {
            return get(key, key.getDefault());
        }

        /** {@inheritDoc} */
        public <T> T get(Attribute<T> key, T defaultValue) {
            return attribute == key ? (T) value : defaultValue;
        }

        /** {@inheritDoc} */
        public boolean get(BooleanAttribute key) {
            return get(key, key.getDefaultValue());
        }

        /** {@inheritDoc} */
        public boolean get(BooleanAttribute key, boolean defaultValue) {
            return attribute == key ? (Boolean) value : defaultValue;
        }

        /** {@inheritDoc} */
        public byte get(ByteAttribute key) {
            return get(key, key.getDefaultValue());
        }

        /** {@inheritDoc} */
        public byte get(ByteAttribute key, byte defaultValue) {
            return attribute == key ? (Byte) value : defaultValue;
        }

        /** {@inheritDoc} */
        public char get(CharAttribute key) {
            return get(key, key.getDefaultValue());
        }

        /** {@inheritDoc} */
        public char get(CharAttribute key, char defaultValue) {
            return attribute == key ? (Character) value : defaultValue;
        }

        /** {@inheritDoc} */
        public double get(DoubleAttribute key) {
            return get(key, key.getDefaultValue());
        }

        /** {@inheritDoc} */
        public double get(DoubleAttribute key, double defaultValue) {
            return attribute == key ? (Double) value : defaultValue;
        }

        /** {@inheritDoc} */
        public float get(FloatAttribute key) {
            return get(key, key.getDefaultValue());
        }

        /** {@inheritDoc} */
        public float get(FloatAttribute key, float defaultValue) {
            return attribute == key ? (Float) value : defaultValue;
        }

        /** {@inheritDoc} */
        public int get(IntAttribute key) {
            return get(key, key.getDefaultValue());
        }

        /** {@inheritDoc} */
        public int get(IntAttribute key, int defaultValue) {
            return attribute == key ? (Integer) value : defaultValue;
        }

        /** {@inheritDoc} */
        public long get(LongAttribute key) {
            return get(key, key.getDefaultValue());
        }

        /** {@inheritDoc} */
        public long get(LongAttribute key, long defaultValue) {
            return attribute == key ? (Long) value : defaultValue;
        }

        /** {@inheritDoc} */
        public short get(ShortAttribute key) {
            return get(key, key.getDefaultValue());
        }

        /** {@inheritDoc} */
        public short get(ShortAttribute key, short defaultValue) {
            return attribute == key ? (Short) value : defaultValue;
        }

        /** {@inheritDoc} */
        @Override
        public int hashCode() {
            return attribute.hashCode() ^ (value == null ? 0 : value.hashCode());
        }

        /** {@inheritDoc} */
        public boolean isEmpty() {
            return false;
        }

        /** {@inheritDoc} */
        public <T> T put(Attribute<T> key, T value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public boolean put(BooleanAttribute key, boolean value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public byte put(ByteAttribute key, byte value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public char put(CharAttribute key, char value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public double put(DoubleAttribute key, double value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public float put(FloatAttribute key, float value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public int put(IntAttribute key, int value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public long put(LongAttribute key, long value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public short put(ShortAttribute key, short value) {
            throw new UnsupportedOperationException("map is immutable");
        }

        public void putAll(AttributeMap attributes) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public <T> T remove(Attribute<T> key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public boolean remove(BooleanAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public byte remove(ByteAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public char remove(CharAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public double remove(DoubleAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public float remove(FloatAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public int remove(IntAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public long remove(LongAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public short remove(ShortAttribute key) {
            throw new UnsupportedOperationException("map is immutable");
        }

        /** {@inheritDoc} */
        public int size() {
            return 1;
        }

        /** {@inheritDoc} */
        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("{");
            buf.append(attribute);
            buf.append("=");
            buf.append(value);
            buf.append("}");
            return buf.toString();
        }

        /** {@inheritDoc} */
        public Collection<Object> values() {
            return Collections.singleton(value);
        }
    }
}
