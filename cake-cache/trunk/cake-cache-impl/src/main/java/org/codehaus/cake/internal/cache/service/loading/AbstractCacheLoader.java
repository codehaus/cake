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
package org.codehaus.cake.internal.cache.service.loading;

import java.util.Map;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.service.loading.BlockingCacheLoader;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;
import org.codehaus.cake.ops.Ops.Op;

public abstract class AbstractCacheLoader<K, V> implements InternalCacheLoader<K, V> {

    private InternalCacheExceptionService<K, V> exceptionHandler;

    public AbstractCacheLoader(InternalCacheExceptionService<K, V> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public V doLoad(BlockingCacheLoader<K, V> loader, K key, AttributeMap attributes) {
        V v = null;
        try {
            v = loader.load(key, attributes);
        } catch (Throwable e) {
            v = exceptionHandler.loadFailed(e, key, attributes);
        }
        return v;
    }

    static <K, V> BlockingCacheLoader<K, V> getSimpleLoader(CacheLoadingConfiguration<K, V> conf) {
        Object loader = conf.getLoader();
        if (loader instanceof Op) {
            return new OpToSimpleLoader((Op) loader);
        } else if (loader instanceof BlockingCacheLoader) {
            return (BlockingCacheLoader) loader;
        } else {
            throw new IllegalArgumentException("Unknown loader type [type =" + loader.getClass() + "]");
        }
    }

    static class OpToSimpleLoader<K, V> implements BlockingCacheLoader<K, V> {
        private final Op<K, V> op;

        public OpToSimpleLoader(Op<K, V> op) {
            this.op = op;
        }

        public V load(K key, AttributeMap attributes) throws Exception {
            return op.op(key);
        }
    }

    public void loadAsync(Map<? extends K, AttributeMap> map) {
        for (Map.Entry<? extends K, AttributeMap> e : map.entrySet()) {
            loadAsync(e.getKey(), e.getValue());
        }
    }
}
