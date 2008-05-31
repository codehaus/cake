/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * This class tests the {@link Cache#clear()} operation.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Clear.java 538 2007-12-31 00:18:13Z kasper $
 */
public class Clear extends AbstractCacheTCKTest {

    /**
     * {@link Cache#clear()} removes all mappings.
     */
    @Test
    public void clearRemoves() {
        init(5).assertSize(5);
        clear().assertSize(0);
    }

    /**
     * {@link Cache#clear()} lazy starts the cache.
     */
    @Test
    public void clearLazyStart() {
        assertNotStarted().clear().assertStarted();
    }

    /**
     * {@link Cache#clear()} should not fail when cache has been shutdown
     */
    @Test
    public void clearShutdown() {
        init(5).assertStarted();
        shutdown().clear();
        awaitTermination().clear();
    }
}
