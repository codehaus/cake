package org.codehaus.cake.cache.policy.costsize;

import static org.codehaus.cake.cache.CacheEntry.COST;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.AbstractHeapReplacementPolicy;

/**
 * A replacement policy that replaces the entry with the smallest {@link CacheEntry#COST} when evicting. The rational for this
 * policy is that we should attempt to keep entries that have the highest cost to retrieve again.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: FIFOPolicy.java 536 2007-12-30 00:14:25Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of values maintained by the cache
 */
public class ReplaceCostliestPolicy<K, V> extends AbstractHeapReplacementPolicy<K, V> {

    /** A unique policy name. */
    public static final String NAME = "ReplaceCostliest";

    /**
     * Creates a new ReplaceCostliestPolicy.
     */
    public ReplaceCostliestPolicy() {
        // This is used to make sure that users have registered the COST attribute
        // with CacheAttributeConfiguration#add(Attribute...)}
        // it would not make sense using this policy if entries didn't have a COST attached
        dependHard(COST);
    }

    /** {@inheritDoc} */
    protected int compareEntry(CacheEntry<K, V> o1, CacheEntry<K, V> o2) {
        return -COST.compare(o1, o2);
    }
}
