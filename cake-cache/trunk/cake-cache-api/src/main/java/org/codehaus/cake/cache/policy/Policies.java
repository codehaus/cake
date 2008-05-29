package org.codehaus.cake.cache.policy;

import org.codehaus.cake.cache.policy.costsize.ReplaceBiggestPolicy;
import org.codehaus.cake.cache.policy.costsize.ReplaceCostliestPolicy;
import org.codehaus.cake.cache.policy.paging.FIFOReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.LIFOReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.LRUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.MRUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.RandomReplacementPolicy;

/**
 * Factory methods for different {@link ReplacementPolicy} implementations. This class provides shortcuts for the
 * specific implementations of policies defined in <tt>org.codehaus.cake.cache.policy</tt>.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Policies.java 491 2007-11-30 22:05:50Z kasper $
 */
public class Policies {
    // /CLOVER:OFF
    /** Cannot instantiate. */
    private Policies() {}

    // /CLOVER:ON

    /**
     * Returns a new {@link org.codehaus.cake.cache.policy.paging.FIFOReplacementPolicy FIFO Replacement Policy}.
     * 
     * @return a new FIFO policy
     * @param <E>
     *            the type of data maintained by the policy
     */
    public static <K, V> ReplacementPolicy<K, V> newFIFO() {
        return new FIFOReplacementPolicy<K, V>();
    }

    /**
     * Returns a new {@link org.codehaus.cake.cache.policy.paging.LIFOReplacementPolicy LIFO Replacement Policy}.
     * 
     * @return a new LIFO policy
     * @param <K,V>
     *            the type of data maintained by the policy
     */
    public static <K, V> ReplacementPolicy<K, V> newLIFO() {
        return new LIFOReplacementPolicy<K, V>();
    }

    /**
     * Returns a new {@link org.codehaus.cake.cache.policy.paging.LRUReplacementPolicy LRU Replacement Policy}.
     * 
     * @return a new LRU policy
     * @param <K,V>
     *            the type of data maintained by the policy
     */
    public static <K, V> ReplacementPolicy<K, V> newLRU() {
        return new LRUReplacementPolicy<K, V>();
    }

    /**
     * Returns a new {@link org.codehaus.cake.cache.policy.paging.MRUReplacementPolicy MRU Replacement Policy}.
     * 
     * @return a new MRU policy
     * @param <K,V>
     *            the type of data maintained by the policy
     */
    public static <K, V> ReplacementPolicy<K, V> newMRU() {
        return new MRUReplacementPolicy<K, V>();
    }

    /**
     * Returns a new {@link org.codehaus.cake.cache.policy.paging.RandomReplacementPolicy Random Replacement Policy}.
     * 
     * @return a new Random policy
     * @param <K,V>
     *            the type of data maintained by the policy
     */
    public static <K, V> ReplacementPolicy<K, V> newRandom() {
        return new RandomReplacementPolicy<K, V>();
    }

    /**
     * Returns a new {@link org.codehaus.cake.cache.policy.costSize.ReplaceCostliestPolicy Replacement Biggest Policy}.
     * 
     * @return a new replace biggest policy
     * @param <E>
     *            the type of data maintained by the policy
     */
    public static <K, V> ReplacementPolicy<K, V> newReplaceBiggest() {
        return new ReplaceBiggestPolicy<K, V>();
    }
    
    /**
     * Returns a new {@link org.codehaus.cake.cache.policy.costSize.ReplaceCostliestPolicy Replacement Costliest Policy}.
     * 
     * @return a new replace costliest policy
     * @param <E>
     *            the type of data maintained by the policy
     */
    public static <K, V> ReplacementPolicy<K, V> newReplaceCostliest() {
        return new ReplaceCostliestPolicy<K, V>();
    }
}
