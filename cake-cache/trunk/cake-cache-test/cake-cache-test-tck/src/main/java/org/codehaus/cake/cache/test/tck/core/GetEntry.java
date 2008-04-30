/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests {@link Cache#containsKey}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: GetEntry.java 511 2007-12-13 14:37:02Z kasper $
 */
public class GetEntry extends AbstractCacheTCKTest {

    @Test
    public void getEntry() {
        c = newCache(5);
        assertNull(c.getEntry(6));
        assertEquals(M1.getValue(), c.getEntry(M1.getKey()).getValue());
        assertEquals(M1.getKey(), c.getEntry(M1.getKey()).getKey());
        assertEquals(M5.getValue(), c.getEntry(M5.getKey()).getValue());
        assertEquals(M5.getKey(), c.getEntry(M5.getKey()).getKey());
    }


    /**
     * {@link Cache#get} lazy starts the cache.
     */
    @Test
    public void getLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.getEntry(M6.getKey());
        checkLazystart();
    }

    /**
     * get(null) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void getNPE() {
        c = newCache(5);
        c.getEntry(null);
    }

    /**
     * {@link Cache#getEntry(Object)} should not fail when cache is shutdown.
     * 
     */
    @Test(expected = IllegalStateException.class)
    public void getShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        c.getEntry(M1.getKey());
    }

    @Test
    public void setValue() {
        init(5);
        CacheEntry<Integer, String> ce = c.getEntry(M1.getKey());
        try {
            ce.setValue("foo");
            assertEquals("foo", get(M1));
        } catch (UnsupportedOperationException ok) {}
    }
    
    @Test
    public void get() {
        newConfigurationClean();
        init();
        c = newCache(0);
        assertNull(c.getEntry(M1.getKey()));
        c = newCache(1);
        CacheEntry<Integer, String> ce = c.getEntry(M1.getKey());
        assertEquals(M1.getKey(), ce.getKey());
        assertEquals(M1.getValue(), ce.getValue());
    }
    
    @Test
    public void put() {
        newConfigurationClean();
        init();
        c = newCache(0);
        assertNull(c.getEntry(M1.getKey()));
        c = newCache(1);
        CacheEntry<Integer, String> ce = c.getEntry(M1.getKey());
        c.put(M1.getKey(), M2.getValue());
        ce = c.getEntry(M1.getKey());
        assertEquals(M1.getKey(), ce.getKey());
        assertEquals(M2.getValue(), ce.getValue());
    }
}
