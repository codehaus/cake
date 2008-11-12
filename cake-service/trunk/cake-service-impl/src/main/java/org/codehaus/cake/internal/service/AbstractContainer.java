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
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.internal.service.spi.ContainerInfo;
import org.codehaus.cake.service.Container;

public abstract class AbstractContainer implements Container {

    private final String name;

    private final RunState runState;

    private final ServiceManager sm;

    protected AbstractContainer(Composer composer) {
        ContainerInfo info = composer.get(ContainerInfo.class);
        name = info.getContainerName();
        composer.registerInstance(Container.class, this);
        composer.registerInstance(info.getContainerType(), this);
        sm = composer.get(ServiceManager.class);
        runState = composer.get(RunState.class);
    }

    protected AbstractContainer(AbstractContainer parent) {
        this.runState = parent.runState;
        this.sm = parent.sm;
        this.name = parent.name;
    }

    /** {@inheritDoc} */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return runState.awaitTermination(timeout, unit);
    }

    /** {@inheritDoc} */
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType) {
        return getService(serviceType, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType, AttributeMap attributes) {
        lazyStart();
        return sm.getService(serviceType, attributes);
    }

    /** {@inheritDoc} */
    public boolean hasService(Class<?> type) {
        lazyStart();
        return sm.hasService(type);
    }

    /** {@inheritDoc} */
    public boolean isShutdown() {
        return runState.isAtLeastShutdown();
    }

    /** {@inheritDoc} */
    public boolean isStarted() {
        return runState.isAtLeastRunning();
    }

    /** {@inheritDoc} */
    public boolean isTerminated() {
        return runState.isTerminated();
    }

    /** {@inheritDoc} */
    protected void lazyStart() {
        runState.isRunningLazyStart(false);
    }

    /** {@inheritDoc} */
    protected void lazyStartFailIfShutdown() {
        runState.isRunningLazyStart(true);
    }

    /** {@inheritDoc} */
    public Set<Class<?>> serviceKeySet() {
        lazyStart();
        return sm.serviceKeySet();
    }

    /** {@inheritDoc} */
    public void shutdown() {
        runState.shutdown(false);
    }

    /** {@inheritDoc} */
    public void shutdownNow() {
        runState.shutdown(true);
    }
}
