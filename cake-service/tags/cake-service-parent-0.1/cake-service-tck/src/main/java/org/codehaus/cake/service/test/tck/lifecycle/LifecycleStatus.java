/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.service.test.tck.lifecycle;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.Test;

/**
 * Test basic functionality of a Cache. This test should be applicable for any cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Lifecycle.java 526 2007-12-27 01:32:16Z kasper $
 */
public class LifecycleStatus extends AbstractTCKTest<Container, ContainerConfiguration> {

    @Test
    public void initialStatus() {
        newContainer();
        assertFalse(c.isStarted());
        assertFalse(c.isTerminated());
        assertFalse(c.isShutdown());
    }

    private void startCache() {
        prestart();
    }

    @Test
    public void lazyStart() {
        newContainer();
        startCache();
        assertTrue(c.isStarted());
        assertFalse(c.isTerminated());
        assertFalse(c.isShutdown());
    }

    @Test
    public void shutdownNoOp() {
        newContainer();
        c.shutdown();
        assertTrue(c.isShutdown());
        assertTrue(c.isTerminated());
        c.shutdown();
        assertTrue(c.isShutdown());
        // TODO c is started???

    }

    @Test
    public void shutdownNowNoOp() {
        newContainer();
        c.shutdownNow();
        assertTrue(c.isShutdown());
        assertTrue(c.isTerminated());
        c.shutdownNow();
        assertTrue(c.isShutdown());
        // TODO c is started???

    }

    @Test
    public void shutdownCache() {
        newContainer();
        startCache();
        assertTrue(c.isStarted());
        assertFalse(c.isShutdown());
        c.shutdown();
        assertTrue(c.isStarted());
        assertTrue(c.isShutdown());
    }

    @Test
    public void shutdownNow() {
        newContainer();
        startCache();
        assertTrue(c.isStarted());
        assertFalse(c.isShutdown());
        c.shutdownNow();
        assertTrue(c.isStarted());
        assertTrue(c.isShutdown());
    }

    @Test
    public void shutdownTerminated() throws InterruptedException {
        newContainer();
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
        newContainer();
        startCache();
        assertTrue(c.isStarted());
        c.shutdownNow();
        assertTrue(c.awaitTermination(10, TimeUnit.SECONDS));
        assertTrue(c.isStarted());
        assertTrue(c.isShutdown());
        assertTrue(c.isTerminated());
    }
}
