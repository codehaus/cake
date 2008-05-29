package org.codehaus.cake.internal.cache.attribute;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.costsize.ReplaceCostliestPolicy;
import org.codehaus.cake.cache.policy.spi.AbstractReplacementPolicy;
import org.codehaus.cake.cache.service.attribute.CacheAttributeConfiguration;

/**
 * These are various internal methods used by {@link AbstractReplacementPolicy}.
 * 
 */
public interface InternalCacheAttributeService {
    void attachToPolicy(Attribute<?> attribute);

    /**
     * Depends on another attributes. If the user has not registered this attribute with
     * {@link CacheAttributeConfiguration#add(Attribute...)} the attribute will be silently registered.
     * 
     * For example, LFU will depend on CacheEntry.HITS. This is a non-hard dependency because if the user has not
     * already defined it, it will just be added as a secret.
     * 
     * 
     * @param attribute
     */
    void dependOnSoft(Attribute<?> attribute);

    /**
     * Depends on another attributes. If the user has not registered this attribute with
     * {@link CacheAttributeConfiguration#add(Attribute...)} the cache will fail to startup. For example, it does not
     * make sense to use {@link ReplaceCostliestPolicy} if the user does not supply entries a {@link CacheEntry#COST}
     * attached.
     * 
     * @see #dependOnSoft(Attribute)
     */
    void dependOnHard(Attribute<?> attribute);
}
