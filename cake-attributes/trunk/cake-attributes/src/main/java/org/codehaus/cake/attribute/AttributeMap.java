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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A map specifically for the storage of Attribute->Object values.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public interface AttributeMap extends GetAttributer {

    /**
     * Removes all of the mappings from this map (optional operation). The map will be empty after
     * this call returns.
     * 
     * @throws UnsupportedOperationException
     *             if the <tt>clear</tt> operation is not supported by this map
     */
    void clear();

    /**
     * Associates the specified boolean value with the specified key in this map (optional
     * operation). If the map previously contained a mapping for this key, the old value is replaced
     * by the specified value.
     * 
     * @param attribute
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @return the previous value associated with <tt>attribute</tt>, or
     *         <tt>{@link BooleanAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this map. For example, for a
     *             read-only attribute-map
     * @throws IllegalArgumentException
     *             if some aspect of this key or value prevents it from being stored in this map.
     * @throws NullPointerException
     *             if the key is <tt>null</tt>
     */
    boolean put(BooleanAttribute attribute, boolean value);

    /**
     * Removes the mapping for an attribute from this map if it is present (optional operation).
     * <p>
     * The map will not contain a mapping for the specified attribute once the call returns.
     * 
     * @param attribute
     *            the attribute to be removed
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link BooleanAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this map
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    boolean remove(BooleanAttribute attribute);

    /**
     * Associates the specified byte value with the specified key in this map (optional operation).
     * If the map previously contained a mapping for this key, the old value is replaced by the
     * specified value.
     * 
     * @param attribute
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link ByteAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this map. For example, for a
     *             read-only attribute-map
     * @throws IllegalArgumentException
     *             if some aspect of this key or value prevents it from being stored in this map.
     * @throws NullPointerException
     *             if the key is <tt>null</tt>
     */
    byte put(ByteAttribute attribute, byte value);

    /**
     * Removes the mapping for an attribute from this map if it is present (optional operation).
     * <p>
     * The map will not contain a mapping for the specified attribute once the call returns.
     * 
     * @param attribute
     *            the attribute to be removed
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link ByteAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this map
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    byte remove(ByteAttribute attribute);

    /**
     * Associates the specified char value with the specified key in this map (optional operation).
     * If the map previously contained a mapping for this key, the old value is replaced by the
     * specified value.
     * 
     * @param attribute
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link CharAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this map. For example, for a
     *             read-only attribute-map
     * @throws IllegalArgumentException
     *             if some aspect of this key or value prevents it from being stored in this map.
     * @throws NullPointerException
     *             if the key is <tt>null</tt>
     */
    char put(CharAttribute attribute, char value);

    /**
     * Removes the mapping for an attribute from this map if it is present (optional operation).
     * <p>
     * The map will not contain a mapping for the specified attribute once the call returns.
     * 
     * @param attribute
     *            the attribute to be removed
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link CharAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this map
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    char remove(CharAttribute attribute);

    /**
     * Associates the specified double value with the specified key in this map (optional
     * operation). If the map previously contained a mapping for this key, the old value is replaced
     * by the specified value.
     * 
     * @param attribute
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link DoubleAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this map. For example, for a
     *             read-only attribute-map
     * @throws IllegalArgumentException
     *             if some aspect of this key or value prevents it from being stored in this map.
     * @throws NullPointerException
     *             if the key is <tt>null</tt>
     */
    double put(DoubleAttribute attribute, double value);

    /**
     * Removes the mapping for an attribute from this map if it is present (optional operation).
     * <p>
     * The map will not contain a mapping for the specified attribute once the call returns.
     * 
     * @param attribute
     *            the attribute to be removed
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link DoubleAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this map
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    double remove(DoubleAttribute attribute);

    /**
     * Associates the specified float value with the specified key in this map (optional operation).
     * If the map previously contained a mapping for this key, the old value is replaced by the
     * specified value.
     * 
     * @param attribute
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link FloatAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this map. For example, for a
     *             read-only attribute-map
     * @throws IllegalArgumentException
     *             if some aspect of this key or value prevents it from being stored in this map.
     * @throws NullPointerException
     *             if the key is <tt>null</tt>
     */
    float put(FloatAttribute attribute, float value);

    /**
     * Removes the mapping for an attribute from this map if it is present (optional operation).
     * <p>
     * The map will not contain a mapping for the specified attribute once the call returns.
     * 
     * @param attribute
     *            the attribute to be removed
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link FloatAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this map
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    float remove(FloatAttribute attribute);

    /**
     * Associates the specified int value with the specified key in this map (optional operation).
     * If the map previously contained a mapping for this key, the old value is replaced by the
     * specified value.
     * 
     * @param attribute
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link IntAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this map. For example, for a
     *             read-only attribute-map
     * @throws IllegalArgumentException
     *             if some aspect of this key or value prevents it from being stored in this map.
     * @throws NullPointerException
     *             if the key is <tt>null</tt>
     */
    int put(IntAttribute attribute, int value);

    /**
     * Removes the mapping for an attribute from this map if it is present (optional operation).
     * <p>
     * The map will not contain a mapping for the specified attribute once the call returns.
     * 
     * @param attribute
     *            the attribute to be removed
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link IntAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this map
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    int remove(IntAttribute attribute);

    /**
     * Associates the specified long value with the specified key in this map (optional operation).
     * If the map previously contained a mapping for this key, the old value is replaced by the
     * specified value.
     * 
     * @param attribute
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link LongAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this map. For example, for a
     *             read-only attribute-map
     * @throws IllegalArgumentException
     *             if some aspect of this key or value prevents it from being stored in this map.
     * @throws NullPointerException
     *             if the key is <tt>null</tt>
     */
    long put(LongAttribute attribute, long value);

    /**
     * Removes the mapping for an attribute from this map if it is present (optional operation).
     * <p>
     * The map will not contain a mapping for the specified attribute once the call returns.
     * 
     * @param attribute
     *            the attribute to be removed
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link LongAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this map
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    long remove(LongAttribute attribute);

    /**
     * Associates the specified short value with the specified key in this map (optional operation).
     * If the map previously contained a mapping for this key, the old value is replaced by the
     * specified value.
     * 
     * @param attribute
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link ShortAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this map. For example, for a
     *             read-only attribute-map
     * @throws IllegalArgumentException
     *             if some aspect of this key or value prevents it from being stored in this map.
     * @throws NullPointerException
     *             if the key is <tt>null</tt>
     */
    short put(ShortAttribute attribute, short value);

    void putAll(AttributeMap attributes);

    /**
     * Removes the mapping for an attribute from this map if it is present (optional operation).
     * <p>
     * The map will not contain a mapping for the specified attribute once the call returns.
     * 
     * @param attribute
     *            the attribute to be removed
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link ShortAttribute#getDefaultValue()}</tt> if there was no mapping for
     *         <tt>attribute</tt>. (A default return value can also indicate that the map previously
     *         associated the default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this map
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    short remove(ShortAttribute attribute);

    <T> T put(Attribute<T> attribute, T value);

    /**
     * Removes the mapping for an attribute from this map if it is present (optional operation).
     * <p>
     * The map will not contain a mapping for the specified attribute once the call returns.
     * 
     * @param <T>
     *            the type of the attribute being removed
     * @param attribute
     *            the attribute to be removed
     * @return the previous value associated with <tt>attribute</tt>, or <tt>
     *         {@link Attribute#getDefault()}</tt> if there was no mapping for <tt>attribute</tt>.
     *         (A default return value can also indicate that the map previously associated the
     *         default value with <tt>attribute</tt>)
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this map
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    <T> T remove(Attribute<T> attribute);
}
