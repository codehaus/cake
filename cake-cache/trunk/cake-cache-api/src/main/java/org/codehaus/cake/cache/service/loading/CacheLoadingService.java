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
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.CacheServices;
import org.codehaus.cake.service.Container;

/**
 * The cache loading service is used for prefetching entries into the cache. When an entry is actually needed, the data
 * can be accessed much more quickly from the cache than if it had to make a request from external storage. The easist
 * way to obtain a {@link CacheLoadingService} is by using {@link CacheServices}.
 * 
 * <pre>
 * Cache&lt;?, ?&gt; c = someCache;
 * CacheLoadingService&lt;?, ?&gt; cls = c.with().loading();
 * cls.load(someKey);
 * </pre>
 * 
 * Alternatively {@link Cache#getService(Class)} can be used to look up the service.
 * 
 * <pre>
 * Cache&lt;?, ?&gt; c = someCache;
 * CacheLoadingService&lt;?, ?&gt; cls = c.getService(CacheLoadingService.class);
 * cls.loadAll();
 * </pre>
 * 
 * <p>
 * Unless otherwise specified all methods are methods in this class are asynchronously and returns immediately (the
 * actual loading will be done by background threads). Any cache implementation that is not thread-safe (ie supposed to
 * be accessed by a single thread only) will need to load the value before returning from any of the methods in this
 * class. As it cannot allow a background thread to add or update a mapping in the cache once loaded.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: MemoryStoreService.java 563 2008-01-10 15:20:33Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public interface CacheLoadingService<K, V> {

    /**
     * An attribute that can be used as parameter to {@link Container#getService(Class, AttributeMap)} to acquire an
     * instance of this interface that will force loading of entries even if they are already in the cache. The default
     * value of this attribute is <code>false</code> = not-forced.
     * <p>
     * The quickest way to get a Forced instance of a CacheLoadingService is calling
     * 
     * <pre>
     * cache.with().loadingForced()
     * </pre>
     */
    BooleanAttribute IS_FORCED = new CacheLoadingUtils.IsLoadingForcedAttribute();

    /**
     * If no mapping for the specified key exists in the cache calling this method will asynchronously call any
     * configured cache loaded (see {@link CacheLoadingConfiguration#setLoader(BlockingCacheLoader)} or
     * {@link CacheLoadingConfiguration#setLoader(Op)} with the specified key and put any non-null result into the
     * cache. If a mapping already exists for the specified key and a needs-reload-predicate has been set using
     * {@link CacheLoadingConfiguration#setNeedsReloadCondition(Predicate))}. And the predicate evaluates to
     * <code>true</code> for the entry the key maps to, the entry will reloaded asynchronously.
     * <p>
     * If this service is in <tt>forced</tt> mode (cache.with().forcedLoading()) the service will always load a new
     * value, even if an existing mappings already exists in the cache.
     * <p>
     * If the cache has been shutdown any calls to this method will be ignored.
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
     * This method works analogous to the {@link #load(Object)} method. Except that all the attributes in the specified
     * attribute map will be copied into the attribute map parsed to the
     * {@link BlockingCacheLoader#load(Object, AttributeMap)} method.
     * <p>
     * If the cache has been shutdown any calls to this method will be ignored.
     * 
     * @param key
     *            whose associated value is to be loaded.
     * @param attributes
     *            a map of attributes whose content will be available in the attribute map parsed along to the cache
     *            loader
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional).
     * @throws NullPointerException
     *             if the specified key or attribute map is <tt>null</tt>
     */
    void load(K key, AttributeMap attributes);

    /**
     * Equivalent to that of calling {@link #load(Object)} with the key of each entry in the cache. However, This
     * operation may be more efficient than repeatedly calling {@link #load(Object)}.
     * <p>
     * This method can be combined with {@link Cache#select()} to reload parts of the cache. For example, the following
     * example will reload all entries (assuming the {@link CacheEntry#TIME_MODIFIED attribute is enabled} which has not been
     * updated for 1 hour.
     * 
     * <p>
     * If the cache has been shutdown any calls to this method will be ignored.
     */
    void loadAll();

    /**
     * Equivalent to that of calling {@link #load(Object, AttributeMap)} with the key of each entry in the cache and the
     * specified attribute map. However, This operation may be more efficient than repeatedly calling
     * {@link #load(Object, AttributeMap)}.
     * <p>
     * If the cache has been shutdown any calls to this method will be ignored.
     * 
     * @param attributes
     *            a map of attributes
     * @throws NullPointerException
     *             if the specified attribute map is <tt>null</tt>
     * @see #loadAll()
     */
    void loadAll(AttributeMap attributes);

    /**
     * Equivalent to that of calling {@link #load(Object)} once for each key in the specified collection. However, This
     * operation may be more efficient than repeatedly calling {@link #load(Object)} for each key.
     * <p>
     * The behavior of this operation is unspecified if the specified collection is modified while the operation is in
     * progress.
     * <p>
     * If the cache has been shutdown any calls to this method will be ignored.
     * 
     * @param keys
     *            whose associated values are to be loaded.
     * @throws ClassCastException
     *             if any of the keys in the specified collection are of an inappropriate type for this cache
     *             (optional).
     * @throws NullPointerException
     *             if the specified collection of keys is <tt>null</tt> or the specified collection contains a
     *             <tt>null</tt> value
     */
    void loadAll(Iterable<? extends K> keys);

    /**
     * Equivalent to that of calling {@link #load(Object, AttributeMap)} for each key in the specified collection and
     * with the specified attribute map as a parameter. However, This operation may be more efficient than repeatedly
     * calling {@link #load(Object, AttributeMap)} for each key.
     * <p>
     * The behavior of this operation is unspecified if the specified collection or attribute map is modified while the
     * operation is in progress.
     * <p>
     * If the cache has been shutdown any calls to this method will be ignored.
     * 
     * @param keys
     *            whose associated values are to be loaded.
     * @param attributes
     *            a parameter attribute map
     * @throws ClassCastException
     *             if any of the keys in the specified collection are of an inappropriate type for this cache
     *             (optional).
     * @throws NullPointerException
     *             if the specified collection of keys is <tt>null</tt>, the specified collection contains a
     *             <tt>null</tt> value or the specified attribute map is null
     */
    void loadAll(Iterable<? extends K> keys, AttributeMap attributes);

    /**
     * Equivalent to calling {@link #load(Object, AttributeMap)} for each entry in the map.
     * <p>
     * The behavior of this operation is unspecified if the specified map or any of the attributemaps are modified while
     * the operation is in progress.
     * <p>
     * If the cache has been shutdown any calls to this method will be ignored.
     * 
     * @param keysAttributes
     *            the keys that should be loaded and the corresponding attribute map
     * @throws ClassCastException
     *             if any of the keys in the specified map are of an inappropriate type for this cache (optional).
     * @throws NullPointerException
     *             if the specified map is <tt>null</tt>, the specified map contains a <tt>null</tt> key or
     *             <code>null</code> attribute map (value)
     */
    void loadAll(Map<? extends K, ? extends AttributeMap> keysAttributes);
}
