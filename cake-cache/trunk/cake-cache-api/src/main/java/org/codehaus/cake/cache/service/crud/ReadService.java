package org.codehaus.cake.cache.service.crud;

import java.util.Map;

public interface ReadService<K, R> {
 //   BooleanAttribute PEEK = new BooleanAttribute() {};

    // withRead()
    // withReadEntry();

    R get(K key);

    Map<K, R> getAll(Iterable<? extends K> keys);
    // R get(K key, AttributeMap attributes);
}
