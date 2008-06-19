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

public abstract class ObjectAttribute<T> extends Attribute<T> {
    public ObjectAttribute(Class<T> clazz) {
        this(clazz, null);
    }

    public ObjectAttribute(Class<T> clazz, T defaultValue) {
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
    public ObjectAttribute(String name, Class<T> clazz) {
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
    public ObjectAttribute(String name, Class<T> clazz, T defaultValue) {
        super(name, clazz, defaultValue);
    }

    /**
     * Checks if the specified value is valid for this attribute. If the specified value is not valid this method will
     * throw an {@link IllegalArgumentException}.
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
     * Extracts the attribute map from the specified {@link WithAttributes} and returns the value of this attribute from
     * the map. If this attribute is not set in the map, the value of {@link Attribute#getDefault()} will be returned
     * instead.
     * 
     * @param withAttributes
     *            the object containing an attribute map for which to retrieve the value of this attribute
     * @return the value of this attribute
     */
    public final T get(WithAttributes withAttributes) {
        return withAttributes.getAttributes().get(this);
    }

    /**
     * Extracts the attribute map from the specified {@link WithAttributes} and returns the value of this attribute from
     * the map. If this attribute is not set in the map, the specified default value will be returned.
     * 
     * @param withAttributes
     *            the object containing an attribute map for which to retrieve the value of this attribute
     * @return the value of this attribute
     */

    public final T get(WithAttributes withAttributes, T defaultValue) {
        return withAttributes.getAttributes().get(this, defaultValue);
    }

    public T getDefaultValue() {
        return super.getDefault();
    }

    /**
     * Returns whether or not the specified value is valid for this attribute. This method can be overriden to only
     * accept certain values.
     * 
     * @param value
     *            the specified value to check
     * @return <code>true</code> if the specified value is valid for this attribute, otherwise <code>false</code>
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
     * @return the specified attribute map
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

    //<S extends WithAttributes> S set(S withAttribute, T value)
    public void set(WithAttributes withAttributes, T value) {
         set(withAttributes.getAttributes(), value);
    }

    /**
     * Returns an AttributeMap containing only this attribute mapping to the specified value. The returned map is
     * immutable.
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
