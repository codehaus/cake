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
package org.codehaus.cake.attribute.common;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.WithAttributes;

/**
 * An attribute mapping to an amount of time. The unit of time is nanoseconds.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
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
     * Creates a new DurationAttribute.
     * 
     * @param name
     *            the name of the attribute
     */
    protected DurationAttribute(String name) {
        super(name, DEFAULT_DURATION);
    }

    long convertFrom(long value, TimeUnit unit) {
        if (value == FOREVER) {
            return FOREVER;
        } else {
            return unit.toNanos(value);
        }
    }

    long convertTo(long value, TimeUnit unit) {
        if (value == FOREVER) {
            return FOREVER;
        } else {
            return unit.convert(value, TimeUnit.NANOSECONDS);
        }
    }

    /**
     * Analogous to {@link #getValue(AttributeMap)} except taking a parameter indicating what time unit the value should
     * be returned in.
     * 
     * @param attributes
     *            the attribute map to retrieve the value of this attribute from
     * @param unit
     *            the time unit to return the value in
     * @return the value of this attribute
     */
    public long getValue(WithAttributes attributes, TimeUnit unit) {
        return convertTo(attributes.getAttributes().get(this), unit);
    }

    /**
     * Analogous to {@link LongAttribute#getValue(AttributeMap)} except taking a parameter indicating what time unit the
     * value should be returned in.
     * 
     * @param attributes
     *            the attribute map to retrieve the value of this attribute from
     * @param unit
     *            the time unit to return the value in
     * @return the value of this attribute
     */
    public long getValue(WithAttributes attributes, TimeUnit unit, long defaultValue) {
        long val = attributes.getAttributes().get(this, 0);
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

    /** {@inheritDoc} */
    @Override
    public final void checkValid(long value) {
        if (value<=0) {
            throw new IllegalArgumentException("Duration must be greater then 0, was " + value);
        }
    }
    public void set(AttributeMap attributes, Long duration, TimeUnit unit) {
        set(attributes, duration.longValue(), unit);
    }

    public void set(AttributeMap attributes, long duration, TimeUnit unit) {
        set(attributes, convertFrom(duration, unit));
    }

    /**
     * Returns an immutable AttributeMap containing only this attribute mapping to the specified value.
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
}
