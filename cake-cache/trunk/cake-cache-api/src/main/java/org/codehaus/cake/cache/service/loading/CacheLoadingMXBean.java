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
package org.codehaus.cake.cache.service.loading;

import org.codehaus.cake.service.common.management.ManagementConfiguration;

/**
 * The management interface for the loading service.
 * <p>
 * This managed bean is only available at runtime if a cache loader has been set using
 * {@link CacheLoadingConfiguration#setLoader(BlockingCacheLoader)} or
 * {@link CacheLoadingConfiguration#setLoader(org.codehaus.cake.ops.Ops.Op)} and
 * {@link ManagementConfiguration#setEnabled(boolean)} has been set to <code>true</code>.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLoadingMXBean.java 415 2007-11-09 08:25:23Z kasper $
 */
public interface CacheLoadingMXBean {

    /**
     * Attempts to reload all entries that are currently held in the cache.
     */
    void forceLoadAll();

    /**
     * Attempts to reload all entries that needs reloading as decided by
     * {@link CacheLoadingConfiguration#getNeedsReloadFilter()}. If no filter has been set, calls to this method is
     * ignored.
     */
    void loadAll();
}
