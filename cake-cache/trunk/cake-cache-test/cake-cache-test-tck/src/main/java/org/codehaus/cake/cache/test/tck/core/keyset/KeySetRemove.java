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
package org.codehaus.cake.cache.test.tck.core.keyset;

import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN1;
import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN2;

import java.util.Arrays;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.test.util.CollectionTestUtil;
import org.junit.Test;

/**
 * Tests the modifying functions of a keySet().
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class KeySetRemove extends AbstractCacheTCKTest {

    @Test(expected = NullPointerException.class)
    public void removeNPE() {
        newCache(0).keySet().remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNPE2() {
        newCache(5).keySet().remove(null);
    }

    @Test
    public void remove() {
        c = newCache(0);
        assertFalse(c.keySet().remove(MNAN2.getKey()));
        assertFalse(c.keySet().remove(MNAN1.getKey()));

        c = newCache(5);
        assertTrue(c.keySet().remove(M1.getKey()));
        assertEquals(4, c.size());
        assertFalse(c.keySet().contains(M1.getKey()));

        c = newCache(1);
        assertTrue(c.keySet().remove(M1.getKey()));
        assertTrue(c.isEmpty());
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeLazyStart() {
        init();
        assertFalse(c.isStarted());
        c.keySet().remove(MNAN1.getKey());
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
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        assertFalse(c.keySet().remove(MNAN1.getKey()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void removeAll() {
        c = newCache(5);
        assertFalse(c.keySet().removeAll(Arrays.asList("F", "H")));
        assertTrue(c.keySet().removeAll(Arrays.asList("F", M2.getKey(), "H")));
        assertEquals(4, c.size());
        assertFalse(c.keySet().contains(M2.getKey()));
        assertTrue(c.keySet().removeAll(Arrays.asList(M1.getKey(), M4.getKey())));
        assertFalse(c.keySet().contains(M4.getKey()));
        assertFalse(c.keySet().contains(M1.getKey()));
        assertEquals(2, c.size());
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE() {
        newCache(0).keySet().removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE1() {
        newCache(5).keySet().removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE2() {
        newCache(5).keySet().removeAll(Arrays.asList(1, null));
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeAllLazyStart() {
        init();
        assertFalse(c.isStarted());
        c.keySet().removeAll(CollectionTestUtil.asList(2, 3));
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
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        assertFalse(c.keySet().removeAll(CollectionTestUtil.asList(2, 3)));
    }
}
