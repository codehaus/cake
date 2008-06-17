package org.codehaus.cake.internal.cache.service.attribute;

import org.codehaus.cake.attribute.AttributeMap;

public interface CacheAttributeMapFactory<K, V> {
    AttributeMap create(K key, V value, AttributeMap params, AttributeMap previous);

    void access(AttributeMap map);
}
