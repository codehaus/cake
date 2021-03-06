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

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class KeySetContains extends AbstractAsMapTCKTest {

    /**
     * containsKey returns true for contained key
     */
    @Test
    public void contains() {
       init(5);
        assertTrue(asMap().keySet().contains(1));
        assertFalse(asMap().keySet().contains(1111));
        assertFalse(asMap().keySet().contains(6));
    }

    @Test
    public void containsAll() {
       init(5);
        assertTrue(asMap().keySet().containsAll(Arrays.asList(1, 5)));
        assertFalse(asMap().keySet().containsAll(Arrays.asList(1, 6)));
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsAllLazyStart() {
        init();
        assertNotStarted();
        asMap().keySet().containsAll(Arrays.asList(1, 5));
        checkLazystart();
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsAllNPE() {
        newCache(5);asMap().keySet().containsAll(null);
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsAllNPE1() {
        newCache(5);asMap().keySet().containsAll(Arrays.asList(M1.getKey(), null));
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void containsAllShutdown() throws InterruptedException {
       init(5);
        assertStarted();
        shutdown();

        // should not fail, but result is undefined until terminated
        asMap().keySet().containsAll(Arrays.asList(1, 5));

        shutdownAndAwaitTermination();

        boolean containsKeys = asMap().keySet().containsAll(Arrays.asList(1, 5));
        assertFalse(containsKeys);// cache should be empty
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsLazyStart() {
        init();
        assertNotStarted();
        asMap().keySet().contains(1);
        checkLazystart();
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsNPE() {
        newCache(5);asMap().keySet().contains(null);
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void containsShutdown() throws InterruptedException {
       init(5);
        assertStarted();
        shutdown();

        // should not fail, but result is undefined until terminated
        asMap().keySet().contains(1);

        shutdownAndAwaitTermination();

        boolean containsKey = asMap().keySet().contains(1);
        assertFalse(containsKey);// cache should be empty
    }

}
