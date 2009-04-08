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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Various management utility functions.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class Managements {

    /** Cannot instantiate. */
    // /CLOVER:OFF
    private Managements() {}

    // /CLOVER:ON

    /**
     * Copies the specified array.
     * @param original the array to copy
     * @return a copy of the specified array
     */
    static <T> T[] copyOf(T[] original) {
        return (T[]) copyOf(original, original.length, original.getClass());
    }

    @SuppressWarnings("cast")
    static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        T[] copy = (Object) newType == (Object) Object[].class ? (T[]) new Object[newLength] : (T[]) Array.newInstance(
                newType.getComponentType(), newLength);
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    /**
     * A wrapper class that exposes only the ManagedGroup methods of a ManagedGroup implementation.
     */
    public static ManagedGroup delegatedManagedGroup(final ManagedGroup group) {
        if (group == null) {
            throw new NullPointerException("group is null");
        }
        return new ManagedGroup() {

            public ManagedGroup add(Object o) {
                group.add(o);
                return this;
            }

            public ManagedGroup addChild(String name, String description) {
                return group.addChild(name, description);
            }

            public Collection<ManagedGroup> getChildren() {
                return group.getChildren();
            }

            public String getDescription() {
                return group.getDescription();
            }

            public String getName() {
                return group.getName();
            }

            public ObjectName getObjectName() {
                return group.getObjectName();
            }

            public Collection<?> getObjects() {
                return group.getObjects();
            }

            public ManagedGroup getParent() {
                return group.getParent();
            }

            public MBeanServer getServer() {
                return group.getServer();
            }

            public boolean isRegistered() {
                return group.isRegistered();
            }

            public void register(MBeanServer server, ObjectName objectName) throws JMException {
                group.register(server, objectName);
            }

            public void remove() {
                group.remove();
            }

            public String toString() {
                return group.toString();
            }

            public void unregister() throws JMException {
                group.unregister();
            }
            public ManagedGroup getChild(String name) {
                return group.getChild(name);
            }
        };
    }

    public static ManagedVisitor hierarchicalRegistrant(MBeanServer server, String domain, String... levels) {
        return new HierarchicalRegistrant(server, domain, levels);
    }

    /**
     * Returns a ManagedVisitor that will unregister a ManagedGroup and all its children. The map returned from the
     * {@link ManagedVisitor#traverse(Object)} method will contain a mapping from any group that failed to unregister to
     * the cause of the failure. If all groups where succesfully unregistered the returned map is empty.
     * 
     * @return a ManagedVisitor that will unregister a ManagedGroup and all its children.
     */
    public static ManagedVisitor<Map<ManagedGroup, Exception>> unregister() {
        return new UnregisterAll();
    }

    static class HierarchicalRegistrant implements ManagedVisitor {
        /** The base domain to register at. */
        private final String domain;

        private final String[] levels;

        /** The MBeanServer to register with. */
        private final MBeanServer server;

        HierarchicalRegistrant(MBeanServer server, String domain, String... levels) {
            if (server == null) {
                throw new NullPointerException("server is null");
            } else if (domain == null) {
                throw new NullPointerException("domain is null");
            } else if (levels == null) {
                throw new NullPointerException("levels is null");
            }
            for (String level : levels) {
                if (level == null) {
                    throw new NullPointerException("levels contained a null");
                }
            }
            this.server = server;
            this.domain = domain;
            this.levels = copyOf(levels);
        }

        // /CLOVER:ON
        /** {@inheritDoc} */
        public Object traverse(Object node) throws JMException {
            visitManagedGroup((ManagedGroup) node);
            return Void.TYPE;
        }

        /** {@inheritDoc} */
        public void visitManagedGroup(ManagedGroup mg) throws JMException {
            String prefix = domain + ":" + levels[0] + "=" + mg.getName();
            visitManagedGroup(mg, prefix, 0);
        }

        private void visitManagedGroup(ManagedGroup mg, String prefix, int level) throws JMException {
            ObjectName on = new ObjectName(prefix);
            if (mg.getObjects().size() > 0) {
                mg.register(server, on);
            }
            for (ManagedGroup group : mg.getChildren()) {
                String p = prefix + "," + levels[level + 1] + "=" + group.getName();
                visitManagedGroup(group, p, level + 1);
            }
        }

        // /CLOVER:OFF
        /** {@inheritDoc} */
        public void visitManagedObject(Object o) throws JMException {}
        // /CLOVER:ON
    }

    static class UnregisterAll implements ManagedVisitor<Map<ManagedGroup, Exception>> {
        private void depthFirstVisit(ManagedGroup group, Map<ManagedGroup, Exception> map) {
            for (ManagedGroup child : group.getChildren()) {
                depthFirstVisit(child, map);
            }
            try {
                visitManagedGroup(group);
            } catch (Exception e) {
                map.put(group, e);
            }
        }

        /** {@inheritDoc} */
        public Map<ManagedGroup, Exception> traverse(Object node) throws JMException {
            Map<ManagedGroup, Exception> map = new HashMap<ManagedGroup, Exception>();
            ManagedGroup group = (ManagedGroup) node;
            depthFirstVisit(group, map);
            return map;
        }

        /** {@inheritDoc} */
        public void visitManagedGroup(ManagedGroup mg) throws JMException {
            mg.unregister();
        }

        /** {@inheritDoc} */
        // /CLOVER:OFF
        public void visitManagedObject(Object o) throws JMException {}
        // /CLOVER:ON
    }
}
