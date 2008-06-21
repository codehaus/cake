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

import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.internal.cache.service.management.DefaultMemoryStoreMXBean;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;

public abstract class AbstractMemoryStore<K, V> implements MemoryStore<K, V>, Manageable, MemoryStoreService<K, V> {

    /** {@inheritDoc} */
    public void manage(ManagedGroup parent) {
        ManagedGroup g = parent.addChild("MemoryStore", "MemoryStore attributes and operations");
        g.add(new DefaultMemoryStoreMXBean(this));
    }

    /**
     * Returns the maximum size configured in the specified configuration.
     * 
     * @param conf
     *            the configuration to read the maximum size from
     * @return the maximum size configured in the specified configuration
     */
    static int initializeMaximumSize(MemoryStoreConfiguration<?, ?> conf) {
        int tmp = conf.getMaximumSize();
        return tmp == 0 ? Integer.MAX_VALUE : tmp;
    }

    /**
     * Returns the maximum volume configured in the specified configuration.
     * 
     * @param conf
     *            the configuration to read the maximum volume from
     * @return the maximum volume configured in the specified configuration
     */
    static long initializeMaximumVolume(MemoryStoreConfiguration<?, ?> conf) {
        long tmp = conf.getMaximumVolume();
        return tmp == 0 ? Long.MAX_VALUE : tmp;
    }
}
