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
package org.codehaus.cake.cache.test.tck.asmap.entryset;

import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN1;
import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN2;

import java.util.Arrays;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * Tests the modifying functions of a keySet().
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class EntrySetRemove extends AbstractAsMapTCKTest {

    @Test(expected = NullPointerException.class)
    public void removeNPE() {
        newCache(0);asMap().entrySet().remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNPE2() {
        newCache(5);asMap().entrySet().remove(null);
    }

    @Test
    public void remove() {
        init();
        assertFalse(asMap().entrySet().remove(1));
        assertFalse(asMap().entrySet().remove(MNAN1));
       init(5);
        assertTrue(asMap().entrySet().remove(M1));
        assertSize(4);
        assertFalse(asMap().entrySet().contains(M1));

       init(1);
        assertTrue(asMap().entrySet().remove(M1));
        assertIsEmpty();
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeLazyStart() {
        init();
        assertNotStarted();
        asMap().entrySet().remove(MNAN1);
        checkLazystart();
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test
    public void removeShutdownISE() {
       init(5);
        assertStarted();
        shutdown();

        // should fail
        asMap().entrySet().remove(MNAN1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void removeAll() {
       init(5);
        assertFalse(asMap().entrySet().removeAll(Arrays.asList(MNAN1, MNAN2)));
        assertTrue(asMap().entrySet().removeAll(Arrays.asList(MNAN1, M2, MNAN2)));
        assertSize(4);
        assertFalse(asMap().entrySet().contains(M2));
        assertTrue(asMap().entrySet().removeAll(Arrays.asList(M1, M4)));
        assertFalse(asMap().entrySet().contains(M4));
        assertFalse(asMap().entrySet().contains(M1));
        assertSize(2);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE() {
        newCache(0);asMap().entrySet().removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE1() {
        newCache(5);asMap().entrySet().removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE2() {
        newCache(5);asMap().entrySet().removeAll(Arrays.asList(1, null));
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeAllLazyStart() {
        init();
        assertNotStarted();
        asMap().entrySet().removeAll(Arrays.asList(M1, M4));
        checkLazystart();
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test
    public void removeAllShutdownISE() {
       init(5);
        assertStarted();
        shutdown();

        // should fail
        assertFalse(asMap().entrySet().removeAll(Arrays.asList(M1, M4)));
    }
}
