package org.codehaus.cake.internal.cache.service.attribute;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.cache.attribute.InternalCacheAttributeService;

public interface InternalAttributeService<K, V> extends InternalCacheAttributeService {

    AttributeMap create(K key, V value, AttributeMap params);

    AttributeMap update(K key, V value, AttributeMap params, AttributeMap previous);

    AttributeMap remove(AttributeMap params);
    void access(AttributeMap map);
}
