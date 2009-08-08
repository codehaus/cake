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
        ExportedService previous = services.get(key);
        if (previous == null) {
            ies.debug("  Service registered [key=" + key + ", service=" + service + "]");
        } else {
            if (previous instanceof SingleServiceFactory) {
                ies.debug("  Service registered, replacing existing service [key=" + key + ", service=" + service
                        + ", existing=" + ((SingleServiceFactory) previous).service + "]");
            } else {
                ies.debug("  Service registered, replacing existing serviceprovider [key=" + key + ", service="
                        + service + ", existing=" + ((LookupNextServiceFactory<?>) previous).factory + "]");

            }
        }
        services.put(key, new SingleServiceFactory(service));
    }

    <T> void registerServiceFactory(Class<T> key, ServiceProvider<T> provider) {
        ExportedService previous = services.get(key);
        if (ies.isDebugEnabled()) {
            if (previous == null) {
                ies.debug("  ServiceProvider registered [key=" + key + ", provider=" + provider + "]");
            } else {
                if (previous instanceof SingleServiceFactory) {
                    ies.debug("  ServiceProvider registered, replacing existing service [key=" + key + ", provider="
                            + provider + ", existing=" + ((SingleServiceFactory) previous).service + "]");
                } else {
                    ies.debug("  ServiceProvider registered, replacing existing serviceprovider [key=" + key
                            + ", provider=" + provider + ", existing="
                            + ((LookupNextServiceFactory<?>) previous).factory + "]");

                }
            }
        }
        services.put(key, new LookupNextServiceFactory<T>(provider, previous));
    }

}
