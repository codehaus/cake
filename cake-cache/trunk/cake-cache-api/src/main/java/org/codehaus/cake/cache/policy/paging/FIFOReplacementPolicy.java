package org.codehaus.cake.cache.policy.paging;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.AbstractDoubleLinkedReplacementPolicy;

/**
 * A FIFO based replacement policy.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: FIFOPolicy.java 536 2007-12-30 00:14:25Z kasper $
 * @param <T>
 *            the type of data maintained by this policy
 */
public class FIFOReplacementPolicy<K, V> extends AbstractDoubleLinkedReplacementPolicy<K, V> {
    
    /** A unique policy name. */
    public static final String NAME = "FIFO";
    
    /** {@inheritDoc} */
    @Override
    public boolean add(CacheEntry<K, V> entry) {
        addHead(entry);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void touch(CacheEntry<K, V> entry) {}

    /** {@inheritDoc} */
    @Override
    public CacheEntry<K, V> evictNext() {
        return removeHead();
    }
}
