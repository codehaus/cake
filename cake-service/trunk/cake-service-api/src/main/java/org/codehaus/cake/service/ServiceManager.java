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
 * A ServiceManager is the entry-point for accessing other services.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheServiceManagerService.java 537 2007-12-30 19:21:20Z kasper $
 */
public interface ServiceManager {

    /**
     * Returns a service of the specified type or throws a {@link UnsupportedOperationException} if no such service
     * exists. Calling this method is equivalent to calling {@link #getService(Class, AttributeMap)} with an empty
     * {@link AttributeMaps}.
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

    /**
     * Returns a service of the specified type or throws a {@link UnsupportedOperationException} if no such service
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
     *             if the specified service type is null
     */
    <T> T getService(Class<T> serviceType, AttributeMap attributes);

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

    /**
     * Returns a {@link Set} consisting of the types of all registered services.
     * 
     * @return a Set consisting of the types of all registered services
     */
    Set<Class<?>> serviceKeySet();
}
