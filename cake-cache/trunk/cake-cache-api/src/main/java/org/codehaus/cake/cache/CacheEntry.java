package org.codehaus.cake.cache;

import java.util.Map;

import org.codehaus.cake.attribute.WithAttributes;

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
}
