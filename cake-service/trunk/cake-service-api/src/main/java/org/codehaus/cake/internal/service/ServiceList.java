package org.codehaus.cake.internal.service;

import java.util.LinkedHashSet;
import java.util.Set;

import org.codehaus.cake.service.ServiceFactory;

public class ServiceList {
    private Set<Object> services = new LinkedHashSet<Object>();

    public void addLifecycle(Object service) {
        if (service == null) {
            throw new NullPointerException("o is null");
        } else if (services.contains(service)) {
            throw new IllegalArgumentException("Object has already been registered");
        }
        services.add(service);
    }

    public <T> void addService(Class<? extends T> key, T service) {
        services.add(new Factory(key, service));
    }

    public <T> void addServiceFactory(Class<? extends T> key, ServiceFactory<T> factory) {
        services.add(new Factory(key, factory));
    }

    public Iterable<Object> getServices() {
        return services;
    }

    public static class Factory {
        private final Object factory;

        private final Class key;

        public Factory(Class key, Object factory) {
            this.factory = factory;
            this.key = key;
        }

        public Object getFactory() {
            return factory;
        }
        public Class getKey() {
            return key;
        }
    }
}
