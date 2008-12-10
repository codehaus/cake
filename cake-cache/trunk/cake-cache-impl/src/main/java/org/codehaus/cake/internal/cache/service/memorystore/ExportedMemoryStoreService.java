package org.codehaus.cake.internal.cache.service.memorystore;

import java.util.Comparator;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreMXBean;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.InternalCacheAttributes;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;
import org.codehaus.cake.internal.service.configuration.ConfigurationService;
import org.codehaus.cake.management.ManagedAttribute;
import org.codehaus.cake.management.ManagedObject;
import org.codehaus.cake.management.ManagedOperation;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.ExportAsService;
import org.codehaus.cake.service.ServiceFactory;

/**
 * This class wraps CacheEvictionService as a CacheEvictionMXBean.
 * <p>
 * This class must be public to allow for reflection.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
@ManagedObject(defaultValue = "MemoryStore", description = "MemoryStore attributes and operations")
@ExportAsService(MemoryStoreService.class)
public class ExportedMemoryStoreService<K, V> implements ServiceFactory<ExportedMemoryStoreService<K, V>>,
        MemoryStoreService<K, V>, MemoryStoreMXBean {

    /** The configuration service. */
    private final ConfigurationService configurationService;

    /** A filter that is applied to the cache */
    private final Predicate<CacheEntry<K, V>> filter;
    /** The memory store we are exposing. */
    private final MemoryStore<K, V> memoryStore;
    /** The processor for trimtoxxx requests. */
    private final CacheProcessor<K, V> processor;

    private final CacheRequestFactory<K, V> requestFactory;

    /**
     * Creates a new CacheEvictionMXBean by wrapping a CacheEvictionService.
     * 
     * @param memoryStore
     *            the service to wrap.
     */
    public ExportedMemoryStoreService(ConfigurationService configurationService,
            CacheRequestFactory<K, V> requestFactory, CacheProcessor<K, V> processor, MemoryStore<K, V> memoryStore) {
        this.configurationService = configurationService;
        this.memoryStore = memoryStore;
        this.requestFactory = requestFactory;
        this.processor = processor;
        this.filter = null;
    }

    ExportedMemoryStoreService(ExportedMemoryStoreService parent, Predicate<CacheEntry<K, V>> predicate) {
        this.configurationService = parent.configurationService;
        this.memoryStore = parent.memoryStore;
        this.requestFactory = parent.requestFactory;
        this.processor = parent.processor;
        this.filter = predicate;
    }

    /** {@inheritDoc} */
    public int getMaximumSize() {
        return configurationService.getAttributes().get(MemoryStoreAttributes.MAX_SIZE);
    }

    /** {@inheritDoc} */
    public long getMaximumVolume() {
        return configurationService.getAttributes().get(MemoryStoreAttributes.MAX_VOLUME);
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The current size of the cache")
    public int getSize() {
        return memoryStore.size(filter);
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The current volume of the cache")
    public long getVolume() {
        return memoryStore.getVolume(filter);
    }

    /** {@inheritDoc} */
    public boolean isDisabled() {
        return configurationService.getAttributes().get(MemoryStoreAttributes.IS_DISABLED);
    }

    public ExportedMemoryStoreService<K, V> lookup(
            org.codehaus.cake.service.ServiceFactory.ServiceFactoryContext<ExportedMemoryStoreService<K, V>> context) {
        Predicate p = context.getAttributes().get(InternalCacheAttributes.CACHE_FILTER);
        return p == null ? this : new ExportedMemoryStoreService<K, V>(this, p);
    }

    /** {@inheritDoc} */
    public void setDisabled(boolean isDisabled) {
        configurationService.update(MemoryStoreAttributes.IS_DISABLED, isDisabled);
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The maximum size of the cache")
    public void setMaximumSize(int maximumSize) {
        configurationService.update(MemoryStoreAttributes.MAX_SIZE, maximumSize);
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The maximum volume of the cache")
    public void setMaximumVolume(long maximumVolume) {
        configurationService.update(MemoryStoreAttributes.MAX_VOLUME, maximumVolume);
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Trims the cache to the specified size")
    public void trimToSize(int size) {
        TrimToSizeRequest<K, V> r = requestFactory.createTrimToSizeRequest(size, null);
        processor.process(r);
    }

    /** {@inheritDoc} */
    public void trimToSize(int size, Comparator<? extends CacheEntry<K, V>> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        TrimToSizeRequest<K, V> r = requestFactory.createTrimToSizeRequest(size, comparator);
        processor.process(r);
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Trims the cache to the specified volume")
    public void trimToVolume(long volume) {
        TrimToVolumeRequest<K, V> r = requestFactory.createTrimToVolumeRequest(volume, null);
        processor.process(r);
    }

    /** {@inheritDoc} */
    public void trimToVolume(long volume, Comparator<? extends CacheEntry<K, V>> comparator) {
        if (comparator == null) {
            throw new NullPointerException("comparator is null");
        }
        TrimToVolumeRequest<K, V> r = requestFactory.createTrimToVolumeRequest(volume, comparator);
        processor.process(r);
    }
}
