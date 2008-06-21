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
import java.util.concurrent.atomic.AtomicLong;

/**
 * Attribute-value pairs are a fundamental data representation in many computing systems and applications. Designers
 * often desire an open-ended data structure that allows for future extension without modifying existing code or data.
 * In such situations, all or part of the data model may be expressed as a collection of tuples attribute name, value;
 * each element is an attribute-value pair.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
 * @param <T>
 *            the type of objects that this attribute maps to
 * @see AttributeMap
 */
public abstract class Attribute<T> implements Serializable {
    /* All fields are transient because all attribute implementations should be singletons. */

    /** An AtomicLong for generating unique names, if user does not specify name. */
    private static final AtomicLong NAME = new AtomicLong();

    /** The type of this attribute, as returned {@link #getType()}. */
    private final transient Class<T> clazz;

    /** The default value of this attribute. */
    private final transient T defaultValue;

    /** The hashcode of this attribute. */
    private final transient int hashCode;

    /** The name of this attribute. */
    private final transient String name;

    /**
     * Creates a new Attribute.
     * 
     * @param clazz
     *            the type of this attribute
     * @param defaultValue
     *            the default value of this attribute
     * @throws IllegalArgumentException
     *             if the specified default value is not valid according to {@link #checkValid(Object)}
     * @throws NullPointerException
     *             if the specified class is null
     */
    Attribute(Class<T> clazz, T defaultValue) {
        if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        this.name = getClass().toString() + NAME.incrementAndGet();
        this.clazz = clazz;
        checkValid(defaultValue);
        this.defaultValue = defaultValue;
        // not that smart, considering clazz uses Object.hashCode
        // maybe just cache System.identityHashCode(x)
        hashCode = name.hashCode() ^ clazz.hashCode();
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
     * @throws IllegalArgumentException
     *             if the specified default value is not valid according to {@link #checkValid(Object)}
     * @throws NullPointerException
     *             if the specified name or class is null
     */
    Attribute(String name, Class<T> clazz, T defaultValue) {
        if (name == null) {
            throw new NullPointerException("name is null");
        } else if (clazz == null) {
            throw new NullPointerException("clazz is null");
        }
        this.name = name;
        this.clazz = clazz;
        checkValid(defaultValue);
        this.defaultValue = defaultValue;
        // not that smart, considering clazz uses Object.hashCode
        // maybe just cache System.identityHashCode(x)
        hashCode = name.hashCode() ^ clazz.hashCode();
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
     * Returns the <code>true</code> if and only id the specified object is the same as this, otherwise returns
     * <code>false</code>.
     * 
     * @param obj
     *            the reference object with which to compare.
     * @return true if the specified object is the same as this, otherwise false
     */

    public final boolean equals(Object obj) {
        return obj == this;
    }

    /**
     * Returns the default value of this attribute (<code>null</code> values are allowed).
     * 
     * @return the default value of this attribute
     */
    public T getDefault() {
        return defaultValue;
    }

    /**
     * Returns the name of the attribute.
     * 
     * @return the name of the attribute
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the type of this attribute.
     * 
     * @return the type of this attribute
     */
    public final Class<T> getType() {
        return clazz;
    }

    /** {@inheritDoc} */
    public final int hashCode() {
        return hashCode;
    }

    /**
     * Returns whether or not this attribute is set in the specified attribute map. This method is useful for
     * distinguishing those case where an attribute maps to the default value of the attribute.
     * 
     * @param attributes
     *            the attribute map to check if this attribute is set
     * @return <code>true</code> if this attribute is set in the specified attribute map, otherwise false
     */
    public boolean isSet(WithAttributes attributes) {
        return attributes.getAttributes().contains(this);
    }

    /**
     * Returns whether or not the specified value is valid for this attribute. This method can be overridden to only
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

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return name;
    }
}