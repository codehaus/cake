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
package org.codehaus.cake.cache;

import java.util.concurrent.ScheduledExecutorService;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.service.ServiceManager;

/**
 * A utility class to get hold of different cache services in an easy and typesafe manner. For example, the following
 * will return the {@link MemoryStoreService} for a given cache.
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

    /** A singleton attribute map which forces loading. */
    private static final AttributeMap FORCE_LOAD = CacheLoadingService.IS_FORCED.singleton(true);

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

    public <R> CrudWriter<K, V, R> write(Op<CacheEntry<K, V>, R> extractor) {
        return getService(CrudWriter.class, CrudWriter.WRITE_TRANSFORMER.singleton(extractor));
    }

    public CrudWriter<K, V, CacheEntry<K, V>> writeReturnEntry() {
        return write((Op) CacheDataExtractor.WHOLE_ENTRY);
    }

    public CrudWriter<K, V, V> writeReturnValue() {
        return write((Op) CacheDataExtractor.ONLY_VALUE);
    }

    /**
     * This method can be called by subclasses to retrieve services from the cache that this object is wrapping.
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

    /**
     * This method can be called by subclasses to retrieve services from the cache that this object is wrapping.
     * 
     * @param <T>
     *            the type of service
     * @param serviceType
     *            the type of services
     * @param attributes
     *            map of attributes
     * @return an instance of the specified type with the specified attributes
     * @throws UnsupportedOperationException
     *             if no service of the specified type and attributes is available
     */
    protected <T> T getService(Class<T> serviceType, AttributeMap attributes) {
        return serviceManager.getService(serviceType, attributes);
    }

    /**
     * Returns the cache loading service.
     * 
     * @return the cache loading service for the cache
     * @throws UnsupportedOperationException
     *             if no cache loading service is available
     */
    @SuppressWarnings("unchecked")
    public CacheLoadingService<K, V> loading() {
        return getService(CacheLoadingService.class);
    }

    @SuppressWarnings("unchecked")
    public CacheLoadingService<K, V> loadingForced() {
        return getService(CacheLoadingService.class, FORCE_LOAD);
    }

    /**
     * Returns the memory store service.
     * 
     * @return the memory store service for the cache
     * @throws UnsupportedOperationException
     *             if no memory store service is available
     */
    @SuppressWarnings("unchecked")
    public MemoryStoreService<K, V> memoryStore() {
        return getService(MemoryStoreService.class);
    }

    /**
     * Returns the default {@link ScheduledExecutorService}.
     * 
     * @return the default scheduled executor service for the cache
     * @throws UnsupportedOperationException
     *             if no scheduled executor service is available
     */
    public ScheduledExecutorService scheduledExecutor() {
        return getService(ScheduledExecutorService.class);
    }
}
