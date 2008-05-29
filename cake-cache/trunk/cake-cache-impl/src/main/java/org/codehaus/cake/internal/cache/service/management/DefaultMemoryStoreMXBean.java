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
