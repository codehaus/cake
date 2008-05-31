/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.keyset;

import java.util.Set;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: KeySetClear.java 538 2007-12-31 00:18:13Z kasper $
 */
public class KeySetClear extends AbstractCacheTCKTest {

    /**
     * {@link Set#clear()} removes all mappings.
     */
    @Test
    public void clearKeySet() {
        c = newCache(5);
        assertEquals(c.keySet().size(), 5);
        assertFalse(c.keySet().isEmpty());
        assertEquals(c.size(), 5);
        assertFalse(c.isEmpty());

        c.keySet().clear();

        assertEquals(c.keySet().size(), 0);
        assertTrue(c.keySet().isEmpty());
        assertEquals(c.size(), 0);
        assertTrue(c.isEmpty());
    }

    /**
     * {@link Set#clear()} lazy starts the cache.
     */
    @Test
    public void clearLazyStart() {
        c = newCache(0);
        c.keySet().clear();
        checkLazystart();
    }

    /**
     * {@link Set#clear()} fails when the cache is shutdown.
     */
    @Test
    public void clearShutdown() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();
        c.keySet().clear();
    }
}
