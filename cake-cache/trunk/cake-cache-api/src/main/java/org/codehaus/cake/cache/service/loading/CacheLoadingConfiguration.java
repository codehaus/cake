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

import java.util.Collection;

import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.crud.CrudWriter;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

/**
 * This class is used for configuring the cache loading service prior to usage.
 * <p>
 * If no loader is specified the {@link CacheLoadingService} will not be available at runtime. All values must then be
 * put into the cache by using {@link Cache#put(Object, Object)}, {@link Cache#putAll(java.util.Map)} or some of the
 * methods in {@link CrudWriter}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public class CacheLoadingConfiguration<K, V> {

    /** The needs reload predicate. */
    private Predicate<? super CacheEntry<K, V>> needsReloadCondition;

    /** The cache loader. */
    private Object loader;

    /**
     * Returns the cache loader that has been set using either {@link #setLoader(Op)} or
     * {@link #setLoader(BlockingCacheLoader)}.
     * 
     * @return the loader that the cache should use for loading elements.
     * @see #setLoader(Op)
     * @see #setLoader(BlockingCacheLoader)
     */
    public Object getLoader() {
        return loader;
    }

    /**
     * Sets the cache loader that should be used for loading elements into the cache.
     * <p>
     * Any previously loader that been set will be replaced by the specified loader
     * 
     * @param loader
     *            the cache loader to use
     * @return this configuration
     */
    public CacheLoadingConfiguration<K, V> setLoader(Op<? super K, ? extends V> loader) {
        this.loader = loader;
        return this;
    }

    /**
     * Sets the cache loader that should be used for loading elements into the cache.
     * <p>
     * Any previously loader that has been set will be replaced by the specified loader
     * 
     * @param loader
     *            the cache loader to set
     * @return this configuration
     */
    public CacheLoadingConfiguration<K, V> setLoader(BlockingCacheLoader<? super K, ? extends V> loader) {
        this.loader = loader;
        return this;
    }

    /**
     * Returns the configured needs reload predicate.
     * 
     * @return the configured needs reload predicate
     * @see #setNeedsReloadCondition(Predicate)
     */
    public Predicate<? super CacheEntry<K, V>> getNeedsReloadCondition() {
        return needsReloadCondition;
    }

    /**
     * Sets a ({@link Predicate}) that is used to determind if an element needs to be reloaded. The predicate is
     * checked when invoking any of the load methods in {@link CacheLoadingService}. If the predicate evaluates to
     * <code>true</code> for the entry the entry will be reloaded.
     * <p>
     * Cache implementations might also check the predicate on calls to
     *{@link Cache#get(Object)} but this is not required.
     * 
     * @param predicate
     *            the needs reload predicate
     * @return this configuration
     * @see #getNeedsReloadCondition()
     */
    public CacheLoadingConfiguration<K, V> setNeedsReloadCondition(Predicate<? super CacheEntry<K, V>> predicate) {
        needsReloadCondition = predicate;
        return this;
    }
}
