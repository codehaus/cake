/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.internal.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.Container.State;

public final class SynchronizedRunState extends RunState {
    private final Object mutex;
    private final AtomicInteger state = new AtomicInteger();

    /** CountDownLatch used for signaling termination. */
    private final Map<State, CountDownLatch> latches = new ConcurrentHashMap<State, CountDownLatch>();

    public SynchronizedRunState(Container container, InternalExceptionService<?> ies, Composer composer) {
        super(composer, ies);
        mutex = container;
        for (State s : State.values()) {
            latches.put(s, new CountDownLatch(1));
        }
    }

    @Override
    boolean awaitState(State state, long timeout, TimeUnit unit) throws InterruptedException {
        return latches.get(state).await(timeout, unit);
    }

    @Override
    protected int get() {
        return state.get();
    }

    void shutdown(boolean shutdownNow) {
        synchronized (mutex) {
            if (!getState().isShutdown()) {
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
                    start();
                }
                if (state == SHUTDOWN) {
                    runShutdown();
                    this.state.set(TERMINATED);
                    state = TERMINATED;
                }
                if (state == TERMINATED) {
                    latches.get(State.TERMINATED).countDown();
                }
                return true;
            }
        }
    }

    void tryStart() {
        synchronized (mutex) {
            checkExceptions();
            if (get() == STARTING) {
                throw new IllegalStateException(
                        "Cannot invoke this method from a @Startable method, should be invoked from an @AfterStart method");
            }
            transitionTo(STARTING);
        }
    }
}
