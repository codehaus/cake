/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.service.memorystore;

import java.util.Comparator;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheAttributes;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.CacheServices;

/**
 * The memory store service controls the size of the cache and what entries to evict at runtime.
 * <p>
 * An instance of this interface can be retrieved by using {@link Cache#getService(Class)} to look
 * it up.
 * 
 * <pre>
 * Cache&lt;?, ?&gt; c = someCache;
 * MemoryStoreService&lt;?, ?&gt; mss = c.getService(MemoryStoreService.class);
 * mss.trimToSize(10);
 * </pre>
 * 
 * Or by using {@link CacheServices}:
 * 
 * <pre>
 * Cache&lt;?, ?&gt; c = someCache;
 * MemoryStoreService&lt;?, ?&gt; ces = c.with().memoryStore();
 * ces.setDisabled(true);
 * </pre>
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: MemoryStoreService.java 563 2008-01-10 15:20:33Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache containing this service
 * @param <V>
 *            the type of mapped values
 */
public interface MemoryStoreService<K, V> /* extends Map<K,V> */{

    /**
     * Returns the maximum number of elements that this cache can hold. If the cache has no upper
     * limit {@link Integer#MAX_VALUE} is returned.
     * 
     * @return the maximum number of elements that this cache can hold or {@link Integer#MAX_VALUE}
     *         if no such limit exist
     * @see #setMaximumSize(int)
     * @see Cache#size()
     */
    int getMaximumSize();

    /**
     * Returns the maximum allowed volume of the cache or {@link Long#MAX_VALUE} if there is no
     * limit.
     * 
     * @return the maximum allowed volume of the cache or {@link Long#MAX_VALUE} if there is no
     *         limit.
     * @see #setMaximumVolume(long)
     * @see Cache#volume()
     * @see CacheAttributes#ENTRY_SIZE
     * @see #getVolume()
     */
    long getMaximumVolume();

    /**
     * Returns the size of the cache. This method is equivalent to calling {@link Cache#size()} but
     * is provided here for convenience.
     * 
     * @return the current size of the cache
     */
    int getSize();

    /**
     * Returns the current volume of this cache. If the current volume of this cache is greater then
     * Long.MAX_VALUE, this method returns Long.MAX_VALUE.
     * <p>
     * The volume is defined as the sum of all entries {@link CacheAttributes#ENTRY_SIZE}.
     * 
     * @return the current volume of this cache
     * @see CacheAttributes#ENTRY_SIZE
     */
    long getVolume();

    /**
     * Returns whether or not caching is disabled.
     * 
     * @return <code>true</code> if caching is disabled, otherwise <code>false</code>
     * @see #setDisabled(boolean)
     */
    boolean isDisabled();

    /**
     * Sets whether or not caching is disabled. If caching is disabled, the cache will not cache any
     * new items that are added. This can sometimes be useful while testing.
     * <p>
     * Note: the following applies Note: setting this value to <code>true</code> will not remove
     * elements already present in the cache. Put will override values, already present. Loading of
     * values will override values already present?
     * 
     * @param isDisabled
     *            whether or not caching is disabled
     */
    void setDisabled(boolean isDisabled);

    /**
     * Sets that maximum number of elements that the cache is allowed to contain. If the limit is
     * reached the cache must evict existing elements before adding new elements. Preferable using
     * the replacement policy set using
     * {@link MemoryStoreConfiguration#setPolicy(org.codehaus.cake.cache.policy.ReplacementPolicy)}.
     * But if no policy is set, the cache is free to choose any other way to determine which
     * elements to remove.
     * <p>
     * To indicate that a cache can hold an unlimited number of items, {@link Integer#MAX_VALUE}
     * should be specified.
     * <p>
     * If the specified maximum size is 0, unless otherwise specified will mean unlimited amount
     * 
     * @param maximumSize
     *            the maximum number of elements the cache can hold or Integer.MAX_VALUE if there is
     *            no limit
     * @throws IllegalArgumentException
     *             if the specified maximum size is negative
     * @throws UnsupportedOperationException
     *             if the cache does not support changing the maximum size at runtime
     */
    void setMaximumSize(int maximumSize);

