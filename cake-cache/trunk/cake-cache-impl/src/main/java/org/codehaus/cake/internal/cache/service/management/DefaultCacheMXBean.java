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

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheMXBean;
import org.codehaus.cake.management.annotation.ManagedAttribute;
import org.codehaus.cake.management.annotation.ManagedOperation;

/**
 * A class that exposes a {@link Cache} as a {@link CacheMXBean}.
 */
public final class DefaultCacheMXBean implements CacheMXBean {
    /** The cache that is wrapped. */
    private final Cache<?, ?> cache;

    /**
     * Creates a new DelegatedCacheMXBean.
     * 
     * @param cache
     *            the cache to wrap
     */
    public DefaultCacheMXBean(Cache<?, ?> cache) {
        if (cache == null) {
            throw new NullPointerException("cache is null");
        }
        this.cache = cache;
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Clears the cache")
    public void clear() {
        cache.clear();
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The name of the cache")
    public String getName() {
        return cache.getName();
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The number of elements contained in the cache")
    public int getSize() {
        return cache.size();
    }
}
