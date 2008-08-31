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
package org.codehaus.cake.internal.cache.service.loading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.management.annotation.ManagedObject;
import org.codehaus.cake.management.annotation.ManagedOperation;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;

@ManagedObject(defaultValue = "Loading", description = "Cache Loading attributes and operations")
public class DefaultCacheLoadingService<K, V> implements ServiceFactory<CacheLoadingService>, CompositeService {

    private Cache<K, V> cache;

    private InternalCacheLoadingService<K, V> loader;
    private Predicate<? super CacheEntry<K, V>> needsReloadFilter;

    private Collection<Object> childServices = new ArrayList<Object>();

    private CacheLoadingService<K, V> service = new NoForceLoading();
    private CacheLoadingService<K, V> serviceForced = new ForcedLoading();

    public DefaultCacheLoadingService(Cache<K, V> cache, CacheLoadingConfiguration<K, V> loadingConf,
            InternalCacheLoadingService<K, V> loader) {
        this.cache = cache;
        this.loader = loader;
        this.needsReloadFilter = loadingConf.getNeedsReloadFilter();
        childServices.add(loadingConf.getLoader());
        childServices.add(needsReloadFilter);
    }

    Map<? extends K, ? extends AttributeMap> filterNeedsReload(Map<? extends K, ? extends AttributeMap> map) {
        for (Iterator<? extends K> iterator = map.keySet().iterator(); iterator.hasNext();) {
            K key = iterator.next();
            if (!needsReload(key)) {
                iterator.remove();
            }
        }
        return map;
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "reload all mappings")
    public void forceLoadAll() {
        serviceForced.loadAll();
    }

    /** {@inheritDoc} */
    @ManagedOperation(description = "Attempts to reload all entries that are either expired or which needs refreshing")
    public void loadAll() {
        service.loadAll();
    }

    boolean needsReload(K key) {
        CacheEntry<K, V> entry = cache.peekEntry(key);
        return entry == null || (needsReloadFilter != null && needsReloadFilter.op(entry));
    }

    @Startable
    public void start(ContainerConfiguration<?> configuration, ServiceRegistrant serviceRegistrant) {
        serviceRegistrant.registerFactory(CacheLoadingService.class, this);
    }

    public Collection<?> getChildServices() {
        return childServices;
    }

    class NoForceLoading extends AbstractCacheLoadingService<K, V> {
        void doLoad(K key, AttributeMap attributes) {
            if (!cache.isShutdown()) {
                if (needsReload(key)) {
                    loader.loadAsync(key, attributes);
                }
            }
        }

        void doLoadAll(AttributeMap attributes) {
            loadAll(cache.keySet(), attributes);
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
            if (!cache.isShutdown()) {
                loader.loadAsync((Map) filterNeedsReload(mapWithAttributes));
            }
        }
    }

    class ForcedLoading extends AbstractCacheLoadingService<K, V> {
        void doLoad(K key, AttributeMap attributes) {
            if (!cache.isShutdown()) {
                loader.loadAsync(key, attributes);
            }
        }

        void doLoadAll(AttributeMap attributes) {
            loadAll(cache.keySet(), attributes);
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
            if (!cache.isShutdown()) {
                loader.loadAsync((Map) mapWithAttributes);
            }
        }
    }

    public CacheLoadingService lookup(
            org.codehaus.cake.service.ServiceFactory.ServiceFactoryContext<CacheLoadingService> context) {
        if (CacheLoadingService.IS_FORCED.isTrue(context)) {
            return serviceForced;
        }
        return service;
    }

}
