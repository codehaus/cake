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
package org.codehaus.cake.internal.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.codehaus.cake.internal.service.spi.ContainerInfo;
import org.codehaus.cake.service.Container;

public final class SynchronizedRunState extends RunState {
    private final Object mutex;

    private final AtomicReference<Throwable> startupException = new AtomicReference<Throwable>();

    private final AtomicInteger state = new AtomicInteger();

    /** CountDownLatch used for signaling termination. */
    private final CountDownLatch terminationLatch = new CountDownLatch(1);

    public SynchronizedRunState(Container container, ContainerInfo info, LifecycleManager lifecycleManager) {
        super(info, lifecycleManager);
        mutex = container;
    }

    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return terminationLatch.await(timeout, unit);
    }

    void checkExceptions() {
        Throwable re = startupException.get();
        if (re != null) {
            throw new IllegalStateException("Cache failed to start previously", re);
        }
    }

    @Override
    protected int get() {
        return state.get();
    }

    void shutdown(boolean shutdownNow) {
        synchronized (mutex) {
            if (!isAtLeastShutdown()) {
                transitionTo(SHUTDOWN);
            }
        }
    }

    boolean transitionTo(int state) {
        for (;;) {
            int s = get();
            if (s >= state)
                return false;
            if (this.state.compareAndSet(s, state)) {
                if (state == STARTING) {
                    lifecycleManager.start();
                }
                if (state == SHUTDOWN) {
                    lifecycleManager.runShutdown();
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

    void trySetStartupException(Throwable cause) {
        startupException.compareAndSet(null, cause);
    }

    void tryStart() {
        synchronized (mutex) {
            checkExceptions();
            if (isStarting()) {
                throw new IllegalStateException(
                        "Cannot invoke this method from a @Startable method, should be invoked from an @AfterStart method");
            }
            transitionTo(STARTING);
        }
    }
}
