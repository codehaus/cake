package org.codehaus.cake.internal.cache.service.memorystore;

import java.util.Comparator;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreMXBean;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.processor.CacheRequestFactory;
import org.codehaus.cake.internal.cache.processor.request.TrimToSizeRequest;
import org.codehaus.cake.internal.cache.processor.request.TrimToVolumeRequest;
import org.codehaus.cake.internal.service.configuration.ConfigurationService;
import org.codehaus.cake.management.annotation.ManagedAttribute;
import org.codehaus.cake.management.annotation.ManagedObject;
import org.codehaus.cake.management.annotation.ManagedOperation;
import org.codehaus.cake.service.annotation.ExportAsService;

/**
 * This class wraps CacheEvictionService as a CacheEvictionMXBean.
 * <p>
 * This class must be public to allow for reflection.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java 520 2007-12-21 17:53:31Z kasper $
 */
@ManagedObject(defaultValue = "MemoryStore", description = "MemoryStore attributes and operations")
@ExportAsService(MemoryStoreService.class)
public final class ExportedMemoryStoreService<K, V> implements MemoryStoreService<K, V>, MemoryStoreMXBean {
    private final CacheProcessor<K, V> processor;

    private final CacheRequestFactory<K, V> requestFactory;
    /** The service we are wrapping. */
    private final MemoryStore<?, ?> service;
    private final ConfigurationService settingsService;

    /**
     * Creates a new CacheEvictionMXBean by wrapping a CacheEvictionService.
     * 
     * @param service
     *            the service to wrap.
     */
    public ExportedMemoryStoreService(ConfigurationService settingsService, CacheRequestFactory<K, V> requestFactory,
            CacheProcessor<K, V> processor, MemoryStore<?, ?> service) {
        this.settingsService = settingsService;
        this.service = service;
        this.requestFactory = requestFactory;
        this.processor = processor;
    }

    /** {@inheritDoc} */
    public int getMaximumSize() {
        return settingsService.getAttributes().get(MemoryStoreAttributes.MAX_SIZE);
    }

    /** {@inheritDoc} */
    public long getMaximumVolume() {
        return settingsService.getAttributes().get(MemoryStoreAttributes.MAX_VOLUME);
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The current size of the cache")
    public int getSize() {
        return service.getSize();
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The current volume of the cache")
    public long getVolume() {
        return service.getVolume();
    }

    /** {@inheritDoc} */
    public boolean isDisabled() {
        return settingsService.getAttributes().get(MemoryStoreAttributes.IS_DISABLED);
    }

    /** {@inheritDoc} */
    public void setDisabled(boolean isDisabled) {
        settingsService.update(MemoryStoreAttributes.IS_DISABLED, isDisabled);
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The maximum size of the cache")
    public void setMaximumSize(int maximumSize) {
        settingsService.update(MemoryStoreAttributes.MAX_SIZE, maximumSize);
    }

    /** {@inheritDoc} */
    @ManagedAttribute(description = "The maximum volume of the cache")
    public void setMaximumVolume(long maximumVolume) {
        settingsService.update(MemoryStoreAttributes.MAX_VOLUME, maximumVolume);
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
