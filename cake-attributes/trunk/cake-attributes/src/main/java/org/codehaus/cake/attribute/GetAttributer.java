package org.codehaus.cake.attribute;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface GetAttributer {

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

    /**
     * Returns the number of attribute-value mappings in this attribute map. If the map contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns <tt>Integer.MAX_VALUE</tt>.
     * 
     * @return the number of attribute-value mappings in this map
     */
    int size();

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
    Set<Attribute> attributes();

    Set<Map.Entry<Attribute, Object>> entrySet();

    /**
     * Returns <tt>true</tt> if this map contains no attribute-value mappings.
     * 
     * @return <tt>true</tt> if this map contains no attribute-value mappings
     */
    boolean isEmpty();

    Collection<Object> values();

}
