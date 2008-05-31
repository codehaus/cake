package org.codehaus.cake.internal.service;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.internal.service.spi.ContainerInfo;

public class UnsynchronizedRunState extends RunState {

    private int state;

    private Throwable startupException;

    public UnsynchronizedRunState(ContainerInfo info, LifecycleManager lifecycleManager) {
        super(info, lifecycleManager);
    }

    void tryStart() {
        checkExceptions();
        if (isStarting()) {
            throw new IllegalStateException(
                    "Cannot invoke this method from a @Startable method, should be invoked from an @AfterStart method");
        }
        if (state < STARTING) {
            state = STARTING;
            lifecycleManager.start();
        }
    }

    boolean transitionTo(int state) {
        int s = get();
        if (s >= state)
            return false;
        this.state = state;
        if (state == RUNNING) {
            // startedPhase.run(this);
        } else if (state == TERMINATED) {
            
        }
        return true;
    }

    void shutdown(boolean shutdownNow) {
        if (!isAtLeastShutdown()) {
            lifecycleManager.runShutdown();
            transitionTo(SHUTDOWN);
            transitionTo(TERMINATED);
        }
    }

    @Override
    void trySetStartupException(Throwable cause) {
        if (startupException == null) {
            startupException = cause;
        }
    }

    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return isTerminated();
    }

    @Override
    void checkExceptions() {
        Throwable re = startupException;
        if (re != null) {
            throw new IllegalStateException(containerType + " failed while starting previously", re);
        }
    }

    @Override
    protected int get() {
        return state;
    }

    Throwable getStartupException() {
        return startupException;
    }
}
