package org.codehaus.cake.cache.loading;

import org.codehaus.cake.attribute.AttributeMap;

public interface CacheLoadingService<K, V> {
    WithLoad<V> withKey(K key);

    WithLoad<V> withKey(K key, AttributeMap attributes);

    WithLoad<V> withKeys(Iterable<? extends K> keys);

    WithLoad<V> withKeys(Iterable<? extends K> keys, AttributeMap attributes);

    WithLoad<V> withAll();

    WithLoad<V> withAll(AttributeMap attributes);

    interface WithLoad<V> {
        void forceLoad();

        void load();
    }
}
