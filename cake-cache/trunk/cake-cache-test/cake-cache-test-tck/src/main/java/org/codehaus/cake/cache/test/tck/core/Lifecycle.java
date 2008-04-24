/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */

package org.codehaus.cake.cache.test.tck.core;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Test basic functionality of a Cache. This test should be applicable for any cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Lifecycle.java 526 2007-12-27 01:32:16Z kasper $
 */
public class Lifecycle extends AbstractCacheTCKTest {

    @Test
    public void initialStatus() {
        init();
        assertFalse(c.isStarted());
        assertFalse(c.isTerminated());
        assertFalse(c.isShutdown());
    }

    private void startCache() {
        // we don't have anything better to start with right now
        c.put(1, "foo");
    }

    @Test
    public void lazyStart() {
        init();
        startCache();
        assertTrue(c.isStarted());
        assertFalse(c.isTerminated());
        assertFalse(c.isShutdown());
    }

    @Test
    public void shutdownNoOp() {
        init();
        c.shutdown();
        assertTrue(c.isShutdown());
        assertTrue(c.isTerminated());
        c.shutdown();
        assertTrue(c.isShutdown());
        // TODO c is started???

    }
    @Test
    public void shutdownNowNoOp() {
        init();
        c.shutdownNow();
        assertTrue(c.isShutdown());
        assertTrue(c.isTerminated());
        c.shutdownNow();
        assertTrue(c.isShutdown());
        // TODO c is started???

    }
    @Test
    public void shutdownCache() {
        init();
        startCache();
        assertTrue(c.isStarted());
        assertFalse(c.isShutdown());
        c.shutdown();
        assertTrue(c.isStarted());
        assertTrue(c.isShutdown());
    }

    @Test
    public void shutdownNow()  {
        init();
        startCache();
        assertTrue(c.isStarted());
        assertFalse(c.isShutdown());
        c.shutdownNow();
        assertTrue(c.isStarted());
        assertTrue(c.isShutdown());
    }

    @Test
    public void shutdownTerminated() throws InterruptedException {
        init();
        startCache();
        assertTrue(c.isStarted());
        c.shutdown();
        assertTrue(c.awaitTermination(10, TimeUnit.SECONDS));
        assertTrue(c.isStarted());
        assertTrue(c.isShutdown());
        assertTrue(c.isTerminated());
    }

    @Test
    public void shutdownNowTerminated() throws InterruptedException {
        init();
        startCache();
        assertTrue(c.isStarted());
        c.shutdownNow();
        assertTrue(c.awaitTermination(10, TimeUnit.SECONDS));
        assertTrue(c.isStarted());
        assertTrue(c.isShutdown());
        assertTrue(c.isTerminated());
    }
}
