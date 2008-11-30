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

import java.util.ArrayList;
import java.util.Random;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests put operations for a cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class Put extends AbstractCacheTCKTest {

    @Test
    public void put() {
        init();
        c.put(1, "B");
        assertEquals(1, c.size());
        c.put(1, "C");
        assertEquals(1, c.size());
        assertEquals("C", peek(M1));
    }

    @Test
    public void putMany() {
        final int count = 500;
        Random r = new Random(1123123);
        for (int i = 0; i < count; i++) {
            int key = r.nextInt(250);
            c.put(key, "" + key);
        }
        assertEquals(c.size(), new ArrayList(c.entrySet()).size());
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void putLazyStart() {
        init();
        assertFalse(c.isStarted());
        c.put(1, "B");
        checkLazystart();
    }

    /**
     * {@link Cache#containsKey()} should fail when cache is shutdown.
     */
    @Test(expected = IllegalStateException.class)
    public void putShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        c.put(1, "B");
    }

    @Test(expected = NullPointerException.class)
    public void putKeyNPE() {
        init();
        c.put(null, "A");
    }

    @Test(expected = NullPointerException.class)
    public void putValueNPE() {
        init();
        c.put(1, null);
    }
}
