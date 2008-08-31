package org.codehaus.cake.cache.service.crud;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;

public interface ReadService<K, R> {
    // BooleanAttribute PEEK = new BooleanAttribute() {};

    R get(K key);

    R get(K key, AttributeMap attributes);

    boolean contains(R r);

    Map<K, R> getAll(Iterable<? extends K> keys);
}
