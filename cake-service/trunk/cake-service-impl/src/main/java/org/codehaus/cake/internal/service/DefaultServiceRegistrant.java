package org.codehaus.cake.internal.service;

import java.util.Arrays;
import java.util.Collection;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;

public class DefaultServiceRegistrant implements ServiceRegistrant {
    private DefaultServiceManager manager;
    private final Object lock = new Object();

    public DefaultServiceRegistrant(DefaultServiceManager manager) {
        this.manager = manager;
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

    void finished() {
        synchronized (lock) {
            manager = null;
        }
    }

    static class SingleServiceFactory<T> implements ServiceFactory<T>, CompositeService {
        private final Class<T> clazz;
        private final T service;

        public Class<T> getType() {
            return clazz;
        }

        public T lookup() {
            return service;
        }

        public T lookup(AttributeMap attributes) {
            return service;// TODO warn if attributes non empty??? I think so
        }

        public Collection<?> getChildServices() {
            return Arrays.asList(service);
        }

        public SingleServiceFactory(Class<T> clazz, T service) {
            this.clazz = clazz;
            this.service = service;
        }

    }

}
