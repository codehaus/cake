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
package org.codehaus.cake.service;

import java.util.Set;

import org.codehaus.cake.attribute.AttributeMap;

/**
 * 
 * This is the entry-point for accessing other services, An interface giving access to services.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheServiceManagerService.java 537 2007-12-30 19:21:20Z kasper $
 */
public interface ServiceManager {

    /**
     * Returns whether or not this service manager contains a service of the specified type.
     * 
     * @param serviceType
     *            the type of service
     * @return true if this service manager contains a service of the specified type, otherwise false
     * @throws NullPointerException
     *             if the specified service type is null
     * 
     * @see ServiceManager#serviceKeySet()
     */
    boolean hasService(Class<?> serviceType);

    //boolean hasService(Class<?> serviceType, AttributeMap attributes);

    Set<Class<?>> serviceKeySet();
    
    //Map<Class<?>, ServiceFactory<?>> getAll();
    /**
     * Returns a service of the specified type or throws a {@link UnsupportedOperationException} if no such service
     * exists.
     * 
     * @param <T>
     *            the type of service to retrieve
     * @param serviceType
     *            the type of service to retrieve
     * @return a service of the specified type
     * @throws UnsupportedOperationException
     *             if no service of the specified type exist
     * @throws NullPointerException
     *             if the specified service type is null
     */
    <T> T getService(Class<T> serviceType);

    <T> T getService(Class<T> serviceType, AttributeMap attributes);
}
