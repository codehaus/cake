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

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.internal.service.spi.ContainerInfo;

public class UnsynchronizedRunState extends RunState {

    private int state;

    public UnsynchronizedRunState(ContainerInfo info, LifecycleManager lifecycleManager) {
        super(info, lifecycleManager);
    }

    void tryStart() {
        lifecycleManager.checkExceptions();
        if (isStarting()) {
            throw new IllegalStateException(
                    "Cannot invoke this method from a @Startable method, should be invoked from an @AfterStart method");
        }
        if (state < STARTING) {
            state = STARTING;
            lifecycleManager.start(this);
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

    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return isTerminated();
    }


    @Override
    protected int get() {
        return state;
    }
}
