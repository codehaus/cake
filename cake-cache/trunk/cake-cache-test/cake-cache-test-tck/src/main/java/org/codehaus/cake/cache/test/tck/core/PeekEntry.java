/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class PeekEntry extends AbstractCacheTCKTest {

    @Test
    public void setValue() {
        init(5);
        CacheEntry<Integer, String> ce = peekEntry(M1);
        try {
            ce.setValue("foo");
            assertEquals("foo", get(M1));
        } catch (UnsupportedOperationException ok) {}
    }
    @Test
    public void peekEntry() {
        c = newCache(5);
        assertNull(peekEntry(M6));
        assertEquals(M1.getValue(), peekEntry(M1).getValue());
        assertEquals(M1.getKey(), peekEntry(M1).getKey());
        assertEquals(M5.getValue(), peekEntry(M5).getValue());
        assertEquals(M5.getKey(), peekEntry(M5).getKey());
    }

    /**
     * {@link Cache#peek} lazy starts the cache.
     */
    @Test
    public void peekEntryLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        peekEntry(M1);
        checkLazystart();
    }

    @Test(expected = NullPointerException.class)
    public void peekEntryNPE() {
        c = newCache(5);
        peekEntry(null);
    }

    /**
     * {@link Cache#containsValue()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void peekEntryShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        peekEntry(M1);

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        Object peekEntry = peekEntry(M1);
        assertNull(peekEntry);// cache should be empty
    }
}
