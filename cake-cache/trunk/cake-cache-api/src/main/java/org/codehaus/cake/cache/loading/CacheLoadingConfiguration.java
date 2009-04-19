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
package org.codehaus.cake.cache.loading;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.CacheWriter;
import org.codehaus.cake.util.ops.Ops.Predicate;

/**
 * This class is used for configuring the cache loading service prior to usage.
 * <p>
 * If no loader is specified the {@link CacheLoadingService} will not be available at runtime. All values must then be
 * put into the cache by using {@link Cache#put(Object, Object)}, {@link Cache#putAll(java.util.Map)} or some of the
 * methods in {@link CacheWriter}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLoadingConfiguration.java 327 2009-04-08 09:34:27Z kasper $
 * @param <K>
 *            the type of keys maintained by the cache
 * @param <V>
 *            the type of mapped values
 */
public class CacheLoadingConfiguration<K, V> {

    /** The cache loader. */
    private Object loader;

    /** The needs reload predicate. */
    private Predicate<? super CacheEntry<K, V>> needsReloadCondition;

    /**
     * Returns the cache loader that has been set using either {@link #setLoader(Object)}.
     * 
     * @return the loader that the cache should use for loading elements.
     * @see #setLoader(Object)
     */
    public Object getLoader() {
        return loader;
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
     * Sets the cache loader that should be used for loading elements into the cache.
     * <p>
     * Any previously loader that has been set will be replaced by the specified loader
     * 
     * @param loader
     *            the cache loader to set
     * @return this configuration
     */
    public CacheLoadingConfiguration<K, V> setLoader(Object loader) {
        this.loader = loader;
        // verify loader
        if (loader instanceof Class) {
            //check public constructor
        }
        //verify one and only one method
        return this;
    }

    /**
     * Sets a ({@link Predicate}) that is used to determine if an element needs to be reloaded. The predicate is checked
     * when invoking any of the load methods in {@link CacheLoadingService}. If the predicate evaluates to
     * <code>true</code> for the entry the entry will be reloaded.
     * <p>
     * Cache implementations might also check the predicate on calls to {@link Cache#get(Object)} but this is not
     * required.
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
