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

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: KeySetContains.java 511 2007-12-13 14:37:02Z kasper $
 */
public class KeySetContains extends AbstractCacheTCKTest {

    /**
     * containsKey returns true for contained key
     */
    @Test
    public void contains() {
        c = newCache(5);
        assertTrue(c.keySet().contains(1));
        assertFalse(c.keySet().contains(1111));
        assertFalse(c.keySet().contains(6));
    }

    @Test
    public void containsAll() {
        c = newCache(5);
        assertTrue(c.keySet().containsAll(Arrays.asList(1, 5)));
        assertFalse(c.keySet().containsAll(Arrays.asList(1, 6)));
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsAllLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.keySet().containsAll(Arrays.asList(1, 5));
        checkLazystart();
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsAllNPE() {
        newCache(5).keySet().containsAll(null);
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsAllNPE1() {
        newCache(5).keySet().containsAll(Arrays.asList(M1.getKey(), null));
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void containsAllShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        c.keySet().containsAll(Arrays.asList(1, 5));

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean containsKeys = c.keySet().containsAll(Arrays.asList(1, 5));
        assertFalse(containsKeys);// cache should be empty
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.keySet().contains(1);
        checkLazystart();
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsNPE() {
        newCache(5).keySet().contains(null);
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void containsShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        c.keySet().contains(1);

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean containsKey = c.keySet().contains(1);
        assertFalse(containsKey);// cache should be empty
    }

}
