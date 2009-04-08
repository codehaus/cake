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
package org.codehaus.cake.service.test.tck.service.management;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.management.ManagedVisitor;
import org.codehaus.cake.management.ManagementConfiguration;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.service.test.tck.RequireService;
import org.junit.Test;

@RequireService(value = { Manageable.class })
public class ManagementServiceRegistrant extends AbstractTCKTest<Container, ContainerConfiguration> {

    boolean wasCalled;

    @Test
    public void registrant() throws Exception {
        MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        int count = mbs.getMBeanCount();
        conf.addService(new Manageable() {
            public void manage(ManagedGroup parent) {
                parent.addChild("foo1", "foodesc").addChild("foo2", "foodesc2");
            }
        });
        conf.setName("managementtest");
        withConf(ManagementConfiguration.class).setEnabled(true).setMBeanServer(mbs).setRegistrant(
                new ManagedVisitor() {
                    public Object traverse(Object node) throws JMException {
                        assertTrue(node instanceof ManagedGroup);
                        ManagedGroup mg = (ManagedGroup) node;
                        for (ManagedGroup m : mg.getChildren()) {
                            if (m.getName().equals("foo1")) {
                                assertEquals("foodesc", m.getDescription());
                                assertEquals(1, m.getChildren().size());
                                assertEquals("foo2", m.getChildren().iterator().next().getName());
                                assertEquals("foodesc2", m.getChildren().iterator().next().getDescription());
                                wasCalled = true;
                            }
                        }
                        return Void.TYPE;
                    }

                    public void visitManagedGroup(ManagedGroup mg) throws JMException {
                        throw new AssertionError("Should not have been called");
                    }

                    public void visitManagedObject(Object o) throws JMException {
                        throw new AssertionError("Should not have been called");
                    }
                });
        newContainer();
        prestart();
        assertTrue(wasCalled);
        assertEquals(count, mbs.getMBeanCount().intValue());// nothing registred
    }

    @Test(expected = RuntimeException.class)
    public void registrantFailed() throws Exception {
        withConf(ManagementConfiguration.class).setEnabled(true).setRegistrant(new ManagedVisitor() {
            public Object traverse(Object node) throws JMException {
                throw new JMException();
            }

            public void visitManagedGroup(ManagedGroup mg) throws JMException {
                throw new AssertionError("Should not have been called");
            }

            public void visitManagedObject(Object o) throws JMException {
                throw new AssertionError("Should not have been called");
            }
        });
        newContainer();
        try {
            prestart();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof JMException);
            throw e;
        }
    }
}
