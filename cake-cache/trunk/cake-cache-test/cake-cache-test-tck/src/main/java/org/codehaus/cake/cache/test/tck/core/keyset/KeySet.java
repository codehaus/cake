/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.keyset;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: KeySet.java 415 2007-11-09 08:25:23Z kasper $
 */
public class KeySet extends AbstractCacheTCKTest {
    /**
     * Calls to {@link Cache#keySet} should not start the cache.
     */
    @Test
    public void noLazyStart() {
        c = newCache(0);
        c.keySet();
        assertFalse(c.isStarted());
    }

    /**
     * Calls to {@link Cache#keySet} should not fail when the cache is shutdown.
     */
    @Test
    public void noFailOnShutdown() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();
        c.keySet(); // should not fail
    }
}
