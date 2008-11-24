package org.codehaus.cake.cache.service.crud;

import java.util.concurrent.ConcurrentMap;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.Caches;
import org.codehaus.cake.internal.util.Pair;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.ContainerAlreadyShutdownException;

/**
 * This service can be used to create, update, or delete entries from the cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cache.java 520 2007-12-21 17:53:31Z kasper $
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 * @param <R>
 *            the type returned from methods, this is ordinarily determined by the {@link #WRITE_TRANSFORMER} used
 */
public interface CrudWriter<K, V, R> {
    // CacheWriter??

    /**
     * Used for transforming the CacheEntry that is created/updated/delete to something the user needs.
     */
    ObjectAttribute<Op<?, ?>> WRITE_TRANSFORMER = (ObjectAttribute) new ObjectAttribute<Op>(Op.class) {};

    /**
     * Associates the specified value with the specified key in the cache (optional operation). If the cache previously
     * contained a mapping for this key, the old value is replaced by the specified value. (A cache <tt>c</tt> is said
     * to contain a mapping for a key <tt>k</tt> if and only if
     * {@link org.codehaus.cake.cache.Cache#containsKey(Object) c.containsKey(k)} would return <tt>true</tt>.))
     * <p>
     * It is often more efficient to specify a {@link org.codehaus.cake.cache.service.loading.BlockingCacheLoader} that
     * implicitly loads values then to explicitly add them to cache using the various write operations in this
     * interface.
     * 
     * @param key
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * @return context dependent information about what was previous associated with the specified key
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this cache.
     * @throws ClassCastException
     *             if the class of the specified key or value prevents it from being stored in this cache.
     * @throws IllegalArgumentException
     *             if some aspect of this key or value prevents it from being stored in this cache.
     * @throws ContainerAlreadyShutdownException
     *             if the cache has been shutdown
     * @throws NullPointerException
     *             if the specified key or value is <tt>null</tt>.
     */
    R put(K key, V value);

    R put(K key, V value, AttributeMap attributes);

    /**
     * Conditionally associates the specified value with the specified key in the cache.
     * 
     * <pre>
     * CacheEntry e = cache.getEntry(key)
     * if (condition.op(e))
     *     return cache.put(key, value);
     * else
     *     return cache.get(key);
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param condition
     * @param key
     * @param value
     * @return
     */
    R putIf(Predicate<CacheEntry<K, V>> condition, K key, V value);

    /**
     * The cache entry provided to the specified predicate will be null if no entry exists for the specified key.
     * 
     * @parem condition
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     * @param attributes
     * @return
     */
    R putIf(Predicate<CacheEntry<K, V>> condition, K key, V value, AttributeMap attributes);

    /**
     * If the specified key is not already associated with a value, associate it with the given value. This is
     * equivalent to
     * 
     * <pre>
     * if (!cache.containsKey(key))
     *     return cache.put(key, value);
     * else
     *     return cache.get(key);
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * <p>
     * If the foo Attribute is set the returned value will be transformed through it.
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

    /**
     * Analogoues to the {@link #putIfAbsent(Object, Object, AttributeMap)} except that this method also takes a map of
     * attributes.
     * 
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     * @param attributes
     *            a map of attributes
     * @return the previous value associated with the specified key, or <tt>null</tt> if there was no mapping for the
     *         key.
     * @throws ClassCastException
     *             if the class of the specified key, value or contents of the attributemap prevents it from being
     *             stored in this map
     * @throws IllegalArgumentException
     *             if some property of the specified key, value or contents of the attributemap prevents it from being
     *             stored in this cache
     * @throws NullPointerException
     *             if the specified key, value or attributemap is null is null
     * @throws SecurityException
     *             If a cache security manager exists and its <tt>checkPermission</tt> method doesn't allow creation
     *             of entries.
     * @throws UnsupportedOperationException
     *             if the <tt>put</tt> operation is not supported by this map
     * @see ConcurrentMap#putIfAbsent(Object, Object)
     */
    R putIfAbsent(K key, V value, AttributeMap attributes);

    /**
     * If the specified key is not already associated with a value, used the specified op to create a CacheEntry that is
     * should be associated with.
     * 
     * <pre>
     * if (!cache.containsKey(key))
     *     CacheEntry&lt;K,V&gt; tmp = factory.op(key);
     *     return cache.put(key, tmp.getValue(), tmp.getAttributes());
     * else
     *     return cache.get(key);
     * </pre>
     * 
     * <p>
     * Construction time should be short, as usual a lock needs to be held while the specified factory is called.
     * 
     * @param key
     *            key to be inserted
     * 
     * @param factory
     *            a lazily created factory
     * @return
     * @see {@link Caches#newEntry(Object, Object)}
     * @see Caches#newEntry(Object, Object, AttributeMap)
     */
    R putIfAbsentLazy(K key, Op<? extends K, Pair<V, AttributeMap>> factory);

    R putIfLazy(Predicate<CacheEntry<K, V>> condition, K key, Op<? extends K, Pair<V, AttributeMap>> factory);

    /**
     * Removes the mapping for a key from this cache if it is present (optional operation). More formally, if this cache
     * contains a mapping from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping is removed. (The cache can contain at most
     * one such mapping.)
     * <p>
     * Returns the value to which this cache previously associated the key, or <tt>null</tt> if the cache contained no
     * mapping for the key.
     * <p>
     * The cache will not contain a mapping for the specified key once the call returns.
     * 
     * @param key
     *            key whose mapping is to be removed from the cache
     * @return the previous value associated with the specified <tt>key</tt>, or <tt>null</tt> if there was no
     *         mapping for the specified <tt>key</tt>
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this cache
     * @throws ContainerAlreadyShutdownException
     *             if the cache has been shutdown
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified key is null
     */
    R remove(K key);

