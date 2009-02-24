/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.omg.CORBA.Object;

/**
 * This service can be used to create, update, or delete entries from the cache.
 * <p>
 * The behavior of this operation is unspecified if the specified collection is modified while the operation is in
 * progress.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 * @param <R>
 *            the type returned from methods, this is ordinarily determined by the {@link #WRITE_TRANSFORMER} used
 */
public interface CacheBatchWriter<K, V, R> {

    /**
     * This attribute can be used to indicate that a given operation should be performed atomically. This can be
     * significantly slower then performing each insert/update/remove separetaly FIX.
     * 
     */
    BooleanAttribute ATOMIC_OPERATION = null;// get rid of any/all

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

    R putAll(Map<? extends K, ? extends V> t);

    R putAll(Map<? extends K, ? extends V> t, AttributeMap attributes);

    R removeAll();

    /**
     * Attempts to remove all of the mappings for the specified collection of keys. The effect of this call is
     * equivalent to that of calling {@link org.codehaus.cake.cache.Cache#remove(Object)} on the cache once for each key
     * in the specified collection. However, in some cases it can be much faster to remove several cache items at once,
     * for example, if some of the values must also be removed on a remote host.
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

}
