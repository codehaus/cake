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
package org.codehaus.cake.cache.test.tck.asmap.keyset;

import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN1;
import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN2;

import java.util.Arrays;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.codehaus.cake.test.util.CollectionTestUtil;
import org.junit.Test;

/**
 * Tests the modifying functions of a keySet().
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class KeySetRemove extends AbstractAsMapTCKTest {

    @Test(expected = NullPointerException.class)
    public void removeNPE() {
        newCache(0);asMap().keySet().remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNPE2() {
        newCache(5);asMap().keySet().remove(null);
    }

    @Test
    public void remove() {
        init();
        assertFalse(asMap().keySet().remove(MNAN2.getKey()));
        assertFalse(asMap().keySet().remove(MNAN1.getKey()));

       init(5);
        assertTrue(asMap().keySet().remove(M1.getKey()));
        assertSize(4);
        assertFalse(asMap().keySet().contains(M1.getKey()));

       init(1);
        assertTrue(asMap().keySet().remove(M1.getKey()));
        assertIsEmpty();
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeLazyStart() {
        init();
        assertNotStarted();
        asMap().keySet().remove(MNAN1.getKey());
        checkLazystart();
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void removeShutdownISE() {
       init(5);
        assertStarted();
        shutdown();

        // should fail
        assertFalse(asMap().keySet().remove(MNAN1.getKey()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void removeAll() {
       init(5);
        assertFalse(asMap().keySet().removeAll(Arrays.asList("F", "H")));
        assertTrue(asMap().keySet().removeAll(Arrays.asList("F", M2.getKey(), "H")));
        assertSize(4);
        assertFalse(asMap().keySet().contains(M2.getKey()));
        assertTrue(asMap().keySet().removeAll(Arrays.asList(M1.getKey(), M4.getKey())));
        assertFalse(asMap().keySet().contains(M4.getKey()));
        assertFalse(asMap().keySet().contains(M1.getKey()));
        assertSize(2);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE() {
        newCache(0);asMap().keySet().removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE1() {
        newCache(5);asMap().keySet().removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE2() {
        newCache(5);asMap().keySet().removeAll(Arrays.asList(1, null));
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeAllLazyStart() {
        init();
        assertNotStarted();
        asMap().keySet().removeAll(CollectionTestUtil.asList(2, 3));
        checkLazystart();
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void removeAllShutdownISE() {
       init(5);
        assertStarted();
        shutdown();

        // should fail
        assertFalse(asMap().keySet().removeAll(CollectionTestUtil.asList(2, 3)));
    }
}
