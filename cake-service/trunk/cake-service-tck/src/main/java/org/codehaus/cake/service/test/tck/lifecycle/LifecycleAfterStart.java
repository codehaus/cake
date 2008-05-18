package org.codehaus.cake.service.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.service.AfterStart;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
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
        conf.addService(new Started1());
        newContainer();
        prestart();
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

    @Test
    public void register() {
        latch = new CountDownLatch(2);
        conf.addService(new Register());
        conf.addService(new checkRegister());
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

        @AfterStart
        public void start(Integer i) {
            assertTrue(c.isStarted());
            assertEquals(1000, i.intValue());
            latch.countDown();
            
        }
    }

    public class checkRegister {

        @AfterStart
        public void start(Integer i) {
            assertEquals(1000, i.intValue());
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
