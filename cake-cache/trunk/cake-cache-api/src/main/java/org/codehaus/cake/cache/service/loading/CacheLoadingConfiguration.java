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
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

/**
 * This class is used to configure the loading service prior to usage.
 * <p>
 * If the specified loader is <code>null</code> no loader will be used for loading new key-value bindings. And the
 * {@link CacheLoadingService} will not be available at runtime. All values must then put into the cache by using
 * {@link Cache#put(Object, Object)}, {@link Cache#putAll(java.util.Map)} or some of the other put operations.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLoadingConfiguration.java 529 2007-12-27 17:09:26Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public class CacheLoadingConfiguration<K, V> {

    /** The needs reload predicate. */
    private Predicate<? super CacheEntry<K, V>> needsReloadSelector;

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
     * Sets a loader that should be used for loading new elements into the cache.
     * 
     * @param loader
     *            the cache loader to set
     * @return this configuration
     */
    public CacheLoadingConfiguration<K, V> setLoader(Op<? super K, ? extends V> loader) {
        this.loader = loader;
        return this;
    }

    /**
     * Sets a blocking cache loader that should be used for loading new elements into the cache.
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
     * Returns the configured needs reload filter.
     * 
     * @return the configured needs reload filter
     * @see #setNeedsReloadFilter(Predicate)
     */
    public Predicate<? super CacheEntry<K, V>> getNeedsReloadFilter() {
        return needsReloadSelector;
    }

    /**
     * Sets a filter ({@link Predicate}) that is used for determining if an element needs to be reloaded. The predicate
     * is checked when various load methods in {@link CacheLoadingService} are called.
     * <p>
     * Some implementations might also check the predicate on calls to
     * {@link org.codehaus.cake.cache.Cache#getAll(Collection)}, {@link org.codehaus.cake.cache.Cache#getEntry(Object)},
     * but this is not required.
     * 
     * @param selector
     *            the needs reload predicate
     * @return this configuration
     * @see #getNeedsReloadFilter()
     */
    public CacheLoadingConfiguration<K, V> setNeedsReloadFilter(Predicate<? super CacheEntry<K, V>> selector) {
        needsReloadSelector = selector;
        return this;
    }

    /**
     * An Attribute indicating whether or not a loading should be forced.
     */
    static final class IsLoadingForcedAttribute extends BooleanAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new SizeAttribute. */
        IsLoadingForcedAttribute() {
            super("LoadingForced");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheLoadingService.IS_FORCED;
        }
    }
}
