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
 * @version $Id: AttributeMap.java 415 2007-11-09 08:25:23Z kasper $
 */
public interface AttributeMap {

    /**
     * Returns a set view of the attributes contained in this map. The set is backed by the map, so
     * changes to the map are reflected in the set, and vice-versa. If the map is modified while an
     * iteration over the set is in progress (except through the iterator's own <tt>remove</tt>
     * operation), the results of the iteration are undefined. The set supports element removal,
     * which removes the corresponding mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt> <tt>retainAll</tt>, and <tt>clear</tt> operations. It
     * does not support the add or <tt>addAll</tt> operations.
     * 
     * @return a set view of the attributess contained in this map.
     */
    Set<Attribute> attributeSet();

    /**
     * Removes all of the mappings from this map (optional operation). The map will be empty after
     * this call returns.
     * 
     * @throws UnsupportedOperationException
     *             if the <tt>clear</tt> operation is not supported by this map
     */
    void clear();

    /**
     * Returns <tt>true</tt> if this attribute map contains a mapping for the specified attribute.
     * More formally, returns <tt>true</tt> if and only if this map contains a mapping for a
     * attribute <tt>a</tt> such that <tt>(attribute==a)</tt>. (There can be at most one such
     * mapping.)
     * 
     * @param attribute
     *            attribute whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified attribute
     * @throws NullPointerException
     *             if the specified attribute is null
     */
    boolean contains(Attribute<?> attribute);

    Set<Map.Entry<Attribute, Object>> entrySet();

    <T> T get(Attribute<T> attribute);

    <T> T get(Attribute<T> attribute, T defaultValue);

