/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.memorystore;

import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests EvictionService.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EvictionService.java 559 2008-01-09 16:28:27Z kasper $
 */
public class MemoryStoreGeneral extends AbstractCacheTCKTest {

    @Test
    public void testServiceAvailable() {
        assertTrue(c.hasService(MemoryStoreService.class));
        assertNotNull(c.getService(MemoryStoreService.class));
    }

    /**
     * Tests maximum capacity.
     */
    @Test
    public void maximumVolume() {
        assertEquals(Long.MAX_VALUE, withMemoryStore().getMaximumVolume());
        withMemoryStore().setMaximumVolume(1000);
        assertEquals(1000, withMemoryStore().getMaximumVolume());

        // start value
        conf.withMemoryStore().setMaximumVolume(5000);
        init();
        assertEquals(5000, withMemoryStore().getMaximumVolume());
    }

    /**
     * Tests maximum capacity IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void maximumCapacityIAE() {
        withMemoryStore().setMaximumVolume(-1);
    }

    /**
     * Tests maximum capacity.
     */
    @Test
    public void maximumSize() {
        assertEquals(Integer.MAX_VALUE, withMemoryStore().getMaximumSize());
        withMemoryStore().setMaximumSize(1000);
        assertEquals(1000, withMemoryStore().getMaximumSize());

        // start value
        conf.withMemoryStore().setMaximumSize(5000);
        init();
        assertEquals(5000, withMemoryStore().getMaximumSize());
    }

    /**
     * Tests maximum capacity IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void maximumSizeIAE() {
        withMemoryStore().setMaximumSize(-1);
    }

}
