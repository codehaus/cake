package org.codehaus.cake.internal.service;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.cake.internal.service.AbstractContainer.LookupNextServiceFactory;
import org.codehaus.cake.internal.service.AbstractContainer.RegisteredFactory;
import org.codehaus.cake.internal.service.AbstractContainer.SingleServiceFactory;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.service.ServiceFactory;

public class ServiceManager {
    private final Map<Class<?>, RegisteredFactory> services = new ConcurrentHashMap<Class<?>, RegisteredFactory>();

    private final InternalExceptionService<?> ies;

    public ServiceManager(InternalExceptionService<?> ies) {
        this.ies = ies;
    }
    public Map<Class<?>, RegisteredFactory> getAll() {
        return services;
    }

    <T> void registerService(Class<T> key, T service) {
        if (ies.isDebugEnabled()) {
            ies.debug("  A Service was registered [key=" + key + ", service=" + service + "]");
        }
        services.put(key, new SingleServiceFactory<T>(service));
    }

    <T> void registerServiceFactory(Class<T> key, ServiceFactory<T> serviceFactory) {
        RegisteredFactory<?> previous = services.get(key);
        if (ies.isDebugEnabled()) {
            ies.debug("  A ServiceFactory was registered [key=" + key + ", factory=" + serviceFactory + "]");
        }
        services.put(key, new LookupNextServiceFactory(serviceFactory, previous));
    }

}