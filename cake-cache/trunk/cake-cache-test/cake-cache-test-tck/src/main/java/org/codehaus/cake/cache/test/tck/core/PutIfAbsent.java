/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests put operations for a cache.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: PutIfAbsent.java 554 2008-01-08 23:32:04Z kasper $
 */
public class PutIfAbsent extends AbstractCacheTCKTest {

    /**
     * Tests the putIfAbsent(K key, V value) method.
     */
    @Test
    public void putIfAbsent() {
        assertNull(c.putIfAbsent(M1.getKey(), M1.getValue()));
        assertEquals(M1.getValue(), peek(M1));
        assertEquals(M1.getValue(), c.putIfAbsent(M1.getKey(), M2.getValue()));
        assertFalse(c.containsValue(M2.getValue()));
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void putIfAbsentLazyStart() {
        init();
        assertFalse(c.isStarted());
        c.putIfAbsent(M1.getKey(), M1.getValue());
        checkLazystart();
    }

    /**
     * Tests that putIfAbsent(null, Object) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void putIfAbsentKeyNPE() {
        init();
        c.putIfAbsent(null, "A");
    }
    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test(expected = IllegalStateException.class)
    public void putIfAbsentShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        c.putIfAbsent(M1.getKey(), M1.getValue());
    }
    /**
     * Tests that putIfAbsent(Object, null) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void putIfAbsentValueNPE() {
        init();
        c.putIfAbsent(1, null);
    }
}
