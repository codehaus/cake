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
import org.codehaus.cake.service.Startable;

public class DefaultServiceRegistrant implements ServiceRegistrant {
    private final Object lock = new Object();
    private volatile DefaultServiceManager manager;//volatile is not strictly needed

    public DefaultServiceRegistrant(DefaultServiceManager manager) {
        this.manager = manager;
    }

    void finished() {
        synchronized (lock) {
            manager = null;
        }
    }

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

    public <T> ServiceRegistrant registerService(Class<T> key, T service) {
        synchronized (lock) {
            if (manager == null) {
                throw new IllegalStateException("Services must be registered with methods using @"
                        + Startable.class.getSimpleName());
            }
            manager.registerService(key,service);
        }
        return this;
    }
}
