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
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class PeekEntry extends AbstractCacheTCKTest {

    @Test
    public void setValue() {
        init(5);
        CacheEntry<Integer, String> ce = peekEntry(M1);
        try {
            ce.setValue("foo");
            assertEquals("foo", get(M1));
        } catch (UnsupportedOperationException ok) {}
    }

    @Test
    public void peekEntry() {
       init(5);
        assertNull(peekEntry(M6));
        assertEquals(M1.getValue(), peekEntry(M1).getValue());
        assertEquals(M1.getKey(), peekEntry(M1).getKey());
        assertEquals(M5.getValue(), peekEntry(M5).getValue());
        assertEquals(M5.getKey(), peekEntry(M5).getKey());
    }

    /**
     * {@link Cache#peek} lazy starts the cache.
     */
    @Test
    public void peekEntryLazyStart() {
        init();
        assertNotStarted();
        peekEntry(M1);
        checkLazystart();
    }

    @Test(expected = NullPointerException.class)
    public void peekEntryNPE() {
       init(5);
        peekEntry(null);
    }

    /**
     * {@link Cache#containsValue()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void peekEntryShutdown() throws InterruptedException {
       init(5);
        assertStarted();
        shutdown();

        // should not fail, but result is undefined until terminated
        peekEntry(M1);

        shutdownAndAwaitTermination();

        Object peekEntry = peekEntry(M1);
        assertNull(peekEntry);// cache should be empty
    }
}
