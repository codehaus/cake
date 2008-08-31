package org.codehaus.cake.cache.service.crud;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.ops.Ops.Op;

public interface WriteService<K, V, R> {

    ObjectAttribute<Op<?, ?>> OP = (ObjectAttribute) new ObjectAttribute<Op>(Op.class) {};

    R put(K key, V value);

    R put(K key, V value, AttributeMap attributes);

    R replace(K key, V value);

    R replace(K key, V oldValue, V newValevalue);

    R replace(K key, V value, AttributeMap attributes);

    R putIfAbsent(K key, V value);

    R putIfAbsent(K key, V value, AttributeMap attributes);

    R remove(K key);

    R remove(K key, V value);

    void putAll(Map<? extends K, ? extends V> t);
}
