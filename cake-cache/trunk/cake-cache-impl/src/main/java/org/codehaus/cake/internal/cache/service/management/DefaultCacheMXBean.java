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
//        if (cache == null) {
//            throw new NullPointerException("cache is null");
//        }
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
