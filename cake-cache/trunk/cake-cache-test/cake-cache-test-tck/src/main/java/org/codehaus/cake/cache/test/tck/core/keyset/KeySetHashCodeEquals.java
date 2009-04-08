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
package org.codehaus.cake.cache.test.tck.core.keyset;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests non modifying actions for a caches value set {@link org.codehaus.cake.cache.Cache#keySet()}
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class KeySetHashCodeEquals extends AbstractCacheTCKTest {

    /**
     * Maps with same contents are equal
     */
    @Test
    public void testEquals() {
        // assertTrue(c5.values().equals(c5.values()));
        init();
        assertFalse(c.keySet().equals(null));
        assertFalse(c.keySet().equals(newCache(1).keySet()));

        c = newCache(5);
        assertFalse(c.keySet().equals(null));
        assertFalse(c.keySet().equals(newCache(4).keySet()));

        c = newCache(5);
        assertFalse(c.keySet().equals(newCache(6).keySet()));

        c = newCache(5);
        assertFalse(c.keySet().equals(new HashSet<Integer>(Arrays.asList(1, 2, 3, 4, null))));
        assertFalse(c.keySet().equals(new HashSet(Arrays.asList("1", "2", "3", "4", "5"))));
        assertTrue(c.keySet().equals(c.keySet()));
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
        c.keySet().equals(new HashSet());

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean equals = c.keySet().equals(new HashSet());
        assertTrue(equals);// cache should be empty
    }

    // TODO test hashCode shutdown

    @Test
    public void testHashCode() {
        c = newCache(5);
        assertEquals(c.keySet().hashCode(), 1 + 2 + 3 + 4 + 5);
    }

}
