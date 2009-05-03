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
package org.codehaus.cake.cache.test.tck.asmap.values;

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
public class ValuesRemove extends AbstractAsMapTCKTest {

    @Test(expected = NullPointerException.class)
    public void removeNPE() {
        newCache(0);asMap().keySet().remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNPE2() {
        newCache(5);asMap().values().remove(null);
    }

    @Test
    public void remove() {
        init();
        assertFalse(asMap().values().remove(MNAN2.getValue()));
        assertFalse(asMap().values().remove(MNAN1.getValue()));

       init(5);
        assertTrue(asMap().values().remove(M1.getValue()));
        assertSize(4);
        assertFalse(asMap().values().contains(M1.getValue()));

       init(1);
        assertTrue(asMap().values().remove(M1.getValue()));
        assertIsEmpty();
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeLazyStart() {
        init();
        assertNotStarted();
        asMap().values().remove(MNAN1.getValue());
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
        assertFalse(asMap().values().remove(M1.getValue()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void removeAll() {
       init(5);
        assertFalse(asMap().values().removeAll(Arrays.asList(1, 3)));
        assertTrue(asMap().values().removeAll(Arrays.asList(4, M2.getValue(), 5)));
        assertSize(4);
        assertFalse(asMap().values().contains(M2.getValue()));
        assertTrue(asMap().values().removeAll(Arrays.asList(M1.getValue(), M4.getValue())));
        assertFalse(asMap().values().contains(M4.getValue()));
        assertFalse(asMap().values().contains(M1.getValue()));
        assertSize(2);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE() {
        newCache(0);asMap().values().removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE1() {
        newCache(5);asMap().values().removeAll(null);
    }

    // TODO fix
    @Test
    // (expected = NullPointerException.class)
    public void removeAllNPE2() {
    // newCache(5).values().removeAll(Arrays.asList(M1.getValue(), null));
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeAllLazyStart() {
        init();
        assertNotStarted();
        asMap().values().removeAll(Arrays.asList(M1.getValue(), M2.getValue()));
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
        assertFalse(asMap().values().removeAll(Arrays.asList(M1.getValue(), M2.getValue())));
    }
}
