package org.codehaus.cake.internal.cache.service.loading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.loading.CacheLoadingService;
import org.codehaus.cake.internal.cache.service.management.DefaultCacheLoadingMXBean;
import org.codehaus.cake.internal.service.spi.CompositeService;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;

public class DefaultCacheLoadingService<K, V> implements CacheLoadingService<K, V>,
         Manageable, CompositeService {

    private Cache<K, V> cache;

    private InternalCacheLoader<K, V> loader;
    private Predicate<CacheEntry<K, V>> needsReloadFilter;

    private Collection col = new ArrayList();

    public DefaultCacheLoadingService(Cache<K, V> cache,
            CacheLoadingConfiguration<K, V> loadingConf, InternalCacheLoader<K, V> loader) {
        this.cache = cache;
        this.loader = loader;
        this.needsReloadFilter = loadingConf.getNeedsReloadFilter();
        col.add(loadingConf.getLoader());
        col.add(needsReloadFilter);
    }

    Map<? extends K, AttributeMap> filterNeedsReload(Map<? extends K, AttributeMap> map) {
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
        g.add(new DefaultCacheLoadingMXBean(this));
    }

    boolean needsReload(K key) {
        CacheEntry<K, V> entry = cache.peekEntry(key);
        return entry == null || (needsReloadFilter != null && needsReloadFilter.op(entry));
    }

    @Startable
    public void start(ContainerConfiguration<?> configuration, ServiceRegistrant serviceRegistrant)
            throws Exception {
        serviceRegistrant
                .registerService(CacheLoadingService.class, LoadingUtils.wrapService(this));
    }

    public WithLoad<V> withAll() {
        return withAll(Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public WithLoad<V> withAll(final AttributeMap attributes) {
        if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        return withKeys(cache.keySet(), attributes);
    }

    public WithLoad<V> withKey(final K key) {
        return withKey(key, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    public WithLoad<V> withKey(final K key, final AttributeMap attributes) {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        return new WithLoad<V>() {
            public void forceLoad() {
                if (!cache.isShutdown()) {
                    loader.loadAsync(key, attributes);
                }
            }

            public void load() {
                if (needsReload(key)) {
                    forceLoad();
                }
            }
        };
    }

 
    public WithLoad<V> withKeys(Iterable<? extends K> keys) {
        return withKeys(keys, Attributes.EMPTY_ATTRIBUTE_MAP);
    }


    public WithLoad<V> withKeys(Iterable<? extends K> keys, AttributeMap attributes) {
        if (keys == null) {
            throw new NullPointerException("keys is null");
        } else if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
        HashMap<K, AttributeMap> map = new HashMap<K, AttributeMap>();
        for (K key : keys) {
            if (key == null) {
                throw new NullPointerException("Collection contains a null key");
            }
            map.put(key, attributes);
        }
        return withKeys(map);
    }

    private WithLoad<V> withKeys(final Map<? extends K, AttributeMap> mapAndAttributes) {
        return new WithLoad<V>() {
            public void forceLoad() {
                if (!cache.isShutdown()) {
                    loader.loadAsync(mapAndAttributes);
                }
            }

            public void load() {
                if (!cache.isShutdown()) {
                    loader.loadAsync(filterNeedsReload(mapAndAttributes));
                }
            }
        };
    }


    public Collection<?> getChildServices() {
        return col;
    }
}
