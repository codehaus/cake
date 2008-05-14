package org.codehaus.cake.internal.service;

import org.codehaus.cake.service.ServiceRegistrant;

public class DefaultServiceRegistrant implements ServiceRegistrant {
    private final DefaultServiceManager manager;

    public DefaultServiceRegistrant(DefaultServiceManager manager) {
        this.manager = manager;
    }

    public <T> void registerService(Class<T> key, T service) {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (service == null) {
            throw new NullPointerException("service is null");
        }
        if (manager.services.containsKey(key)) {
            throw new IllegalArgumentException("A service with the specified key has already been registered [key= "
                    + key + "]");
        }
        manager.services.put(key, service);
    }
}
