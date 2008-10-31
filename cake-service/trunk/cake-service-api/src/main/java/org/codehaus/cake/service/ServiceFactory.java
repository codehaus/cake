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
import org.codehaus.cake.attribute.WithAttributes;

/**
 * 
 * ServiceFactory can be used for additional control.
 * 
 * The following can be used to introduce
 * A service factory can be implemented for
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ContainerConfiguration.java 559 2008-01-09 16:28:27Z kasper $ *
 * @param <T>
 *            the type of service returned from {@link #lookup(AttributeMap)}
 */
public interface ServiceFactory<T> {

    T lookup(ServiceFactoryContext<T> context);

    interface ServiceFactoryContext<T> extends WithAttributes {
        /**
         * @return the key that used for looking of the service, can be useful if a ServiceFactory is used for
         *         constructing multiple services
         */
        Class<? extends T> getKey();

        /**
         * @return attributes that was used for looking up the service
         * @see Container#getService(Class, AttributeMap)
         */
        AttributeMap getAttributes();

        /**
         * This method should be called if the lookup method cannot construct a service based upon the key and attribute
         * map
         * 
         * @return can be used for chaining instances
         */
        T handleNext();
    }
}
