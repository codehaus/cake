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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.internal.service.spi.ExportedService;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ServiceProvider;

public abstract class AbstractContainer implements Container {

    private final String name;

    private final RunState runState;

    private final ServiceManager sm;

    private volatile Map<Class<?>, ExportedService> services = new ConcurrentHashMap<Class<?>, ExportedService>();

    private final AbstractContainer parent;

    private final List<AbstractContainer> children = null;

    protected AbstractContainer(Composer composer) {
        name = composer.getContainerName();
        composer.registerInstance(Container.class, this);
        composer.registerInstance(composer.getContainerType(), this);
        sm = composer.get(ServiceManager.class);
        runState = composer.get(RunState.class);
        parent = null;
    }

    protected AbstractContainer(AbstractContainer parent) {
        this.runState = parent.runState;
        this.sm = parent.sm;
        this.name = parent.name;
        this.parent = parent.parent;
    }

    protected AbstractContainer(Composer composer, AbstractContainer parent) {
        name = composer.getContainerName();
        composer.registerInstance(Container.class, this);
        composer.registerInstance(composer.getContainerType(), this);
        sm = composer.get(ServiceManager.class);
        runState = composer.get(RunState.class);
        this.parent = parent;
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
        if (serviceType == null) {
            throw new NullPointerException("serviceType is null");
        } else if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        Map<Class<?>, ExportedService> services = this.services;// volatile read

        ExportedService<T> f = services.get(serviceType);
        if (f == null && services.size() == 0) {// No services registered, most likely the container hasn't been started
            lazyStart(); // make sure its started
            this.services = services = new HashMap<Class<?>, ExportedService>(sm.getAll());
            f = services.get(serviceType);
        }

        if (f == null) {
            throw new UnsupportedOperationException(
                    "No service or service factory has been registered for the specified type [type="
                            + serviceType.getCanonicalName() + "]");
        }
        T service = f.lookup(serviceType, attributes);
        if (service == null) {
            throw new UnsupportedOperationException("Unknown service [type=" + serviceType.getCanonicalName()
                    + ", attributes=" + attributes + "]");
        }
        return service;
    }

    /** {@inheritDoc} */
    public boolean hasService(Class<?> type) {
        Map<Class<?>, ExportedService> services = this.services;// volatile read
        if (services.containsKey(type)) {
            return true;
        }
        // Okay either the container hasn't been started or the container does not have a service of the specified type
        if (services.size() == 0) { // No services registered, most likely the container hasn't been started
            lazyStart(); // make sure its started
            this.services = services = new HashMap<Class<?>, ExportedService>(sm.getAll());
            return services.containsKey(type);
        }
        return false;
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
    public Set<Class<?>> serviceKeySet() {
        Map<Class<?>, ExportedService> services = this.services;// volatile read
        // Okay either the container hasn't been started or the container does not have a service of the specified type
        if (services.size() == 0) { // No services registered, most likely the container hasn't been started
            lazyStart(); // make sure its started
            this.services = services = new HashMap<Class<?>, ExportedService>(sm.getAll());
        }
        return services.keySet();
    }

    /** {@inheritDoc} */
    public void shutdown() {
        runState.shutdown(false);
    }

    /** {@inheritDoc} */
    public void shutdownNow() {
        runState.shutdown(true);
    }

    /**
     * A {@link ServiceProvider} that returns the same service for any attributes.
     */
    static class SingleServiceFactory<T> implements ExportedService<T> {
        private final T service;

        SingleServiceFactory(T service) {
            this.service = service;
        }

        public T lookup(Class<T> key, AttributeMap attributes) {
            return service;
        }

        public boolean exportTo(Container container) {
            return true;
        }
    }

    static class LookupNextServiceFactory<T> implements ExportedService<T> {
        private final ServiceProvider<T> factory;
        private final ExportedService<T> next;

        LookupNextServiceFactory(ServiceProvider<T> factory, ExportedService<T> next) {
            this.factory = factory;
            this.next = next;
        }

        public T lookup(final Class<T> key, final AttributeMap attributes) {
            return factory.lookup(new ServiceProvider.ServiceFactoryContext<T>() {
                public AttributeMap getAttributes() {
                    return attributes;
                }

                public Class<? extends T> getKey() {
                    return key;
                }

                public T handleNext() {
                    if (next == null) {
                        throw new UnsupportedOperationException(
                                "No other services registered for the specified key, [key=" + key.getCanonicalName()
                                        + "]");
                    }
                    return next.lookup(key, attributes);
                }
            });
        }

        public boolean exportTo(Container container) {
            return true;
        }
    }
}
