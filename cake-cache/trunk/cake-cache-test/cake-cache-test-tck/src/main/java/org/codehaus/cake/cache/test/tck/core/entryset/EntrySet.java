/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core.entryset;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests {@link Cache#entrySet()} lazy start and shutdown.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EntrySet.java 526 2007-12-27 01:32:16Z kasper $
 */
public class EntrySet extends AbstractCacheTCKTest {

    /**
     * Calls to {@link Cache#entrySet} should not start the cache.
     */
    @Test
    public void noLazyStart() {
        init();
        c.entrySet();
        assertNotStarted();
    }

    /**
     * Calls to {@link Cache#entrySet} should not fail when the cache is shutdown.
     */
    @Test
    public void noFailOnShutdown() {
        init(5).assertStarted();
        shutdown();
        c.entrySet();
        awaitTermination();
        c.entrySet();
    }
}
