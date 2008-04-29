package org.codehaus.cake.cache.policy.paging;

import java.util.Random;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.AbstractArrayReplacementPolicy;

/**
 * A replacement policy that picks a random element to evict.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: RandomPolicy.java 536 2007-12-30 00:14:25Z kasper $
 * @param <T>
 *            the type of data maintained by this policy
 */
public class RandomReplacementPolicy<K, V> extends AbstractArrayReplacementPolicy<K, V> {

    /** A unique policy name. */
    public static final String NAME = "Random";

    private final Random rnd = new Random();

    /** {@inheritDoc} */
    @Override
    public CacheEntry<K, V> evictNext() {
        int size = size();
        return size == 0 ? null : removeByIndex(rnd.nextInt(size));
    }
}
