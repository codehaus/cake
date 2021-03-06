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
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.After;
import org.junit.Test;

public class LifecycleStart extends AbstractTCKTest<Container, ContainerConfiguration> {
    private CountDownLatch latch = new CountDownLatch(0);

    @After
    public void after() {
        assertEquals(0, latch.getCount());
    }

    @Test
    public void noArg() {
        assertFalse(c.isStarted());
        assertFalse(c.isShutdown());
        assertFalse(c.isTerminated());
        latch = new CountDownLatch(1);
        conf.addService(new Started1());
        newContainer();
        assertFalse(c.isStarted());
        assertFalse(c.isShutdown());
        assertFalse(c.isTerminated());
        prestart();
        assertTrue(c.isStarted());
        assertFalse(c.isShutdown());
        assertFalse(c.isTerminated());
    }

    @Test
    public void twoMethod() {
        latch = new CountDownLatch(2);
        conf.addService(new Started2());
        newContainer();
        prestart();
    }

    @Test
    public void twoMethodWithArgs() {
        latch = new CountDownLatch(2);
        conf.addService(new Started3());
        newContainer();
        prestart();
    }

    public class Started1 {
        @Startable
        public void start() {
            latch.countDown();
        }
    }

    public class Started2 {
        @Startable
        public void start1() {
            latch.countDown();
        }

        @Startable
        public void start2(ServiceRegistrant registrant) {
            assertNotNull(registrant);
            latch.countDown();
        }
    }

    public class Started3 {
        @Startable
        public void start(ContainerConfiguration configuration) {
            assertSame(conf, configuration);
            latch.countDown();
        }

        @Startable
        public void start(ServiceRegistrant registrant) {
            assertNotNull(registrant);
            latch.countDown();
        }
    }
}
