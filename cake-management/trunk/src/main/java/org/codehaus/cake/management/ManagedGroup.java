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

import java.util.Collection;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * A ManagedGroup is passive collection of attributes pretty similar to a MBean. Easy to register as
 * MBean.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public interface ManagedGroup {

    /**
     * Adds an object to the group. The attributes and methods of this object will be added to the
     * aggregated methods and operations of this group.
     * 
     * @param o
     *            the object to add
     * @return this group
     * @throws NullPointerException
     *             if the specified object is <tt>null</tt>
     * @throws IllegalStateException
     *             if this group has already been register with a {@link MBeanServer}
     * @throws IllegalArgumentException
     *             if the object has already been registered, if it contains no methods or
     *             operations, or if operations or methods with the same name has already been
     *             registered
     */
    ManagedGroup add(Object o);

    /**
     * Adds a child group.
     * 
     * @param name
     *            the name of the group. Cannot be the empty string
     * @param description
     *            the description of the group
     * @return this group
     * @throws NullPointerException
     *             if the specified name or description is null
     * @throws IllegalArgumentException
     *             if a group with the specified name has already been added or the specified name
     *             is the empty string
     */
    ManagedGroup addChild(String name, String description);

    /**
     * Returns all this groups child groups.
     * 
     * @return all this groups child groups
     */
    Collection<ManagedGroup> getChildren();

    /**
     * Returns the child with the specified name.
     * 
     * @param name
     * @return the child group with the specified name, or null if no child group exists with the specified name
     */
    ManagedGroup getChild(String name);

    /**
     * Returns the description of this group.
     * 
     * @return the description of this group.
     */
    String getDescription();

    /**
     * Returns the name of this group.
     * 
     * @return the name of this group.
     */
    String getName();

    /**
     * @return the objectname this group is registered under, or <code>null</code> if it has not yet
     *         been registered.
     */
    ObjectName getObjectName();

    /**
     * Returns the objects that are registered in this group.
     * 
     * @return the objects that are registered in this group
     */
    Collection<?> getObjects();

    /**
     * Returns the parent of this group or <code>null</code> if this group does not have a parent.
     * 
     * @return the parent of this group or <code>null</code> if this group does not have a parent
     */
    ManagedGroup getParent();

    /**
     * @return the MBeanServer this group is registered with or <tt>null</tt> if this group is not
     *         registered.
     */
    MBeanServer getServer();

    /**
     * Returns whether or not this group has been registered with a {@link MBeanServer}.
     * 
     * @return whether or not this group has been registered with a {@link MBeanServer}
     */
    boolean isRegistered();

    /**
     * Registers this group with the specified server under the specified object name.
     * 
     * @param server
     *            the mbean server where this group should be registered
     * @param objectName
     *            the objectname of this group
     * @throws JMException
     *             if the mbean could not be properly registered
     * @throws IllegalStateException
     *             if this group has already been registered with a {@link MBeanServer}
     */
    void register(MBeanServer server, ObjectName objectName) throws JMException;

    /**
     * Remove this group from its parent. If this group does not have a parent, calls to this method
     * is ignored.
     */
    void remove();

    /**
     * Unregisters this group from the registered {@link MBeanServer} server. Any child groups will
     * not be unregistered. If this group it not registered with a {@link MBeanServer}, calls to
     * this method is ignored.
     * 
     * @throws JMException
     *             if the mbean could not be properly unregistered
     */
    void unregister() throws JMException;
}
