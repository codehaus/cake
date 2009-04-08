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

import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN1;
import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN2;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class Remove extends AbstractCacheTCKTest {

    @Test
    public void remove() {
        c = newCache(0);
        assertNull(c.remove(MNAN2.getKey()));
        assertNull(c.remove(MNAN1.getKey()));

        c = newCache(5);
        assertEquals(M1.getValue(), c.remove(M1.getKey()));
        assertEquals(4, c.size());
        assertFalse(c.containsKey(M1.getKey()));

        c = newCache(1);
        assertEquals(M1.getValue(), c.remove(M1.getKey()));
        assertTrue(c.isEmpty());
    }

    @Test
    public void removeMany() {
        int step = 1000;
        for (int i = 0; i < step; i++) {
            c.put(i, "" + i);
        }
        
        for (int i = 0; i < step; i++) {
            assertSize(step - i);
            assertEquals("" + i, c.remove(i));
        }
    }

    @Test
    public void removeManyStep100() {
        int step = 1000;
        for (int i = 0; i < step; i++) {
            c.put(i * 100, "" + i);
        }
        for (int i = 0; i < step; i++) {
            assertSize(step - i);
            assertEquals("" + i, c.remove(i * 100));
        }
    }

    @Test
    public void removeManyReverse() {
        for (int i = 0; i < 1000; i++) {
            c.put(i, "" + i);
        }
        for (int i = 999; i >= 0; i--) {
            assertSize(i + 1);
            assertEquals("" + i, c.remove(i));
        }
    }

    @Test
    public void remove2() {
        c = newCache(2);
        assertTrue(c.remove(M2.getKey(), M2.getValue()));
        assertEquals(1, c.size());
        assertFalse(c.remove(M1.getKey(), M2.getValue()));
        assertEquals(1, c.size());
    }

    /**
     * remove(null,Value) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void remove2KeyNPE() {
        c = newCache(1);
        c.remove(null, M1.getValue());
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void remove2LazyStart() {
        init();
        assertFalse(c.isStarted());
        c.remove(M1.getKey(), M2.getValue());
        checkLazystart();
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test
    public void remove2ShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        assertFalse(c.remove(M1.getKey(), M2.getValue()));
    }

    /**
     * remove(Key,null) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void remove2ValueNPE() {
        init();
        c.remove(M1.getKey(), null);
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeLazyStart() {
        init();
        assertFalse(c.isStarted());
        c.remove(M1.getKey());
        checkLazystart();
    }

    @Test(expected = NullPointerException.class)
    public void removeNPE() {
        newCache(0).remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNPE1() {
        newCache(5).remove(null);
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test
    public void removeShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        assertNull(c.remove(MNAN1.getKey()));
    }
}