    /**
     * Sets that maximum volume of the cache. The total volume of the cache is the sum of all the
     * individual element sizes (sum of all elements {@link CacheEntry#getSize()}. If the limit is
     * reached the cache must evict existing elements before adding new elements. Preferable using
     * the replacement policy set using
     * {@link MemoryStoreConfiguration#setPolicy(org.codehaus.cake.cache.policy.ReplacementPolicy)}.
     * But if no policy is set, the cache is free to choose any other way to determine which
     * elements to remove.
     * <p>
     * To indicate that a cache can have an unlimited volume, {@link Long#MAX_VALUE} should be
     * specified.
     * 
     * @param maximumVolume
     *            the maximum volume.
     * @throws IllegalArgumentException
     *             if the specified maximum volume is negative
     * @throws UnsupportedOperationException
     *             if the cache does not support changing the maximum volume at runtime
     */
    void setMaximumVolume(long maximumVolume);

    /**
     * If the specified size is <tt>positive or 0</tt> this method will keeps evicting entries
     * until the size of the cache is equal to the specified size. If the specified size is greater
     * then the current size of the cache no action is taken.
     * <p>
     * If the specified size is negative this method will evict the number of entries specified. For
     * example, if -10 is specified then 10 entries will be evicted.
     * <p>
     * 
     * @param size
     *            if positive of 0, the size to the trim the cache down to, otherwise the number of
     *            elements to remove
     */
    void trimToSize(int size);

    /**
     * Works analogues to {@link #trimToSize(int)} except that specified comparator will choose the
     * ordering among the entries.
     * <p>
     * For example the following snippet will trim the cache downto 70 % of the current size,
     * removing those entries that was created earliest.
     * 
     * <pre>
     * Cache&lt;?, ?&gt; c = someCache;
     * MemoryStoreService&lt;?, ?&gt; mss = c.with().memoryStore();
     * mss.trimToSize((int) (mss.getSize() * 0.7), CacheAttributes.ENTRY_DATE_CREATED);
     * </pre>
     * 
     * @param size
     *            if positive of 0, the size to the trim the cache down to, otherwise the number of
     *            elements to remove
     * 
     * @param comparator
     *            used to determind order among entries
     */
    void trimToSize(int size, Comparator<? extends CacheEntry<K, V>> comparator);

    /**
     * If the specified volume is <tt>positive or 0</tt> this method will keeps evicting entries
     * until the volume of the cache is equal to the specified size. If the specified volume is
     * greater then the current volume of the cache no action is taken.
     * <p>
     * If the specified volume is negative this method will evict enough entries to make sure the
     * volume of the cache is at least reduced with the specified volume.
     * 
     * @param volume
     *            if positive of 0, the volume to the trim the cache down to, otherwise the amount
     *            the caches volume should be reduced with
     */
    void trimToVolume(long volume);

    /**
     * Works analogues to {@link #trimToVolume(long)} except that specified comparator will choose
     * the ordering among the entries.
     * <p>
     * For example the following snippet will trim the cache downto 50 % of the current size,
     * removing those entries that are less costly to refetch.
     * 
     * <pre>
     * Cache&lt;?, ?&gt; c = someCache;
     * MemoryStoreService&lt;?, ?&gt; mss = c.with().memoryStore();
     * mss.trimToVolume((long) (mss.getVolume() * 0.5), CacheAttributes.ENTRY_COST);
     * </pre>
     * 
     * The example assumes that the cache is configured to use the
     * {@link CacheAttributes#ENTRY_COST} attribute.
     * 
     * @param size
     *            if positive of 0, the size to the trim the cache down to, otherwise the number of
     *            elements to remove
     * 
     * @param comparator
     *            used to determind order among entries
     */
    void trimToVolume(long volume, Comparator<? extends CacheEntry<K, V>> comparator);
}
