package org.codehaus.cake.cache.policy.paging;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.AbstractDoubleLinkedReplacementPolicy;

/**
 * A LIFO based replacement policy.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LIFOPolicy.java 536 2007-12-30 00:14:25Z kasper $
 * @param <T>
 *            the type of data maintained by this policy
 */
public class LIFOReplacementPolicy<K, V> extends AbstractDoubleLinkedReplacementPolicy<K, V> {
    /** A unique policy name. */
    public static final String NAME = "LIFO";

    /** {@inheritDoc} */
    public boolean add(CacheEntry<K, V> entry) {
        addLast(entry);
        return true;
    }

    /** {@inheritDoc} */
    public CacheEntry<K, V> evictNext() {
        return removeFirst();
    }
}
