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
import java.util.concurrent.atomic.AtomicReference;

import javax.management.JMException;

import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.internal.service.management.DefaultManagementService;
import org.codehaus.cake.internal.util.ArrayUtils;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.util.TimeFormatter;

public class LifecycleManager {
    private Composer composer;
    private final InternalExceptionService<?> ies;
    private final List<LifecycleObject> list = new ArrayList<LifecycleObject>();

    private final AtomicReference<Throwable> startupException = new AtomicReference<Throwable>();

    public LifecycleManager(InternalExceptionService<?> ies, Composer composer) {
        this.ies = ies;
        this.composer = composer;
    }

    void checkExceptions() {
        Throwable re = startupException.get();
        if (re != null) {
            throw new IllegalStateException("Cache failed to start previously", re);
        }
    }

    private void doStart(RunState state) {
        Set<?> allServices = composer.initializeComponents();
        for (Object o : allServices) {
            if (o != null) {
                list.add(new LifecycleObject(this, o));
            }
        }
        ServiceManager dsr = composer.get(ServiceManager.class);
        for (LifecycleObject lo : list) {
            lo.runStart(allServices, composer.get(ContainerConfiguration.class), dsr,ies);
        }

        if (composer.hasService(DefaultManagementService.class)) {
            try {
                composer.get(DefaultManagementService.class).register(composer, allServices);
            } catch (JMException e) {
                trySetStartupException(e);
                throw new IllegalStateException("Could not start cache", e);
            }
        }
        state.transitionToRunning();
        /* AfterStart */
        for (Iterator<LifecycleObject> iterator = list.iterator(); iterator.hasNext();) {
            LifecycleObject lo = iterator.next();
            lo.runAfterStart(ies, composer.get(ContainerConfiguration.class), composer.get(Container.class));
            if (!lo.isStoppableOrDisposable()) {
                iterator.remove();
            }
        }
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

    void start(RunState state) {
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

        // Run start
        try {
            try {
                doStart(state);
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
        } finally {
            composer = null;
        }

    }

    void trySetStartupException(Throwable cause) {
        startupException.compareAndSet(null, cause);
    }
}
