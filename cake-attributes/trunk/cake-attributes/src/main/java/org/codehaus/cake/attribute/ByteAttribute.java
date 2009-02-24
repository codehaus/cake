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
import java.util.Comparator;
/**
 * An implementation of an {@link Attribute} mapping to a byte. This implementation adds a number of
 * methods that works on primitive bytes instead of their object counterpart.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public abstract class ByteAttribute extends Attribute<Byte> implements
         Comparator<AttributeMap>, Serializable {
    
    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;
         
    /** The default value of this attribute. */
    private final transient byte defaultValue;

    /**
     * Creates a new ByteAttribute with a generated name and a default value of <tt>0</tt>.
     * 
     * @throws IllegalArgumentException
     *             if 0 is not a valid value according to {@link #checkValid(byte)}
     */
    public ByteAttribute() {
        this((byte) 0);
    }

    /**
     * Creates a new ByteAttribute with a generated name.
     * 
     * @param defaultValue
     *            the default value of this attribute
     * @throws IllegalArgumentException
     *             if the specified default value is not a valid value according to
     *             {@link #checkValid(byte)}
     */
    public ByteAttribute(byte defaultValue) {
        super(Byte.TYPE, defaultValue);
        this.defaultValue = defaultValue;
    }

    /**
     * Creates a new ByteAttribute with a default value of <tt>0</tt>.
     * 
     * @param name
     *            the name of the attribute
     * @throws NullPointerException
     *             if the specified name is <code>null</code>
     * @throws IllegalArgumentException
     *             if 0 is not a valid value according to {@link #checkValid(byte)}
     */
    public ByteAttribute(String name) {
        this(name, (byte) 0);
    }

    /**
     * Creates a new ByteAttribute.
     * 
     * @param name
     *            the name of the attribute
     * @param defaultValue
     *            the default value of the attribute
     * @throws NullPointerException
     *             if the specified name is <code>null</code>
     * @throws IllegalArgumentException
     *             if the specified default value is not a valid value according to
     *             {@link #checkValid(byte)}
     */
    public ByteAttribute(String name, byte defaultValue) {
        super(name, Byte.TYPE, defaultValue);
        this.defaultValue = defaultValue;
    }
    
    /** {@inheritDoc} */
    @Override
    public final void checkValid(Byte o) {
        checkValid(o.byteValue());
    }
    
    /**
     * Analogous to {@link #checkValid(Byte)} except taking a primitive byte.
     * <p>
     * The default implementation accept all values.
     * @param value
     *            the value to check
     * @throws IllegalArgumentException
     *             if the specified value is not valid
     */
    public final void checkValid(byte value) { 
        if (!isValid(value)) {
            throw new IllegalArgumentException(checkValidFailureMessage(value));
        }
    }
    
    /** {@inheritDoc} */
    public int compare(AttributeMap w1, AttributeMap w2) {
        byte thisVal = w1.get(this);
        byte anotherVal = w2.get(this);
        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
    }
    
    
    /**
     * Creates a value instance of this attribute from the specified string.
     * 
     * @param str
     *            the string to create the value from.
     * @return a value instance from the specified string
     * @throws IllegalArgumentException
     *             if a valid value could not be created from the string.
     */
    public byte fromString(String str) {
        return Byte.parseByte(str);
    }

    /**
     * Returns the default primitive value of this attribute. This is equivalent to calling
     * {@link #getDefault()}, but returning a primitive int instead.
     * 
     * @return the default value of this attribute
     */
    public byte getDefaultValue() {
        return defaultValue;
    }

   /**
     * Analogous to {@link Attribute#isValid(Object)} except taking a primitive byte as
     * parameter.
     * <p>
     * The default version returns true for all parameters
     * 
     * @param value
     *            the value to check
     * @return whether or not the value is valid
     */
    public boolean isValid(byte value) {
        return true;
    }
    /** {@inheritDoc} */
    @Override
    public final boolean isValid(Byte value) {
        return isValid(value.byteValue());
    }

    /**
     * Returns an AttributeMap containing only this attribute mapping to the specified value. The
     * returned map is immutable.
     * 
     * @param value
     *            the value to create the singleton from
     * @return an AttributeMap containing only this attribute mapping to the specified value
     */
    public AttributeMap singleton(byte value) {
        return super.singleton(value);
    }
    
}
