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
package org.codehaus.cake.service.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceManager;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.annotation.AfterStart;
import org.codehaus.cake.service.annotation.Startable;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.After;
import org.junit.Test;

public class LifecycleAfterStart extends AbstractTCKTest<Container, ContainerConfiguration> {
    private CountDownLatch latch = new CountDownLatch(0);

    @After
    public void after() {
    // assertEquals(0, latch.getCount());
    }

    @Test
    public void noArg() {
        latch = new CountDownLatch(1);
        conf.addServiceToLifecycle(new Started1());
        newContainer();
        prestart();
    }

    @Test
    public void twoMethod() {
        latch = new CountDownLatch(2);
        conf.addServiceToLifecycle(new Started2());
        newContainer();
        prestart();
    }

    @Test
    public void twoMethodWithArgs() {
        latch = new CountDownLatch(2);
        conf.addServiceToLifecycle(new Started3());
        newContainer();
        prestart();
    }

    @Test
    public void register() {
        latch = new CountDownLatch(2);
        conf.addServiceToLifecycle(new Register());
        conf.addServiceToLifecycle(new CheckRegister());
        newContainer();
        prestart();
    }

    @Test(expected = IllegalStateException.class)
    public void notAvailable() {
        conf.addServiceToLifecycle(new AfterStartNotAvailable());
        newContainer();
        prestart();
    }

    public class Started1 {
        @AfterStart
        public void start() {
            latch.countDown();
        }
    }

    public class Started2 {
        @AfterStart
        public void start1() {
            latch.countDown();
        }

        @AfterStart
        public void start2(Container container) {
            assertNotNull(container);
            latch.countDown();
        }
    }

    public class Started3 {
        @AfterStart
        public void start(ContainerConfiguration configuration) {
            assertSame(conf, configuration);
            latch.countDown();
        }

        @AfterStart
        public void start(Container container) {
            assertSame(c, container);
            latch.countDown();
        }
    }

    public class Register {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 1000);
            assertFalse(c.isStarted());
        }
    }

    public class AfterStartNotAvailable {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 1000);
            assertFalse(c.isStarted());
        }

        @AfterStart
        public void start(Integer i) {}
    }

    public class CheckRegister {

        @AfterStart
        public void start(ServiceManager i) {
            assertEquals(1000, i.getService(Integer.class).intValue());
            latch.countDown();
        }
    }

    public class WithRegisteredService {
        @AfterStart
        public void start(ContainerConfiguration configuration) {
            assertSame(conf, configuration);
            latch.countDown();
        }

        @AfterStart
        public void start(Container container) {
            assertSame(c, container);
            latch.countDown();
        }
    }
}
