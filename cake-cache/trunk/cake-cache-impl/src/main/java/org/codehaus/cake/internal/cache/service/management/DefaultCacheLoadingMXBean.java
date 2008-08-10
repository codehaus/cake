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
package org.codehaus.cake.internal.cache.service.management;

import org.codehaus.cake.cache.service.loading.CacheLoadingMXBean;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.management.annotation.ManagedOperation;

/**
 * A class that exposes a {@link CacheLoadingService} as a {@link CacheLoadingMXBean}.
 * <p>
 * This class must be public to allow for reflection.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java 520 2007-12-21 17:53:31Z kasper $
 */
public final class DefaultCacheLoadingMXBean implements CacheLoadingMXBean {

    /** The forced CacheLoadingService that is being wrapped. */
    private final CacheLoadingService<?, ?> forced;

    /** The non-forced CacheLoadingService that is being wrapped. */
    private final CacheLoadingService<?, ?> noForce;

    /**
     * Creates a new DelegatedCacheLoadingMXBean.
     * 
     * @param noForce
     *            the non-forced CacheLoadingService to wrap
     * @param forced
     *            the forced CacheLoadingService to wrap
     */
    public DefaultCacheLoadingMXBean(CacheLoadingService<?, ?> noForce, CacheLoadingService<?, ?> forced) {
        if (forced == null) {
            throw new NullPointerException("service is null");
        } else if (noForce == null) {
            throw new NullPointerException("noForce is null");
        }
        this.forced = forced;
        this.noForce = noForce;
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "reload all mappings")
    public void forceLoadAll() {
        forced.loadAll();
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Attempts to reload all entries that are either expired or which needs refreshing")
    public void loadAll() {
        noForce.loadAll();
    }
}
