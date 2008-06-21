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

import org.codehaus.cake.cache.service.memorystore.MemoryStoreMXBean;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.management.annotation.ManagedAttribute;
import org.codehaus.cake.management.annotation.ManagedOperation;

/**
 * This class wraps CacheEvictionService as a CacheEvictionMXBean.
 * <p>
 * Must be a public class to allow for reflection.
 */
public final class DefaultMemoryStoreMXBean implements MemoryStoreMXBean {
    /** The service we are wrapping. */
    private final MemoryStoreService<?, ?> service;

    /**
     * Creates a new CacheEvictionMXBean by wrapping a CacheEvictionService.
     * 
     * @param service
     *            the service to wrap.
     */
    public DefaultMemoryStoreMXBean(MemoryStoreService<?, ?> service) {
        if (service == null) {
            throw new NullPointerException("service is null");
        }
        this.service = service;
    }

    /** {@inheritDoc} */
    public int getMaximumSize() {
        return service.getMaximumSize();
    }

    /** {@inheritDoc} */
    public long getMaximumVolume() {
        return service.getMaximumVolume();
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The maximum size of the cache")
    public void setMaximumSize(int maximumSize) {
        service.setMaximumSize(maximumSize);
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The maximum volume of the cache")
    public void setMaximumVolume(long maximumVolume) {
        service.setMaximumVolume(maximumVolume);
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Trims the cache to the specified size")
    public void trimToSize(int size) {
        service.trimToSize(size);
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Trims the cache to the specified volume")
    public void trimToVolume(long capacity) {
        service.trimToVolume(capacity);
    }

    @ManagedAttribute(description = "The current volume of the cache")
    public long getVolume() {
        return service.getVolume();
    }

    @ManagedAttribute(description = "The current size of the cache")
    public int getSize() {
        return service.getSize();
    }
}
