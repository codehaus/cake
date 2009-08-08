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
package org.codehaus.cake.cache.loading;

import org.codehaus.cake.management.ManagementConfiguration;

/**
 * The management interface for the loading service.
 * <p>
 * This managed bean is only available at runtime if a cache loader has been set up using
 * {@link CacheLoadingConfiguration#setLoader(Object) and {@link ManagementConfiguration#setEnabled(boolean)} has been
 * set to <code>true</code>.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLoadingMXBean.java 327 2009-04-08 09:34:27Z kasper $
 */
public interface CacheLoadingMXBean {

    /**
     * Attempts to forcefully reload all entries that are currently held in the cache. This is equivalent to calling
     * 
     * <pre>
     * cache.with().loadingForced().loadAll();
     * </pre>
     */
    void forceLoadAll();

    /**
     * Attempts to reload all entries that needs reloading is the cache. This is equivalent to calling
     * 
     * <pre>
     * cache.with().loading().loadAll();
     * </pre>
     */
    void loadAll();
}
