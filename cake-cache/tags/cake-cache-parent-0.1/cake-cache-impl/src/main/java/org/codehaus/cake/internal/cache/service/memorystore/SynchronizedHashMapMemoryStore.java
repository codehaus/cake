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
