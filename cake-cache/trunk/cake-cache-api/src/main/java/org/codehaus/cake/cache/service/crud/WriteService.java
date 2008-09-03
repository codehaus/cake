package org.codehaus.cake.cache.service.crud;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

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

    /**
     * If the specified key is not already associated with a value, associate it with the given value. This is
     * equivalent to
     * 
     * <pre>
     * if (!cache.containsKey(key))
     *     Object prev = cache.put(key, value);
     *     return op.op(prev);
     * else
     *     return cache.get(key);
     * </pre>
     * 
     * except that the action is performed atomically and the type of value returned is determinded by the type of
     * WriteService.
     * 
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     * @return the previous value associated with the specified key, or <tt>null</tt> if there was no mapping for the
     *         key.
     * @throws ClassCastException
     *             if the class of the specified key or value prevents it from being stored in this map
     * @throws IllegalArgumentException
     *             if some property of the specified key or value prevents it from being stored in this cache
     * @throws NullPointerException
     *             if the specified key or value is null
     * @throws SecurityException
     *             If a cache security manager exists and its <tt>checkPermission</tt> method doesn't allow creation
     *             of entries.
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this map
     * @see ConcurrentMap#putIfAbsent(Object, Object)
     */
    R putIfAbsent(K key, V value);

    R putIfAbsent(K key, V value, AttributeMap attributes);

    R remove(K key);

    R remove(K key, V value);

    void putAll(Map<? extends K, ? extends V> t);
}
