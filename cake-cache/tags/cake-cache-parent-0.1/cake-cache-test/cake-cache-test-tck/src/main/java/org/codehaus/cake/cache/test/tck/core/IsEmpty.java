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
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class IsEmpty extends AbstractCacheTCKTest {

    /**
     * isEmpty is true of empty map and false for non-empty.
     */
    @Test
    public void isEmptyCache() {
        assertTrue(newCache(0).isEmpty());
        assertFalse(newCache(5).isEmpty());
    }

    /**
     * {@link Cache#isEmpty()} lazy starts the cache.
     */
    @Test
    public void isEmptyLazyStart() {
        init().assertNotStarted();
        c.isEmpty();
        assertStarted();
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void isEmptyShutdown() throws InterruptedException {
        init(5).assertStarted();
        shutdown().isEmpty();
        assertTrue(awaitTermination().isEmpty());
    }
}
