package org.codehaus.cake.internal.cache.service.memorystore;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.InternalCacheAttributes;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.service.configuration.ConfigurationService;
import org.codehaus.cake.management.ManagedAttribute;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.ExportAsService;

@ExportAsService(MemoryStoreService.class)
public class ExportedSynchronizedMemoryStoreService<K, V> extends ExportedMemoryStoreService<K, V> {

    private final Object mutex;

    public ExportedSynchronizedMemoryStoreService(Cache<K, V> cache, ConfigurationService configurationService,
            CacheRequestFactory<K, V> requestFactory, CacheProcessor<K, V> processor, MemoryStore<K, V> memoryStore) {
        super(configurationService, requestFactory, processor, memoryStore);
        this.mutex = cache;
    }

    ExportedSynchronizedMemoryStoreService(ExportedSynchronizedMemoryStoreService<K, V> service,
            Predicate<CacheEntry<K, V>> predicate) {
        super(service, predicate);
        this.mutex = service.mutex;
    }

    @ManagedAttribute(description = "The current size of the cache")
    public int getSize() {
        synchronized (mutex) {
            return super.getSize();
        }
    }

    @ManagedAttribute(description = "The current volume of the cache")
    public long getVolume() {
        synchronized (mutex) {
            return super.getVolume();
        }
    }

    public ExportedMemoryStoreService<K, V> lookup(
            org.codehaus.cake.service.ServiceFactory.ServiceFactoryContext<ExportedMemoryStoreService<K, V>> context) {
        Predicate p = context.getAttributes().get(InternalCacheAttributes.CACHE_FILTER);
        return p == null ? this : new ExportedSynchronizedMemoryStoreService<K, V>(this, p);
    }

}
