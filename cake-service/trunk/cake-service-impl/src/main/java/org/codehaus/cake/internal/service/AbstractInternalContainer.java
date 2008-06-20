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

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.spi.ContainerInfo;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ServiceManager;

public abstract class AbstractInternalContainer implements Container {

    private final String name;;

    private final RunState runState;

    private final ServiceManager sm;

    public AbstractInternalContainer(Composer composer) {
        ContainerInfo info = composer.get(ContainerInfo.class);
        name = info.getContainerName();
        if (!composer.hasService(Container.class)) {
            // This is an internal container,add self to composer
            composer.registerInstance(Container.class, this);
            composer.registerInstance(info.getContainerType(), this);
        }
        sm = composer.get(ServiceManager.class);
        runState = composer.get(RunState.class);
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return runState.awaitTermination(timeout, unit);
    }

    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    public final <T> T getService(Class<T> serviceType) {
        lazyStart();
        return sm.getService(serviceType);
    }

    /** {@inheritDoc} */
    public final <T> T getService(Class<T> serviceType, AttributeMap attributes) {
        lazyStart();
        return sm.getService(serviceType, attributes);
    }

    /** {@inheritDoc} */
    public final boolean hasService(Class<?> type) {
        lazyStart();
        return sm.hasService(type);
    }

    public boolean isShutdown() {
        return runState.isAtLeastShutdown();
    }

    public boolean isStarted() {
        return runState.isAtLeastRunning();
    }

    public boolean isTerminated() {
        return runState.isTerminated();
    }

    protected void lazyStart() {
        runState.isRunningLazyStart(false);
    }

    protected void lazyStartFailIfShutdown() {
        runState.isRunningLazyStart(true);
    }

    public Set<Class<?>> serviceKeySet() {
        lazyStart();
        return sm.serviceKeySet();
    }

    public void shutdown() {
        runState.shutdown(false);
    }

    public void shutdownNow() {
        runState.shutdown(true);
    }
}
