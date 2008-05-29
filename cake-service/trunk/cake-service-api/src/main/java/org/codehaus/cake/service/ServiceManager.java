/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.service;

import java.util.Map;
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
