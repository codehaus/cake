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
package org.codehaus.cake.internal.cache;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.service.AfterStart;
import org.codehaus.cake.util.ops.Ops.Procedure;

/**
 * Runs the specified procedure after the cache has started. Must be public to allow for introspection.
 * 
 * @param <K>
 *            the type of keys maintained by this cache
 * @param <V>
 *            the type of mapped values
 */
public class RunAfterCacheStartProcedure<K, V> {
    /** The procedure to run after the cache starts. */
    private final Procedure<Cache<K, V>> afterStartProcedure;

    /**
     * Creates a new RunAfterCacheStartProcedure.
     * 
     * @param afterStartProcedure
     *            the procedure to run after the cache has been started
     * @throws NullPointerException
     *             if the specified procedure is null
     */
    public RunAfterCacheStartProcedure(Procedure<Cache<K, V>> afterStartProcedure) {
        if (afterStartProcedure == null) {
            throw new NullPointerException("afterStartProcedure is null");
        }
        this.afterStartProcedure = afterStartProcedure;
    }

    /**
     * Runs the procedure specified at construction time.
     * 
     * @param cache
     *            the cache that has been started
     */
    @AfterStart
    public void runAfterStart(Cache<K, V> cache) {
        afterStartProcedure.op(cache);
    }
}
