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
package org.codehaus.cake.cache.test.tck.core;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class Peek extends AbstractCacheTCKTest {

    @Test
    public void peek() {
       init(5);
        assertNull(peek(M6));
        assertEquals(M1.getValue(), peek(M1));
        assertEquals(M5.getValue(), peek(M5));
    }

    /**
     * {@link Cache#peek} lazy starts the cache.
     */
    @Test
    public void peekLazyStart() {
        init();
        assertNotStarted();
        peek(M1);
        checkLazystart();
    }

    @Test(expected = NullPointerException.class)
    public void peekNPE() {
       init(5);
        peek((Integer) null);
    }

    /**
     * {@link Cache#containsValue()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void peekShutdown() throws InterruptedException {
       init(5);
        assertStarted();
        shutdown();

        // should not fail, but result is undefined until terminated
        peek(M1);

        shutdownAndAwaitTermination();

        Object peek = peek(M1);
        assertNull(peek);// cache should be empty
    }
}
