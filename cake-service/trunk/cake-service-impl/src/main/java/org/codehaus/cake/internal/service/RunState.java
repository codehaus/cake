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

public abstract class RunState {
    protected static final int READY = 0;
    protected static final int STARTING = 1;
    protected static final int RUNNING = 2;
    protected static final int SHUTDOWN = 4;
    protected static final int STOPPING = 8;
    protected static final int TERMINATED = 16;
    final String containerType;
    private final String containerName;
    final LifecycleManager lifecycleManager;

    public RunState(ContainerInfo info, LifecycleManager lifecycleManager) {
        this.containerType = info.getContainerTypeName();
        this.containerName = info.getContainerName();
        this.lifecycleManager = lifecycleManager;
        lifecycleManager.state = this;
    }

    final boolean isRunning() {
        return get() == RUNNING;
    }

    final boolean isStarting() {
        return get() == STARTING;
    }

    //
    // final boolean isShutdown() {
    // return get() == SHUTDOWN;
    // }
    //
    // final boolean isStopping() {
    // return get() == STOPPING;
    // }

    final boolean isTerminated() {
        return get() == TERMINATED;
    }

    final boolean isAtLeastRunning() {
        return get() >= RUNNING;
    }

    final boolean isAtLeastShutdown() {
        return get() >= SHUTDOWN;
    }

    // final boolean isAtLeastStopping() {
    // return get() >= STOPPING;
    // }

    final boolean transitionToRunning() {
        return transitionTo(RUNNING);
    }

    /** {@inheritDoc} */
    boolean isRunningLazyStart(boolean failIfShutdown) {
        while (!isRunning()) {
            if (isAtLeastShutdown()) {
                checkExceptions();
                if (failIfShutdown) {
                    throw new IllegalStateException(containerType + " [name=" + containerName
                            + "] has been shutdown, cannot invoke method");
                } else {
                    return false;
                }
            }
            tryStart();
        }
        return true;
    }

    abstract int get();

    abstract boolean transitionTo(int state);

    abstract boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

    abstract void checkExceptions();

    abstract void shutdown(boolean shutdownNow);

    abstract Throwable getStartupException();

    abstract void trySetStartupException(Throwable cause);

    abstract void tryStart();
}
