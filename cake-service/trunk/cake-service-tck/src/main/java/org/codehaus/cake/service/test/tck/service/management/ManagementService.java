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

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicReference;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedAttribute;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.management.ManagedObject;
import org.codehaus.cake.management.ManagedOperation;
import org.codehaus.cake.management.ManagementConfiguration;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.service.test.tck.RequireService;
import org.junit.Test;

@RequireService(value = { Manageable.class })
public class ManagementService extends AbstractTCKTest<Container, ContainerConfiguration> {

    @Test
    public void manageble() throws Exception {
        conf.addService(new MyService());
        withConf(ManagementConfiguration.class).setEnabled(true);
        newContainer();
        String pck = getContainerInterface().getPackage().getName();
        ObjectName on = new ObjectName(pck + ":name=" + c.getName() + ",service=foofoo");
        prestart();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        assertEquals("desc", mbs.getMBeanInfo(on).getDescription());
        MyServiceMXBean mxBean = (MyServiceMXBean) MBeanServerInvocationHandler.newProxyInstance(mbs, on,
                MyServiceMXBean.class, false);
        assertEquals(5, mxBean.getFoo());
        mxBean.op();
        assertEquals(105, mxBean.getFoo());
        mxBean.setFoo(300);
        assertEquals(300, mxBean.getFoo());
    }

    @Test
    public void managebleObject() throws Exception {
        conf.addService(new MyServiceObject());
        withConf(ManagementConfiguration.class).setEnabled(true);
        newContainer();
        String pck = getContainerInterface().getPackage().getName();
        ObjectName on = new ObjectName(pck + ":name=" + c.getName() + ",service=Name");
        prestart();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        assertEquals("Desc", mbs.getMBeanInfo(on).getDescription());
        MyServiceMXBean mxBean = (MyServiceMXBean) MBeanServerInvocationHandler.newProxyInstance(mbs, on,
                MyServiceMXBean.class, false);
        assertEquals(5, mxBean.getFoo());
        mxBean.op();
        assertEquals(105, mxBean.getFoo());
        mxBean.setFoo(300);
        assertEquals(300, mxBean.getFoo());
    }
    @Test
    public void customServer() throws Exception {
        MBeanServer mbs = MBeanServerFactory.createMBeanServer();

        conf.addService(new MyService());
        withConf(ManagementConfiguration.class).setEnabled(true).setMBeanServer(mbs);
        newContainer();
        String pck = getContainerInterface().getPackage().getName();
        ObjectName on = new ObjectName(pck + ":name=" + c.getName() + ",service=foofoo");
        prestart();
        MyServiceMXBean mxBean = (MyServiceMXBean) MBeanServerInvocationHandler.newProxyInstance(mbs, on,
                MyServiceMXBean.class, false);
        assertEquals(5, mxBean.getFoo());
        mxBean.op();
        assertEquals(105, mxBean.getFoo());
        mxBean.setFoo(300);
        assertEquals(300, mxBean.getFoo());

        MBeanServerFactory.releaseMBeanServer(mbs);
    }

    @Test
    public void customDomain() throws Exception {
        conf.addService(new MyService());
        withConf(ManagementConfiguration.class).setEnabled(true).setDomain("org.foo");
        newContainer();
        ObjectName on = new ObjectName("org.foo:name=" + c.getName() + ",service=foofoo");
        prestart();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        MyServiceMXBean mxBean = (MyServiceMXBean) MBeanServerInvocationHandler.newProxyInstance(mbs, on,
                MyServiceMXBean.class, false);
        assertEquals(5, mxBean.getFoo());
        mxBean.op();
        assertEquals(105, mxBean.getFoo());
        mxBean.setFoo(300);
        assertEquals(300, mxBean.getFoo());
    }

    @Test
    public void onlyIfEnabled() throws Exception {
        // withConf(ManagementConfiguration.class).setEnabled(true);
        conf.addService(new Manageable() {
            public void manage(ManagedGroup parent) {
                fail("Should not have been called");
            }
        });
        newContainer();
        prestart();
    }

    @Test(expected = IllegalStateException.class)
    public void lateRegistering() throws Exception {
        final AtomicReference<ManagedGroup> ar = new AtomicReference<ManagedGroup>();
        withConf(ManagementConfiguration.class).setEnabled(true);
        conf.addService(new Manageable() {
            public void manage(ManagedGroup parent) {
                ar.set(parent);
            }
        });
        newContainer();
        prestart();
        ar.get().addChild("foo", "description");// already started
    }

    public static class MyService implements Manageable {
        int i = 5;

        public int getFoo() {
            return i;
        }

        public void manage(ManagedGroup parent) {
            parent.addChild("foofoo", "desc").add(this);
        }

        @ManagedOperation
        public void op() {
            i += 100;
        }

        @ManagedAttribute
        public void setFoo(int i) {
            this.i = i;
        }
    }
    @ManagedObject(defaultValue = "Name", description = "Desc")
    public static class MyServiceObject {
        int i = 5;

        public int getFoo() {
            return i;
        }

        public void manage(ManagedGroup parent) {
            parent.addChild("foofoo", "desc").add(this);
        }

        @ManagedOperation
        public void op() {
            i += 100;
        }

        @ManagedAttribute
        public void setFoo(int i) {
            this.i = i;
        }
    }
    public interface MyServiceMXBean {
        int getFoo();

        void op();

        void setFoo(int i);
    }
}
