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

import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.annotation.Startable;

/**
 * Default implementation of {@link ServiceRegistrant}. Will throw {@link IllegalStateException} if registration methods
 * are not invoked from within methods annotated with {@link Startable}.
 * <p>
 * NOTICE: This is an internal class and should not be directly referred. No guarantee is made to the compatibility of
 * this class between different releases.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheWorkerManager.java 479 2007-11-27 13:40:08Z kasper $
 */
public class DefaultServiceRegistrant implements ServiceRegistrant {
    /** An internal lock for this class. */
    private final Object lock = new Object();

    /** The service manager to register services within. */
    private DefaultServiceManager manager;

    /**
     * Creates a new DefaultServiceRegistrant
     * 
     * @param manager
     *            the service manager to register services with
     */
    public DefaultServiceRegistrant(DefaultServiceManager manager) {
        this.manager = manager;
    }

    /**
     * Disables further registration. Any attempt to register services or service factories will result in a thrown
     * {@link IllegalStateException}.
     */
    void disableRegistration() {
        synchronized (lock) {
            manager = null;
        }
    }

    /** {@inheritDoc} */
    public <T> ServiceRegistrant registerFactory(Class<T> key, ServiceFactory<T> serviceFactory) {
        synchronized (lock) {
            if (manager == null) {
                throw new IllegalStateException("Services must be registered with methods using @"
                        + Startable.class.getSimpleName());
            }
            manager.registerServiceFactory(key, serviceFactory);
        }
        return this;
    }

    /** {@inheritDoc} */
    public <T> ServiceRegistrant registerService(Class<T> key, T service) {
        synchronized (lock) {
            if (manager == null) {
                throw new IllegalStateException("Services must be registered with methods using @"
                        + Startable.class.getSimpleName());
            }
            manager.registerService(key, service);
        }
        return this;
    }
}
