package org.codehaus.cake.internal.cache.service.attribute;

import org.codehaus.cake.attribute.AttributeMap;

public interface InternalAttributeService<K, V> {

    AttributeMap create(K key, V value, AttributeMap params);

    AttributeMap update(K key, V value, AttributeMap params, AttributeMap previous);

    AttributeMap remove(AttributeMap params);
}
