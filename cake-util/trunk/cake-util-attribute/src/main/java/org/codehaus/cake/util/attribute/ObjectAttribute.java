/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.util.attribute;

import java.io.Serializable;

/**
 * An implementation of an {@link Attribute} mapping to an Object.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <T>
 *            the datatype of this attribute
 */
public abstract class ObjectAttribute<T> extends Attribute<T> implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ObjectAttribute with the specified datatype, a generated name and a default value of <tt>null</tt>.
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
     * Creates a new ObjectAttribute with the specified datatype and default value, and a generated name.
     * 
     * @param clazz
     *            the type of the values
     * @param defaultValue
     *            default value of this attribute
     * @throws IllegalArgumentException
     *             if the specified default value is not a valid value according to {@link #checkValid(Object)}
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

    /** {@inheritDoc} */
    public final void checkValid(T value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException(checkValidFailureMessage(value));
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
        //TODO should we allow parsing string that not #isValid()???
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
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
    public void set(MutableAttributeMap attributes, T value) {
        if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        checkValid(value);
        attributes.put(this, value);
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
