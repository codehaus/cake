/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link Cache#containsKey}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Get.java 537 2007-12-30 19:21:20Z kasper $
 */
public class Get extends AbstractCacheTCKTest {

    @Before
    public void setup() {
        conf = newConfigurationClean();
    }

    /**
     * Test simple get.
     */
    @Test
    public void get() {
        c = newCache(5);
        assertEquals(M1.getValue(), c.get(M1.getKey()));
        assertEquals(M5.getValue(), c.get(M5.getKey()));
        assertNull(c.get(M6.getKey()));
    }

    /**
     * {@link Cache#get} lazy starts the cache.
     */
    @Test
    public void getLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.get(M6.getKey());
        checkLazystart();
    }

    /**
     * get(null) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void getNPE() {
        c = newCache(5);
        c.get(null);
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test(expected = IllegalStateException.class)
    public void getShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        c.get(M1.getKey());
    }

}
