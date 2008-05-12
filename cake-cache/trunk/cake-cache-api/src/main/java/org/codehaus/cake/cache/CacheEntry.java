package org.codehaus.cake.cache;

import java.util.Map;

import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.WithAttributes;
import org.codehaus.cake.attribute.common.DurationAttribute;
import org.codehaus.cake.attribute.common.TimeInstanceAttribute;

/**
 * A <tt>CacheEntry</tt> describes a value-key mapping much like {@link java.util.Map.Entry}.
 * However, this interface extends with a map of attribute->value pairs. Holding information such as
 * creation time, access patterns, size, cost etc.
 * <p>
 * Unless otherwise specified a cache entry obtained from a cache is always an immmutable copy of
 * the existing entry. If the value for a given key is updated while another thread holds a cache
 * entry for the key. It will not be reflected in calls to {@link #getValue()}.
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
     * The <tt>Cost attribute</tt> can be used to indicate the cost of retrieving an entry. The
     * idea is that when memory is sparse the cache can choose to evict entries that are less costly
     * to retrieve again. Currently this attribute is not used by any of the build in replacement
     * policies.
     * <p>
     * While not required the unit of this attribute is most often <tt>time</tt>. For example,
     * how many milliseconds does it take to retrieve the entry.
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
     * <td>All values, except {@link Double#NEGATIVE_INFINITY}, {@link Double#POSITIVE_INFINITY}
     * and {@link Double#NaN}</td>
     * </tr>
     * </table> </blockquote>
     */
    public static final DoubleAttribute COST = new Caches.CostAttribute();

    /**
     * Currently not in use.
     */
    public static final TimeInstanceAttribute TIME_ACCESSED = new Caches.TimeCreatedAttribute();

    /**
     * The time between when the entry was created and midnight, January 1, 1970 UTC. This is also
     * the value returned by {@link System#currentTimeMillis()}. <p/> The
     * <ul>
     * <li>
     * If the entry is being loaded and the cache loader and this attribute has been set with a
     * positive value (>0). This value will be used to as the entrys creation time.
     * </li>
     * <li>
     * If a mapping already exists in the cache for the given key, the creation time is inherited
     * from this entry (the entry that is being replaced).
     * </li>
     * <li>
     * If no element is currently mapped for the key and a clock is set through
     * {@link CacheConfiguration#setClock(org.codehaus.cake.util.Clock)} a timestamp is obtained by
     * calling {@link org.codehaus.cake.util.Clock#timeOfDay()}.
     * </li>
     * <li>
     * Finally, if no clock was set when constructing the cache, System#currentTimeMillis() is used
     * for obtaining a timestamp.
     * </li>
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
     * </table> </blockquote>
     * 
     * The creation time value is of type <tt>long</tt> and between 1 and {@link Long#MAX_VALUE}.
     */
    public static final TimeInstanceAttribute TIME_CREATED = new Caches.TimeCreatedAttribute();

    /**
     * The time between when the entry was last modified and midnight, January 1, 1970 UTC. This is
     * also the value returned by {@link System#currentTimeMillis()}.
     * <p>
     * The mapped value must be of a type <tt>long</tt> between 1 and {@link Long#MAX_VALUE}.
     */
    public static final TimeInstanceAttribute TIME_MODIFIED = new Caches.TimeModificedAttribute();

    /**
     * The size of the cache entry.
     */
    public static final LongAttribute SIZE = new Caches.SizeAttribute();

    /**
     * The size of the cache entry.
     */
    public static final LongAttribute HITS = new Caches.HitsAttribute();

    
    public static final LongAttribute VERSION = new Caches.VersionAttribute();
    
    // public static final TimeInstanceAttribute ENTRY_TIME_TO_LIVE = ENTRY_DATE_MODIFIED;

    // cost of retrieving the item
    // time to live
    // expiration time = time to live + System.timestamp
    // Logger <-detailed logging about an entry.

}
