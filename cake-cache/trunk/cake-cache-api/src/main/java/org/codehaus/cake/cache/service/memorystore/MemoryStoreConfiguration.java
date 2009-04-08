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

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.internal.cache.service.memorystore.MemoryStoreAttributes;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.DefaultAttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;
import org.codehaus.cake.util.attribute.WithAttributes;
import org.codehaus.cake.util.ops.Ops;
import org.codehaus.cake.util.ops.Ops.Procedure;

/**
 * Used for configuring the memory store of a cache prior to usage.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values<
 */
public class MemoryStoreConfiguration<K, V> implements WithAttributes {

    private final MutableAttributeMap attributes = new DefaultAttributeMap();

    private Ops.Procedure<MemoryStoreService<K, V>> evictor;

    /** A filter used for filtering what items should be cached. */
    private IsCacheablePredicate<? super K, ? super V> isCacheableFilter;

    /** Whether or not caching is disabled. */
    private boolean isDisabled;

    /** The maximum size of the cache. */
    private int maximumSize = Integer.MAX_VALUE;

    /** The maximum volume of the cache. */
    private long maximumVolume = Long.MAX_VALUE;

    private Class<? extends ReplacementPolicy> replacementPolicy;

    public AttributeMap getAttributes() {
        return attributes;
    }

    public Ops.Procedure<MemoryStoreService<K, V>> getEvictor() {
        return evictor;
    }

    /**
     * Returns a {@link IsCacheablePredicate} that determinds if a given key and value should be cached.
     * 
     * @return the iscacheable predicate if it has been set, otherwise <code>null</code>
     * @see #setIsCacheableFilter(IsCacheablePredicate)
     */
    public IsCacheablePredicate<? super K, ? super V> getIsCacheableFilter() {
        return isCacheableFilter;
    }

    /**
     * Returns the maximum allowed size of the cache or {@link Integer#MAX_VALUE} if there is no upper limit.
     * 
     * @return the maximum allowed size of the cache or Integer.MAX_VALUE if there is no limit.
     * @see #setMaximumSize(int)
     */
    public final int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Returns the maximum allowed volume of the cache or {@link Long#MAX_VALUE} if there is no upper limit.
     * 
     * @return the maximum allowed volume of the cache or Long.MAX_VALUE if there is no limit.
     * @see #setMaximumVolume(long)
     */
    public long getMaximumVolume() {
        return maximumVolume;
    }

    /**
     * Returns the type of replacement policy that the cache should use for choosing elements to evict when needed.
     * 
     * @return the type of replacement policy
     * @see #setPolicy(Class)
     */
    public Class<? extends ReplacementPolicy> getPolicy() {
        return replacementPolicy;
    }

    /**
     * Returns whether or not caching is disabled.
     * 
     * @return <code>true</code> if caching is disabled, otherwise <code>false</code>
     * @see #setDisabled(boolean)
     * @see MemoryStoreService#isDisabled()
     */
    public boolean isDisabled() {
        return isDisabled;
    }

    /**
     * Sets whether or not caching is disabled. If caching is disabled, the cache will not cache any items added. This
     * can sometimes be useful while testing.
     * 
     * @param isDisabled
     *            whether or not caching is disabled
     * @return this configuration
     * @see #isDisabled()
     * @see MemoryStoreService#setDisabled(boolean)
     */
    public MemoryStoreConfiguration<K, V> setDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
        attributes.put(MemoryStoreAttributes.IS_DISABLED, isDisabled);
        return this;
    }

    /**
     * Normally cache instances will only evict the minimum number of elements needed in order to maximum size and
     * volume.
     * 
     * Controls how many entries are evicted whenever the maximum volume or size is reached.
     * <p>
     * If no evictor is specified the cache will normally evict the minimum number of entries possible to make room for
     * the new entry.
     * <p>
     * The following example shows a Procedure that trims the cache downto 80 % of if its maximum size whenever the
     * maximum size is reached
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
     * <p>
     * The behavior of using the MemoryStoreService parsed along to the procedures {@link Procedure#op(Object)} method
     * anywhere but within that method is undefined.
     * 
     * @param evictor
     *            the evictor to use
     * @return this configuration
     * @see #getEvictor()
     */
    public MemoryStoreConfiguration<K, V> setEvictor(Procedure<MemoryStoreService<K, V>> evictor) {
        this.evictor = evictor;
        return this;
    }

    /**
     * Sets a IsCacheablePredicate that the cache will use to determind if a cache entry should be cached. For example,
     * 
     * @param predicate
     *            the predicate that decides if a given cache entry should be cached
     * @return this configuration
     * @see #getIsCacheableFilter()
     */
    public MemoryStoreConfiguration<K, V> setIsCacheableFilter(IsCacheablePredicate<? super K, ? super V> predicate) {
        this.isCacheableFilter = predicate;
        return this;
    }

    /**
     * Sets that maximum number of elements that a cache can hold. If the limit is reached the cache must evict an
     * existing element(s) before adding a new element. For example, if the maximum size is 10 and the cache currently
     * holds 10 elements. Then, if a user tries to add a new element the cache must choose one of the 10 elements to
     * remove from the cache before it inserts the new element. As an alternative the cache might choose to keep the 10
     * existing elements and not add the new element. For example, if it estimates that the likelihood of requesting
     * anyone of the 10 elements in the near future are higher then the likelihood of the new element being requested.
     * <p>
     * To indicate that a cache can hold an unlimited number of elements, specify {@link Integer#MAX_VALUE} which is
     * also the default value.
     * <p>
     * If the specified maximum size is 0, the cache will never store any elements internally.
     * 
     * @param maximumSize
     *            the maximum number of elements the cache can hold or Integer.MAX_VALUE if there is no limit
     * @throws IllegalArgumentException
     *             if the specified maximum size is negative
     * @return this configuration
     */
    public final MemoryStoreConfiguration<K, V> setMaximumSize(int maximumSize) {
        if (maximumSize < 0) {
            throw new IllegalArgumentException("number of maximum elements must be 0 or greater, was " + maximumSize);
        }
        attributes.put(MemoryStoreAttributes.MAX_SIZE, maximumSize);
        this.maximumSize = maximumSize;
        return this;
    }

    /**
     * Sets that maximum volume of the cache. The total volume of the cache is the sum of all the individual element
     * sizes (sum of {@link CacheEntry#SIZE}). If the limit is reached the cache must evict existing elements before
     * adding new elements.
     * <p>
     * To indicate that a cache can have an unlimited volume, specify {@link Long#MAX_VALUE} which is also the default
     * value.
     * 
     * @param maximumVolume
     *            the maximum volume.
     * @throws IllegalArgumentException
     *             if the specified m aximum volume is negative
     * @return this configuration
     */
    public MemoryStoreConfiguration<K, V> setMaximumVolume(long maximumVolume) {
        MemoryStoreAttributes.MAX_VOLUME.checkValid(maximumVolume);
        attributes.put(MemoryStoreAttributes.MAX_VOLUME, maximumVolume);
        this.maximumVolume = maximumVolume;
        return this;
    }

    /**
     * Sets the type of replacement policy that the cache should use to choose which elements to evict when the cache is
     * full and new elements are being added.
     * <p>
     * If no replacement policy is specified and the cache needs to evict elements, it may choose the elements to evict
     * in any possible way.
     * 
     * @param replacementPolicy
     *            the replacement policy
     * @return this configuration
     */
    public MemoryStoreConfiguration<K, V> setPolicy(Class<? extends ReplacementPolicy> replacementPolicy) {
        this.replacementPolicy = replacementPolicy;
        return this;
    }
}
