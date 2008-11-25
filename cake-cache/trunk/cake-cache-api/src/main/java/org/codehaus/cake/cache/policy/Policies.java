/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache.policy;

import org.codehaus.cake.cache.policy.costsize.ReplaceBiggestPolicy;
import org.codehaus.cake.cache.policy.costsize.ReplaceCostliestPolicy;
import org.codehaus.cake.cache.policy.paging.FIFOReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.LIFOReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.LRUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.MRUReplacementPolicy;
import org.codehaus.cake.cache.policy.paging.RandomReplacementPolicy;

/**
 * Factory methods for different {@link ReplacementPolicy} implementations. This class provides shortcuts for various
 * replacement policy implementations.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Policies.java 491 2007-11-30 22:05:50Z kasper $
 */
public final class Policies {
    // /CLOVER:OFF
    /** Cannot instantiate. */
    private Policies() {
    }

    // /CLOVER:ON

    /**
     * Returns a new {@link FIFOReplacementPolicy FIFO Replacement Policy}.
     * 
     * @return a new FIFO policy
     * @param <K>
     *            the type of keys maintained by the cache
     * @param <V>
     *            the type of values maintained by the cache
     */
    public static <K, V> ReplacementPolicy<K, V> newFIFO() {
        return new FIFOReplacementPolicy<K, V>();
    }

    /**
     * Returns a new {@link LIFOReplacementPolicy LIFO Replacement Policy}.
     * 
     * @return a new LIFO policy
     * @param <K>
     *            the type of keys maintained by the cache
     * @param <V>
     *            the type of values maintained by the cache
     */
    public static <K, V> ReplacementPolicy<K, V> newLIFO() {
        return new LIFOReplacementPolicy<K, V>();
    }

    /**
     * Returns a new {@link LRUReplacementPolicy LRU Replacement Policy}.
     * 
     * @return a new LRU policy
     * @param <K>
     *            the type of keys maintained by the cache
     * @param <V>
     *            the type of values maintained by the cache
     */
    public static <K, V> ReplacementPolicy<K, V> newLRU() {
        return new LRUReplacementPolicy<K, V>();
    }

    /**
     * Returns a new {@link MRUReplacementPolicy MRU Replacement Policy}.
     * 
     * @return a new MRU policy
     * @param <K>
     *            the type of keys maintained by the cache
     * @param <V>
     *            the type of values maintained by the cache
     */
    public static <K, V> ReplacementPolicy<K, V> newMRU() {
        return new MRUReplacementPolicy<K, V>();
    }

    /**
     * Returns a new {@link RandomReplacementPolicy Random Replacement Policy}.
     * 
     * @return a new Random policy
     * @param <K>
     *            the type of keys maintained by the cache
     * @param <V>
     *            the type of values maintained by the cache
     * 
     */
    public static <K, V> ReplacementPolicy<K, V> newRandom() {
        return new RandomReplacementPolicy<K, V>();
    }

    /**
     * Returns a new {@link ReplaceBiggestPolicy Replacement Biggest Policy}.
     * 
     * @return a new replace biggest policy
     * @param <K>
     *            the type of keys maintained by the cache
     * @param <V>
     *            the type of values maintained by the cache
     */
    public static <K, V> ReplacementPolicy<K, V> newReplaceBiggest() {
        return new ReplaceBiggestPolicy<K, V>();
    }

    /**
     * Returns a new {@link ReplaceCostliestPolicy Replacement Costliest Policy}.
     * 
     * @return a new replace costliest policy
     * @param <K>
     *            the type of keys maintained by the cache
     * @param <V>
     *            the type of values maintained by the cache
     * 
     */
    public static <K, V> ReplacementPolicy<K, V> newReplaceCostliest() {
        return new ReplaceCostliestPolicy<K, V>();
    }
}
