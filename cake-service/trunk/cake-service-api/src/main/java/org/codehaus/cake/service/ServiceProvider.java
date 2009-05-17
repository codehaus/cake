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
package org.codehaus.cake.service;

import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

/**
 * A service provider is used for constructing services of a specific type.
 * 
 * The following can be used to introduce A service factory can be implemented for
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$ *
 * @param <T>
 *            the type of service returned from {@link #lookup(MutableAttributeMap)}
 */
public interface ServiceProvider<T> {

    /**
     * Called by a {@link Container} to lookup a specific service.
     * 
     * @param context
     *            the context the factory should use to decide what service should be returned
     * @return a service matching the specified context
     */
    T lookup(Context<T> context); // TODO return null or Throw an UOE??

    /** A Context is used as the parameter to {@link ServiceProvider#lookup(Context)} method. */
    interface Context<T> {

        /**
         * Return the key that is used for acquiring the service, as parsed along to {@link Container#getService(Class)}
         * or {@link Container#getService(Class, MutableAttributeMap)}.
         * 
         * @return the key that used for looking of the service.
         */
        Class<? extends T> getKey();

        /**
         * Return the attributes parsed to {@link Container#getService(Class, MutableAttributeMap)} or an empty
         * attribute map if {@link Container#getService(Class)} was called.
         * 
         * @return attribute map that was used for looking up the service
         */
        AttributeMap getAttributes();

        /**
         * This method can be called to allow other service providers to provide a service for the specified type.
         * 
         * @return an instance of the service
         */
        T handleNext();
    }
}
