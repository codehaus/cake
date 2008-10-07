package org.codehaus.cake.cache.service.crud;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.BooleanAttribute;

public interface CrudBatchWriter<K, V, R> {
    BooleanAttribute REMOVE_FROM_SOURCE_ON_INSERT = null;

    BooleanAttribute ATOMIC_OPERATION = null;// get rid of any/all

    R putAll(Map<? extends K, ? extends V> t);

    R putAll(Map<? extends K, ? extends V> t, AttributeMap attributes);

    /**
     * Attempts to remove all of the mappings for the specified collection of keys. The effect of this call is
     * equivalent to that of calling {@link org.codehaus.cake.cache.Cache#removeByIndex(Object)} on the cache once for
     * each key in the specified collection. However, in some cases it can be much faster to remove several cache items
     * at once, for example, if some of the values must also be removed on a remote host.
     * 
     * @param keys
     *            a collection of keys whose associated mappings are to be removed.
     * @throws UnsupportedOperationException
     *             if the <tt>remove</tt> operation is not supported by this cache.
     * @throws IllegalStateException
     *             if the cache has been shutdown
     * @throws NullPointerException
     *             if the specified collection or any of its containing keys are <tt>null</tt>.
     */
    void removeAll(Iterable<? extends K> keys);
}
