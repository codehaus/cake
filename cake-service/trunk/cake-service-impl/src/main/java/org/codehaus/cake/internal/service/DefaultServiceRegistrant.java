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

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;

public class DefaultServiceRegistrant implements ServiceRegistrant {
    private final Object lock = new Object();
    private DefaultServiceManager manager;

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
            if (key == null) {
                throw new NullPointerException("key is null");
            } else if (serviceFactory == null) {
                throw new NullPointerException("serviceFactory is null");
            }
            if (manager == null) {
                throw new IllegalStateException("Services must be registered with methods using @"
                        + Startable.class.getSimpleName());
            }
            if (manager.services.containsKey(key)) {
                throw new IllegalArgumentException(
                        "A service with the specified key has already been registered [key= " + key + "]");
            }
            manager.services.put(key, serviceFactory);
        }
        return this;
    }

    public <T> ServiceRegistrant registerService(Class<T> key, T service) {
        synchronized (lock) {
            if (key == null) {
                throw new NullPointerException("key is null");
            } else if (service == null) {
                throw new NullPointerException("service is null");
            }
            if (manager == null) {
                throw new IllegalStateException("Services must be registered with methods using @"
                        + Startable.class.getSimpleName());
            }
            if (manager.services.containsKey(key)) {
                throw new IllegalArgumentException(
                        "A service with the specified key has already been registered [key= " + key + "]");
            }
            manager.services.put(key, new SingleServiceFactory(key, service));
        }
        return this;
    }

    /**
     * A {@link ServiceFactory} that returns the same service for any attributes.
     */
    static class SingleServiceFactory<T> implements ServiceFactory<T> {
        private final Class<T> clazz;
        private final T service;

        SingleServiceFactory(Class<T> clazz, T service) {
            this.clazz = clazz;
            this.service = service;
        }

        public T lookup(AttributeMap attributes) {
            return service;// TODO warn if attributes non empty??? I think so
        }

    }

}
