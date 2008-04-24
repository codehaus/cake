package org.codehaus.cake.cache.policy.paging;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.AbstractDoubleLinkedReplacementPolicy;

/**
 * A Least Recently Used (LRU) replacement policy that discards the least recently used elements
 * first.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LRUPolicy.java 536 2007-12-30 00:14:25Z kasper $
 * @param <T>
 *            the type of data maintained by this policy
 */
public class LRUReplacementPolicy<K, V> extends AbstractDoubleLinkedReplacementPolicy<K, V> {
    /** A unique policy name. */
    public static final String NAME = "LRU";

    /** {@inheritDoc} */
    @Override
    public boolean add(CacheEntry<K, V> entry) {
        addHead(entry);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void touch(CacheEntry<K, V> entry) {
        moveToHead(entry);
    }

    /** {@inheritDoc} */
    @Override
    public CacheEntry<K, V> evictNext() {
        return removeTail();
    }
}
