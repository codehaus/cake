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
package org.codehaus.cake.cache.test.tck.asmap;

import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN1;
import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN2;

import org.codehaus.cake.cache.Cache;
import org.junit.Test;

public class Remove extends AbstractAsMapTCKTest {

    @Test
    public void remove() {
        init(0);
        assertNull(remove(MNAN2));
        assertNull(remove(MNAN1));

        init(5);
        assertEquals(M1.getValue(), remove(M1));
        assertSize(4);
        assertNotContainsKey(M1);

        init(1);
        assertEquals(M1.getValue(), remove(M1));
        assertIsEmpty();
    }

    @Test
    public void removeMany() {
        int step = 1000;
        for (int i = 0; i < step; i++) {
            put(i, "" + i);
        }

        for (int i = 0; i < step; i++) {
            assertSize(step - i);
            assertEquals("" + i, remove(i));
        }
    }

    @Test
    public void removeManyStep100() {
        int step = 1000;
        for (int i = 0; i < step; i++) {
            put(i * 100, "" + i);
        }
        for (int i = 0; i < step; i++) {
            assertSize(step - i);
            assertEquals("" + i, remove(i * 100));
        }
    }

    @Test
    public void removeManyReverse() {
        for (int i = 0; i < 1000; i++) {
            put(i, "" + i);
        }
        for (int i = 999; i >= 0; i--) {
            assertSize(i + 1);
            assertEquals("" + i, remove(i));
        }
    }

    @Test
    public void remove2() {
        init(2);
        assertTrue(remove(M2.getKey(), M2.getValue()));
        assertSize(1);
        assertFalse(remove(M1.getKey(), M2.getValue()));
        assertSize(1);
    }

    /**
     * remove(null,Value) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void remove2KeyNPE() {
        init(1);
        remove(null, M1.getValue());
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void remove2LazyStart() {
        init();
        assertNotStarted();
        remove(M1.getKey(), M2.getValue());
        assertStarted();
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test
    public void remove2ShutdownISE() {
        init(5);
        assertStarted();
        shutdown();

        assertFalse(remove(M1.getKey(), M2.getValue())); // should fail
    }

    /**
     * remove(Key,null) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void remove2ValueNPE() {
        init();
        remove(M1.getKey(), null);
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeLazyStart() {
        init();
        assertNotStarted();
        remove(M1.getKey());
        assertStarted();
    }

    @Test(expected = NullPointerException.class)
    public void removeNPE() {
        init(0);
        asMap().remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNPE1() {
        init(5);
        asMap().remove(null);
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test
    public void removeShutdownISE() {
        init(5);
        assertStarted();
        shutdown();

        assertNull(remove(MNAN1)); // should fail
    }
}
