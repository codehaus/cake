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
package org.codehaus.cake.cache.test.tck.core.values;

import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN1;
import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN2;

import java.util.Arrays;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests the modifying functions of a keySet().
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ValuesRemove.java 554 2008-01-08 23:32:04Z kasper $
 */
public class ValuesRemove extends AbstractCacheTCKTest {

    @Test(expected = NullPointerException.class)
    public void removeNPE() {
        newCache(0).keySet().remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeNPE2() {
        newCache(5).values().remove(null);
    }

    @Test
    public void remove() {
        c = newCache(0);
        assertFalse(c.values().remove(MNAN2.getValue()));
        assertFalse(c.values().remove(MNAN1.getValue()));

        c = newCache(5);
        assertTrue(c.values().remove(M1.getValue()));
        assertEquals(4, c.size());
        assertFalse(c.values().contains(M1.getValue()));

        c = newCache(1);
        assertTrue(c.values().remove(M1.getValue()));
        assertTrue(c.isEmpty());
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void removeLazyStart() {
        init();
        assertFalse(c.isStarted());
        c.values().remove(MNAN1.getValue());
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
        assertFalse(c.values().remove(M1.getValue()));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void removeAll() {
        c = newCache(5);
        assertFalse(c.values().removeAll(Arrays.asList(1, 3)));
        assertTrue(c.values().removeAll(Arrays.asList(4, M2.getValue(), 5)));
        assertEquals(4, c.size());
        assertFalse(c.values().contains(M2.getValue()));
        assertTrue(c.values().removeAll(Arrays.asList(M1.getValue(), M4.getValue())));
        assertFalse(c.values().contains(M4.getValue()));
        assertFalse(c.values().contains(M1.getValue()));
        assertEquals(2, c.size());
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE() {
        newCache(0).values().removeAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeAllNPE1() {
        newCache(5).values().removeAll(null);
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
        assertFalse(c.isStarted());
        c.values().removeAll(Arrays.asList(M1.getValue(), M2.getValue()));
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
        assertFalse(c.values().removeAll(Arrays.asList(M1.getValue(), M2.getValue())));
    }
}
