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
package org.codehaus.cake.cache.test.util;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;

public class BlockingCacheLoader<K, V> implements SimpleCacheLoader<K, V> {

    public V load(K key, AttributeMap attributes) throws Exception {
        // delay
        // data generator
        return null;
    }

    // /** {@inheritDoc} */
    // public final void loadAll(
    // Collection<? extends LoaderCallback<? extends K, ? super V>> loadCallbacks) {
    // for (LoaderCallback<? extends K, ? super V> req : loadCallbacks) {
    // try {
    // V result = load(req.getKey(), req.getAttributes());
    // req.completed(result);
    // } catch (Throwable t) {
    // req.failed(t);
    // }
    // }
    // }

}
