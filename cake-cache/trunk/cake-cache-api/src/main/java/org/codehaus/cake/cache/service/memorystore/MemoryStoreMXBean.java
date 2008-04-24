/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.service.memorystore;

/**
 * The management interface for the eviction service.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: MemoryStoreMXBean.java 559 2008-01-09 16:28:27Z kasper $
 */
public interface MemoryStoreMXBean {

    /**
     * Keeps evicting entries until the size of the cache is equal to the specified size. If the
     * specified size is greater then the current size of the cache no action is taken.
     * 
     * @param size
     *            the number of elements to trim the cache down to
     */
    void trimToSize(int size);

    /**
     * Keeps evicting entries until the volume of the cache is equal to or less then the specified
     * volume. If the specified volume is greater then the current volume no action is taken.
     * 
     * @param volume
     *            the volume to trim the cache down to
     */
    void trimToVolume(long volume);

    /**
     * Returns the current size of the cache. But is provided here for convenience.
     * 
     * @return
     */
    int getSize();

    /**
     * Returns the current volume of this cache. If the current volume of this cache is greater then
     * Long.MAX_VALUE, this method returns Long.MAX_VALUE.
     * 
     * @return the current volume of this cache
     */
    long getVolume();

    long getMaximumVolume();

    int getMaximumSize();

    void setMaximumVolume(long maximumVolume);

    void setMaximumSize(int maximumSize);
}
