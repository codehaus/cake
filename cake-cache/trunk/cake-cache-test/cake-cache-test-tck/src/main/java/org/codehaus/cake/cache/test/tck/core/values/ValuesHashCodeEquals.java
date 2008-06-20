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

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests non modifying actions for a caches value set {@link org.codehaus.cake.cache.Cache#keySet()}
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ValuesHashCodeEquals.java 526 2007-12-27 01:32:16Z kasper $
 */
public class ValuesHashCodeEquals extends AbstractCacheTCKTest {

    /**
     * Maps with same contents are equal
     */
    @Test
    public void testEquals() {
        // assertTrue(c5.values().equals(c5.values()));
        init();
        assertFalse(c.values().equals(null));
        assertFalse(c.values().equals(newCache(1).values()));
        c = newCache(5);
        assertFalse(c.values().equals(null));
        assertFalse(c.values().equals(newCache(4).values()));
        assertFalse(c.values().equals(newCache(6).values()));
        assertTrue(c.values().equals(c.values()));
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    // @Test TODO fix
    public void equalsShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        c.values().equals(new HashSet());

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean equals = c.values().equals(new HashSet());
        assertTrue(equals);// cache should be empty
    }

    @Test
    public void testHashCode() {
    // assertEquals(c5.values().hashCode(), c5.values().hashCode());
    }
    // TODO test hashCode shutdown
}
