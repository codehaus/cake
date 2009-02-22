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

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.MutableAttributeMap;

/**
 * A service factory is used for constructing services of a specific type.
 * 
 * The following can be used to introduce A service factory can be implemented for
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$ *
 * @param <T>
 *            the type of service returned from {@link #lookup(MutableAttributeMap)}
 */
public interface ServiceFactory<T> {

    /**
     * Called by a {@link Container} to lookup a specific service.
     * 
     * @param context
     *            the context the factory should use to decide what service should be returned
     * @return a service matching the specified context
     */
    // TODO return null or Throw an UOE??
    T lookup(ServiceFactoryContext<T> context);

    /**
     * A ServiceFactoryContext is used as the parameter to the {@link ServiceFactory#lookup(ServiceFactoryContext)}
     * method.
     */
    interface ServiceFactoryContext<T> {
        /**
         * Return the key that is used for acquiring the service, as parsed along to {@link Container#getService(Class)}
         * or {@link Container#getService(Class, MutableAttributeMap)}. This is primaraily used if the ServiceFactory returns
         * services for multiple service types
         * 
         * @return the key that used for looking of the service.
         */
        Class<? extends T> getKey();

        /**
         * Return the attribute map parsed to {@link Container#getService(Class, MutableAttributeMap)} or an empty attribute
         * map if {@link Container#getService(Class)} was called.
         * 
         * @return attribute map that was used for looking up the service
         */
        AttributeMap getAttributes();

        /**
         * This method can be called to allow other service factories to create a service for specified service type and
         * map of attributes map.
         * 
         * @return can be used for chaining instances
         */
        T handleNext();
    }
}
