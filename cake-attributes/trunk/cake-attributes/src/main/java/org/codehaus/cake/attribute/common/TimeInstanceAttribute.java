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

import org.codehaus.cake.attribute.LongAttribute;

public abstract class TimeInstanceAttribute extends LongAttribute {
    /** The default value of this attribute. */
    static final long DEFAULT_VALUE = 0;

    /** serialVersionUID. */
    private static final long serialVersionUID = -2353351535602223603L;

    /**
     * Creates a new SizeAttribute.
     * 
     * @param name
     *            the name of the attribute
     */
    public TimeInstanceAttribute(String name) {
        super(name, DEFAULT_VALUE);
    }

    /** {@inheritDoc} */
    @Override
    public void checkValid(long time) {
        if (time < 0) {
            throw new IllegalArgumentException(getName() + " was negative (" + getName() + " = " + time + ")");
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isValid(long time) {
        return time >= 0;
    }
}
