package org.codehaus.cake.cache.policy.costsize;

import static org.codehaus.cake.cache.CacheEntry.COST;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.AbstractHeapReplacementPolicy;

public class ReplaceCostliestPolicy<K, V> extends AbstractHeapReplacementPolicy<K, V> {

    /** A unique policy name. */
    public static final String NAME = "ReplaceCostliest";

    /**
     * Creates a new ReplaceCostliestPolicy. 
     * 
     */
    public ReplaceCostliestPolicy() {
        // This is used to make sure that users have registered the COST attribute
        // with CacheAttributeConfiguration#add(Attribute...)} 
        dependHard(COST);
    }

    public int compare(CacheEntry<K, V> o1, CacheEntry<K, V> o2) {
        return -COST.compare(o1, o2);
    }
}
