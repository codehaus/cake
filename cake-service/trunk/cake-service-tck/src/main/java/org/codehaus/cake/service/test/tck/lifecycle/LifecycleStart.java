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
    int countdown;
    private CountDownLatch latch = new CountDownLatch(0);

    @After
    public void after() {
        assertEquals(0, latch.getCount());
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

    @Test(expected = IllegalStateException.class)
    public void unknownObject() {
        conf.addService(new Started4());
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
        public void start2() {
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

    public class Started4 {
        @Startable
        public void start(Object unknown) {
        }
    }

}
