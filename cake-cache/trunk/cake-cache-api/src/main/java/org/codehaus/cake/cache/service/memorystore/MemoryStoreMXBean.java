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
package org.codehaus.cake.cache.service.memorystore;

import org.codehaus.cake.cache.CacheMXBean;

/**
 * The management interface for the eviction service.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public interface MemoryStoreMXBean {

    int getMaximumSize();

    long getMaximumVolume();

    /**
     * Returns the current size of the cache. This is the same value returned by {@link CacheMXBean#getSize()}, but is
     * also provided here for convenience.
     * 
     * @return the size of the cache
     */
    int getSize();

    /**
     * Returns the current volume of this cache. If the current volume of this cache is greater then Long.MAX_VALUE,
     * this method returns Long.MAX_VALUE.
     * 
     * @return the current volume of this cache
     */
    long getVolume();

    void setMaximumSize(int maximumSize);

    void setMaximumVolume(long maximumVolume);

    /**
     * Keeps evicting entries until the size of the cache is equal to the specified size. If the specified size is
     * greater then the current size of the cache no action is taken.
     * 
     * @param size
     *            the number of elements to trim the cache down to
     */
    void trimToSize(int size);

    /**
     * Keeps evicting entries until the volume of the cache is equal to or less then the specified volume. If the
     * specified volume is greater then the current volume no action is taken.
     * 
     * @param volume
     *            the volume to trim the cache down to
     */
    void trimToVolume(long volume);
}
