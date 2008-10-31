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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.service.ServiceFactory;

public class DefaultServiceManager implements ServiceManager {
    private final Map<Class<?>, RegisteredFactory> services = new ConcurrentHashMap<Class<?>, RegisteredFactory>();

    private final InternalExceptionService<?> ies;

    public DefaultServiceManager(InternalExceptionService<?> ies) {
        this.ies = ies;
    }

    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType) {
        return getService(serviceType, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    public boolean hasService(Class<?> type) {
        return services.containsKey(type);
    }

    /** {@inheritDoc} */
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

    private interface RegisteredFactory<T> {
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
}
