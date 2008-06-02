package org.codehaus.cake.cache.policy.paging;

import static org.codehaus.cake.cache.CacheEntry.HITS;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.AbstractHeapReplacementPolicy;

/**
 * Not widely used. See, for example, http://citeseer.ist.psu.edu/mekhiel95multilevel.html
 * 
 * @param <K>
 * @param <V>
 */
public class MFUReplacementPolicy<K, V> extends AbstractHeapReplacementPolicy<K, V> {
    /** A unique policy name. */
    public static final String NAME = "MFU";

    public MFUReplacementPolicy() {
        // This is used to make sure that the cache will lazy register the HITS attribute
        // if the user has not already done so using CacheAttributeConfiguration#add(Attribute...)}
        dependSoft(HITS);
    }

    @Override
    protected int compareEntry(CacheEntry<K, V> o1, CacheEntry<K, V> o2) {
        return -HITS.compare(o1, o2);
    }

    @Override
    public void touch(CacheEntry<K, V> entry) {
        siftUp(entry);
    }
}
