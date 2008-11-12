package org.codehaus.cake.internal.service;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.service.ServiceFactory;

public class ServiceManager {
    private final Map<Class<?>, RegisteredFactory> services = new ConcurrentHashMap<Class<?>, RegisteredFactory>();

    private final InternalExceptionService<?> ies;

    public ServiceManager(InternalExceptionService<?> ies) {
        this.ies = ies;
    }

    /**
     * Returns a service of the specified type or throws an {@link UnsupportedOperationException} if no such service
     * exists. The map of attributes will be parsed along to the {@link ServiceFactory} responsible for constructing the
     * service.
     * 
     * @param <T>
     *            the type of service to retrieve
     * @param serviceType
     *            the type of service to retrieve
     * @param attributes
     *            a map of additional attributes
     * @return a service of the specified type
     * @throws UnsupportedOperationException
     *             if no service of the specified type exist
     * @throws NullPointerException
     *             if the specified service type or attribute map is null
     */
    public <T> T getService(final Class<T> serviceType, final AttributeMap attributes) {
        if (serviceType == null) {
            throw new NullPointerException("serviceType is null");
        } else if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        RegisteredFactory<T> f = services.get(serviceType);
        if (f == null) {
            throw new UnsupportedOperationException("Unknown service [type=" + serviceType.getCanonicalName() + "]");
        }
        T service = f.lookup(serviceType, attributes);
        if (service == null) {
            throw new UnsupportedOperationException("Unknown service [type=" + serviceType.getCanonicalName()
                    + ", attributes=" + attributes + "]");
        }
        return service;
    }

    /**
     * Returns whether or not this service manager contains a service of the specified type.
     * 
     * @param serviceType
     *            the type of service
     * @return true if this service manager contains a service of the specified type, otherwise false
     * @throws NullPointerException
     *             if the specified service type is null
     * 
     * @see #serviceKeySet()
     */
    public boolean hasService(Class<?> type) {
        return services.containsKey(type);
    }

    /**
     * Returns a {@link Set} consisting of the types of all registered services.
     * 
     * @return a Set consisting of the types of all registered services
     */
    public Set<Class<?>> serviceKeySet() {
        return new HashSet<Class<?>>(services.keySet());
    }

    <T> void registerService(Class<T> key, T service) {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (service == null) {
            throw new NullPointerException("service is null");
        }
        if (ies.isDebugEnabled()) {
            ies.debug("  A Service was registered [key=" + key + ", service=" + service + "]");
        }
        services.put(key, new SingleServiceFactory<T>(service));
    }

    <T> void registerServiceFactory(Class<T> key, ServiceFactory<T> serviceFactory) {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (serviceFactory == null) {
            throw new NullPointerException("serviceFactory is null");
        }
        RegisteredFactory<?> previous = services.get(key);
        if (ies.isDebugEnabled()) {
            ies.debug("  A ServiceFactory was registered [key=" + key + ", factory=" + serviceFactory + "]");
        }
        services.put(key, new LookupNextServiceFactory(serviceFactory, previous));

    }

    interface RegisteredFactory<T> {
        T lookup(Class<T> key, AttributeMap attributes);
    }

    /**
     * A {@link ServiceFactory} that returns the same service for any attributes.
     */
    static class SingleServiceFactory<T> implements RegisteredFactory<T> {
        private final T service;

        SingleServiceFactory(T service) {
            this.service = service;
        }

        public T lookup(Class<T> key, AttributeMap attributes) {
            return service;
        }
    }

    static class LookupNextServiceFactory<T> implements RegisteredFactory<T> {
        private final ServiceFactory<T> factory;
        private final RegisteredFactory<T> next;

        LookupNextServiceFactory(ServiceFactory<T> factory, RegisteredFactory<T> next) {
            this.factory = factory;
            this.next = next;
        }

        public T lookup(final Class<T> key, final AttributeMap attributes) {
            return factory.lookup(new ServiceFactory.ServiceFactoryContext<T>() {
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
    }

    public <T> T getService(Class<T> serviceType) {
        return getService(serviceType, Attributes.EMPTY_ATTRIBUTE_MAP);
    }
}