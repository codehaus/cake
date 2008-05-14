package org.codehaus.cake.internal.service;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.internal.service.spi.ContainerInfo;

public class UnsynchronizedRunState extends RunState {
    // private final SynchronizedRunState runState;

    private final LifecycleManager lifecycleManager;

    private int state;

    private Throwable startupException;

    public UnsynchronizedRunState(ContainerInfo info, LifecycleManager lifecycleManager) {
        super(info);
        this.lifecycleManager = lifecycleManager;
    }

    public boolean tryStart() {
        checkExceptions();
        if (isStarting()) {
            throw new IllegalStateException(
                    "Cannot invoke this method from CacheLifecycle.start(Map services), should be invoked from CacheLifecycle.started(Cache c)");
        }
        return transitionToStarting();
    }

    public boolean transitionTo(int state) {
        int s = get();
        if (s >= state)
            return false;
        this.state = state;
        if (state == STARTING) {
            lifecycleManager.start(this);
        } else if (state == RUNNING) {
            // startedPhase.run(this);
        } else if (state == TERMINATED) {

        }
        return true;
    }

    public void shutdown(boolean shutdownNow) {
        if (!isAtLeastShutdown()) {
            lifecycleManager.runShutdown(this);
            transitionToShutdown();
            transitionToTerminated();
        }
    }

    @Override
    public void trySetStartupException(Throwable cause) {
        if (startupException == null) {
            startupException = cause;
            // this.state=SHUTDOWN;
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return isTerminated();
    }

    @Override
    public void checkExceptions() {
        Throwable re = startupException;
        if (re != null) {
            throw new IllegalStateException(containerType + " failed while starting previously", re);
        }
    }

    @Override
    protected int get() {
        return state;
    }
}
