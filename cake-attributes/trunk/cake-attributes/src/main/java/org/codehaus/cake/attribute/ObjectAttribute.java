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

/**
 * An implementation of an {@link Attribute} mapping to an Object.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LongAttribute.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
 * @param <T>
 *            the datatype of this attribute
 */
public abstract class ObjectAttribute<T> extends Attribute<T> implements Serializable {

    /**
     * Creates a new ObjectAttribute with the specified datatype, a generated name and a default
     * value of <tt>null</tt>.
     * 
     * @param clazz
     *            the type of the values
     * @throws IllegalArgumentException
     *             if null is not a valid value according to {@link #checkValid(Object)}
     * @throws NullPointerException
     *             if the specified class is null
     */
    protected ObjectAttribute(Class<T> clazz) {
        this(clazz, null);
    }

    /**
     * Creates a new ObjectAttribute with the specified datatype and default value, and a generated
     * name.
     * 
     * @param clazz
     *            the type of the values
     * @param defaultValue
     *            default value of this attribute
     * @throws IllegalArgumentException
     *             if the specified default value is not a valid value according to
     *             {@link #checkValid(Object)}
     * @throws NullPointerException
     *             if the specified class is null
     */
    protected ObjectAttribute(Class<T> clazz, T defaultValue) {
        super(clazz, defaultValue);
    }

    /**
     * Creates a new Attribute with <code>null</code> as default value.
     * 
     * @param name
     *            the name of the attribute
     * @param clazz
     *            the type of this attribute
     */
    protected ObjectAttribute(String name, Class<T> clazz) {
        this(name, clazz, null);
    }

    /**
     * Creates a new AbstractAttribute.
     * 
     * @param name
     *            the name of the attribute
     * @param clazz
     *            the type of this attribute
     * @param defaultValue
     *            the default value of this attribute
     */
    protected ObjectAttribute(String name, Class<T> clazz, T defaultValue) {
        super(name, clazz, defaultValue);
    }

    /**
     * Checks if the specified value is valid for this attribute. If the specified value is not
     * valid this method will throw an {@link IllegalArgumentException}.
     * 
     * @param value
     *            the value to check
     * @throws IllegalArgumentException
     *             if the specified value is not valid
     */
    public void checkValid(T value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Illegal value for attribute " + getName() + ", value = " + value);
        }
    }

    /**
     * Creates a value instance of this attribute from the specified string.
     * 
     * @param str
     *            the string to create the value from.
     * @return a value instance from the specified string
     * @throws UnsupportedOperationException
     *             if this operation is not supported by this attribute
     * @throws IllegalArgumentException
     *             if a valid attribute value could not be created from the string.
     */
    public T fromString(String str) {
        throw new UnsupportedOperationException();
    }

    /**
     * Extracts the attribute map from the specified {@link WithAttributes} and returns the value of
     * this attribute from the map. If this attribute is not set in the map, the value of
     * {@link Attribute#getDefault()} will be returned instead.
     * 
     * @param withAttributes
     *            the object containing an attribute map for which to retrieve the value of this
     *            attribute
     * @return the value of this attribute
     */
    public final T get(WithAttributes withAttributes) {
        return withAttributes.getAttributes().get(this);
    }

    /**
     * Extracts the attribute map from the specified {@link WithAttributes} and returns the value of
     * this attribute from the map. If this attribute is not set in the map, the specified default
     * value will be returned.
     * 
     * @param withAttributes
     *            the object containing an attribute map for which to retrieve the value of this
     *            attribute
     * @param defaultValue
     *            the default value to return if this attribute is not present in the map
     * @return the value of this attribute
     */

    public final T get(WithAttributes withAttributes, T defaultValue) {
        return withAttributes.getAttributes().get(this, defaultValue);
    }

    /**
     * Returns whether or not the specified value is valid for this attribute. This method can be
     * overriden to only accept certain values.
     * 
     * @param value
     *            the specified value to check
     * @return <code>true</code> if the specified value is valid for this attribute, otherwise
     *         <code>false</code>
     */
    public boolean isValid(T value) {
        return true; // all values are accepted by default.
    }

    /**
     * Sets the specified value in the specified attribute map.
     * 
     * @param attributes
     *            the attribute map to set the value in.
     * @param value
     *            the value that should be set
     * @throws IllegalArgumentException
     *             if the specified value is not valid accordingly to {@link #checkValid(Object)}
     */
    public void set(AttributeMap attributes, T value) {
        if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        checkValid(value);
        attributes.put(this, value);
    }

    /**
     * Sets the specified value in the specified attribute holder (WithAttributes).
     * 
     * @param withAttributes
     *            the the attribute holder to set the value in.
     * @param value
     *            the value that should be set
     * @throws IllegalArgumentException
     *             if the specified value is not valid accordingly to {@link #checkValid(Object)}
     */
    public void set(WithAttributes withAttributes, T value) {
        // <S extends WithAttributes> S set(S withAttribute, T value)
        set(withAttributes.getAttributes(), value);
    }

    /**
     * Returns an AttributeMap containing only this attribute mapping to the specified value. The
     * returned map is immutable.
     * 
     * @param value
     *            the value to create the singleton from
     * @return an AttributeMap containing only this attribute mapping to the specified value
     */
    public AttributeMap singleton(T value) {
        checkValid(value);
        return Attributes.singleton(this, value);
    }
}
