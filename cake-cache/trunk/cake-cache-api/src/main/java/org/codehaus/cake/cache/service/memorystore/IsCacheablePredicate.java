package org.codehaus.cake.cache.service.memorystore;

import org.codehaus.cake.cache.CacheEntry;

/**
 * Normally there a no restrictions on what type of data that is added to a cache. However, in some situations it might
 * make sense to choose not cache an entry.
 * <p>
 * Consider a simple scenario where Web pages are being cached. The type of keys are <tt>Strings</tt> urls and the
 * type of values are <tt>byte[]</tt>. If we want to avoid caching pages that are requested through the
 * <tt>https</tt> protocol, but only pages that are requested through the <tt>http</tt> protocol. We can write an
 * IsCacheablePredicate like this:
 * 
 * <pre>
 * class NoHttps implements IsCacheablePredicate&lt;String, byte[]&gt; {
 *     public boolean add(CacheEntry&lt;String, byte[]&gt; entry) {
 *         return !entry.getKey().startsWith(&quot;https://&quot;));
 *     }
 *     public CacheEntry&lt;K, V&gt; replace(CacheEntry&lt;K, V&gt; existing, CacheEntry&lt;K, V&gt; entry) {
 *         return add(entry) ? entry : null;
 *     }
 * }
 * </pre>
 * 
 * IMPORTANT: entries that are rejected when calling {@link #add(CacheEntry)} should also be rejected when calling
 * {@link #replace(CacheEntry, CacheEntry)}.
 * 
 */
public interface IsCacheablePredicate<K, V> {

    /**
     * Called before the cache adds the specified entry to the cache. If this method returns <code>true</code> the
     * cache will add the entry to the cache, otherwise it will not be added to the cache.
     * 
     * @param entry
     *            the entry that is about to be added
     * @return whether or not the entry should be added
     */
    boolean add(CacheEntry<K, V> entry);

    /**
     * Called whenever an entry is being added to the cache and there already exist an entry with the same key.
     * Depending on the return value of this method the cache can either keep the existing entry, replaces the existing
     * entry with the new entry, or keep neither of the entries.
     * <ul>
     * <li> Returns newEntry: Accepts the new entry, and removes the existing entry.</li>
     * <li> Returns existingEntry: Rejects the new entry and keeps the existing entry.</li>
     * <li> Returns <code>null</code>: Rejects the new entry and removes the existing entry.</li>
     * </ul>
     * <p>
     * IMPORTANT: entries that are rejected by this method should also be rejected when using {@link #add(CacheEntry)} .
     * 
     * @param existingEntry
     *            the existing entry
     * @param newEntry
     *            the new entry
     * @return <tt>newEntry</tt> if the policy accepted the new entry, <tt>existingEntry</tt> if the policy wants to
     *         keep the existing entry, <code>null</code> if neither entries should be keept in the cache.
     */
    CacheEntry<K, V> replace(CacheEntry<K, V> existingEntry, CacheEntry<K, V> newEntry);
}
