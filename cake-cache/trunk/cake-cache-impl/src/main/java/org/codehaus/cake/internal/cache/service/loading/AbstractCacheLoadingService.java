package org.codehaus.cake.internal.cache.service.loading;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.internal.util.CollectionUtils;

abstract class AbstractCacheLoadingService<K, V> implements CacheLoadingService<K, V> {

    abstract void doLoad(K key, AttributeMap attributes);

    abstract void doLoadAll(AttributeMap attributes);

    abstract void doLoadAll(Iterable<? extends K> keys, AttributeMap attributes);

    abstract void doLoadAll(Map<? extends K, ? extends AttributeMap> mapsWithAttributes);

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
        CollectionUtils.checkCollectionForNulls(keys);
        doLoadAll(keys, attributes);
    }

    public void loadAll(Map<? extends K, ? extends AttributeMap> mapsWithAttributes) {
        if (mapsWithAttributes == null) {
            throw new NullPointerException("mapsWithAttributes is null");
        }
        CollectionUtils.checkMapForNulls(mapsWithAttributes);
    }
}
