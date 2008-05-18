package org.codehaus.cake.cache.policy.costsize;

import static org.codehaus.cake.cache.CacheEntry.SIZE;

import org.codehaus.cake.cache.CacheEntry;

import org.codehaus.cake.cache.policy.spi.AbstractHeapReplacementPolicy;

public class ReplaceBiggestPolicy<K, V> extends AbstractHeapReplacementPolicy<K, V> {

    /** A unique policy name. */
    public static final String NAME = "ReplaceBiggest";

    public ReplaceBiggestPolicy() {
        // This is used to make sure that users have registered the SIZE attribute
        // with CacheAttributeConfiguration#add(Attribute...)} 
        dependHard(SIZE);
    }

    public int compare(CacheEntry<K, V> o1, CacheEntry<K, V> o2) {
        return -SIZE.compare(o1, o2);
    }
}
