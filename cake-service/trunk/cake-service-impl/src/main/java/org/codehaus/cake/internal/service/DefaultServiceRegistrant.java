package org.codehaus.cake.internal.service;

import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;

public  class DefaultServiceRegistrant implements ServiceRegistrant {
    private DefaultServiceManager manager;
    private final Object lock = new Object();

    public DefaultServiceRegistrant(DefaultServiceManager manager) {
        this.manager = manager;
    }

    public <T> void registerService(Class<T> key, T service) {
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
            manager.services.put(key, service);
        }
    }

    void finished() {
        synchronized (lock) {
            manager = null;
        }
    }
}