    /**
     * Conditionally removes the mapping for a key from this cache if it is present (optional operation). This is
     * equivalent to
     * 
     * <pre>
     * if (cache.containsKey(key) &amp;&amp; condition.op(cache.getEntry(key))
     *     return cache.remove(key); //Previous
     * } else
     *     return null; //Previous
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param condition
     *            the predicate that the entry being removed must be accepted by before it can be removed
     * @param key
     *            key whose mapping is to be removed from the cache
     * @return the previous value associated with the specified key, or <tt>null</tt> if there was no mapping for the
     *         key.
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this cache
     * @throws ContainerAlreadyShutdownException
     *             if the cache has been shutdown
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified condition or key is null
     */
    R removeIf(Predicate<CacheEntry<K, V>> condition, K key);

    /**
     * Removes the entry for a key only if currently mapped to a given value. This is equivalent to
     * 
     * <pre>
     * if (cache.containsKey(key) &amp;&amp; cache.get(key).equals(value)) {
     *     return cache.remove(key); //Previous
     * } else
     *     return null; //Previous
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param key
     *            key with which the specified value is associated
     * @param value
     *            value expected to be associated with the specified key
     * @return the previous value associated with the specified key if the value was removed, otherwise <tt>null</tt>
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported
     * @throws ContainerAlreadyShutdownException
     *             if the cache has been shutdown
     * @throws ClassCastException
     *             if the key or value is of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified key or value is null
     */
    R remove(K key, V value);

    /**
     * Replace the entry for the specified key only if it is currently mapped to some value. Acts as
     * 
     * <pre>
     *  if ((cache.containsKey(key)) {
     *     return cache.put(key, value);
     * } else return null;
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param key
     *            key with which the specified value is associated.
     * @param value
     *            value to be associated with the specified key.
     * @return the previous value associated with the specified key if the value was removed, otherwise <tt>null</tt>
     * @throws UnsupportedOperationException
     *             if the <tt>update</tt> operation is not supported by this cache
     * @throws ContainerAlreadyShutdownException
     *             if the cache has been shutdown
     * @throws ClassCastException
     *             if the key or value is of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified key or value is <tt>null</tt>.
     */
    R replace(K key, V value);

    /**
     * Replace the entry for the specified key only if it is currently mapped to some value. Acts as
     * 
     * <pre>
     *  if ((cache.containsKey(key)) {
     *     return cache.put(key, value, attributes);
     * } else return null;
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param key
     *            key with which the specified value is associated.
     * @param value
     *            value to be associated with the specified key.
     * @param attributes
     *            a map of attributes
     * @return the previous value associated with the specified key if the value was removed, otherwise <tt>null</tt>
     * @throws UnsupportedOperationException
     *             if the <tt>update</tt> operation is not supported by this cache
     * @throws ContainerAlreadyShutdownException
     *             if the cache has been shutdown
     * @throws ClassCastException
     *             if the key or value is of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified key, value or attribute map is <tt>null</tt>
     */
    R replace(K key, V value, AttributeMap attributes);

    /**
     * Replace entry for key only if currently mapped to given value. Acts as
     * 
     * <pre>
     *  
     *  if ((cache.containsKey(key) &amp;&amp; cache.get(key).equals(oldValue)) {
     *     return cache.put(key, newValue);
     * } else return null;
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param key
     *            key with which the specified value is associated.
     * @param oldValue
     *            value expected to be associated with the specified key.
     * @param newValue
     *            value to be associated with the specified key.
     * @return the previous value associated with the specified key if the value was removed, otherwise <tt>null</tt>
     * @throws UnsupportedOperationException
     *             if the <tt>update</tt> operation is not supported by this cache
     * @throws ContainerAlreadyShutdownException
     *             if the cache has been shutdown
     * @throws ClassCastException
     *             if the key or any of the values are of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified key or any of the values are <tt>null</tt>
     */
    R replace(K key, V oldValue, V newValue);

    /**
     * Replace entry for key only if currently mapped to given value. Acts as
     * 
     * <pre>
     *  
     *  if ((cache.containsKey(key) &amp;&amp; cache.get(key).equals(oldValue)) {
     *     return cache.put(key, newValue, attributes);
     * } else return null;
     * </pre>
     * 
     * except that the action is performed atomically.
     * 
     * @param key
     *            key with which the specified value is associated.
     * @param oldValue
     *            value expected to be associated with the specified key.
     * @param newValue
     *            value to be associated with the specified key.
     * @param attributes
     *            a map of attributes
     * @return the previous value associated with the specified key if the value was removed, otherwise <tt>null</tt>
     * @throws UnsupportedOperationException
     *             if the <tt>update</tt> operation is not supported by this cache.
     * @throws ContainerAlreadyShutdownException
     *             if the cache has been shutdown
     * @throws ClassCastException
     *             if the key or any of the values are of an inappropriate type for this cache (optional)
     * @throws NullPointerException
     *             if the specified key, any of the values or the  attribute map is <tt>null</tt>
     */
    R replace(K key, V oldValue, V newValue, AttributeMap attributes);
}
