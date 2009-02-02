package org.codehaus.cake.cache.service.memorystore;

import org.codehaus.cake.cache.CacheEntry;

/**
 * Normally all data is accepted by a cache.
 * 
 *  cache will accept all entries in which case <code>true</code> should be returned from this method.
 * However, some policies might want to tell the cache to not cache a specific entry. Consider a simple scenario where
 * Web pages are being cached. The type of keys are <tt>Strings</tt> and the type of values are <tt>byte[]</tt>. If
 * we do not want to cache pages that are requested through the <tt>https</tt> protocol, but only pages that are
 * requested through the <tt>http</tt> protocol. In which case we would write an add method similar to this:
 * 
 * <pre>
 * public boolean add(CacheEntry&lt;String, byte[]&gt; entry) {
 *     if (entry.getKey().startsWith(&quot;https://&quot;)) {
 *         return false; //tell the cache to *not* add the entry.
 *     }
 *     //cache the entry in the policy
 *     return true; //tell the cache to add the entry.
 * }
 * </pre>
 * 
 * IMPORTANT: entries that are rejected by this method should also be rejected by the
 * {@link #replace(CacheEntry, CacheEntry)} method.
 * 
 */
public interface IsCacheablePredicate<K, V> {
    boolean add(CacheEntry<K, V> entry);

    /**
     * The implementation of this policy must now choose between
     * <ul>
     * <li> Accepting the new entry, and removing the old entry (normal behaviour). In which case it should return the
     * new entry to indicate to the cache that it should remove the old entry and keep the entry.</li>
     * <li> Rejecting the new entry and keeping the old entry. In which case it should return the previous.</li>
     * <li> Reject the new entry and removing the old entry. In which case <code>null</code> should be returned.</li>
     * </ul>
     * IMPORTANT: new entries that are rejected by this method should also be rejected by the {@link #add(CacheEntry)}
     * method.
     * 
     * @param previous
     *            the previous entry
     * @param newEntry
     *            the new entry
     * @return <tt>newEntry</tt> if the policy accepted the new entry, <tt>previous</tt> if the policy wants to keep
     *         the previous entry, <code>null</code> if neither entries should be keept in the cache.
     */
    CacheEntry<K, V> replace(CacheEntry<K, V> existing, CacheEntry<K, V> entry);
}
