package org.codehaus.cake.internal.service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceManager;

public class DefaultServiceManager implements ServiceManager {
    final Map<Class<?>, ServiceFactory> services = new ConcurrentHashMap<Class<?>, ServiceFactory>();

    /** {@inheritDoc} */
    public final <T> T getService(Class<T> serviceType) {
        if (serviceType == null) {
            throw new NullPointerException("serviceType is null");
        }
        ServiceFactory<T> t = services.get(serviceType);
        if (t == null) {
            throw new UnsupportedOperationException("Unknown service " + serviceType);
        }
        return t.lookup();
    }

    /** {@inheritDoc} */
    public final boolean hasService(Class<?> type) {
        return services.containsKey(type);
    }
    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType, AttributeMap attributes) {
        if (serviceType == null) {
            throw new NullPointerException("serviceType is null");
        } else if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        ServiceFactory<T> t = services.get(serviceType);
        if (t == null) {
            throw new UnsupportedOperationException("Unknown service " + serviceType);
        }
        T service = t.lookup(attributes);
        if (service == null) {
            throw new UnsupportedOperationException("Unknown service [type=" + serviceType + ", attributes="
                    + attributes + "]");
        }
        return service;
    }
    /** {@inheritDoc} */
    public Set<Class<?>> serviceKeySet() {
        return new HashSet<Class<?>>(services.keySet());
    }
}
