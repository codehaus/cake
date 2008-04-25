/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.service.loading;

import java.util.Collection;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;

/**
 * This class is used to configure the loading service prior to usage.
 * <p>
 * If the specified loader is <code>null</code> no loader will be used for loading new
 * key-value bindings. And the {@link CacheLoadingService} will not be available at
 * runtime. All values must then put into the cache by using
 * {@link Cache#put(Object, Object)}, {@link Cache#putAll(java.util.Map)} or some of the
 * other put operations.
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
    private Predicate<CacheEntry<K, V>> needsReloadSelector;

    /** The cache loader. */
    private Object loader;

    /**
     * Returns the cache loader that has been set using either {@link #setLoader(Op)} or
     * {@link #setLoader(SimpleCacheLoader)}.
     * 
     * @return the loader that the cache should use for loading elements.
     * @see #setLoader(Op)
     * @see #setLoader(SimpleCacheLoader)
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
    public CacheLoadingConfiguration<K, V> setLoader(Op<K, V> loader) {
        this.loader = loader;
        return this;
    }

    /**
     * Sets a loader that should be used for loading new elements into the cache.
     * 
     * @param loader
     *            the cache loader to set
     * @return this configuration
     */
    public CacheLoadingConfiguration<K, V> setLoader(
            SimpleCacheLoader<? super K, ? extends V> loader) {
        this.loader = loader;
        return this;
    }

    /**
     * Returns the configured needs reload filter.
     * 
     * @return the configured needs reload filter
     * @see #setNeedsReloadFilter(Predicate)
     */
    public Predicate<CacheEntry<K, V>> getNeedsReloadFilter() {
        return needsReloadSelector;
    }

    /**
     * Sets a filter ({@link Predicate}) that is used for determining if an element
     * needs to be reloaded. The predicate is checked when various load methods in
     * {@link CacheLoadingService} are called.
     * <p>
     * Some implementations might also check the predicate on calls to
     * {@link org.codehaus.cake.cache.Cache#getAll(Collection)},
     * {@link org.codehaus.cake.cache.Cache#getEntry(Object)}, but this is not required.
     * 
     * @param selector
     *            the needs reload predicate
     * @return this configuration
     * @see #getNeedsReloadFilter()
     */
    public CacheLoadingConfiguration<K, V> setNeedsReloadFilter(Predicate<CacheEntry<K, V>> selector) {
        needsReloadSelector = selector;
        return this;
    }
}
