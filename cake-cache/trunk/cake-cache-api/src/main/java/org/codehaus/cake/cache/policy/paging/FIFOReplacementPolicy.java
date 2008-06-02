package org.codehaus.cake.cache.policy.paging;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.AbstractDoubleLinkedReplacementPolicy;

/**
 * A FIFO based replacement policy.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: FIFOPolicy.java 536 2007-12-30 00:14:25Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of values maintained by the cache
 */
public class FIFOReplacementPolicy<K, V> extends AbstractDoubleLinkedReplacementPolicy<K, V> {
    
    /** A unique policy name. */
    public static final String NAME = "FIFO";
    
    /** {@inheritDoc} */
    public boolean add(CacheEntry<K, V> entry) {
        addFirst(entry);
        return true;
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> evictNext() {
        return removeFirst();
    }
}
