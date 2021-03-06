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

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_SET;

import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class EntrySetHashCodeEquals extends AbstractAsMapTCKTest {

    /**
     * Maps with same contents are equal
     */
    @Test
    public void testEquals() {
        init();

        assertTrue(new HashSet().equals(asMap().entrySet()));
        assertTrue(asMap().entrySet().equals(new HashSet()));
        assertTrue(asMap().entrySet().equals(asMap().entrySet()));

        assertFalse(asMap().entrySet().equals(null));
        assertFalse(asMap().entrySet().equals(newCache(1).asMap().entrySet()));

        // abstractCacheEntry corner case
        init();
        put(M1.getKey(), M2.getValue());
        assertFalse(asMap().entrySet().equals(new HashSet(Collections.singletonList(M1))));

       init(5);
        assertTrue(M1_TO_M5_SET.equals(asMap().entrySet()));
        assertTrue(asMap().entrySet().equals(M1_TO_M5_SET));

        assertFalse(asMap().entrySet().equals(null));
        assertFalse(asMap().entrySet().equals(newCache(4).asMap().entrySet()));
        assertFalse(asMap().entrySet().equals(newCache(6).asMap().entrySet()));

    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void equalsShutdown() throws InterruptedException {
       init(5);
        assertStarted();
        shutdown();

        // should not fail, but result is undefined until terminated
        asMap().entrySet().equals(new HashSet());

        shutdownAndAwaitTermination();

        boolean equals = asMap().entrySet().equals(new HashSet());
        assertTrue(equals);// cache should be empty
    }

    @Test
    public void testHashCode() {
        assertEquals(M1_TO_M5_SET.hashCode(), newCache(5).asMap().entrySet().hashCode());
        assertEquals(new HashSet().hashCode(), newCache().asMap().entrySet().hashCode());
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    // @Test TODO fix
    public void hashCodeShutdown() throws InterruptedException {
       init(5);
        assertStarted();
        shutdown();

        // should not fail, but result is undefined until terminated
        asMap().entrySet().hashCode();

        shutdownAndAwaitTermination();

        assertEquals(asMap().entrySet().hashCode(), new HashSet().hashCode());// cache should be
    }

}
