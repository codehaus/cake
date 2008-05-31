/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import java.util.ArrayList;
import java.util.Random;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests put operations for a cache.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Put.java 554 2008-01-08 23:32:04Z kasper $
 */
public class Put extends AbstractCacheTCKTest {

    @Test
    public void put() {
        init();
        c.put(1, "B");
        assertEquals(1, c.size());
        c.put(1, "C");
        assertEquals(1, c.size());
        assertEquals("C", peek(M1));
    }

    @Test
    public void putMany() {
        final int count = 500;
        Random r = new Random(1123123);
        for (int i = 0; i < count; i++) {
            int key = r.nextInt(250);
            c.put(key, "" + key);
        }
        assertEquals(c.size(), new ArrayList(c.entrySet()).size());
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void putLazyStart() {
        init();
        assertFalse(c.isStarted());
        c.put(1, "B");
        checkLazystart();
    }

    /**
     * {@link Cache#containsKey()} should fail when cache is shutdown.
     */
    @Test(expected = IllegalStateException.class)
    public void putShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        c.put(1, "B");
    }

    @Test(expected = NullPointerException.class)
    public void putKeyNPE() {
        init();
        c.put(null, "A");
    }

    @Test(expected = NullPointerException.class)
    public void putValueNPE() {
        init();
        c.put(1, null);
    }
}
