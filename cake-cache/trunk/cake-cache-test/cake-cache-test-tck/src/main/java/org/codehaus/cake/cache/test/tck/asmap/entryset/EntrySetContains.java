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

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_NULL;
import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_SET;

import java.util.ArrayList;
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
public class EntrySetContains extends AbstractAsMapTCKTest {

    /**
     * containsKey returns true for contained key
     */
    @Test
    public void contains() {
       init(5);
        assertTrue(asMap().entrySet().contains(M1));
        assertFalse(asMap().entrySet().contains(M1_NULL));
        assertFalse(asMap().entrySet().contains(M6));
        assertFalse(asMap().entrySet().contains(M6.getKey()));
        assertFalse(asMap().entrySet().contains(M6.getValue()));
        assertFalse(asMap().entrySet().contains(M7));
    }

    @Test
    public void containsAll() {
       init(5);
        assertTrue(asMap().entrySet().containsAll(M1_TO_M5_SET));
        assertFalse(asMap().entrySet().containsAll(Arrays.asList(M1, M2, M3, M4, M5, M6)));
        assertFalse(asMap().entrySet().containsAll(Arrays.asList(M1, M1.getKey())));
        assertFalse(asMap().entrySet().containsAll(Arrays.asList(M1, M1.getValue())));
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsAllLazyStart() {
        init();
        assertNotStarted();
        asMap().entrySet().containsAll(new ArrayList((M1_TO_M5_SET)));
        checkLazystart();
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsAllNPE() {
        newCache(5);
        asMap().entrySet().containsAll(null);
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsAllNPE1() {
        newCache(5);
        asMap().entrySet().containsAll(Arrays.asList(M1, null));
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
        asMap().entrySet().containsAll(M1_TO_M5_SET);

        shutdownAndAwaitTermination();

        boolean containsKeys = asMap().entrySet().containsAll(M1_TO_M5_SET);
        assertFalse(containsKeys);// cache should be empty
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsLazyStart() {
        init();
        assertNotStarted();
        asMap().entrySet().contains(M1);
        checkLazystart();
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsNPE() {
        newCache(5);
        asMap().entrySet().contains(null);
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
        asMap().entrySet().contains(M1);

        shutdownAndAwaitTermination();

        boolean containsKey = asMap().entrySet().contains(M1);
        assertFalse(containsKey);// cache should be empty
    }

}
