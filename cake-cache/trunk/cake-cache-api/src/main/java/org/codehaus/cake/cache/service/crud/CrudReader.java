package org.codehaus.cake.cache.service.crud;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.service.ContainerAlreadyShutdownException;

public interface CrudReader<K, R> {

    /**
     * An attribute used to indicate that a ReadService should only look in the in-memory store. It will not try to
     * fetch missing items, it will only return a value if it actually exists in the cache. Furthermore, it will not
     * effect any statistics gathered by the cache.
     */
    BooleanAttribute PEEK = new BooleanAttribute() {};

    BooleanAttribute READ_THROUGH = new BooleanAttribute() {};
    // Get throws IllegalStateException if a value could not be found
    // getAll() if not _all_ values could be found
    
    /**
     * This attribute can be used for asserting that a mapping exists for the specified key(s) when calling any of the
     * <tt>get</tt> methods. If this attribute is set calling, for example, {@link #get(Object)} with a key for which
     * no mapping exists in the cache. The method will throw an {@link IllegalStateException}.
     * 
     * The following example shows how this attribute can be set.
     * 
     * <pre>
     * Cache&lt;Integer, String&gt; c = somecache;
     * AttributeMap attributes = CrudReader.ASSERT_GET.singleton(true);
     * c.crud().reader(attributes).get(4);
     * </pre>
     */
    BooleanAttribute ASSERT_GET = new BooleanAttribute() {};

    public enum ReadType {
        PEEK, NORMAL, READ_THROUGH, READ_THROUGH_NO_CACHE
    }

    ObjectAttribute<Op<?, ?>> READ_TRANSFORMER = (ObjectAttribute) new ObjectAttribute<Op>(Op.class) {};

    /**
     * Works as {@link Map#get(Object)} with the following modifications.
     * <p>
     * If the cache has a configured CacheLoader. And no mapping exists for the specified key or the specific mapping
     * has expired. The cache will transparently attempt to load a value for the specified key through the cache loader.
     * <p>
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
     * @return the associated value
     */
    R get(K key);

    /**
     * As
     * 
     * @param key
     * @param attributes
     * @return
     */
    R get(K key, AttributeMap attributes);

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
