package org.codehaus.cake.internal.cache.loading;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.loading.CacheLoadingMXBean;
import org.codehaus.cake.cache.loading.CacheLoadingService;
import org.codehaus.cake.management.ManagedObject;
import org.codehaus.cake.management.ManagedOperation;

@ManagedObject(defaultValue = "Loading", description = "Cache Loading attributes and operations")
public class DefaultCacheLoadingMXBean implements CacheLoadingMXBean {
    private final Cache<?, ?> cache;
    private volatile CacheLoadingService<?, ?> forced;
    private volatile CacheLoadingService<?, ?> notforced;

    public DefaultCacheLoadingMXBean(Cache<?, ?> cache) {
        if (cache == null) {
            throw new NullPointerException("cache is null");
        }
        this.cache = cache;
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Reload all mappings")
    public void forceLoadAll() {
        CacheLoadingService<?, ?> forced = this.forced;
        if (forced == null) {
            this.forced = forced = cache.with().loadingForced();
        }
        if (forced == null) {
            throw new UnsupportedOperationException();
        }
        forced.loadAll();
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Attempts to reload all entries that are either expired or which needs refreshing")
    public void loadAll() {
        CacheLoadingService<?, ?> notforced = this.notforced;
        if (notforced == null) {
            this.notforced = notforced = cache.with().loading();
        }
        if (notforced == null) {
            throw new UnsupportedOperationException();
        }
        notforced.loadAll();
    }
}