    /**
     * Returns the boolean value to which this attribute-map maps the specified key. Returns the
     * value returned by the specified attributes {@link BooleanAttribute#getDefaultValue()} method
     * if this map contains no mapping for the specified attribute. A return value of
     * <tt>{@link BooleanAttribute#getDefaultValue()}</tt> does not <i>necessarily</i> indicate that
     * this map contains no mapping for the key; it's also possible that this map explicitly maps
     * the key to <tt>{@link BooleanAttribute#getDefaultValue()}</tt>. The
     * <tt>{@link #contains(Attribute)}</tt> operation may be used to distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @return the value to which this map maps the specified attribute, or
     *         <tt>{@link BooleanAttribute#getDefaultValue()}</tt> if the map contains no mapping
     *         for this attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a boolean (or Boolean)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    boolean get(BooleanAttribute attribute);

    /**
     * Returns the boolean value to which this attribute-map maps the specified key. Returns the
     * specified defaultValue if this map contains no mapping for the specified attribute. A return
     * value of <tt>defaultValue</tt> does not <i>necessarily</i> indicate that this map contains no
     * mapping for the key; it's also possible that this map explicitly maps the key to
     * <tt>defaultValue</tt>. The <tt>{@link #contains(Attribute)}</tt> operation may be used to
     * distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @param defaultValue
     *            the default value to return if no mapping for the specified attribute exist
     * @return the value to which this map maps the specified attribute, or <tt>defaultValue</tt> if
     *         the map contains no mapping for this attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a boolean (or Boolean)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    boolean get(BooleanAttribute attribute, boolean defaultValue);

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
     * Returns the byte value to which this attribute-map maps the specified key. Returns the value
     * returned by the specified attributes {@link ByteAttribute#getDefaultValue()} method if this
     * map contains no mapping for the specified attribute. A return value of <tt>
     * {@link ByteAttribute#getDefaultValue()}</tt> does not <i>necessarily</i> indicate that this
     * map contains no mapping for the key; it's also possible that this map explicitly maps the key
     * to <tt>{@link ByteAttribute#getDefaultValue()}</tt>. The <tt>{@link #contains(Attribute)}
     * </tt> operation may be used to distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @return the value to which this map maps the specified attribute, or <tt>
     *         {@link ByteAttribute#getDefaultValue()}</tt> if the map contains no mapping for this
     *         attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a byte (or Byte)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    byte get(ByteAttribute attribute);

    /**
     * Returns the byte value to which this attribute-map maps the specified key. Returns the
     * specified defaultValue if this map contains no mapping for the specified attribute. A return
     * value of <tt>defaultValue</tt> does not <i>necessarily</i> indicate that this map contains no
     * mapping for the key; it's also possible that this map explicitly maps the key to
     * <tt>defaultValue</tt>. The <tt>{@link #contains(Attribute)}</tt> operation may be used to
     * distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @param defaultValue
     *            the default value to return if no mapping for the specified attribute exist
     * @return the value to which this map maps the specified attribute, or <tt>defaultValue</tt> if
     *         the map contains no mapping for this attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a byte (or Byte)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    byte get(ByteAttribute attribute, byte defaultValue);

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
     * Returns the char value to which this attribute-map maps the specified key. Returns the value
     * returned by the specified attributes {@link CharAttribute#getDefaultValue()} method if this
     * map contains no mapping for the specified attribute. A return value of <tt>
     * {@link CharAttribute#getDefaultValue()}</tt> does not <i>necessarily</i> indicate that this
     * map contains no mapping for the key; it's also possible that this map explicitly maps the key
     * to <tt>{@link CharAttribute#getDefaultValue()}</tt>. The <tt>{@link #contains(Attribute)}
     * </tt> operation may be used to distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @return the value to which this map maps the specified attribute, or <tt>
     *         {@link CharAttribute#getDefaultValue()}</tt> if the map contains no mapping for this
     *         attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a char (or Character)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    char get(CharAttribute attribute);

    /**
     * Returns the char value to which this attribute-map maps the specified key. Returns the
     * specified defaultValue if this map contains no mapping for the specified attribute. A return
     * value of <tt>defaultValue</tt> does not <i>necessarily</i> indicate that this map contains no
     * mapping for the key; it's also possible that this map explicitly maps the key to
     * <tt>defaultValue</tt>. The <tt>{@link #contains(Attribute)}</tt> operation may be used to
     * distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @param defaultValue
     *            the default value to return if no mapping for the specified attribute exist
     * @return the value to which this map maps the specified attribute, or <tt>defaultValue</tt> if
     *         the map contains no mapping for this attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a char (or Character)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    char get(CharAttribute attribute, char defaultValue);

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
     * Returns the double value to which this attribute-map maps the specified key. Returns the
     * value returned by the specified attributes {@link DoubleAttribute#getDefaultValue()} method
     * if this map contains no mapping for the specified attribute. A return value of <tt>
     * {@link DoubleAttribute#getDefaultValue()}</tt> does not <i>necessarily</i> indicate that this
     * map contains no mapping for the key; it's also possible that this map explicitly maps the key
     * to <tt>{@link DoubleAttribute#getDefaultValue()}</tt>. The <tt>{@link #contains(Attribute)}
     * </tt> operation may be used to distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @return the value to which this map maps the specified attribute, or <tt>
     *         {@link DoubleAttribute#getDefaultValue()}</tt> if the map contains no mapping for
     *         this attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a double (or Double)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    double get(DoubleAttribute attribute);

    /**
     * Returns the double value to which this attribute-map maps the specified key. Returns the
     * specified defaultValue if this map contains no mapping for the specified attribute. A return
     * value of <tt>defaultValue</tt> does not <i>necessarily</i> indicate that this map contains no
     * mapping for the key; it's also possible that this map explicitly maps the key to
     * <tt>defaultValue</tt>. The <tt>{@link #contains(Attribute)}</tt> operation may be used to
     * distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @param defaultValue
     *            the default value to return if no mapping for the specified attribute exist
     * @return the value to which this map maps the specified attribute, or <tt>defaultValue</tt> if
     *         the map contains no mapping for this attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a double (or Double)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    double get(DoubleAttribute attribute, double defaultValue);

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
     * Returns the float value to which this attribute-map maps the specified key. Returns the value
     * returned by the specified attributes {@link FloatAttribute#getDefaultValue()} method if this
     * map contains no mapping for the specified attribute. A return value of <tt>
     * {@link FloatAttribute#getDefaultValue()}</tt> does not <i>necessarily</i> indicate that this
     * map contains no mapping for the key; it's also possible that this map explicitly maps the key
     * to <tt>{@link FloatAttribute#getDefaultValue()}</tt>. The <tt>{@link #contains(Attribute)}
     * </tt> operation may be used to distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @return the value to which this map maps the specified attribute, or <tt>
     *         {@link FloatAttribute#getDefaultValue()}</tt> if the map contains no mapping for this
     *         attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a float (or Float)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    float get(FloatAttribute attribute);

    /**
     * Returns the float value to which this attribute-map maps the specified key. Returns the
     * specified defaultValue if this map contains no mapping for the specified attribute. A return
     * value of <tt>defaultValue</tt> does not <i>necessarily</i> indicate that this map contains no
     * mapping for the key; it's also possible that this map explicitly maps the key to
     * <tt>defaultValue</tt>. The <tt>{@link #contains(Attribute)}</tt> operation may be used to
     * distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @param defaultValue
     *            the default value to return if no mapping for the specified attribute exist
     * @return the value to which this map maps the specified attribute, or <tt>defaultValue</tt> if
     *         the map contains no mapping for this attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a float (or Float)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    float get(FloatAttribute attribute, float defaultValue);

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
     * Returns the int value to which this attribute-map maps the specified key. Returns the value
     * returned by the specified attributes {@link IntAttribute#getDefaultValue()} method if this
     * map contains no mapping for the specified attribute. A return value of <tt>
     * {@link IntAttribute#getDefaultValue()}</tt> does not <i>necessarily</i> indicate that this
     * map contains no mapping for the key; it's also possible that this map explicitly maps the key
     * to <tt>{@link IntAttribute#getDefaultValue()}</tt>. The <tt>{@link #contains(Attribute)}</tt>
     * operation may be used to distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @return the value to which this map maps the specified attribute, or <tt>
     *         {@link IntAttribute#getDefaultValue()}</tt> if the map contains no mapping for this
     *         attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a int (or Integer)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    int get(IntAttribute attribute);

    /**
     * Returns the int value to which this attribute-map maps the specified key. Returns the
     * specified defaultValue if this map contains no mapping for the specified attribute. A return
     * value of <tt>defaultValue</tt> does not <i>necessarily</i> indicate that this map contains no
     * mapping for the key; it's also possible that this map explicitly maps the key to
     * <tt>defaultValue</tt>. The <tt>{@link #contains(Attribute)}</tt> operation may be used to
     * distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @param defaultValue
     *            the default value to return if no mapping for the specified attribute exist
     * @return the value to which this map maps the specified attribute, or <tt>defaultValue</tt> if
     *         the map contains no mapping for this attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a int (or Integer)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    int get(IntAttribute attribute, int defaultValue);

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
     * Returns the long value to which this attribute-map maps the specified key. Returns the value
     * returned by the specified attributes {@link LongAttribute#getDefaultValue()} method if this
     * map contains no mapping for the specified attribute. A return value of <tt>
     * {@link LongAttribute#getDefaultValue()}</tt> does not <i>necessarily</i> indicate that this
     * map contains no mapping for the key; it's also possible that this map explicitly maps the key
     * to <tt>{@link LongAttribute#getDefaultValue()}</tt>. The <tt>{@link #contains(Attribute)}
     * </tt> operation may be used to distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @return the value to which this map maps the specified attribute, or <tt>
     *         {@link LongAttribute#getDefaultValue()}</tt> if the map contains no mapping for this
     *         attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a long (or Long)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    long get(LongAttribute attribute);

    /**
     * Returns the long value to which this attribute-map maps the specified key. Returns the
     * specified defaultValue if this map contains no mapping for the specified attribute. A return
     * value of <tt>defaultValue</tt> does not <i>necessarily</i> indicate that this map contains no
     * mapping for the key; it's also possible that this map explicitly maps the key to
     * <tt>defaultValue</tt>. The <tt>{@link #contains(Attribute)}</tt> operation may be used to
     * distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @param defaultValue
     *            the default value to return if no mapping for the specified attribute exist
     * @return the value to which this map maps the specified attribute, or <tt>defaultValue</tt> if
     *         the map contains no mapping for this attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a long (or Long)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    long get(LongAttribute attribute, long defaultValue);

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
     * Returns the short value to which this attribute-map maps the specified key. Returns the value
     * returned by the specified attributes {@link ShortAttribute#getDefaultValue()} method if this
     * map contains no mapping for the specified attribute. A return value of <tt>
     * {@link ShortAttribute#getDefaultValue()}</tt> does not <i>necessarily</i> indicate that this
     * map contains no mapping for the key; it's also possible that this map explicitly maps the key
     * to <tt>{@link ShortAttribute#getDefaultValue()}</tt>. The <tt>{@link #contains(Attribute)}
     * </tt> operation may be used to distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @return the value to which this map maps the specified attribute, or <tt>
     *         {@link ShortAttribute#getDefaultValue()}</tt> if the map contains no mapping for this
     *         attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a short (or Short)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    short get(ShortAttribute attribute);

    /**
     * Returns the short value to which this attribute-map maps the specified key. Returns the
     * specified defaultValue if this map contains no mapping for the specified attribute. A return
     * value of <tt>defaultValue</tt> does not <i>necessarily</i> indicate that this map contains no
     * mapping for the key; it's also possible that this map explicitly maps the key to
     * <tt>defaultValue</tt>. The <tt>{@link #contains(Attribute)}</tt> operation may be used to
     * distinguish these two cases.
     * 
     * @param attribute
     *            attribute whose associated value is to be returned.
     * @param defaultValue
     *            the default value to return if no mapping for the specified attribute exist
     * @return the value to which this map maps the specified attribute, or <tt>defaultValue</tt> if
     *         the map contains no mapping for this attribute.
     * @throws ClassCastException
     *             if the associated value is of another type then a short (or Short)
     * @throws NullPointerException
     *             if the specified attribute is <tt>null</tt>.
     * @see #contains(Attribute)
     */
    short get(ShortAttribute attribute, short defaultValue);

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

    /**
     * Returns <tt>true</tt> if this map contains no attribute-value mappings.
     * 
     * @return <tt>true</tt> if this map contains no attribute-value mappings
     */
    boolean isEmpty();

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

    /**
     * Returns the number of attribute-value mappings in this attribute map. If the map contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns <tt>Integer.MAX_VALUE</tt>.
     * 
     * @return the number of attribute-value mappings in this map
     */
    int size();

    Collection<Object> values();
}
