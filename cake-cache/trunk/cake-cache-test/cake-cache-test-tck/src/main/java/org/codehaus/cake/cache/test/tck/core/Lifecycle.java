/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache.test.tck.core;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Test basic functionality of a Cache. This test should be applicable for any cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class Lifecycle extends AbstractCacheTCKTest {

    @Test
    public void initialStatus() {
        init();
        assertNotStarted();
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
        assertStarted();
        assertFalse(c.isTerminated());
        assertFalse(c.isShutdown());
    }

    @Test
    public void shutdownNoOp() {
        init();
        shutdown();
        assertTrue(c.isShutdown());
        assertTrue(c.isTerminated());
        shutdown();
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
        assertStarted();
        assertFalse(c.isShutdown());
        shutdown();
        assertStarted();
        assertTrue(c.isShutdown());
    }

    @Test
    public void shutdownNow() {
        init();
        startCache();
        assertStarted();
        assertFalse(c.isShutdown());
        c.shutdownNow();
        assertStarted();
        assertTrue(c.isShutdown());
    }

    @Test
    public void shutdownTerminated() throws InterruptedException {
        init();
        startCache();
        assertStarted();
        shutdown();
        assertTrue(c.awaitTermination(10, TimeUnit.SECONDS));
        assertStarted();
        assertTrue(c.isShutdown());
        assertTrue(c.isTerminated());
    }

    @Test
    public void shutdownNowTerminated() throws InterruptedException {
        init();
        startCache();
        assertStarted();
        c.shutdownNow();
        assertTrue(c.awaitTermination(10, TimeUnit.SECONDS));
        assertStarted();
        assertTrue(c.isShutdown());
        assertTrue(c.isTerminated());
    }
}
