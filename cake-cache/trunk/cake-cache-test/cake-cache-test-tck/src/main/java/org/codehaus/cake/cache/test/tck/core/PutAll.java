/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_NULL;
import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN1;
import static org.codehaus.cake.test.util.CollectionTestUtil.NULL_A;
import static org.codehaus.cake.test.util.CollectionTestUtil.asMap;

import java.util.Map;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests put operations for a cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: PutAll.java 526 2007-12-27 01:32:16Z kasper $
 */
public class PutAll extends AbstractCacheTCKTest {

    @SuppressWarnings("unchecked")
    @Test
    public void putAll() {
        init();
        c.putAll(asMap(M1, M5));
        assertEquals(2, c.size());
        assertTrue(c.entrySet().contains(M1));
        assertTrue(c.entrySet().contains(M5));

        c.putAll(asMap(M1, M5));
        assertEquals(2, c.size());

        c.putAll(asMap(MNAN1, M4));
        assertEquals(3, c.size());
        assertFalse(c.entrySet().contains(M1));

    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void putAllLazyStart() {
        init();
        assertFalse(c.isStarted());
        c.putAll(asMap(M1, M5));
        checkLazystart();
    }

    /**
     * {@link Cache#putAll(Map)} should fail when the cache is shutdown.
     */
    @Test(expected = IllegalStateException.class)
    public void putAllShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        c.putAll(asMap(M1, M5));
    }

    @Test(expected = NullPointerException.class)
    public void putAllNPE() {
        init();
        putAll((Map.Entry) null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void putAllKeyMappingNPE() {
        init();
        c.putAll(asMap(M1, NULL_A));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void putAllValueMappingNPE() {
        init();
        c.putAll(asMap(M1, M1_NULL));
    }
}
