/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import java.util.Arrays;
import java.util.Map;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link Cache#containsKey}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: GetAll.java 511 2007-12-13 14:37:02Z kasper $
 */
public class GetAll extends AbstractCacheTCKTest {

    @Before
    public void setup() {
        conf = newConfigurationClean();
    }

    @Test
    public void getAll() {
        c = newCache(4);
        Map<Integer, String> map = c.getAll(asList(M1.getKey(), M5.getKey(), M4.getKey()));
        assertEquals(3, map.size());
        assertEquals(M1.getValue(), map.get(M1.getKey()));
        assertTrue(map.entrySet().contains(M1));

        assertEquals(M4.getValue(), map.get(M4.getKey()));
        assertTrue(map.entrySet().contains(M4));

        assertNull(map.get(M5.getKey()));
        assertFalse(map.entrySet().contains(M5));
    }

    /**
     * {@link Cache#getAll} lazy starts the cache.
     */
    @Test
    public void getAllLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.getAll(asList(M1.getKey(), M5.getKey(), M4.getKey()));
        checkLazystart();
    }

    @Test(expected = NullPointerException.class)
    public void getAllNPE() {
        c = newCache(5);
        c.getAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void getAllNPE1() {
        c = newCache(5);
        c.getAll(Arrays.asList(1, null));
    }

    /**
     * {@link Cache#get()} should not fail when cache is shutdown.
     */
    @Test(expected = IllegalStateException.class)
    public void getAllShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        c.getAll(asList(M1.getKey(), M5.getKey(), M4.getKey()));
    }

}
