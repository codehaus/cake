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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.internal.picocontainer.MutablePicoContainer;
import org.codehaus.cake.internal.picocontainer.defaults.DefaultPicoContainer;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.internal.service.spi.ContainerInfo;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceManager;
import org.codehaus.cake.service.ServiceRegistrant;

@UseInternals
public class Composer {

    private final MutablePicoContainer baseContainer;

    private final MutablePicoContainer container;

    public Composer(Class clazz, ContainerConfiguration configuration) {
        baseContainer = new DefaultPicoContainer();
        baseContainer.registerComponentInstance(configuration);
        baseContainer.registerComponentInstance(configuration.getClock());
        for (Object c : configuration.getConfigurations()) {
            baseContainer.registerComponentInstance(c);
        }
        container = baseContainer.makeChildContainer();
        container.registerComponentInstance(this);
        container.registerComponentInstance(new ContainerInfo(clazz, configuration));
        container.registerComponentImplementation(LifecycleManager.class);
        container.registerComponentImplementation(ServiceManager.class, DefaultServiceManager.class);
        container.registerComponentImplementation(ServiceRegistrant.class, DefaultServiceRegistrant.class);
    }

    public void registerImplementation(Class<?> clazz) {
        container.registerComponentImplementation(clazz);
    }

    //
    // public void registerImplementation(Object key, Class<?> clazz) {
    // container.registerComponentImplementation(key, clazz);
    // }

    public void registerInstance(Object key, Object value) {
        container.registerComponentInstance(key, value);
    }

    <T> boolean hasService(Class<T> serviceType) {
        return container.getComponentAdapterOfType(serviceType) != null;
    }

    public <T> T get(Class<T> serviceType) {
        T service = (T) container.getComponentInstanceOfType(serviceType);
        // if (service == null) {
        // throw new IllegalArgumentException("Unknown service: " + serviceType);
        // }
        return service;
    }

    public <T> T getIfAvailable(Class<T> serviceType) {
        T service = (T) container.getComponentInstanceOfType(serviceType);
        return service;
    }

    // public Object getFromKeyIfAvailable(Object key) {
    // return container.getComponentInstance(key);
    // }

    Set<?> prepareStart() {
        ContainerConfiguration conf = get(ContainerConfiguration.class);
        Set result = new LinkedHashSet(container.getComponentInstances());
        for (Object object : new ArrayList(result)) {
            if (object instanceof CompositeService) {
                for (Object oo : ((CompositeService) object).getChildServices()) {
                    result.add(oo);
                }
            }
        }
        ServiceList sl=(ServiceList) conf.getServices();
        for (Object o : sl.getServices()) {
            result.add(o);
        }
        return result;
    }
}
