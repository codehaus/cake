package org.codehaus.cake.service.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.Stoppable;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.After;
import org.junit.Test;

public class LifecycleStoppable extends AbstractTCKTest<Container, ContainerConfiguration> {
    private CountDownLatch latch = new CountDownLatch(0);

    @After
    public void after() {
        assertEquals(0, latch.getCount());
    }

    @Test
    public void noArg() {
        latch = new CountDownLatch(1);
        conf.addService(new Stoppable1());
        newContainer();
        assertFalse(c.isStarted());
        assertFalse(c.isShutdown());
        assertFalse(c.isTerminated());
        prestart();
        assertTrue(c.isStarted());
        assertFalse(c.isShutdown());
        assertFalse(c.isTerminated());
        c.shutdown();
        assertTrue(c.isStarted());
        assertTrue(c.isShutdown());
        awaitTermination();
        assertTrue(c.isTerminated());
    }

    public class Stoppable1 {
        @Stoppable
        public void stop() {
            latch.countDown();
        }
    }

}
