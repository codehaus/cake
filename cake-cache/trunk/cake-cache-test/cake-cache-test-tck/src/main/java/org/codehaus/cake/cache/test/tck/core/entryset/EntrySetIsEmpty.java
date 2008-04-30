/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.entryset;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EntrySetIsEmpty.java 415 2007-11-09 08:25:23Z kasper $
 */
public class EntrySetIsEmpty extends AbstractCacheTCKTest {

    /**
     * isEmpty is true of empty map and false for non-empty.
     */
    @Test
    public void isEmptyEntrySet() {
        assertTrue(newCache(0).entrySet().isEmpty());
        assertFalse(newCache(5).entrySet().isEmpty());
    }

    /**
     * {@link Cache#isEmpty()} lazy starts the cache.
     */
    @Test
    public void isEmptyLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.entrySet().isEmpty();
        checkLazystart();
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void isEmptyShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        c.entrySet().isEmpty();

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        boolean isEmpty = c.entrySet().isEmpty();
        assertTrue(isEmpty);// cache should be empty
    }
}
