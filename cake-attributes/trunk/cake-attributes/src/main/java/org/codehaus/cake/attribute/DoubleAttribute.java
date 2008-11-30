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

import java.util.Comparator;
import java.io.Serializable;
/**
 * An implementation of an {@link Attribute} mapping to a double. This implementation adds a number of
 * methods that works on primitive doubles instead of their object counterpart.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public abstract class DoubleAttribute extends Attribute<Double> implements
         Comparator<WithAttributes>, Serializable {
    
    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;
         
    /** The default value of this attribute. */
    private final transient double defaultValue;

    /**
     * Creates a new DoubleAttribute with a generated name and a default value of <tt>0</tt>.
     * 
     * @throws IllegalArgumentException
     *             if 0 is not a valid value according to {@link #checkValid(double)}
     */
    public DoubleAttribute() {
        this(0D);
    }

    /**
     * Creates a new DoubleAttribute with a generated name.
     * 
     * @param defaultValue
     *            the default value of this attribute
     * @throws IllegalArgumentException
     *             if the specified default value is not a valid value according to
     *             {@link #checkValid(double)}
     */
    public DoubleAttribute(double defaultValue) {
        super(Double.TYPE, defaultValue);
        this.defaultValue = defaultValue;
    }

    /**
     * Creates a new DoubleAttribute with a default value of <tt>0</tt>.
     * 
     * @param name
     *            the name of the attribute
     * @throws NullPointerException
     *             if the specified name is <code>null</code>
     * @throws IllegalArgumentException
     *             if 0 is not a valid value according to {@link #checkValid(double)}
     */
    public DoubleAttribute(String name) {
        this(name, 0D);
    }

    /**
     * Creates a new DoubleAttribute.
     * 
     * @param name
     *            the name of the attribute
     * @param defaultValue
     *            the default value of the attribute
     * @throws NullPointerException
     *             if the specified name is <code>null</code>
     * @throws IllegalArgumentException
     *             if the specified default value is not a valid value according to
     *             {@link #checkValid(double)}
     */
    public DoubleAttribute(String name, double defaultValue) {
        super(name, Double.TYPE, defaultValue);
        this.defaultValue = defaultValue;
    }
    
    /** {@inheritDoc} */
    @Override
    public final void checkValid(Double o) {
        checkValid(o.doubleValue());
    }
    
    /**
     * Analogous to {@link #checkValid(Double)} except taking a primitive double.
     * <p>
     * The default implementation fails if the specified value is either
     * {@link Double#NEGATIVE_INFINITY}, {@link Double#POSITIVE_INFINITY} or {@link Double#NaN}.
     * 
     * @param value
     *            the value to check
     * @throws IllegalArgumentException
     *             if the specified value is not valid
     */
    public final void checkValid(double value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException(checkValidFailureMessage(value));
        }
    }
    
    /** {@inheritDoc} */
    public int compare(WithAttributes w1, WithAttributes w2) {
        double thisVal = get(w1);
        double anotherVal = get(w2);
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
    public double fromString(String str) {
        return Double.parseDouble(str);
    }

    /**
     * Returns the default primitive value of this attribute. This is equivalent to calling
     * {@link #getDefault()}, but returning a primitive int instead.
     * 
     * @return the default value of this attribute
     */
    public double getDefaultValue() {
        return defaultValue;
    }
    
    /**
     * Extracts the attribute map from the specified {@link WithAttributes} and returns the value of
     * this attribute from the map. If this attribute is not set in the map, the value of
     * {@link #getDefaultValue()} will be returned instead.
     * 
     * @param withAttributes
     *            an object containing an AttributeMap
     * @return the value of this attribute if this attribute is present in the extracted map. Otherwise
     *         {@link #getDefaultValue()}
     */
    public double get(WithAttributes withAttributes) {
        return withAttributes.getAttributes().get(this);
    }

    /**
     * Analogous to {@link #get(WithAttributes)} except returning a primitive <tt>double</tt>.
     * 
     * @param withAttributes
     *            an object containing an AttributeMap
     * @param defaultValue
     *            the default value to return if this attribute is not present in the map
     * @return the value of this attribute if this attribute is present in the map. Otherwise the
     *         specified default value
     */
    public double get(WithAttributes withAttributes, double defaultValue) {
        return withAttributes.getAttributes().get(this, defaultValue);
    }

   /**
     * Analogous to {@link Attribute#isValid(Object)} except taking a primitive double as
     * parameter.
     * <p>
     * The default version returns true for all parameters, except 
     * {@link Double#NEGATIVE_INFINITY}, {@link Double#POSITIVE_INFINITY} or {@link Double#NaN}.
     *
     * @param value
     *            the value to check
     * @return whether or not the value is valid
     */
    public boolean isValid(double value) {
        return !isNaNInfinity(value);
    }
    /** {@inheritDoc} */
    @Override
    public final boolean isValid(Double value) {
        return isValid(value.doubleValue());
    }

    /**
     * Sets the specified value in the specified attribute map.
     * 
     * @param attributes
     *            the attribute map to set the value in.
     * @param value
     *            the value that should be set
     * @throws IllegalArgumentException
     *             if the specified value is not valid accordingly to {@link #checkValid(double)}
     */
    public void set(AttributeMap attributes, double value) {
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
     *             if the specified value is not valid accordingly to {@link #checkValid(double)}
     */
    public void set(WithAttributes withAttributes, double value) {
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
    public AttributeMap singleton(double value) {
        return super.singleton(value);
    }
    
  
    /**
     * Returns <code>true</code> if the specified value is either {@link Double#NEGATIVE_INFINITY},
     * {@link Double#POSITIVE_INFINITY} or {@link Double#NaN}. Otherwise, false
     * 
     * @param value
     *            the value to check
     * @return whether or not the specified value is Infinity or NaN
     */
    protected boolean isNaNInfinity(double value) {
        return Double.isNaN(value) || Double.isInfinite(value);
    }
}
