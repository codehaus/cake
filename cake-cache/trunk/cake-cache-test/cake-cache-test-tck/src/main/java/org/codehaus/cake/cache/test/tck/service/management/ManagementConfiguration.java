/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.management;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.codehaus.cake.cache.CacheMXBean;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.management.ManagedVisitor;
import org.codehaus.cake.service.test.tck.RequireService;
import org.junit.Test;

@RequireService({Manageable.class})
public class ManagementConfiguration extends AbstractCacheTCKTest {

    @Test
    public void domain() throws Exception {
        MBeanServer mbs = MBeanServerFactory.createMBeanServer();
        conf.setName("managementtest");
        conf.withManagement().setEnabled(true).setMBeanServer(mbs).setDomain("com.acme");
        init();
        ObjectName on = new ObjectName("com.acme:name=managementtest,service=" + CacheMXBean.MANAGED_SERVICE_NAME);
        prestart();
        CacheMXBean mxBean = (CacheMXBean) MBeanServerInvocationHandler.newProxyInstance(mbs, on, CacheMXBean.class,
                false);
        assertEquals(0, mxBean.getSize());

        MBeanServerFactory.releaseMBeanServer(mbs);
    }

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
        conf.withManagement().setEnabled(true).setMBeanServer(mbs).setRegistrant(new ManagedVisitor() {
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
        init();
        prestart();
        assertTrue(wasCalled);
        assertEquals(count, mbs.getMBeanCount().intValue());// nothing registred
        
        MBeanServerFactory.releaseMBeanServer(mbs);
    }

    @Test(expected = RuntimeException.class)
    public void registrantFailed() throws Exception {
        conf.withManagement().setEnabled(true).setRegistrant(new ManagedVisitor() {
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
        init();
        try {
            prestart();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof JMException);
            throw e;
        }
    }
}
