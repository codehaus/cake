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
import org.codehaus.cake.service.Container.State;
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
        assertEquals(State.RUNNING, c.getState());
    }

    @Test
    public void shutdownNoOp() {
        init();
        shutdown();
        assertTrue(c.getState().isShutdown());
        shutdown();
        assertTrue(c.getState().isShutdown());
        // TODO c is started???

    }

    @Test
    public void shutdownNowNoOp() {
        init();
        c.shutdownNow();
        assertTrue(c.getState().isShutdown());
        c.shutdownNow();
        assertTrue(c.getState().isShutdown());
        // TODO c is started???

    }

    @Test
    public void shutdownCache() {
        init();
        startCache();
        assertStarted();
        assertFalse(c.getState().isShutdown());
        shutdown();
        assertStarted();
        assertTrue(c.getState().isShutdown());
    }

    @Test
    public void shutdownNow() {
        init();
        startCache();
        assertStarted();
        assertFalse(c.getState().isShutdown());
        c.shutdownNow();
        assertStarted();
        assertTrue(c.getState().isShutdown());
    }

    @Test
    public void shutdownTerminated() throws InterruptedException {
        init();
        startCache();
        assertStarted();
        shutdown();
        assertTrue(c.awaitState(State.TERMINATED,10, TimeUnit.SECONDS));
        assertStarted();
        assertState(State.TERMINATED);
    }

    @Test
    public void shutdownNowTerminated() throws InterruptedException {
        init();
        startCache();
        assertStarted();
        c.shutdownNow();
        assertTrue(c.awaitState(State.TERMINATED,10, TimeUnit.SECONDS));
        assertStarted();
        assertState(State.TERMINATED);    }
}
