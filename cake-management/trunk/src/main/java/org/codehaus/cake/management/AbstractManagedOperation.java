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
package org.codehaus.cake.management;

import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanOperationInfo;
import javax.management.ReflectionException;

/**
 * An AbstractOperation corresponds to a JMX operation.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
abstract class AbstractManagedOperation {

    /** The description of the operation. */
    private final String description;

    /** The name of the operation. */
    private final String name;

    /**
     * Creates a new AbstractManagedOperation with the specified name and description.
     * 
     * @param name
     *            the name of the operation
     * @param description
     *            the description of the operation
     * @throws NullPointerException
     *             if the specified name or description is <code>null</code>
     */
    AbstractManagedOperation(final String name, final String description) {
        if (name == null) {
            throw new NullPointerException("name is null");
        } else if (description == null) {
            throw new NullPointerException("description is null");
        }
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the description of this operation.
     * 
     * @return the description of this operation
     */
    String getDescription() {
        return description;
    }

    /**
     * Returns the MBeanOperationInfo for this operation.
     * 
     * @return the MBeanOperationInfo for this operation
     * @throws IntrospectionException
     *             could not optain the information for this operation
     */
    abstract MBeanOperationInfo getInfo() throws IntrospectionException;

    /**
     * Returns the name of this operation.
     * 
     * @return the name of this operation
     */
    String getName() {
        return name;
    }

    /**
     * Invoke the operation with specified arguments.
     * 
     * @param arguments
     *            the arguments used for invoking the operation
     * @return the result of the invocation
     * @throws MBeanException
     *             could not invoke the operation
     * @throws ReflectionException
     *             could not invoke the operation
     */
    abstract Object invoke(Object... arguments) throws MBeanException, ReflectionException;
}
