package org.codehaus.cake.internal.cache.service.management;

import org.codehaus.cake.cache.service.loading.CacheLoadingMXBean;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.management.annotation.ManagedOperation;

/**
 * A class that exposes a {@link CacheLoadingService} as a {@link CacheLoadingMXBean}.
 */
public final class DefaultCacheLoadingMXBean implements CacheLoadingMXBean {

    /** The CacheLoadingService that is wrapped. */
    private final CacheLoadingService<?, ?> forced;

    private final CacheLoadingService<?, ?> noForce;

    /**
     * Creates a new DelegatedCacheLoadingMXBean.
     * 
     * @param service
     *            the CacheLoadingService to wrap
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
