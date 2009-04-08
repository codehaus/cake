/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache.service.memorystore;

import org.codehaus.cake.cache.CacheMXBean;

/**
 * The management interface for the memory store service.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public interface MemoryStoreMXBean {

    /**
     * Returns the maximum size of the cache, or {@link Integer#MAX_VALUE} if the cache no maximum size.
     * 
     * @return the maximum size of the cache
     * @see #setMaximumSize(int)
     * @see MemoryStoreService#getMaximumSize()
     */
    int getMaximumSize();

    long getMaximumVolume();

    /**
     * Returns the current size of the cache. This is the same value as returned by {@link CacheMXBean#getSize()}, but
     * is also provided here for convenience.
     * 
     * @return the size of the cache
     */
    int getSize();

    /**
     * Returns the current volume of the cache. If the current volume of the cache is greater then Long.MAX_VALUE, this
     * method returns Long.MAX_VALUE.
     * 
     * @return the current volume of this cache
     */
    long getVolume();

    void setMaximumSize(int maximumSize);

    void setMaximumVolume(long maximumVolume);

    /**
     * If the specified size is <tt>positive or 0</tt> this method will keeps evicting entries until the size of the
     * cache is equal to the specified size. If the specified size is equals to or greater then the current size of the
     * cache no action is taken.
     * <p>
     * If the specified size is negative this method will evict the number of entries specified. For example, if -10 is
     * specified then 10 entries will be evicted.
     * <p>
     * If the memory store
     * {@link MemoryStoreConfiguration#setPolicy(org.codehaus.cake.cache.policy.ReplacementPolicy) uses a replacement policy}
     * the elements will be removed accordingly to the policy. If no policy has been configured, the cache is free to
     * choose any other way to determine which elements to remove.
     * 
     * @param size
     *            if positive of 0, the size to the trim the cache down to, otherwise the number of elements to remove
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
