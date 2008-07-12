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
package org.codehaus.cake.cache.service.loading;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.BooleanAttribute;

public interface CacheLoadingService<K, V> {

    /**
     * A quick way to get a Forced instance is: Cache<?,?> someCache; someCache.with().withLoadingForced().load(5);
     */
    BooleanAttribute IS_FORCED = new CacheLoadingConfiguration.IsLoadingForcedAttribute();

    // Additional attributes
    // Priority?? ->should probably be implemented at the cache loader level
    // Logger
    // Await load done, come up with use cases
    // Runnable callback? come up with use cases

    /**
     * If a mapping for the specified key is not already in the cache. This method will attempt to load the value for
     * the specified key from the configured cache loader.
     * <p>
     * This method does not guarantee that the specified value is ever loaded into the cache. Implementations are free
     * to ignore the hint, however, most implementations won't.
     * <p>
     * Unless otherwise specified the loading is done asynchronously. Any cache implementation that is not thread-safe
     * (ie supposed to be accessed by a single thread only) will need to load the value before returning from this
     * method. Because it cannot allow a background thread to add the value to cache once loaded.
     * <p>
     * If this cache has been shutdown calls to this method is ignored.
     * 
     * @param key
     *            whose associated value is to be loaded.
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional).
     * @throws NullPointerException
     *             if the specified key is <tt>null</tt>
     */
    void load(K key);

    /**
     * This method works analogous to the {@link #load(Object)} method. Except that all the attributes in the the
     * specified attribute map will be parsed along to the {@link BlockingCacheLoader#load(Object, AttributeMap)} method
     * of the configured cache loader.
     * <p>
     * If this cache has been shutdown calls to this method is ignored.
     * 
     * @param key
     *            whose associated value is to be loaded.
     * @param attributes
     *            a map of attributes that will be available in the attribute map parsed to the
     *            {@link BlockingCacheLoader#load(Object, AttributeMap)} method of the configured cache loader
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional).
     * @throws NullPointerException
     *             if the specified key or attribute map is <tt>null</tt>
     */
    void load(K key, AttributeMap attributes);

    /**
     * Attempts to reload all entries that are either expired or which needs refreshing.
     */
    void loadAll();

    /**
     * Attempts to reload all entries that are either expired or which needs refreshing.
     * <p>
     * If this cache has been shutdown calls to this method is ignored.
     * 
     * @param attributes
     *            a map of attributes that will be available in the attribute map parsed to
     *            {@link BlockingCacheLoader#load(Object, AttributeMap)} method of the configured cache loader
     * @throws NullPointerException
     *             if the specified attribute map is <tt>null</tt>
     */
    void loadAll(AttributeMap attributes);

    /**
     * For all keys in the specified collection and where a valid mapping from the key is not already in the cache. This
     * method will attempt to load the value for the key from the configured cache loader. The effect of this call is
     * equivalent to that of calling {@link #load(Object)} once for each key in the specified collection. However, This
     * operation may be more efficient than repeatedly calling {@link #load(Object)} for each key.
     * <p>
     * The behavior of this operation is unspecified if the specified collection is modified while the operation is in
     * progress.
     * <p>
     * If this cache has been shutdown calls to this method is ignored.
     * 
     * @param keys
     *            whose associated values is to be loaded.
     * @throws ClassCastException
     *             if any of the keys in the specified collection are of an inappropriate type for this cache
     *             (optional).
     * @throws NullPointerException
     *             if the specified collection of keys is <tt>null</tt> or the specified collection contains a
     *             <tt>null</tt> value
     */
    void loadAll(Iterable<? extends K> keys);

    void loadAll(Iterable<? extends K> keys, AttributeMap attributes);

    /**
     * <p>
     * If this cache has been shutdown calls to this method is ignored.
     * 
     * @param mapsWithAttributes
     *            a map with keys that should be loaded and a corresponding attribute map
     */
    void loadAll(Map<? extends K, ? extends AttributeMap> mapsWithAttributes);

}
