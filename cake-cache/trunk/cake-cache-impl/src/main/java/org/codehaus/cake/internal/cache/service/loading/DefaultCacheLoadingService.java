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
import org.codehaus.cake.internal.cache.service.management.DefaultCacheLoadingMXBean;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;

public class DefaultCacheLoadingService<K, V> implements ServiceFactory<CacheLoadingService>, Manageable,
        CompositeService {

    private Cache<K, V> cache;

    private InternalCacheLoader<K, V> loader;
    private Predicate<? super CacheEntry<K, V>> needsReloadFilter;

    private Collection<Object> childServices = new ArrayList<Object>();

    private CacheLoadingService<K, V> service = new NoForceLoading();
    private CacheLoadingService<K, V> serviceForced = new ForcedLoading();

    public DefaultCacheLoadingService(Cache<K, V> cache, CacheLoadingConfiguration<K, V> loadingConf,
            InternalCacheLoader<K, V> loader) {
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
    public void manage(ManagedGroup parent) {
        ManagedGroup g = parent.addChild("Loading", "Cache Loading attributes and operations");
        g.add(new DefaultCacheLoadingMXBean(service, serviceForced));
    }

    boolean needsReload(K key) {
        CacheEntry<K, V> entry = cache.peekEntry(key);
        return entry == null || (needsReloadFilter != null && needsReloadFilter.op(entry));
    }

    @Startable
    public void start(ContainerConfiguration<?> configuration, ServiceRegistrant serviceRegistrant) throws Exception {
        // serviceRegistrant.registerService(CacheLoadingService.class, LoadingUtils.wrapService(this));
        serviceRegistrant.registerFactory(CacheLoadingService.class, this);
    }

    public Collection<?> getChildServices() {
        return childServices;
    }

    public CacheLoadingService lookup(AttributeMap attributes) {
        if (attributes.get(CacheLoadingService.IS_FORCED)) {
            return serviceForced;
        }
        return service;
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

}
