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
import org.codehaus.cake.service.ServiceManager;

public class DefaultServiceManager implements ServiceManager {
    private final Map<Class<?>, ServiceFactory> services = new ConcurrentHashMap<Class<?>, ServiceFactory>();

    private final InternalExceptionService<?> ies;

    public DefaultServiceManager(InternalExceptionService<?> ies) {
        this.ies = ies;
    }

    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType) {
        return getService(serviceType, Attributes.EMPTY_ATTRIBUTE_MAP);
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
            throw new UnsupportedOperationException("Unknown service [type=" + serviceType.getCanonicalName() + "]");
        }
        T service = t.lookup(attributes);
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
        if (services.containsKey(key)) {
            throw new IllegalArgumentException(
                    "A service with the specified key has already been registered [key= " + key + "]");
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
        if (services.containsKey(key)) {
            throw new IllegalArgumentException(
                    "A service or servicefactory with the specified key has already been registered [key= " + key + "]");
        }
        if (ies.isDebugEnabled()) {
            ies.debug("  A ServiceFactory was registered [key=" + key + ", factory=" + serviceFactory + "]");
        }
        services.put(key, serviceFactory);

    }

    /**
     * A {@link ServiceFactory} that returns the same service for any attributes.
     */
    static class SingleServiceFactory<T> implements ServiceFactory<T> {
        private final T service;

        SingleServiceFactory(T service) {
            this.service = service;
        }

        public T lookup(AttributeMap attributes) {
            return service;// TODO warn if attributes non empty??? I think so
        }

    }
}
