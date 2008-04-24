/* Copyright 2004 - 2007 Kasper Nielsen <kasper@codehaus.org> Licensed under
 * the Apache 2.0 License, see http://coconut.codehaus.org/license.
 */
package org.codehaus.cake.cache.test.util;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;

public class BlockingCacheLoader<K, V> implements SimpleCacheLoader<K, V> {

    public V load(K key, AttributeMap attributes) throws Exception {
        //delay
        //data generator
        return null;
    }

//    /** {@inheritDoc} */
//    public final void loadAll(
//            Collection<? extends LoaderCallback<? extends K, ? super V>> loadCallbacks) {
//        for (LoaderCallback<? extends K, ? super V> req : loadCallbacks) {
//            try {
//                V result = load(req.getKey(), req.getAttributes());
//                req.completed(result);
//            } catch (Throwable t) {
//                req.failed(t);
//            }
//        }
//    }

}
