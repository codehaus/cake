package org.codehaus.cake.cache.policy.paging;

import static org.codehaus.cake.cache.CacheEntry.HITS;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.AbstractHeapReplacementPolicy;

/**
 * 
 * However, the LFU policy has a number of drawbacks: it requires logarithmic implementation complexity in cache size,
 * pays little attention to recent history, and does not adapt well to changing access patterns since it accumulates
 * stale pages with high frequency counts that may no longer be useful.
 * 
 * @param <K>
 * @param <V>
 */
public class LFUReplacementPolicy<K, V> extends AbstractHeapReplacementPolicy<K, V> {
    /** A unique policy name. */
    public static final String NAME = "LFU";

    public LFUReplacementPolicy() {
        // This is used to make sure that the cache will lazy register the HITS attribute
        // if the user has not already done so using CacheAttributeConfiguration#add(Attribute...)}
        dependSoft(HITS);
    }

    @Override
    public int compare(CacheEntry<K, V> o1, CacheEntry<K, V> o2) {
        return -HITS.compare(o1, o2);
    }

    @Override
    public void touch(CacheEntry<K, V> entry) {
        siftDown(entry);// hits has been incremented
    }
}
