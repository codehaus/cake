/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class Peek extends AbstractCacheTCKTest {

    @Test
    public void peek() {
        c = newCache(5);
        assertNull(peek(M6));
        assertEquals(M1.getValue(), peek(M1));
        assertEquals(M5.getValue(), peek(M5));
    }

    /**
     * {@link Cache#peek} lazy starts the cache.
     */
    @Test
    public void peekLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        peek(M1);
        checkLazystart();
    }

    @Test(expected = NullPointerException.class)
    public void peekNPE() {
        c = newCache(5);
        peek((Integer) null);
    }

    /**
     * {@link Cache#containsValue()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void peekShutdown() throws InterruptedException {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        peek(M1);

        assertTrue(c.awaitTermination(1, TimeUnit.SECONDS));

        Object peek = peek(M1);
        assertNull(peek);// cache should be empty
    }
}
