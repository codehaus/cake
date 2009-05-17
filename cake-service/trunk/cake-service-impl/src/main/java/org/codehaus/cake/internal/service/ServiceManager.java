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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.cake.internal.service.AbstractContainer.LookupNextServiceFactory;
import org.codehaus.cake.internal.service.AbstractContainer.SingleServiceFactory;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.internal.service.spi.ExportedService;
import org.codehaus.cake.service.ServiceProvider;

public class ServiceManager {
    private final Map<Class<?>, ExportedService> services = new ConcurrentHashMap<Class<?>, ExportedService>();

    private final InternalExceptionService<?> ies;

    public ServiceManager(InternalExceptionService<?> ies) {
        this.ies = ies;
    }
    
    public Map<Class<?>, ExportedService> getAll() {
        return services;
    }

    <T> void registerService(Class<T> key, T service) {
        if (ies.isDebugEnabled()) {
            ies.debug("  A Service was registered [key=" + key + ", service=" + service + "]");
        }
        services.put(key, new SingleServiceFactory(service));
    }

    <T> void registerServiceFactory(Class<T> key, ServiceProvider<T> provider) {
        ExportedService previous = services.get(key);
        if (ies.isDebugEnabled()) {
            ies.debug("  A ServiceProvider was registered [key=" + key + ", provider=" + provider + "]");
        }
        services.put(key, new LookupNextServiceFactory<T>(provider, previous));
    }

}