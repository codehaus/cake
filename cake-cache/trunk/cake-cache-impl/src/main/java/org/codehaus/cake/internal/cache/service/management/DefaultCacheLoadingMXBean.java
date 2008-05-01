package org.codehaus.cake.internal.cache.service.management;

import org.codehaus.cake.cache.loading.CacheLoadingMXBean;
import org.codehaus.cake.cache.loading.CacheLoadingService;
import org.codehaus.cake.management.annotation.ManagedOperation;

/**
 * A class that exposes a {@link CacheLoadingService} as a {@link CacheLoadingMXBean}.
 */
public final class DefaultCacheLoadingMXBean implements CacheLoadingMXBean {

    /** The CacheLoadingService that is wrapped. */
    private final CacheLoadingService<?, ?> service;

    /**
     * Creates a new DelegatedCacheLoadingMXBean.
     * 
     * @param service
     *            the CacheLoadingService to wrap
     */
    public DefaultCacheLoadingMXBean(CacheLoadingService<?, ?> service) {
        if (service == null) {
            throw new NullPointerException("service is null");
        }
        this.service=service;
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "reload all mappings")
    public void forceLoadAll() {
        service.withAll().forceLoad();
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Attempts to reload all entries that are either expired or which needs refreshing")
    public void loadAll() {
        service.withAll().load();
    }
}
