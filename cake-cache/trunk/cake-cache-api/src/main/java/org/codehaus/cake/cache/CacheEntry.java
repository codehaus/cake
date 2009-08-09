/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache;

import java.util.Map;

import org.codehaus.cake.cache.policy.ReplaceBiggestPolicy;
import org.codehaus.cake.cache.policy.ReplaceCostliestPolicy;
import org.codehaus.cake.internal.cache.CacheEntryAttributes;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.DoubleAttribute;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.codehaus.cake.util.attribute.TimeInstanceAttribute;

/**
 * A <tt>CacheEntry</tt> describes a value-key mapping extending {@link java.util.Map.Entry} with metadata. The metadata
 * can consist of information such as creation time, access patterns, size, cost of retrieval etc.
 * <p>
 * It is the responsibility of the user to configure which types of metadata the cache should keep for each entry stored
 * in the cache. Metadata that should be retained at runtime must first be added using
 * {@link CacheConfiguration#addEntryAttributes(Attribute...)} before the cache is started.
 * <p>
 * For example, this snippet shows how to configure the cache to keep track of when each cache entry was created and
 * when it was last modified.
 * 
 * <pre>
 * CacheConfiguration&lt;Integer, String&gt; conf = CacheConfiguration.newConfiguration();
 * conf.withAttributes().add(CacheEntry.TIME_CREATED, CacheEntry.TIME_MODIFIED);
 * Cache&lt;Integer, String&gt; cache = SynchronizedCache.from(conf);
 * cache.put(5, &quot;test&quot;);
 * long creationTime = cache.getEntry(5).get(CacheEntry.TIME_CREATED);
 * long modificationTime = cache.getEntry(5).get(CacheEntry.TIME_MODIFIED);
 * </pre>
 * 
 * <p>
 * Unless otherwise specified a cache entry obtained from a cache is always an immmutable copy of the existing entry. If
 * the value for a given key is updated while another thread holds a cache entry for the key. It will not be reflected
 * in calls to {@link #getValue()}. Some implementations might allow for certain attributes to be continuesly updated
 * returned for purely performance reasons. For example {@link #TIME_ACCESSED} and {@link #HITS}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public interface CacheEntry<K, V> extends Map.Entry<K, V>, AttributeMap {

    /**
     * The <tt>Cost attribute</tt> is used to indicate the cost of retrieving an entry. The idea is that when memory is
     * sparse the cache can choose to evict entries that are least costly to retrieve again. Currently this attribute is
     * used only by the {@link ReplaceCostliestPolicy}.
     * <p>
     * A frequent used unit for this attribute is time. For example, how many milliseconds does it take to retrieve the
     * entry. However, any unit can be used. Because any policy should only use the relative cost difference between
     * different entries to determine which of the entries that should be evicted.
     * <p>
     * <blockquote>
     * <table border>
     * <tr>
     * <td>Type</td>
     * <td>double</td>
     * </tr>
     * <tr>
     * <td>Default Value</td>
     * <td>1.0</td>
     * </tr>
     * <tr>
     * <td>Valid Values</td>
     * <td>All values, except {@link Double#NEGATIVE_INFINITY}, {@link Double#POSITIVE_INFINITY} and {@link Double#NaN}</td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Primarily <tt>time</tt>, but any unit is acceptable</td>
     * </tr>
     * </table>
     * </blockquote>
     */
    DoubleAttribute COST = CacheEntryAttributes.COST;

    /**
     * A count of how many times an entry has been accessed through {@link Cache#get(Object)},
     * {@link Cache#getEntry(Object)} or {@link Cache#getAll(java.util.Collection)}.
     * 
     * <p/>
     * The following list describes how this attribute is first obtained.
     * <ul>
     * <li>If an entry is being put or loaded and the <tt>HITS</tt> attribute has been set specified by the user or the
     * cache loader cache will use this value.</li>
     * <li>Else, if this entry is replacing an existing entry the hit count from the existing entry will be used.</li>
     * <li>Else, if this entry is accessed through <tt>get</tt>, <tt>getEntry</tt> or <tt>getAll</tt> the number of hits
     * is incremented by 1</li>
     * </ul>
     * <p>
     * <blockquote>
     * <table border>
     * <tr>
     * <td>Type</td>
     * <td>long</td>
     * </tr>
     * <tr>
     * <td>Default Value</td>
     * <td>0</td>
     * </tr>
     * <tr>
     * <td>Valid Values</td>
     * <td>All positive values (>0)</td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Number of Accesses</td>
     * </tr>
     * </table>
     * </blockquote>
     */
    LongAttribute HITS = CacheEntryAttributes.HITS;

    /**
     * The size of the cache entry. The volume of a cache is defined as the sum of the individual sizes of all entries
     * in the cache.
     * <p>
     * The {@link ReplaceBiggestPolicy} uses the value of this attribute to decidewhich entries to evict. The idea being
     * that when memory is sparse the cache can choose to evict the entries that have the largest size in order to make
     * room for more smaller entries.
     */
    LongAttribute SIZE = new LongAttribute("Size", 1) {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        @Override
        protected String checkValidFailureMessage(Long value) {
            return "invalid size (size = " + value + ")";
        }

        /** {@inheritDoc} */
        @Override
        public boolean isValid(long value) {
            return value >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SIZE;
        }
    };
    /**
     * The time between when the entry was last accessed (through calls to {@link Cache#get(Object)} and midnight,
     * January 1, 1970 UTC. This is also the value returned by {@link System#currentTimeMillis()}.
     * <p>
     * <blockquote>
     * <table border>
     * <tr>
     * <td>Type</td>
     * <td>long</td>
     * </tr>
     * <tr>
     * <td>Default Value</td>
     * <td>System#currentTimeMillis() (see previous description)</td>
     * </tr>
     * <tr>
     * <td>Valid Values</td>
     * <td>All positive values (>0)</td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Milliseconds</td>
     * </tr>
     * </table>
     * </blockquote>
     */
    TimeInstanceAttribute TIME_ACCESSED = CacheEntryAttributes.TIME_ACCESSED;

    /**
     * The time between when the entry was created and midnight, January 1, 1970 UTC. This is also the value returned by
     * {@link System#currentTimeMillis()}.
     * <p/>
     * The following list describes how this attribute is obtained.
     * <ul>
     * <li>If the entry is being put or loaded and the <tt>TIME_CREATED</tt> attribute has been set the cache will use
     * this value.</li>
     * <li>Else, if this entry is replacing an existing entry the creation time from the existing entry will be used.</li>
     * <li>Else, if a clock is set through {@link CacheConfiguration#setClock(org.codehaus.cake.util.Clock)} a timestamp
     * is obtained by calling {@link org.codehaus.cake.util.Clock#timeOfDay()}.</li>
     * <li>Else, if no clock has been set System#currentTimeMillis() is used for obtaining a timestamp.</li>
     * </ul>
     * <p>
     * <blockquote>
     * <table border>
     * <tr>
     * <td>Type</td>
     * <td>long</td>
     * </tr>
     * <tr>
     * <td>Default Value</td>
     * <td>System#currentTimeMillis() (see previous description)</td>
     * </tr>
     * <tr>
     * <td>Valid Values</td>
     * <td>All positive values (>0)</td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Milliseconds</td>
     * </tr>
     * </table>
     * </blockquote>
     */
    TimeInstanceAttribute TIME_CREATED = CacheEntryAttributes.TIME_CREATED;

    /**
     * The time between when the entry was last modified and midnight, January 1, 1970 UTC. This is also the value
     * returned by {@link System#currentTimeMillis()}.
     * <p>
     * The mapped value must be a positive (>0) <tt>long</tt>.
     */
    TimeInstanceAttribute TIME_MODIFIED = CacheEntryAttributes.TIME_MODIFIED;

    /**
     * A count of how many times the value of an entry has been modified.
     * 
     * <p/>
     * The following list describes how this attribute is obtained.
     * <ul>
     * <li>If the entry is being loaded and the <tt>VERSION</tt> attribute has been set the cache will use this value.</li>
     * <li>Else, if this entry is replacing an existing entry the version from the existing entry + 1 will be used.</li>
     * <li>Else, the version is initialized to 1</li>
     * </ul>
     * <p>
     * <blockquote>
     * <table border>
     * <tr>
     * <td>Type</td>
     * <td>long</td>
     * </tr>
     * <tr>
     * <td>Default Value</td>
     * <td>1</td>
     * </tr>
     * <tr>
     * <td>Valid Values</td>
     * <td>All positive values (>0)</td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Number of modifications</td>
     * </tr>
     * </table>
     * </blockquote>
     */
    LongAttribute VERSION = CacheEntryAttributes.VERSION;

    /**
     * Inherits the equals contract from {@link Entry}. So two entries with equal key and value but different attributes
     * attached are still equal.
     * 
     * @param o
     *            object to be compared for equality with this cache entry
     * 
     * @return <tt>true</tt> if the specified object is equal to this cache entry
     */
    boolean equals(Object o);

    /** {@inheritDoc} */
    V getValue();
}
