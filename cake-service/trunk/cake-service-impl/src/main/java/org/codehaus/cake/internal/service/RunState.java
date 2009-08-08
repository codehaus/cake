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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.management.JMException;

import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.internal.service.management.DefaultManagementService;
import org.codehaus.cake.internal.util.ArrayUtils;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ContainerShutdownException;
import org.codehaus.cake.service.Container.State;
import org.codehaus.cake.util.TimeFormatter;

public abstract class RunState {
    protected static final int READY = 0;
    protected static final int RUNNING = 2;
    protected static final int SHUTDOWN = 4;
    protected static final int STARTING = 1;
    protected static final int STOPPING = 8;
    protected static final int TERMINATED = 16;
    private Composer composer;
    private final String containerName;
    private final String containerType;

    private final InternalExceptionService<?> ies;
    private final List<LifecycleObject> list = new ArrayList<LifecycleObject>();
    private final AtomicReference<Throwable> startupException = new AtomicReference<Throwable>();

    private List<StateTransitioner> states;

    public RunState(Composer composer, InternalExceptionService<?> ies) {
        this.composer = composer;
        this.ies = ies;
        this.containerType = composer.getContainerTypeName();
        this.containerName = composer.getContainerName();
    }

    boolean awaitState(State state, long timeout, TimeUnit unit) throws InterruptedException {
        if (state == State.INITIALIZED) {
            return READY <= get();
        } else if (state == State.STARTING) {
            return STARTING <= get();
        } else if (state == State.RUNNING) {
            return RUNNING <= get();
        } else if (state == State.SHUTDOWN) {
            return SHUTDOWN <= get();
        } else /* if (state == TERMINATED) */{
            return TERMINATED <= get();
        }
    }
    void checkExceptions() {
        Throwable re = startupException.get();
        if (re != null) {
            throw new IllegalStateException("Cache failed to start previously", re);
        }
    }

    private void doStart() {
        Set<?> allServices = composer.initializeComponents();
        for (Object o : allServices) {
            if (o != null) {
                list.add(new LifecycleObject(this, o));
            }
        }
        ServiceManager dsr = composer.get(ServiceManager.class);
        for (LifecycleObject lo : list) {
            lo.runStart(allServices, composer.get(ContainerConfiguration.class), dsr, ies);
        }

        if (composer.hasService(DefaultManagementService.class)) {
            try {
                composer.get(DefaultManagementService.class).register(composer, allServices);
            } catch (JMException e) {
                trySetStartupException(e);
                throw new IllegalStateException("Could not start cache", e);
            }
        }
        transitionTo(RUNNING);
        /* AfterStart */
        for (Iterator<LifecycleObject> iterator = list.iterator(); iterator.hasNext();) {
            LifecycleObject lo = iterator.next();
            lo.runAfterStart(ies, composer.get(ContainerConfiguration.class), composer.get(Container.class));
            if (!lo.isStoppableOrDisposable()) {
                iterator.remove();
            }
        }
    }

    abstract int get();

    final State getState() {
        int state = get();
        if (state == READY) {
            return State.INITIALIZED;
        } else if (state == STARTING) {
            return State.STARTING;
        } else if (state == RUNNING) {
            return State.RUNNING;
        } else if (state == SHUTDOWN) {
            return State.SHUTDOWN;
        } else if (state == STOPPING) {
            return State.SHUTDOWN;
        } else /* if (state == TERMINATED) */{
            return State.TERMINATED;
        }
    }

    /** {@inheritDoc} */
    public boolean isRunningLazyStart(boolean failIfShutdown) {
        while (get() != RUNNING) {
            if (getState().isShutdown()) {
                checkExceptions();
                if (failIfShutdown) {
                    throw new ContainerShutdownException(containerType + " [name=" + containerName
                            + "] has been shutdown, cannot invoke method");
                } else {
                    return false;
                }
            }
            tryStart();
        }
        return true;
    }

    void runShutdown() {
        for (Iterator<LifecycleObject> iterator = list.iterator(); iterator.hasNext();) {
            LifecycleObject lo = iterator.next();
            lo.runStop(ies);
        }
        for (Iterator<LifecycleObject> iterator = list.iterator(); iterator.hasNext();) {
            LifecycleObject lo = iterator.next();
            lo.runDispose(ies);
        }
        ies.terminated();
    }

    abstract void shutdown(boolean shutdownNow);

    void start() {
        try {
            start_();
        } finally {
            composer = null;// no matter what, clear the composer
        }
    }

    void start_() {
        long startTime = System.nanoTime();

        // debugging information
        if (ies.isDebugEnabled()) {
            ies.debug("Starting " + composer.getContainerTypeName() + " [name=" + composer.getContainerName()
                    + ", type=" + composer.get(Container.class).getClass().getSimpleName() + "]");
            if (ies.isTraceEnabled()) {
                StringBuilder sb = new StringBuilder();
                sb.append("  ------------" + composer.getContainerTypeName()
                        + " was started by this call--------------\n");
                StackTraceElement[] trace = Thread.currentThread().getStackTrace();
                ArrayUtils.reverse(trace);
                int length = trace.length;
                int start = Math.max(0, trace.length - 12);
                for (int i = 0; i < length - 1 - start; i++) {
                    sb.append("    ");
                    sb.append("                         ".substring(0, i));
                    sb.append(trace[start + i]);
                    if (start + i < length) {
                        sb.append("\n");
                    }
                }
                sb.append("  --------------------------------------------------------");
                ies.trace(sb.toString());
            }
        }

        try {
            doStart();
        } catch (RuntimeException e) {
            trySetStartupException(e);
            runShutdown(); // should we run shutdown;??
            throw e;
        } catch (Error e) {
            trySetStartupException(e);
            throw e;
        }
        ies.info(composer.getContainerTypeName() + " started [name = " + composer.getContainerName()
                + ", startup time = " + TimeFormatter.DEFAULT.formatNanos(System.nanoTime() - startTime) + "]");
    }

    abstract boolean transitionTo(int state);

    void trySetStartupException(Throwable cause) {
        startupException.compareAndSet(null, cause);
    }

    abstract void tryStart();
}
