/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.internal.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.CacheMXBean;
import org.codehaus.cake.cache.CacheServices;
import org.codehaus.cake.internal.cache.service.exceptionhandling.DefaultCacheExceptionService;
import org.codehaus.cake.internal.cache.service.listener.DefaultCacheListener;
import org.codehaus.cake.internal.cache.service.listener.InternalCacheListener;
import org.codehaus.cake.internal.cache.service.management.DefaultCacheMXBean;
import org.codehaus.cake.internal.cache.service.memorystore.HashMapMemoryStore;
import org.codehaus.cake.internal.cache.service.memorystore.MemoryStore;
import org.codehaus.cake.internal.cache.service.memorystore.views.CollectionViews;
import org.codehaus.cake.internal.service.AbstractInternalContainer;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.util.CollectionUtils;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagedGroup;
import org.codehaus.cake.service.Stoppable;

public abstract class AbstractInternalCache<K, V> extends AbstractInternalContainer implements
        InternalCache<K, V>, Manageable {

    final InternalCacheListener<K, V> listener;

    final MemoryStore<K, V> memoryCache;

    private CacheServices<K, V> services;

    private final CollectionViews<K, V> views;

    AbstractInternalCache(Composer composer) {
        super(composer);
        composer.registerInstance(InternalCache.class, this);
        memoryCache = composer.get(MemoryStore.class);
        listener = composer.get(InternalCacheListener.class);
        views = composer.get(CollectionViews.class);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public final boolean containsKey(Object key) {
        return peek((K) key) != null;
    }

    @Override
    public final Set<Entry<K, V>> entrySet() {
        return views.entrySet();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public final V get(Object key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        CacheEntry<K, V> e = getEntry((K) key);
        return e == null ? null : e.getValue();
    }

    public final boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public final Set<K> keySet() {
        return views.keySet();
    }

    /** {@inheritDoc} */
    public final V peek(K key) {
        CacheEntry<K, V> e = peekEntry(key);
        return e == null ? null : e.getValue();
    }

    public void prestart() {
        size();
    }

    public final V put(K key, V value) {
        CacheEntry<K, V> prev = put(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
        return prev == null ? null : prev.getValue();
    }

    /** {@inheritDoc} */
    public void manage(ManagedGroup parent) {
        ManagedGroup g = parent.addChild(CacheMXBean.MANAGED_SERVICE_NAME,
                "General Cache attributes and operations");
        g.add(new DefaultCacheMXBean(this));
    }

    public final void putAll(Map<? extends K, ? extends V> t) {
        HashMap map = new HashMap();
        for (Map.Entry me : t.entrySet()) {
            map.put(me.getKey(), new CollectionUtils.SimpleImmutableEntry(me.getValue(),
                    Attributes.EMPTY_ATTRIBUTE_MAP));
        }
        putAllWithAttributes(map);
    }

    @Stoppable
    public final void stop() {
        memoryCache.removeAll();
    }

    @Override
    public final Collection<V> values() {
        return views.values();
    }

    public final CacheServices<K, V> with() {
        if (services == null) {
            services = new CacheServices<K, V>(this);
        }
        return services;
    }

    static void checkKeyValue(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (value == null) {
            throw new NullPointerException("value is null");
        }
    }

    static void checkPut(Object key, Object value, Object attributes) {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (value == null) {
            throw new NullPointerException("value is null");
        } else if (attributes == null) {
            throw new NullPointerException("attributes is null");
        }
    }

    static void checkReplace(Object key, Object oldValue, Object newValue) {
        if (key == null) {
            throw new NullPointerException("key is null");
        } else if (oldValue == null) {
            throw new NullPointerException("oldValue is null");
        } else if (newValue == null) {
            throw new NullPointerException("newValue is null");
        }
    }

    static Composer newComposer(CacheConfiguration<?, ?> configuration) {
        Composer composer = new Composer(Cache.class, configuration);
        composer.registerInstance(configuration);
        composer.registerImplementation(DefaultCacheExceptionService.class);
        composer.registerImplementation(HashMapMemoryStore.class);
        composer.registerImplementation(DefaultCacheListener.class);
        return composer;
    }
}
