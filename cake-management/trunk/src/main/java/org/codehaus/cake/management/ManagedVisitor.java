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

import javax.management.JMException;

/**
 * Interface realizing a visitor pattern for {@link ManagedGroup}. The visitor should visit the group, its children,
 * and all instantiated components within a group.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public interface ManagedVisitor<T> {

    /**
     * Entry point for the ManagedVisitor traversal. The given node is the first object, that is asked for acceptance.
     * Only objects of type {@link ManagedGroup}, or {@link Manageable} are valid.
     * 
     * @param node
     *            the start node of the traversal.
     * @return a visitor-specific value.
     * @throws IllegalArgumentException
     *             in case of an argument of invalid type.
     * @throws JMException
     *             an exception occured while visiting the node
     */
    T traverse(Object node) throws JMException;

    /**
     * Visit a {@link ManagedGroup} that has to accept the visitor.
     * 
     * @param mg
     *            the managed group to visit
     * @throws JMException
     *             an exception occured while visiting the managed group
     */
    void visitManagedGroup(ManagedGroup mg) throws JMException;

    /**
     * @param o
     *            the managed object to visit
     * @throws JMException
     *             an exception occured while visiting the managed object
     */
    void visitManagedObject(Object o) throws JMException;
}
