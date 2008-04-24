package org.codehaus.cake.cache.policy;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;

/**
 * A (cache) replacement policy determines which data item(s) should be evicted (deleted) from the
 * cache when the free space is insufficient for accommodating a new item to be cached. Normally
 * users should not need to implement this interface, only if they want to implement a custom
 * replacement polices.
 * <p>
 * A replacement policy does not control when or how many entries should be evicted only what
 * entries should be evicted.
 * <p>
 * This library comes with a number of predefined replacement policies, see
 * {@link org.codehaus.cake.cache.policy.Policies} for the most commonly used policies.
 * <p>
 * For performance reasons cache policies are not expected to be thread-safe. Instead, any cache
 * implementation using a replacement policy must maintain thread safety.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 * @version $Id: ReplacementPolicy.java 491 2007-11-30 22:05:50Z kasper $
 * @param <K>
 *            the type of keys cached
 * @param <V>
 *            the type of values cached
 */
public interface ReplacementPolicy<K, V> {

    /**
     * Called whenever a new entry is added to the cache. Normally a replacement policy will accept
     * all entries in which case <code>true</code> should be returned for all entries. However,
     * 
     * If the policy accepts the entry <code>true</code> should be returned an. A
     * <code>false</code> return value indicates that the policy has rejected the entry, for
     * example, if some property . If the entry is rejected the cache should not cache it.
     * 
     * @param entry
     *            the entry to add to the replacement policy
     * @return <code>true</code> if the entry was accepted by the replacement policy otherwise
     *         false.
     */
    boolean add(CacheEntry<K, V> entry);

    /**
     * Called whenever the cache removes references to all entries that are cached. Calling this
     * method should have the same effect as calling {@link #remove(CacheEntry)} for each individual
     * entry currently cached.
     */
    void clear();

    /**
     * Called by the cache when insufficient space is available for new entries to be added to the
     * cache. This method should return the entry that should be evicted next accordingly to the
     * replacement policy.
     * 
     * @return the entry that should be evicted or <code>null</code> if the policy does not
     *         contain any entries
     */
    CacheEntry<K, V> evictNext();

    /**
     * Called whenever an entry is removed by an external action in the cache. For example, the user
     * has removed the entry by calling {@link Cache#remove(Object)}.
     * 
     * @param entry
     *            the entry that was removed
     */
    void remove(CacheEntry<K, V> entry);

    /**
     * The specified <tt>previous</tt>entry wasupdated with a new value. If the policy chooses to
     * cache this new entry instead of the previous entry the new entry should be returned. This is
     * the normal behaviour of a replacement. However, for certain applications it might make sense
     * to keep the previous entry instead of the new entry in which the previous entry should be
     * returned. Finally, if <code>null</code> is returned both the previous and new entry are
     * removed from the cache.
     * 
     * @param previous
     *            the the previous entry
     * @param newEntry
     *            the new entry
     * @return <tt>newEntry</tt> if the policy accepted the new entry,<tt>previous</tt> if the
     *         policy wants to keep the previous entry, <code>null</code> if neither entries
     *         should be keept in the cache.
     */
    CacheEntry<K, V> replace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry);

    /**
     * Called whenever a entry is accessed in the cache. An entry is accessed whenever
     * {@link Cache#get(Object)}, {@link Cache#getEntry(Object)},
     * {@link Cache#getAll(java.util.Collection)} is called. Neither complete scans such as those
     * performed by the cache's iterators or partial scan such as scan performed on a filtered list
     * of entries.
     * 
     * @param entry
     *            the entry that was accessed
     */
    void touch(CacheEntry<K, V> entry);
}
