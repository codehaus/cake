/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.internal.cache.service.loading;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.codehaus.cake.cache.loading.CacheLoader;
import org.codehaus.cake.cache.loading.CacheLoadingConfiguration;
import org.codehaus.cake.internal.cache.service.exceptionhandling.InternalCacheExceptionService;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;
import org.codehaus.cake.util.ops.Ops.Op;

public abstract class AbstractCacheLoader<K, V> implements InternalCacheLoadingService<K, V> {

    private InternalCacheExceptionService<K, V> exceptionHandler;

    public AbstractCacheLoader(InternalCacheExceptionService<K, V> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public V doLoad(BlockingCacheLoader<K, V> loader, K key, MutableAttributeMap attributes) {
        try {
            return loader.load(key, attributes);
        } catch (Throwable e) {
            return exceptionHandler.loadFailed(e, key, attributes);
        }
    }

     static <K, V> BlockingCacheLoader<K, V> getSimpleLoader(CacheLoadingConfiguration<K, V> conf) {
        final Object loader = conf.getLoader();
        Class<?> loaderClass = loader.getClass();
        for (final Method m : loaderClass.getDeclaredMethods()) {
            CacheLoader cl = m.getAnnotation(CacheLoader.class);
            if (cl != null) {
                return new BlockingCacheLoader<K, V>() {
                    public V load(K key, MutableAttributeMap attributes) throws Exception {
                        try {
                        if (m.getParameterTypes().length == 2) {
                            return (V) m.invoke(loader, key, attributes);
                        } else {
                            return (V) m.invoke(loader, key);
                        }} catch (InvocationTargetException e) {
                            if (e.getCause() instanceof Exception) {
                                throw (Exception) e.getCause();
                            } else if (e.getCause() instanceof Error) {
                                throw (Error) e.getCause();
                            }
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        }
        throw new IllegalArgumentException("Unknown loader type [type =" + loader.getClass() + "]");
    }

    static class OpToSimpleLoader<K, V> implements BlockingCacheLoader<K, V> {
        private final Op<K, V> op;

        public OpToSimpleLoader(Op<K, V> op) {
            this.op = op;
        }

        public V load(K key, MutableAttributeMap attributes) throws Exception {
            return op.op(key);
        }
    }

    public void loadAsync(Map<? extends K, AttributeMap> map) {
        for (Map.Entry<? extends K, AttributeMap> e : map.entrySet()) {
            loadAsync(e.getKey(), e.getValue());
        }
    }
}
