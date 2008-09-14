package org.codehaus.cake.cache.service.crud;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.service.ContainerAlreadyShutdownException;

public interface ReadService<K, R> {

    /**
     * An attribute used to indicate that a ReadService should only look in the in-memory store. It will not try to
     * fetch missing items, it will only return a value if it actually exists in the cache. Furthermore, it will not
     * effect any statistics gathered by the cache.
     * 
     * 
     */
    BooleanAttribute PEEK = new BooleanAttribute() {};

    BooleanAttribute READ_THROUGH = new BooleanAttribute() {};
    //Get throws IllegalStateException if a value could not be found
    //getAll() if not _all_ values could be found
    BooleanAttribute ASSERT_GET = new BooleanAttribute() {}; //FAIL_IF_NOT_AVAILABLE

    public enum ReadType {
        PEEK, NORMAL, READ_THROUGH, READ_THROUGH_NO_CACHE
    }

    ObjectAttribute<Op<?, ?>> READ_TRANSFORMER = (ObjectAttribute) new ObjectAttribute<Op>(Op.class) {};

    /**
     * 
     * @param key
     *            key whose associated value is to be returned.
     * @return the value to which this cache maps the specified key, or <tt>null</tt> if the cache contains no mapping
     *         for this key.
     * @throws ClassCastException
     *             if the key is of an inappropriate type for this cache (optional).
     * @throws NullPointerException
     *             if the specified key is <tt>null</tt>
     * @throws ContainerAlreadyShutdownException
     *             if the cache has been shutdown
     * @param key
     * @return
     */
    R get(K key);

    /**
     * @param key
     * @param attributes
     * @return
     */
    R get(K key, AttributeMap attributes);

    Map<K,R> get(K arg, K... args);
    /**
     * Attempts to retrieve all of the mappings for the specified collection of keys. The effect of this call is
     * equivalent to that of calling {@link #get(Object)} on this cache once for each key in the specified collection.
     * However, in some cases it can be much faster to load several cache items at once, for example, if the cache must
     * fetch the values from a remote host.
     * <p>
     * If a value is not contained in the cache and the value cannot be loaded by any of the configured cache backends.
     * The returned map will contain a mapping from the key to <tt>null</tt>.
     * <p>
     * The behavior of this operation is unspecified if the specified collection is modified while the operation is in
     * progress.
     * 
     * @param keys
     *            a collection of keys whose associated values are to be returned.
     * @return a map with mappings from each key to the corresponding value, or to <tt>null</tt> if no mapping for
     *         this key exists.
     * @throws ClassCastException
     *             if any of the keys in the specified collection are of an inappropriate type for this cache
     *             (optional).
     * @throws NullPointerException
     *             if the specified collection of keys is <tt>null</tt> or the specified collection contains a
     *             <tt>null</tt>
     * @throws IllegalStateException
     *             if the cache has been shutdown
     */
    Map<K, R> getAll(Iterable<? extends K> keys);
}
