package org.codehaus.cake.internal.service;

import java.util.LinkedHashSet;
import java.util.Set;

@Deprecated
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

    public void add(Class<?> key, Object service) {
        if (key == null) {
            throw new NullPointerException("key, service is null");
        } else if (service == null) {
            throw new NullPointerException("service is null");
        }
        services.add(new Factory(key, service));
    }

    public Iterable<Object> getServices() {
        return services;
    }

    public static class Factory {
        private final Object factory;

        private final Class<?> key;

        public Factory(Class<?> key, Object factory) {
            this.factory = factory;
            this.key = key;
        }

        public Object getFactory() {
            return factory;
        }

        public Class<?> getKey() {
            return key;
        }
    }
}
