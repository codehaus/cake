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
package org.codehaus.cake.cache;

import org.codehaus.cake.management.ManagementConfiguration;

/**
 * The management interface for a {@link Cache}. Some cache implementations might define additional methods in addition
 * to those defined in this interface. However, all implementations, supporting JMX, must as a minimum support this
 * interface.
 * <p>
 * Management via JMX is disabled per default, but can be enabled by calling
 * <tt>CacheConfiguration.withManagement().setEnabled(true)</tt>
 * <p>
 * If no domain is specified using {@link ManagementConfiguration#setDomain(String)}. This managed bean will be
 * registered under <code>org.codehaus.cake.cache:name=$CACHE_NAME$,service=General</code> where
 * <code>$CACHE_NAME$</code> is replaced by the named returned from {@link Cache#getName()}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public interface CacheMXBean {

    /** This is the service name this interface will be registered under. */
    String MANAGED_SERVICE_NAME = "General";

    /**
     * Clears and removes any element in the cache.
     * <p>
     * Calling this method is effectively equivalent to calling {@link Cache#clear()}.
     */
    void clear();

    /**
     * Returns the name of the cache.
     * <p>
     * The returned value is equivalent to the value returned by {@link Cache#getName()}.
     * 
     * @return the name of the cache
     */
    String getName();

    /**
     * Returns the current number of elements in the cache.
     * <p>
     * The returned value is equivalent to the value returned by {@link Cache#size()}.
     * 
     * @return the number of elements in the cache
     */
    long getSize();
}
