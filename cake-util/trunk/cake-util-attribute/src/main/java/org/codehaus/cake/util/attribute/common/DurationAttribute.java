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
package org.codehaus.cake.util.attribute.common;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

/**
 * An attribute mapping to an amount of time. The unit of time is nanoseconds.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public abstract class DurationAttribute extends LongAttribute {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The default value of this attribute. */
    protected static final long DEFAULT_DURATION = Long.MAX_VALUE;

    /** A value that indicates forever. */
    protected static final long FOREVER = Long.MAX_VALUE;

    /** The time unit of this attribute. */
    protected static final TimeUnit TIME_UNIT = TimeUnit.NANOSECONDS;

    /**
     * Creates a new DurationAttribute. The default duration value is {@link Long#MAX_VALUE} indicating forever.
     * 
     * @param name
     *            the name of the attribute
     */
    protected DurationAttribute(String name) {
        super(name, DEFAULT_DURATION);
    }

    /**
     * Converts the specified duration from the specified timeunit into nanoseconds. Making sure that if the specified
     * duration is {@link #FOREVER}, the duration is not converted, but {@link #FOREVER} is returned.
     * 
     * @param duration
     *            the duration
     * @param unit
     *            the time unit of the duration
     * @return the converted value
     * 
     */
    long convertFrom(long value, TimeUnit unit) {
        if (value == FOREVER) {
            return FOREVER;
        } else {
            return unit.toNanos(value);
        }
    }

    /**
     * Converts the specified duration from nanoseconds into the specified timeunit. Making sure that if the specified
     * duration is {@link #FOREVER}, the duration is not converted, but {@link #FOREVER} is returned.
     * 
     * @param duration
     *            the duration
     * @param unit
     *            the time unit of the duration
     * @return the converted value
     */
    long convertTo(long duration, TimeUnit unit) {
        if (duration == FOREVER) {
            return FOREVER;
        } else {
            return unit.convert(duration, TimeUnit.NANOSECONDS);
        }
    }

    /**
     * Returns the value of this attribute from the specified attribute map in the specified timeunit.
     * 
     * @param attributes
     *            the attribute map to retrieve the value of this attribute from
     * @param unit
     *            the time unit to return the value in
     * @return the value of this attribute
     */
    public long getValue(AttributeMap attributes, TimeUnit unit) {
        return convertTo(attributes.get(this), unit);
    }

    /**
     * Returns the value of this attribute from the specified attribute map in the specified timeunit.
     * 
     * @param attributes
     *            the attribute map to retrieve the value of this attribute from
     * @param unit
     *            the time unit to return the value in
     * @param defaultValue
     *            the default value to return if this attribute is not set in the AttributeMap
     * @return the value of this attribute
     */
    public long getValue(AttributeMap attributes, TimeUnit unit, long defaultValue) {
        long val = attributes.get(this, 0);
        if (val == 0) {
            return defaultValue;
        } else {
            return convertTo(val, unit);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isValid(long value) {
        return value > 0;
    }

    public void set(MutableAttributeMap attributes, Long duration, TimeUnit unit) {
        attributes.put(this, convertFrom(duration, unit));
    }

    /**
     * Returns an immutable AttributeMap containing only this attribute mapping to the specified duration.
     * 
     * @param value
     *            the value to create the singleton from
     * @param unit
     *            the time unit of the value
     * @return an AttributeMap containing only this attribute mapping to the specified value
     */
    public AttributeMap singleton(long value, TimeUnit unit) {
        return super.singleton(convertFrom(value, unit));
    }

    /** {@inheritDoc} */
    @Override
    protected String checkValidFailureMessage(Long value) {
        return " was negative or zero (" + getName() + " = " + value + ")";
    }
}
