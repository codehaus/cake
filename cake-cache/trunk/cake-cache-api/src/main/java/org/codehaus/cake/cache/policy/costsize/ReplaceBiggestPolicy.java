package org.codehaus.cake.cache.policy.costsize;

import static org.codehaus.cake.cache.CacheEntry.SIZE;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.AbstractHeapReplacementPolicy;

/**
 * A replacement policy that replaces the entry with the biggest {@link CacheEntry#SIZE} when evicting. The rational for
 * this policy is that we should rather cache a lot of smaller entries than a few large items.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: FIFOPolicy.java 536 2007-12-30 00:14:25Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of values maintained by the cache
 */
public class ReplaceBiggestPolicy<K, V> extends AbstractHeapReplacementPolicy<K, V> {

    /** A unique policy name. */
    public static final String NAME = "ReplaceBiggest";

    /**
     * Creates a new ReplaceCostliestPolicy.
     */
    public ReplaceBiggestPolicy() {
        // This is used to make sure that users have registered the SIZE attribute
        // with CacheAttributeConfiguration#add(Attribute...)}
        dependHard(SIZE);
    }

    /** {@inheritDoc} */
    protected int compareEntry(CacheEntry<K, V> o1, CacheEntry<K, V> o2) {
        return -SIZE.compare(o1, o2);
    }
}
