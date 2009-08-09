/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.internal.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.internal.picocontainer.MutablePicoContainer;
import org.codehaus.cake.internal.picocontainer.defaults.DefaultPicoContainer;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;

/**
 * A Composer is used for constructing {@link Container} instances.
 */
@UseInternals
public class Composer {

    private final MutablePicoContainer baseContainer;

    private final MutablePicoContainer container;

    private final Class<?> clazz;
    private final String containerName;
    private static final AtomicLong counter=new AtomicLong();
    public Composer(Class<?> clazz, ContainerConfiguration configuration) {
        baseContainer = new DefaultPicoContainer();
        baseContainer.registerComponentInstance(configuration);
        baseContainer.registerComponentInstance(configuration.getClock());
        for (Object c : configuration.getConfigurations()) {
            baseContainer.registerComponentInstance(c);
        }
        container = baseContainer.makeChildContainer();
        container.registerComponentInstance(this);
         container.registerComponentImplementation(ServiceManager.class);
        this.clazz = clazz;
        String name = configuration.getName();
        if (name == null) {
            containerName = clazz.getSimpleName() + counter.incrementAndGet(); // UUID.randomUUID().toString();
        } else {
            containerName = name;
        }
    }

    public static Composer from(Class<?> containerType, ContainerConfiguration configuration) {
        return null;
    }

    public static Composer from(Container parent, Class<?> containerType, ContainerConfiguration configuration) {
        return null;
    }
    
    public String getContainerName() {
        return containerName;
    }

    public Class<?> getContainerType() {
        return clazz;
    }

    public String getContainerTypeName() {
        return getContainerType().getSimpleName();
    }

    public String getDefaultJMXDomain() {
        return getContainerType().getPackage().getName();
    }

    public void registerImplementation(Class<?> clazz) {
        container.registerComponentImplementation(clazz);
    }

    public void registerInstance(Object key, Object value) {
        container.registerComponentInstance(key, value);
    }

    <T> boolean hasService(Class<T> serviceType) {
        return container.getComponentAdapterOfType(serviceType) != null;
    }

    /**
     * Returns all services of the specific type or all service factories that create instances of the specific type
     * 
     * @param <T>
     * @param serviceType
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getAll(Class<T> serviceType) {
        return new LinkedHashSet<T>(container.getComponentInstancesOfType(serviceType));
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> serviceType) {
        return (T) container.getComponentInstanceOfType(serviceType);
    }

    @SuppressWarnings("unchecked")
    Set<?> initializeComponents() {
        ContainerConfiguration conf = get(ContainerConfiguration.class);
        Set<Object> result = new LinkedHashSet<Object>(container.getComponentInstances());
        for (Object object : new ArrayList<Object>(result)) {
            if (object instanceof CompositeService) {
                for (Object oo : ((CompositeService) object).getChildServices()) {
                    result.add(oo);
                }
            }
        }
        for (Object o : conf.getServices()) {
            result.add(o);
        }
        return result;
    }
}
