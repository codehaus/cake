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

/**
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLifecycle.java 511 2007-12-13 14:37:02Z kasper $
 */
public interface ServiceRegistrant {

    /**
     * Registers the specified service. The service can then later be retrieved
     * by calls to {@link ServiceManager#getService(Class)} with the specified
     * key as parameter.
     * 
     * @param <T>
     *            the type of the service
     * @param key
     *            the key of the service
     * @param service
     *            the service instance to register
     * @return this ServiceRegistrant
     * @throws NullPointerException
     *             if either the specified key or service are <code>null</code>
     * @throws IllegalStateException
     *             if registration of new services is not allowed. For example,
     *             if registering services in a container and the container has
     *             already been started
     */
    <T> ServiceRegistrant registerService(Class<T> key, T service);

    /**
     * Registers the specified {@link ServiceFactory}. Services can then later
     * be retrieved by calls to {@link ServiceManager#getService(Class)} or
     * {@link ServiceManager#getService(Class, org.codehaus.cake.attribute.AttributeMap)}
     * with the specified key as parameter and an optional AttributeMap.
     * 
     * @param <T>
     *            the type of the service
     * @param key
     *            the key of the service
     * @param serviceFactory
     *            the service factory responsible for creating instances
     * @return this ServiceRegistrant
     * @throws NullPointerException
     *             if either the specified key or service factory are
     *             <code>null</code>
     * @throws IllegalStateException
     *             if registration of new services is not allowed. For example,
     *             if registering services in a container and the container has
     *             already been started
     */
    <T> ServiceRegistrant registerFactory(Class<T> key,
            ServiceFactory<T> serviceFactory);

    // replace service
    // remove service
}
