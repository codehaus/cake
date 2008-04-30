/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.values;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Values.java 415 2007-11-09 08:25:23Z kasper $
 */
public class Values extends AbstractCacheTCKTest {
    /**
     * Calls to {@link Cache#values} should not start the cache.
     */
    @Test
    public void noLazyStart() {
        c = newCache(0);
        c.values();
        assertFalse(c.isStarted());
    }

    /**
     * Calls to {@link Cache#values} should not fail when the cache is shutdown.
     */
    @Test
    public void noFailOnShutdown() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();
        c.values(); // should not fail
    }
}
