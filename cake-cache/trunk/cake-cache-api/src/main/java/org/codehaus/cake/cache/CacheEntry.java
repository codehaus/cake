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
package org.codehaus.cake.cache;

import java.util.Map;

import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.WithAttributes;
import org.codehaus.cake.attribute.common.TimeInstanceAttribute;
import org.codehaus.cake.cache.service.attribute.CacheAttributeConfiguration;

/**
 * A <tt>CacheEntry</tt> describes a value-key mapping much like {@link java.util.Map.Entry}. However, this interface
 * extends it with a map of attribute->value pairs. Holding information such as creation time, access patterns, size,
 * cost etc.
 * <p>
 * Per default the cache does not keep track of any attributes. Attributes that should be retained at runtime must first
 * be specified in {@link CacheAttributeConfiguration} before starting the cache.
 * 
 * For example, this snippet shows how to configure the cache to keep track of when an entry was created and when it was
 * last modified.
 * 
 * <pre>
 * CacheConfiguration&lt;Integer, String&gt; conf = CacheConfiguration.newConfiguration();
 * conf.withAttributes().add(CacheEntry.TIME_CREATED, CacheEntry.TIME_MODIFIED);
 * Cache&lt;Integer, String&gt; cache = SynchronizedCache.from(conf);
 * cache.put(5, &quot;test&quot;);
 * long creationTime = cache.getEntry(5).getAttributes().get(CacheEntry.TIME_CREATED);
 * </pre>
 * 
 * <p>
 * Unless otherwise specified a cache entry obtained from a cache is always an immmutable copy of the existing entry. If
 * the value for a given key is updated while another thread holds a cache entry for the key. It will not be reflected
 * in calls to {@link #getValue()}. Some implementations might allow for certain attributes to be continuesly updated
 * in the entry for performance reasons. For example {@link #TIME_ACCESSED} and {@link #HITS}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheEntry.java 536 2007-12-30 00:14:25Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public interface CacheEntry<K, V> extends Map.Entry<K, V>, WithAttributes {
    // <T> T get(Attribute<T> attribute);
    // long get(LongAttribute attribute);
    // double get(DoubleAttribute attribute);
    // int get(IntAttribute attribute);

    /* Entry attributes */

    /**
     * The <tt>Cost attribute</tt> is used to indicate the cost of retrieving an entry. The idea is that when memory
     * is sparse the cache can choose to evict entries that are least costly to retrieve again. Currently this attribute
     * is not used by any of the build in replacement policies.
     * <p>
     * A frequent used unit for this attribute is time. For example, how many milliseconds does it take to retrieve the
     * entry. However, any unit can be used. Because policies only use the relative cost difference between entries to
     * determine what entries to evict.
     * <p>
     * <blockquote> <table border>
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
     * <td>All values, except {@link Double#NEGATIVE_INFINITY}, {@link Double#POSITIVE_INFINITY} and
     * {@link Double#NaN}</td>
     * </tr>
     * <tr>
     * <td>Unit</td>
     * <td>Primarily <tt>time</tt>, but any unit is acceptable</td>
     * </tr>
     * </table> </blockquote>
     */
    DoubleAttribute COST = new Caches.CostAttribute();

    /**
     * A count of how many times an entry has been accessed through {@link Cache#get(Object)},
     * {@link Cache#getEntry(Object)} or {@link Cache#getAll(java.util.Collection)}.
     * 
     * <p/> The following list describes how this attribute is obtained.
     * <ul>
     * <li> If the entry is being loaded and the <tt>HITS</tt> attribute has been set the cache will use this value.</li>
     * <li> Else if this entry is replacing an existing entry the hit count from the existing entry will be used. </li>
     * <li> Else if this entry is accessed through <tt>get</tt>, <tt>getEntry</tt> or <tt>getAll</tt> the hit
     * count is incremented by 1</li>
     * </ul>
     * <p>
     * <blockquote> <table border>
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
     * </table> </blockquote>
     */
    LongAttribute HITS = new Caches.HitsAttribute();

    /**
     * The size of the cache entry.
     */
    LongAttribute SIZE = new Caches.SizeAttribute();

    /**
     * The time between when the entry was last accessed and midnight, January 1, 1970 UTC. This is also the value
     * returned by {@link System#currentTimeMillis()}.
     * <p>
     * <blockquote> <table border>
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
     * </table> </blockquote>
     */
    TimeInstanceAttribute TIME_ACCESSED = new Caches.TimeAccessedAttribute();

    /**
     * The time between when the entry was created and midnight, January 1, 1970 UTC. This is also the value returned by
     * {@link System#currentTimeMillis()}. <p/> The following list describes how this attribute is obtained.
     * <ul>
     * <li> If the entry is being loaded and the <tt>TIME_CREATED</tt> attribute has been set the cache will use this
     * value.</li>
     * <li> Else if this entry is replacing an existing entry the creation time from the existing entry will be used.
     * </li>
     * <li> Else if a clock is set through {@link CacheConfiguration#setClock(org.codehaus.cake.util.Clock)} a timestamp
     * is obtained by calling {@link org.codehaus.cake.util.Clock#timeOfDay()}. </li>
     * <li> Else System#currentTimeMillis() is used for obtaining a timestamp. </li>
     * </ul>
     * <p>
     * <blockquote> <table border>
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
     * </table> </blockquote>
     */
    TimeInstanceAttribute TIME_CREATED = new Caches.TimeCreatedAttribute();

    /**
     * The time between when the entry was last modified and midnight, January 1, 1970 UTC. This is also the value
     * returned by {@link System#currentTimeMillis()}.
     * <p>
     * The mapped value must be of a type <tt>long</tt> between 1 and {@link Long#MAX_VALUE}.
     */
    TimeInstanceAttribute TIME_MODIFIED = new Caches.TimeModificedAttribute();

    /**
     * A count of how many times the value of an entry has been modified.
     * 
     * <p/> The following list describes how this attribute is obtained.
     * <ul>
     * <li> If the entry is being loaded and the <tt>VERSION</tt> attribute has been set the cache will use this
     * value.</li>
     * <li> Else if this entry is replacing an existing entry the hit count from the existing entry + 1 will be used.
     * </li>
     * <li> Else the version is initialized to 1</li>
     * </ul>
     * <p>
     * <blockquote> <table border>
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
     * </table> </blockquote>
     */
    LongAttribute VERSION = new Caches.VersionAttribute();

    // cost of retrieving the item
    // Logger <-detailed logging about an entry.
}
