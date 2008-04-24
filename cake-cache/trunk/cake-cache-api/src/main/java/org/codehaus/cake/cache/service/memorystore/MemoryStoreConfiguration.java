/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.service.memorystore;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.ops.Ops;
import org.codehaus.cake.ops.Ops.Predicate;

/**
 * Used for configuring the eviction service prior to usage.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: MemoryStoreConfiguration.java 559 2008-01-09 16:28:27Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public class MemoryStoreConfiguration<K, V> {

    /** A filter used for filtering what items should be cached. */
    private Predicate<CacheEntry<K, V>> isCacheableFilter;

    /** Whether or not caching is disabled. */
    private boolean isDisabled;

    /** The maximum size of the cache. */
    private int maximumSize;

    /** The maximum volume of the cache. */
    private long maximumVolume;

    private ReplacementPolicy<K, V> replacementPolicy;

    public ReplacementPolicy<K, V> getPolicy() {
        return replacementPolicy;
    }

    public MemoryStoreConfiguration<K, V> setPolicy(ReplacementPolicy replacementPolicy) {
        this.replacementPolicy = replacementPolicy;
        return this;
    }

    /**
     * Returns the Predicate that determinds if a given key and value should be cached.
     * 
     * @return the IsCacheable predicate configured or <code>null</code> if no predicate has been
     *         set
     * @see #setIsCacheableFilter(Predicate)
     */
    public Predicate<CacheEntry<K, V>> getIsCacheableFilter() {
        return isCacheableFilter;
    }

    /**
     * Returns the maximum allowed size of the cache or {@link Integer#MAX_VALUE} if there is no
     * upper limit.
     * 
     * @return the maximum allowed size of the cache or Integer.MAX_VALUE if there is no limit.
     * @see #setMaximumSize(int)
     */
    public final int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Returns the maximum allowed volume of the cache or {@link Long#MAX_VALUE} if there is no
     * upper limit.
     * 
     * @return the maximum allowed volume of the cache or Long.MAX_VALUE if there is no limit.
     * @see #setMaximumVolume(long)
     */
    public long getMaximumVolume() {
        return maximumVolume;
    }

    /**
     * Returns whether or not caching is disabled.
     * 
     * @return <code>true</code> if caching is disabled, otherwise <code>false</code>
     */
    public boolean isDisabled() {
        return isDisabled;
    }

    /**
     * Sets whether or not caching is disabled. If caching is disabled, the cache will not cache any
     * items added. This can sometimes be useful while testing.
     * 
     * @param isDisabled
     *            whether or not caching is disabled
     * @return this configuration
     */
    public MemoryStoreConfiguration<K, V> setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
        return this;
    }

    /**
     * Controls how many entries are evicted whenever the maximum volume or size is reached.
     * <p>
     * If no evictor is specified the cache will normally evict the minimum number of entries
     * possible to make room for the new entry.
     * <p>
     * The following example shows a Procedure that trims the cache downto 80 % of if its maximum
     * size whenever the maximum
     * 
     * <pre>
     * public class TrimTo80Pct implements Procedure&lt;MemoryStoreService&gt; {
     *     public void op(MemoryStoreService service) {
     *         double newSize = service.getSize() * 0.8;
     *         service.trimToSize((int) newSize);
     *     }
     * }
     * </pre>
     * 
     * @param evictor
     * @return
     */
    public MemoryStoreConfiguration<K, V> setEvictor(Ops.Procedure<MemoryStoreService<K, V>> evictor) {
        this.evictor = evictor;
        return this;
    }

    private Ops.Procedure<MemoryStoreService<K, V>> evictor;

    public Ops.Procedure<MemoryStoreService<K, V>> getEvictor() {
        return evictor;
    }

    /**
     * Sets a Predicate that the cache will use to determind if a cache entry can be cached. For
     * example,
     * 
     * @param predicate
     *            the predicate that decides if a given key, value combination can be added to the
     *            cache
     * @return this configration
     */
    public MemoryStoreConfiguration<K, V> setIsCacheableFilter(Predicate<CacheEntry<K, V>> predicate) {
        this.isCacheableFilter = predicate;
        return this;
    }

    /**
     * Sets that maximum number of elements that a cache can hold. If the limit is reached the cache
     * must evict an existing element(s) before adding a new element. For example, if the maximum
     * size is 10 and the cache currently holds 10 elements. Then, if a user tries to add a new
     * element the cache must choose one of the 10 elements to remove from the cache before it
     * inserts the new element. As an alternative the cache might choose to keep the 10 existing
     * elements and not add the new element. For example, if it estimates that the likelihood of
     * requesting anyone of the 10 elements in the near future are higher then the likelihood of new
     * element being requested.
     * <p>
     * To indicate that a cache can hold an unlimited number of elements, specify
     * {@link Integer#MAX_VALUE}. This is also the default value.
     * <p>
     * If the specified maximum size is 0, the cache will never store any elements internally.
     * 
     * @param maximumSize
     *            the maximum number of elements the cache can hold or Integer.MAX_VALUE if there is
     *            no limit
     * @throws IllegalArgumentException
     *             if the specified integer is negative
     * @return this configuration
     */
    public final MemoryStoreConfiguration<K, V> setMaximumSize(int maximumSize) {
        if (maximumSize < 0) {
            throw new IllegalArgumentException(
                    "number of maximum elements must be 0 or greater, was " + maximumSize);
        }
        this.maximumSize = maximumSize;
        return this;
    }

    /**
     * Sets that maximum volume of the cache. The total volume of the cache is the sum of all the
     * individual element sizes (sum of all elements {@link CacheEntry#getSize()}. If the limit is
     * reached the cache must evict existing elements before adding new elements.
     * <p>
     * To indicate that a cache can have an unlimited volume, {@link Long#MAX_VALUE} should be
     * specified.
     * 
     * @param maximumVolume
     *            the maximum volume.
     * @return this configuration
     */
    public MemoryStoreConfiguration<K, V> setMaximumVolume(long maximumVolume) {
        if (maximumVolume < 0) {
            throw new IllegalArgumentException("maximumVolume must be a non-negative number, was "
                    + maximumVolume);
        }
        this.maximumVolume = maximumVolume;
        return this;
    }
}
