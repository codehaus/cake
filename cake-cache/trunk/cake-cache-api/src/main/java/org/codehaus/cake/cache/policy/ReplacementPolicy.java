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

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;

/**
 * A (cache) replacement policy determines which data item(s) should be evicted (deleted) from the cache when the free
 * space is insufficient for accommodating a new item to be cached. Normally users should not need to implement this
 * interface, only if they want to implement a custom replacement polices.
 * <p>
 * A replacement policy does not control when or how many entries should be evicted only what entries should be evicted.
 * <p>
 * This library comes with a number of predefined replacement policies, see
 * {@link org.codehaus.cake.cache.policy.Policies} for the most commonly used policies.
 * <p>
 * For performance reasons cache policies are not expected to be thread-safe. Instead, any cache implementation using a
 * replacement policy must maintain thread safety.
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
     * The cache calls this method whenever a new entry is added to the cache. Normally a replacement policy will accept
     * all entries in which case <code>true</code> should be returned for any entry. However, some policies might want
     * to tell the cache to not cache a specific entry. Consider a simple scenario where Web pages are being cached. The
     * type of keys are <tt>Strings</tt> and the type of values are <tt>byte[]</tt>. Let's say we do not want to cache
     * pages that are requested through the <tt>https</tt> protocol only pages that are requested through the
     * <tt>http</tt> protocol. In that case we would write an add method something similar to this:
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
     * @param entry
     *            the entry to add to the replacement policy
     * @return <code>true</code> if the entry was accepted by the replacement policy otherwise false.
     */
    boolean add(CacheEntry<K, V> entry);

    /**
     * The cache calls this method whenever it removes references to all entries that are cached. Calling this method
     * should have the same effect as calling {@link #remove(CacheEntry)} for each individual entry currently cached.
     * However, the implementing class will most likely be able to do it faster.
     */
    void clear();

    /**
     * Called by the cache when insufficient space is available for a new entry to be added to the cache. This method
     * should return the entry that should be evicted next accordingly to the replacement policy that is implemented.
     * 
     * @return the entry that should be evicted from the cache or <code>null</code> if the policy does not contain any
     *         entries
     */
    CacheEntry<K, V> evictNext();

    /**
     * Called whenever an entry is removed by an external action in the cache. For example, the user has removed the
     * entry by calling {@link Cache#remove(Object)}. If the policy contains any references to the entry it should
     * remove them in order to avoid memory leaks.
     * 
     * @param entry
     *            the entry that was removed
     */
    void remove(CacheEntry<K, V> entry);

    /**
     * The specified <tt>previous</tt>entry was updated with a new value. If the policy chooses to cache this new entry
     * instead of the previous entry the new entry should be returned. This is the normal behavior of a replacement
     * policy. However, for certain applications it might make sense to keep the previous entry instead of the new entry
     * in which the previous entry should be returned. Finally, if <code>null</code> is returned both the previous and
     * new entry are removed from the cache.
     * 
     * @param previous
     *            the the previous entry
     * @param newEntry
     *            the new entry
     * @return <tt>newEntry</tt> if the policy accepted the new entry, <tt>previous</tt> if the policy wants to keep the
     *         previous entry, <code>null</code> if neither entries should be keept in the cache.
     * @see #add(CacheEntry)
     */
    CacheEntry<K, V> replace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry);

    /**
     * Called by the cache whenever an entry is accessed. An entry is accessed whenever {@link Cache#get(Object)},
     * {@link Cache#getEntry(Object)}, {@link Cache#getAll(java.util.Collection)} is called. Accessing an entry while
     * using an iterator returned by {@link Cache#entrySet()}, {@link Cache#keySet()} or {@link Cache#values()} will not
     * result in this method being invoked.
     * 
     * @param entry
     *            the entry that was accessed
     */
    void touch(CacheEntry<K, V> entry);
}
