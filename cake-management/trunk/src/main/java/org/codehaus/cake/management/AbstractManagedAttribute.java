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
package org.codehaus.cake.management;

import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.ReflectionException;

/**
 * An AbstractAttribute is a wrapper for a JMX attribute.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
abstract class AbstractManagedAttribute {
    /** The description of the operation. */
    private final String description;

    /** The name of the operation. */
    private final String name;

    /**
     * Creates a new AbstractManagedAttribute with the specified name and description.
     * 
     * @param name
     *            the name of the attribute
     * @param description
     *            the description of the attribute
     * @throws NullPointerException
     *             if the specified name or description is <code>null</code>
     */
    AbstractManagedAttribute(final String name, final String description) {
        if (name == null) {
            throw new NullPointerException("name is null");
        } else if (description == null) {
            throw new NullPointerException("description is null");
        }
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the description of this attribute.
     * 
     * @return the description of this attribute
     */
    String getDescription() {
        return description;
    }

    /**
     * Returns the MBeanAttributeInfo for this attribute.
     * 
     * @return the MBeanAttributeInfo for this attribute
     * @throws IntrospectionException
     *             could not optain the information for this attribute
     */
    abstract MBeanAttributeInfo getInfo() throws IntrospectionException;

    /**
     * Returns the name of this attribute.
     * 
     * @return the name of this attribute
     */
    String getName() {
        return name;
    }

    /**
     * Returns the value of the attribute.
     * 
     * @return the value of the attribute
     * @throws ReflectionException
     *             could not get the value of the attribute
     */
    abstract Object getValue() throws ReflectionException;

    /**
     * Sets the value of the attribute to specified object.
     * 
     * @param o
     *            the value that the attribute should be set to
     * @throws ReflectionException
     *             could not set the attribute
     */
    abstract void setValue(Object o) throws ReflectionException;
}
