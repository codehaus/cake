/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.values;

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_VALUES;

import java.util.Arrays;
import java.util.HashSet;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ValuesToArray.java 526 2007-12-27 01:32:16Z kasper $
 */
public class ValuesToArray extends AbstractCacheTCKTest {

    @SuppressWarnings("unchecked")
    @Test
    public void toArray() {
        init();
        assertEquals(new HashSet(), new HashSet(Arrays.asList(c.values().toArray())));

        assertEquals(new HashSet(), new HashSet(Arrays.asList(c.values().toArray(
                new String[0]))));
        c = newCache(5);
        assertEquals(new HashSet(M1_TO_M5_VALUES), new HashSet(Arrays.asList(c.values()
                .toArray())));

        assertEquals(new HashSet(M1_TO_M5_VALUES), new HashSet(Arrays.asList(c.values()
                .toArray(new String[0]))));
        assertEquals(new HashSet(M1_TO_M5_VALUES), new HashSet(Arrays.asList(c.values()
                .toArray(new String[5]))));
    }

    /**
     * {@link Cache#isEmpty()} lazy starts the cache.
     */
    @Test
    public void toArrayLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.values().toArray();
        checkLazystart();
    }

    /**
     * {@link Cache#isEmpty()} lazy starts the cache.
     */
    @Test
    public void toArrayLazyStart1() {
        c = newCache(0);
        assertFalse(c.isStarted());
        c.values().toArray(new String[5]);
        checkLazystart();
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void toArrayShutdown() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        assertEquals(0, c.values().toArray().length);
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void toArrayShutdown1() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should not fail, but result is undefined until terminated
        assertEquals(5, c.values().toArray(new String[5]).length);
    }
}
