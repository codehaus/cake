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
package org.codehaus.cake.cache.memorystore;

import java.util.Comparator;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.CacheServices;

/**
 * The memory store service controls the size of the cache and what entries to evict at runtime.
 * <p>
 * An instance of this interface can be retrieved by using {@link Cache#getService(Class)} to look it up.
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
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public interface MemoryStoreService<K, V> {

    /**
     * Returns the maximum number of elements that the cache can hold. If the cache has no upper limit
     * {@link Integer#MAX_VALUE} is returned.
     * 
     * @return the maximum number of elements that this cache can hold or {@link Integer#MAX_VALUE} if no such limit
     *         exist
     * @see #setMaximumSize(int)
     * @see #getSize()
     */
    long getMaximumSize();

    /**
     * Returns the maximum allowed volume of the cache or {@link Long#MAX_VALUE} if there is no limit.
     * 
     * @return the maximum allowed volume of the cache or {@link Long#MAX_VALUE} if there is no limit.
     * @see CacheEntry#SIZE
     * @see #setMaximumVolume(long)
     * @see #getVolume()
     */
    long getMaximumVolume();

    /**
     * Returns the number of elements in the cache. This method is equivalent to calling {@link Cache#size()} but is
     * provided here for convenience.
     * 
     * @return the current size of the cache
     * @see Cache#size()
     */
    long getSize();

    /**
     * Returns the current volume of this cache. If the current volume of this cache is greater then Long.MAX_VALUE,
     * this method returns Long.MAX_VALUE.
     * <p>
     * The volume of a cache is defined as the sum of all entries {@link CacheEntry#SIZE}. This is equivalent to
     * 
     * <pre>
     * Cache&lt;?, ?&gt; cache = some cache;
     * long volume = 0;
     * for (CacheEntry&lt;?, ?&gt; e : cache) {
     *     volume += e.getAttributes().get(CacheEntry.SIZE);
     * }
     * </pre>
     * 
     * @return the current volume of this cache
     * @see CacheEntry#SIZE
     * 
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
     * Sets whether or not caching is disabled. If caching is disabled, the cache will not cache any new items that are
     * added. This can sometimes be useful while testing.
     * <p>
     * Note: Setting this value to <code>true</code> will not remove elements already present in the cache. Put will
     * override values, already present. Loading of values will override values already present?
     * 
     * @param isDisabled
     *            whether or not caching is disabled
     */
    void setDisabled(boolean isDisabled);

    /**
     * Sets that maximum number of elements that the cache is allowed to contain. If the limit is reached the cache must
     * evict existing elements before adding new elements. Using the replacement policy set using
     * {@link MemoryStoreConfiguration#setPolicy(org.codehaus.cake.cache.policy.ReplacementPolicy)}. If no policy is
     * set, the cache is free to choose to what elements to remove.
     * <p>
     * To indicate that a cache can hold an unlimited number of items, {@link Integer#MAX_VALUE} should be specified.
     * <p>
     * If the specified maximum size is 0, unless otherwise specified will mean unlimited amount
     * 
     * @param maximumSize
     *            the maximum number of elements the cache can hold or Integer.MAX_VALUE if there should be no limit
     * @throws IllegalArgumentException
     *             if the specified maximum size is negative
     * @throws UnsupportedOperationException
     *             if the cache does not support changing the maximum size at runtime
     */
    void setMaximumSize(long maximumSize);

    /**
     * Sets that maximum volume of the cache. The total volume of the cache is the sum of all the individual element
     * sizes (sum of all elements {@link CacheEntry#SIZE}. If the limit is reached the cache must evict existing
     * elements before adding new elements. Using the replacement policy set using
     * {@link MemoryStoreConfiguration#setPolicy(org.codehaus.cake.cache.policy.ReplacementPolicy)}. If no policy is
     * set, the cache is free to choose any other way to determine which elements to remove.
     * <p>
     * To indicate that a cache can have an unlimited volume, {@link Long#MAX_VALUE} should be specified.
     * 
     * @param maximumVolume
     *            the maximum volume or Long.MAX_VALUE if there should be no limit
     * @throws IllegalArgumentException
     *             if the specified maximum volume is negative
     * @throws UnsupportedOperationException
     *             if the cache does not support changing the maximum volume at runtime
     */
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
    void trimToSize(long size);

    /**
     * Works analogues to {@link #trimToSize(int)} except that specified comparator will choose the ordering among the
     * entries. This is equivalent to
     * 
     * <pre>
     * Cache&lt;K, V&gt; cache = some cache;
     * CacheEntry&lt;K, V&gt;[] entries = c.entrySet().toArray(new CacheEntry[0]);
     * Arrays.sort(entries, comparator);
     * for (int i = 0; i &lt; numberOfItemsToRemove; i++)
     *     c.remove(entries[i].getKey());
     * </pre>
     * 
     * except that is it atomic.
     * <p>
     * However, all entries in the cache needs to be sorted accordingly to the specified comparator. So either the size
     * of the cache must be small, or this operation should be invoked infrequently.
     * <p>
     * The following snippet will trim the cache downto 70 % of the current size, removing those entries that was
     * created earliest.
     * 
     * <pre>
     * Cache&lt;?, ?&gt; c = someCache;
     * MemoryStoreService&lt;?, ?&gt; mss = c.with().memoryStore();
     * mss.trimToSize((int) (mss.getSize() * 0.7), CacheEntry.ENTRY_DATE_CREATED);
     * </pre>
     * 
     * @param size
     *            if positive or 0, the size to the trim the cache down to, otherwise the number of elements to remove
     * @param comparator
     *            used to determine in which order entries should be evicted
     */
    void trimToSize(long size, Comparator<? extends CacheEntry<K, V>> comparator);

    /**
     * If the specified volume is <tt>positive or 0</tt> this method will keeps evicting entries until the volume of
     * the cache is equal to the specified volume. If the specified volume is equal to or greater then the current
     * volume of the cache no action is taken.
     * <p>
     * If the specified volume is negative this method will evict enough entries to make sure the volume of the cache is
     * at least reduced to the specified volume.
     * <p>
     * This method does not gurantuee that the volume will be exact size as specified in this method because. Consider,
     * for example, a cache with two elements each of size 2, totalling a volume of 4. If the cache is asked to trim the
     * volume to 3, the closest it can to is to remove 1 element reducing the volume of the cache to 2.
     * 
     * @param volume
     *            if positive or 0, the maximum volume of the cache after this method returns, otherwise the minimum
     *            amount that the caches volume should be reduced with
     */
    void trimToVolume(long volume);

    /**
     * Works analogues to {@link #trimToVolume(long)} except that specified comparator will choose the ordering among
     * the entries.
     * <p>
     * Assuming the cache is configured to keep track of the {@link CacheEntry#COST} attribute. The following example
     * will trim the volume of the cache downto 50 % of the current volume, removing those entries that are less costly
     * to refetch.
     * 
     * <pre>
     * Cache&lt;?, ?&gt; c = someCache;
     * MemoryStoreService&lt;?, ?&gt; mss = c.with().memoryStore();
     * mss.trimToVolume((long) (mss.getVolume() * 0.5), CacheEntry.ENTRY_COST);
     * </pre>
     * 
     * @param volume
     *            if positive or 0, the maximum volume of the cache after this method returns, otherwise the minimum
     *            amount that the caches volume should be reduced with
     * 
     * @param comparator
     *            used to determine in which order entries should be evicted
     */
    void trimToVolume(long volume, Comparator<? extends CacheEntry<K, V>> comparator);
}
