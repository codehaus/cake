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

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_NULL;
import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_SET;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EntrySetContains.java 537 2007-12-30 19:21:20Z kasper $
 */
public class EntrySetContains extends AbstractCacheTCKTest {

    /**
     * containsKey returns true for contained key
     */
    @Test
    public void contains() {
        c = newCache(5);
        assertTrue(c.entrySet().contains(M1));
        assertFalse(c.entrySet().contains(M1_NULL));
        assertFalse(c.entrySet().contains(M6));
        assertFalse(c.entrySet().contains(M6.getKey()));
        assertFalse(c.entrySet().contains(M6.getValue()));
        assertFalse(c.entrySet().contains(M7));
    }

    @Test
    public void containsAll() {
        c = newCache(5);
        assertTrue(c.entrySet().containsAll(M1_TO_M5_SET));
        assertFalse(c.entrySet().containsAll(Arrays.asList(M1, M2, M3, M4, M5, M6)));
        assertFalse(c.entrySet().containsAll(Arrays.asList(M1, M1.getKey())));
        assertFalse(c.entrySet().containsAll(Arrays.asList(M1, M1.getValue())));
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsAllLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.entrySet().containsAll(new ArrayList((M1_TO_M5_SET)));
        checkLazystart();
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsAllNPE() {
        newCache(5).entrySet().containsAll(null);
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsAllNPE1() {
        newCache(5).entrySet().containsAll(Arrays.asList(M1, null));
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
        c.entrySet().containsAll(M1_TO_M5_SET);

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean containsKeys = c.entrySet().containsAll(M1_TO_M5_SET);
        assertFalse(containsKeys);// cache should be empty
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.entrySet().contains(M1);
        checkLazystart();
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsNPE() {
        newCache(5).entrySet().contains(null);
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
        c.entrySet().contains(M1);

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean containsKey = c.entrySet().contains(M1);
        assertFalse(containsKey);// cache should be empty
    }

}
