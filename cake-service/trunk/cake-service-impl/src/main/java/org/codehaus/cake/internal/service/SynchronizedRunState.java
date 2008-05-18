package org.codehaus.cake.internal.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.codehaus.cake.internal.service.spi.ContainerInfo;
import org.codehaus.cake.service.Container;

public final class SynchronizedRunState extends RunState {
    private final LifecycleManager lifecycleManager;
    
    private final Object mutex;

    // Order among values matters
    private final AtomicReference<Throwable> startupException = new AtomicReference<Throwable>();

    private final AtomicInteger state = new AtomicInteger();

    /** CountDownLatch used for signaling termination. */
    private final CountDownLatch terminationLatch = new CountDownLatch(1);

    public SynchronizedRunState(Container container, ContainerInfo info, LifecycleManager lifecycleManager) {
        super(info);
        this.lifecycleManager = lifecycleManager;
        mutex = container;
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return terminationLatch.await(timeout, unit);
    }

    public void checkExceptions() {
        Throwable re = startupException.get();
        if (re != null) {
            throw new IllegalStateException("Cache failed to start previously", re);
        }
    }

    @Override
    protected int get() {
        return state.get();
    }

    public void shutdown(boolean shutdownNow) {
        synchronized (mutex) {
            if (!isAtLeastShutdown()) {
                transitionToShutdown();
            }
        }
    }

    public boolean transitionTo(int state) {
        for (;;) {
            int s = get();
            if (s >= state)
                return false;
            if (this.state.compareAndSet(s, state)) {
                if (state == STARTING) {
                    lifecycleManager.start(this);
                }
                if (state == SHUTDOWN) {
                    lifecycleManager.runShutdown(this);
                    this.state.set(TERMINATED);
                    state = TERMINATED;
                }
                if (state == TERMINATED) {
                    terminationLatch.countDown();
                }
                return true;
            }
        }
    }

    public void trySetStartupException(Throwable cause) {
        startupException.compareAndSet(null, cause);
    }

    public boolean tryStart() {
        synchronized (mutex) {
            checkExceptions();
            if (isStarting()) {
                throw new IllegalStateException(
                        "Cannot invoke this method from a Startable service, should be invoked from an AfterStart method");
            }
            return transitionToStarting();
        }
    }
}
