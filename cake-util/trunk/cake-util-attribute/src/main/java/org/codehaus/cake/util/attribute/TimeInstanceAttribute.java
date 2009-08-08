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


/**
 * An attribute mapping to an instance in time relative to the standard base time known as "the epoch", namely January
 * 1, 1970, 00:00:00 GMT. The unit of time is milliseconds.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
//TODO Rename to EpochTimeAttribute???????? or EpochInstanceAttribute
//Kig i den time jsr xxx
public abstract class TimeInstanceAttribute extends LongAttribute {
    /** serialVersionUID. */
    private static final long serialVersionUID = 1;

    /**
     * Creates a new TimeInstanceAttribute.
     * 
     * @param name
     *            the name of the attribute
     */
    protected TimeInstanceAttribute(String name) {
        super(name, 0);
    }

    /** {@inheritDoc} */
    @Override
    protected String checkValidFailureMessage(Long value) {
        return " was negative (" + getName() + " = " + value + ")";
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean isValid(long time) {
        return time >= 0;
    }
}
