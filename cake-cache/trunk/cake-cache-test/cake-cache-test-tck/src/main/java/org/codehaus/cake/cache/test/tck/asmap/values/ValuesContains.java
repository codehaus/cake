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

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_VALUES;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class ValuesContains extends AbstractAsMapTCKTest {

    /**
     * containsKey returns true for contained key
     */
    @Test
    public void contains() {
        c = newCache(5);
        assertTrue(asMap().values().contains(M1.getValue()));
        assertFalse(asMap().values().contains("aa"));
        assertFalse(asMap().values().contains(M6.getValue()));
    }

    @Test
    public void containsAll() {
        c = newCache(5);
        assertTrue(asMap().values().containsAll(M1_TO_M5_VALUES));
        assertFalse(asMap().values().containsAll(Arrays.asList(M1.getValue(), M5.getValue(), M6.getValue())));
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsAllLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        asMap().values().containsAll(M1_TO_M5_VALUES);
        checkLazystart();
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsAllNPE() {
        newCache(5);
        asMap().values().containsAll(null);
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsAllNPE1() {
        newCache(5);
        asMap().values().containsAll(Arrays.asList(M1.getValue(), null));
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
        asMap().values().containsAll(M1_TO_M5_VALUES);

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean containsKeys = asMap().values().containsAll(M1_TO_M5_VALUES);
        assertFalse(containsKeys);// cache should be empty
    }

    /**
     * {@link Cache#containsKey} lazy starts the cache.
     */
    @Test
    public void containsLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        asMap().values().contains(M1.getValue());
        checkLazystart();
    }

    /**
     * containsKey(null) throws NPE
     */
    @Test(expected = NullPointerException.class)
    public void containsNPE() {
        newCache(5);
        asMap().values().contains(null);
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
        asMap().values().contains(M1.getValue());

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean containsKey = asMap().values().contains(M1.getValue());
        assertFalse(containsKey);// cache should be empty
    }

}
