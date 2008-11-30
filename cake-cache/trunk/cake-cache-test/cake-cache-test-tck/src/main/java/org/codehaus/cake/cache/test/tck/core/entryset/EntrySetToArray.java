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
package org.codehaus.cake.cache.test.tck.core.entryset;

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_SET;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class EntrySetToArray extends AbstractCacheTCKTest {

    @SuppressWarnings("unchecked")
    @Test
    public void toArray() {
        init();
        assertEquals(new HashSet(), new HashSet(Arrays.asList(c.entrySet().toArray())));

        assertEquals(new HashSet(), new HashSet(Arrays.asList(c.entrySet().toArray(new Map.Entry[0]))));
        c = newCache(5);
        c.entrySet().toArray();
        assertEquals(new HashSet(M1_TO_M5_SET), new HashSet(Arrays.asList(c.entrySet().toArray())));

        assertEquals(new HashSet(M1_TO_M5_SET), new HashSet(Arrays.asList(c.entrySet().toArray(new Map.Entry[0]))));
        assertEquals(new HashSet(M1_TO_M5_SET), new HashSet(Arrays.asList(c.entrySet().toArray(new Map.Entry[5]))));
    }

    /**
     * {@link Cache#isEmpty()} lazy starts the cache.
     */
    @Test
    public void toArrayLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.entrySet().toArray();
        checkLazystart();
    }

    /**
     * {@link Cache#isEmpty()} lazy starts the cache.
     */
    @Test
    public void toArrayLazyStart1() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.entrySet().toArray(new Map.Entry[5]);
        checkLazystart();
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void toArrayShutdown() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        assertEquals(0, c.entrySet().toArray().length);
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     */
    @Test
    public void toArrayShutdown1() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        assertEquals(5, c.entrySet().toArray(new Map.Entry[5]).length);
    }
}
