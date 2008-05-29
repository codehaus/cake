package org.codehaus.cake.cache;

import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.service.ServiceManager;
import org.codehaus.cake.service.executor.ExecutorsService;

/**
 * A utility class to get hold of different cache services in an easy and typesafe manner. For
 * example, the following will return the {@link MemoryStoreService} for a given cache.
 * 
 * <pre>
 * Cache&lt;Integer, String&gt; cache = somecache;
 * MemoryStoreService&lt;Integer, String&gt; service = cache.with().memoryStore();
 * service.trimToSize(10);
 * </pre>
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheServices.java 469 2007-11-17 14:32:25Z kasper $
 */
public class CacheServices<K, V> {

    /** The service manager to extract cache services from. */
    private final ServiceManager serviceManager;

    /**
     * Creates a new {@link CacheServices} from the specified {@link Cache}
     * 
     * @param cache
     *            the cache to retrieve services from
     */
    public CacheServices(Cache<?, ?> cache) {
        this.serviceManager = cache;
    }

    /**
     * Returns the cache loading service.
     * 
     * @return the cache loading service for the cache
     * @throws UnsupportedOperationException
     *             if no cache loading service is available
     * @param <K>
     *            the type of keys maintained by the cache
     * @param <V>
     *            the type of mapped values
     */
    public CacheLoadingService<K, V> loading() {
        return getService(CacheLoadingService.class);
    }

    /**
     * Returns the worker service.
     * 
     * @return the worker service for the cache
     * @throws UnsupportedOperationException
     *             if no worker service is available
     */
    public ExecutorsService executors() {
        return getService(ExecutorsService.class);
    }

    /**
     * Returns the memory store service.
     * 
     * @return the memory store service for the cache
     * @throws UnsupportedOperationException
     *             if no memory store service is available
     * @param <K>
     *            the type of keys maintained by the cache
     * @param <V>
     *            the type of mapped values
     */
    public MemoryStoreService<K, V> memoryStore() {
        return getService(MemoryStoreService.class);
    }

    /**
     * This method can be called by subclasses to retrieve services from the cache that this object
     * is wrapping.
     * 
     * @param <T>
     *            the type of service
     * @param serviceType
     *            the type of services
     * @return an instance of the specified type
     * @throws UnsupportedOperationException
     *             if no service of the specified type is available
     */
    protected <T> T getService(Class<T> serviceType) {
        return serviceManager.getService(serviceType);
    }
}
