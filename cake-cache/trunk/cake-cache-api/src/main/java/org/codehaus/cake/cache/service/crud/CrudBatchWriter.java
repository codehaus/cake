package org.codehaus.cake.cache.service.crud;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.BooleanAttribute;

/**
 * This service can be used to create, update, or delete entries from the cache.
 * <p>
 * The behavior of this operation is unspecified if the specified collection is modified while the operation is in
 * progress.
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
public interface CrudBatchWriter<K, V, R> {

    /**
     * This attribute can be used to indicate that entries should be removed from the specified datasource on
     * insertion/removal For example, when calling {@link #putAll(Map)} all items that are inserted into will be removed
     * from the specifying map.
     * 
     * TODO problem what about maps that change, I think the main reason for having this method was to enable moving
     * caches from one cache to another.
     * 
     * In general this method
     * 
     */
    BooleanAttribute REMOVE_FROM_SOURCE_ON_INSERT = null;
    // MOVE_ENTRIES

    /**
     * This attribute can be used to indicate that a given operation should be performed atomically. This can be
     * significantly slower then performing each insert/update/remove separetaly FIX.
     * 
     */
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
    R removeAll(Iterable<? extends K> keys);

    R removeAll();

}
