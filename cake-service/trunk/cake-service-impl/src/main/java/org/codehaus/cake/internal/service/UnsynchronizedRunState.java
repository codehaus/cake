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

import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;

public class UnsynchronizedRunState extends RunState {

    private int state;

    public UnsynchronizedRunState(Composer composer, InternalExceptionService<?> ies) {
        super(composer, ies);
    }

    void tryStart() {
        checkExceptions();
        if (get() == STARTING) {
            throw new IllegalStateException(
                    "Cannot invoke this method from a method annotated with @RunAfter(STARTING), should be annotated with @RunAfter(RUNNING)");
        }
        if (state < STARTING) {
            state = STARTING;
            start();
        }
    }

    boolean transitionTo(int state) {
        int s = get();
        if (s >= state)
            return false;
        this.state = state;
        if (state == RUNNING) {

        } else if (state == TERMINATED) {

        }
        return true;
    }

    void shutdown(boolean shutdownNow) {
        if (!getState().isShutdown()) {
            runShutdown();
            transitionTo(SHUTDOWN);
            transitionTo(TERMINATED);
        }
    }

    @Override
    protected int get() {
        return state;
    }
}
