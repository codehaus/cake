package org.codehaus.cake.internal.cache.service.memorystore;

import java.util.Comparator;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.internal.cache.service.attribute.InternalAttributeService;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;

public class SynchronizedHashMapMemoryStore<K, V> extends HashMapMemoryStore<K, V> {

    private final Object mutex;

    public SynchronizedHashMapMemoryStore(Cache cache, MemoryStoreConfiguration<K, V> storeConfiguration,
            InternalAttributeService<K, V> attributeService, InternalCacheExceptionService<K, V> ies) {
        super(storeConfiguration, attributeService, ies);
        this.mutex = cache;
    }

    public boolean isDisabled() {
        synchronized (mutex) {
            return super.isDisabled();
        }
    }

    public void setDisabled(boolean isDisabled) {
        synchronized (mutex) {
            super.setDisabled(isDisabled);
        }
    }

    public int getMaximumSize() {
        synchronized (mutex) {
            return super.getMaximumSize();
        }
    }

    public long getMaximumVolume() {
        synchronized (mutex) {
            return super.getMaximumVolume();
        }
    }

    public int getSize() {
        synchronized (mutex) {
            return super.getSize();
        }
    }

    public long getVolume() {
        synchronized (mutex) {
            return super.getVolume();
        }
    }

    public void setMaximumSize(int maximumSize) {
        synchronized (mutex) {
            super.setMaximumSize(maximumSize);
        }
    }

    public void setMaximumVolume(long maximumVolume) {
        synchronized (mutex) {
            super.setMaximumVolume(maximumVolume);
        }
    }

    public void trimToSize(int size) {
        synchronized (mutex) {
            super.trimToSize(size);
        }
    }

    public void trimToSize(int size, Comparator<? extends CacheEntry<K, V>> comparator) {
        synchronized (mutex) {
            super.trimToSize(size, comparator);
        }
    }

    public void trimToVolume(long volume) {
        synchronized (mutex) {
            super.trimToVolume(volume);
        }
    }

    public void trimToVolume(long volume, Comparator<? extends CacheEntry<K, V>> comparator) {
        synchronized (mutex) {
            super.trimToVolume(volume, comparator);
        }
    }

}
