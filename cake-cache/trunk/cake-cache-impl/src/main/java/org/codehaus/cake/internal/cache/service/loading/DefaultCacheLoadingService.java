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
package org.codehaus.cake.internal.cache.service.loading;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.loading.CacheLoadingService;
import org.codehaus.cake.internal.cache.InternalCacheAttributes;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.internal.util.CollectionUtils;
import org.codehaus.cake.management.ManagedObject;
import org.codehaus.cake.management.ManagedOperation;
import org.codehaus.cake.service.ExportAsService;
import org.codehaus.cake.service.ServiceProvider;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.ops.Predicates;
import org.codehaus.cake.util.ops.Ops.Predicate;

@ManagedObject(defaultValue = "Loading", description = "Cache Loading attributes and operations")
@ExportAsService(CacheLoadingService.class)
public class DefaultCacheLoadingService<K, V> implements ServiceProvider<CacheLoadingService<?, ?>>, CompositeService {

    private final Collection<Object> childServices;

    private final InternalCacheLoadingService<K, V> loader;
    private final Predicate<CacheEntry<K, V>> needsReloadFilter;

    private final Loading loadAll;
    private final Loading forceLoadAll;

    public DefaultCacheLoadingService(Cache<K, V> cache, CacheLoadingConfiguration<K, V> loadingConf,
            InternalCacheLoadingService<K, V> loader) {
        this.loader = loader;
        final Predicate<CacheEntry<K, V>> needsReloadFilter = (Predicate) loadingConf.getNeedsReloadCondition();
        this.needsReloadFilter = needsReloadFilter == null ? Predicates.FALSE : needsReloadFilter;
        childServices = Arrays.asList(loadingConf.getLoader(), needsReloadFilter);
        forceLoadAll = new Loading(cache, true);
        loadAll = new Loading(needsReloadFilter == null ? cache : cache.filter().on(
                (Predicate) Predicates.not(needsReloadFilter)), false);
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Reload all mappings")
    public void forceLoadAll() {
        forceLoadAll.loadAll();
    }

    public Collection<?> getChildServices() {
        return childServices;
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Attempts to reload all entries that are either expired or which needs refreshing")
    public void loadAll() {
        loadAll.loadAll();
    }

    public CacheLoadingService<?, ?> lookup(Context<CacheLoadingService<?, ?>> context) {
        Cache<K, V> cache = context.getAttributes().get(InternalCacheAttributes.CONTAINER);
        assert cache != null;
        if (context.getAttributes().get(CacheLoadingService.IS_FORCED)) {
            return new Loading(cache, true);
        }
        return new Loading(cache, false);
    }

    class Loading implements CacheLoadingService<K, V> {

        // forced->select all in cache
        // no-forced->select all in by needsRefresh
        // no-forced->when load(key) test that it isn't is in !needsRefresh
        private final Cache<K, V> needsRefreshCache;
        private final Cache<K, V> needsNotRefreshCache;
        private final boolean isForced;

        Loading(Cache<K, V> cache, boolean isForced) {
            if (isForced) {
                this.needsRefreshCache = cache;
                this.needsNotRefreshCache = cache;
            } else {
                this.needsRefreshCache = cache.filter().on(needsReloadFilter);
                this.needsNotRefreshCache = cache.filter().on((Predicate) Predicates.not(needsReloadFilter));
            }
            this.isForced = isForced;
        }

        void doLoad(K key, AttributeMap attributes) {
            if (!needsRefreshCache.getState().isShutdown() && (isForced || !needsNotRefreshCache.containsKey(key))) {
                loader.loadAsync(key, attributes);
            }
        }

        void doLoadAll(AttributeMap attributes) {
            if (!needsRefreshCache.getState().isShutdown()) {
                HashMap<K, AttributeMap> map = new HashMap<K, AttributeMap>();
                Object[] keys = needsRefreshCache.asMap().keySet().toArray();
                for (Object key : keys) {
                    if (key == null) {
                        throw new NullPointerException("key is null");
                    }
                    K k = (K) key;
                    map.put(k, attributes);
                }
                loader.loadAsync((Map) map);
            }
        }

        void doLoadAll(Iterable<? extends K> keys, AttributeMap attributes) {
            HashMap<K, AttributeMap> map = new HashMap<K, AttributeMap>();
            for (K key : keys) {
                if (key == null) {
                    throw new NullPointerException("Collection contains a null key");
                }
                map.put(key, attributes);
            }
            doLoadAll(map);
        }

        void doLoadAll(Map<? extends K, ? extends AttributeMap> mapWithAttributes) {
            if (!needsNotRefreshCache.getState().isShutdown()) {
                Map<? extends K, ? extends AttributeMap> m = mapWithAttributes;
                if (!isForced) {
                    // TODO should have a check whether or not the size of the cache is 0
                    // in which case we should just reload everything
                    m = new HashMap<K, AttributeMap>(mapWithAttributes);
                    for (Iterator<? extends K> iterator = m.keySet().iterator(); iterator.hasNext();) {
                        K key = iterator.next();
                        if (needsNotRefreshCache.containsKey(key)) {
                            iterator.remove();
                        }
                    }
                }
                loader.loadAsync((Map) m);
            }
        }

        public void load(K key) {
            load(key, Attributes.EMPTY_ATTRIBUTE_MAP);
        }

        public void load(K key, AttributeMap attributes) {
            if (key == null) {
                throw new NullPointerException("key is null");
            } else if (attributes == null) {
                throw new NullPointerException("attributes is null");
            }
            doLoad(key, attributes);
        }

        public void loadAll() {
            loadAll(Attributes.EMPTY_ATTRIBUTE_MAP);
        }

        public void loadAll(AttributeMap attributes) {
            if (attributes == null) {
                throw new NullPointerException("attributes is null");
            }
            doLoadAll(attributes);
        }

        public void loadAll(Iterable<? extends K> keys) {
            loadAll(keys, Attributes.EMPTY_ATTRIBUTE_MAP);
        }

        public void loadAll(Iterable<? extends K> keys, AttributeMap attributes) {
            if (keys == null) {
                throw new NullPointerException("keys is null");
            } else if (attributes == null) {
                throw new NullPointerException("attributes is null");
            }
            // CollectionUtils.checkCollectionForNulls(keys);
            doLoadAll(keys, attributes);
        }

        public void loadAll(Map<? extends K, ? extends AttributeMap> mapsWithAttributes) {
            if (mapsWithAttributes == null) {
                throw new NullPointerException("mapsWithAttributes is null");
            }
            CollectionUtils.checkMapForNulls(mapsWithAttributes);
            doLoadAll(mapsWithAttributes);
        }
    }
}
