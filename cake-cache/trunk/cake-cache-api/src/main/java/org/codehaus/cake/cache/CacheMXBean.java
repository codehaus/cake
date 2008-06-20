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
package org.codehaus.cake.cache;

import javax.management.MBeanServer;

import org.codehaus.cake.service.management.ManagementConfiguration;

/**
 * The management interface for a {@link Cache}. Some cache implementations might define additional methods in addition
 * to those defined in this interface. However, all implementations, supporting JMX, must as a minimum support this
 * interface.
 * <p>
 * If no domain is specified using {@link ManagementConfiguration#setDomain(String)} and no {@link MBeanServer} is
 * specified using {@link ManagementConfiguration#setMBeanServer(MBeanServer)}. This MXBean will be registered under
 * <code>org.codehaus.cake.cache:name=$CACHE_NAME$,service=General</code> where <code>$CACHE_NAME$</code> is
 * replaced by the {@link Cache#getName() name} of the cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheMXBean.java 555 2008-01-09 04:52:48Z kasper $
 */
public interface CacheMXBean {

    /**
     * This is service name this interface will be registered under.
     */
    String MANAGED_SERVICE_NAME = "General";

    /**
     * Returns the current number of elements in the cache.
     * <p>
     * The returned value is equivalent to the value returned by {@link Cache#size()}.
     * 
     * @return the number of elements in the cache
     */
    int getSize();

    /**
     * Returns the name of the cache.
     * <p>
     * The returned value is equivalent to the value returned by {@link Cache#getName()}.
     * 
     * @return the name of the cache
     */
    String getName();

    /**
     * Clears and removes any element in the cache.
     * <p>
     * Calling this method is effectively equivalent to calling {@link Cache#clear()}.
     */
    void clear();
}
