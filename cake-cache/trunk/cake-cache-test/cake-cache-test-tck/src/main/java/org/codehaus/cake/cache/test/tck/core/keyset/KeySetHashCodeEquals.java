/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.keyset;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests non modifying actions for a caches value set
 * {@link org.codehaus.cake.cache.Cache#keySet()}
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: KeySetHashCodeEquals.java 526 2007-12-27 01:32:16Z kasper $
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
        assertFalse(c.keySet().equals(newCache(6).keySet()));
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
    
    //TODO test hashCode shutdown
    
    @Test
    public void testHashCode() {
    // assertEquals(c5.values().hashCode(), c5.values().hashCode());
    }

}
