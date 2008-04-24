/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.entryset;

import java.util.Set;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EntrySetClear.java 538 2007-12-31 00:18:13Z kasper $
 */
public class EntrySetClear extends AbstractCacheTCKTest {

    /**
     * {@link Set#clear()} removes all mappings.
     */
    @Test
    public void clearEntrySet() {
        c = newCache(5);
        assertEquals(c.entrySet().size(), 5);
        assertFalse(c.entrySet().isEmpty());
        assertSize(5);
        assertFalse(c.isEmpty());

        c.entrySet().clear();

        assertEquals(c.entrySet().size(), 0);
        assertTrue(c.entrySet().isEmpty());
        assertSize(0);
        assertTrue(c.isEmpty());
    }

    /**
     * {@link Set#clear()} lazy starts the cache.
     */
    @Test
    public void clearLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.entrySet().clear();
        checkLazystart();
    }

    /**
     * {@link Set#clear()} fails when the cache is shutdown.
     */
    @Test
    public void clearShutdown() {
        c = newCache(5);
        //put(1);
        assertTrue(c.isStarted());
        c.shutdown();
        c.entrySet().clear();
    }
}
