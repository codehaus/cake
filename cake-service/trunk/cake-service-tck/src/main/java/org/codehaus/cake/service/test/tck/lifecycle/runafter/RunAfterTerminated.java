package org.codehaus.cake.service.test.tck.lifecycle.runafter;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.RunAfter;
import org.codehaus.cake.service.Container.State;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.After;
import org.junit.Test;

public class RunAfterTerminated extends AbstractTCKTest<Container, ContainerConfiguration> {

    private CountDownLatch latch = new CountDownLatch(0);

    @After
    public void after() {
        assertEquals(0, latch.getCount());
    }

    public void assertState(State state) {
        assertSame("expected state " + state + " but was " + c.getState(), c.getState(),state);
    }
    @Test
    public void noArg() {
        latch = new CountDownLatch(1);
        conf.addService(new Disposable1());
        newContainer();
        assertState(State.INITIALIZED);
        prestart();
        assertEquals(State.RUNNING, c.getState());
        c.shutdown();
        assertTrue(c.getState().isShutdown());
        awaitTermination();
        assertState(State.TERMINATED);
    }

    public class Disposable1 {
        @RunAfter(State.TERMINATED)
        public void dispose() {
            latch.countDown();
        }
    }
}
