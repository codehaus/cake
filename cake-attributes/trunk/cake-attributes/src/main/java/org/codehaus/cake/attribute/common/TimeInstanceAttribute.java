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

/**
 * An attribute mapping to an instance in time relative to the standard base time known as "the epoch", namely January
 * 1, 1970, 00:00:00 GMT. The unit of this attribute is milliseconds
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java,v 1.2 2005/04/27 15:49:16 kasper Exp $
 */
public abstract class TimeInstanceAttribute extends LongAttribute {
    /** serialVersionUID. */
    private static final long serialVersionUID = -2353351535602223603L;

    /**
     * Creates a new SizeAttribute.
     * 
     * @param name
     *            the name of the attribute
     */
    protected TimeInstanceAttribute(String name) {
        super(name, 0);
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
