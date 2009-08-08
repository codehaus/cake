package org.codehaus.cake.service.test.tck.lifecycle2;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.RunAfter;
import org.codehaus.cake.service.Container.State;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.test.util.verifier.AssertableState;
import org.junit.Ignore;
import org.junit.Test;

public class RunAfterSimpleArguments extends AbstractTCKTest<Container, ContainerConfiguration> {

    @Test
    public void noArg() {
        DisposableNoArg da = addAssert(new DisposableNoArg());
        conf.addService(da);
        newContainer();
        assertState(State.INITIALIZED);
        prestart();
        assertEquals(State.RUNNING, c.getState());
        c.shutdown();
        assertTrue(c.getState().isShutdown());
        awaitTermination();
        assertState(State.TERMINATED);
    }

    public static class DisposableNoArg extends AssertableState {

        @RunAfter(State.TERMINATED)
        public void tert34() {
            advance("Shutdown->");
        }

        @RunAfter(State.INITIALIZED)
        public void asdasd() {
            advance("->Init");
        }

        @RunAfter(State.RUNNING)
        public void asds2asd() {
            advance("Starting->Running");
        }

        @RunAfter(State.STARTING)
        public void zzzz() {
            advance("->Starting");
        }

        @RunAfter(State.SHUTDOWN)
        public void aaa() {
            advance("Running->Shutdown");
        }
    }
    public static class MultipleNoArg extends AssertableState {

        @RunAfter(State.RUNNING)
        public void zsds2asd() {
            advance("Starting-[1/3]>Running");
        }
        @RunAfter(State.TERMINATED)
        public void tert34() {
            advance("Shutdown->");
        }

        @RunAfter(State.INITIALIZED)
        public void asdasd() {
            advance("->Init");
        }

        @RunAfter(State.RUNNING)
        public void asds2asd() {
            advance("Starting-[2/3]>Running");
        }

        @RunAfter(State.STARTING)
        public void zzzz() {
            advance("->Starting");
        }

        @RunAfter(State.SHUTDOWN)
        public void aaa() {
            advance("Running->Shutdown");
        }
        
        @RunAfter(State.RUNNING)
        public void qsds2asd() {
            advance("Starting-[3/3]>Running");
        }
    }
    @Test @Ignore
    public void stateAsArgument() {
        DisposableStateArg da = new DisposableStateArg();
        conf.addService(da);
        newContainer();
        assertState(State.INITIALIZED);
        prestart();
        assertEquals(State.RUNNING, c.getState());
        c.shutdown();
        assertTrue(c.getState().isShutdown());
        awaitTermination();
        assertState(State.TERMINATED);
        da.state.assertEndState();
    }

    public static class DisposableStateArg {
        final AssertableState state = new AssertableState();

        @RunAfter(State.TERMINATED)
        public void tert34(State st) {
            assertSame(State.TERMINATED, st);
            state.advance("Shutdown->");
        }

        @RunAfter(State.INITIALIZED)
        public void asdasd(State ssss) {
            assertSame(State.INITIALIZED, ssss);
            state.advance("->Init");
        }

        @RunAfter(State.RUNNING)
        public void asds2asd(State s) {
            assertSame(State.RUNNING, s);
            state.advance("Starting->Running");
        }

        @RunAfter(State.STARTING)
        public void zzzz(State s) {
            assertSame(State.RUNNING, s);
            state.advance("->Starting");
        }

        @RunAfter(State.SHUTDOWN)
        public void aaa(State s) {
            assertSame(State.SHUTDOWN, s);
            state.advance("Running->Shutdown");
        }
    }
}
