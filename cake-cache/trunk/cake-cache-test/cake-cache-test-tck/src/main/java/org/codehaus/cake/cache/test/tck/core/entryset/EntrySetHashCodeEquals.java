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
package org.codehaus.cake.cache.test.tck.core.entryset;

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_SET;

import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class EntrySetHashCodeEquals extends AbstractCacheTCKTest {

    /**
     * Maps with same contents are equal
     */
    @Test
    public void testEquals() {
        init();

        assertTrue(new HashSet().equals(c.entrySet()));
        assertTrue(c.entrySet().equals(new HashSet()));
        assertTrue(c.entrySet().equals(c.entrySet()));

        assertFalse(c.entrySet().equals(null));
        assertFalse(c.entrySet().equals(newCache(1).entrySet()));

        // abstractCacheEntry corner case
        init();
        c.put(M1.getKey(), M2.getValue());
        assertFalse(c.entrySet().equals(new HashSet(Collections.singletonList(M1))));

        c = newCache(5);
        assertTrue(M1_TO_M5_SET.equals(c.entrySet()));
        assertTrue(c.entrySet().equals(M1_TO_M5_SET));

        assertFalse(c.entrySet().equals(null));
        assertFalse(c.entrySet().equals(newCache(4).entrySet()));
        assertFalse(c.entrySet().equals(newCache(6).entrySet()));

    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void equalsShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        c.entrySet().equals(new HashSet());

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean equals = c.entrySet().equals(new HashSet());
        assertTrue(equals);// cache should be empty
    }

    @Test
    public void testHashCode() {
        assertEquals(M1_TO_M5_SET.hashCode(), newCache(5).entrySet().hashCode());
        assertEquals(new HashSet().hashCode(), newCache().entrySet().hashCode());
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    // @Test TODO fix
    public void hashCodeShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        c.entrySet().hashCode();

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        assertEquals(c.entrySet().hashCode(), new HashSet().hashCode());// cache should be
    }

}
