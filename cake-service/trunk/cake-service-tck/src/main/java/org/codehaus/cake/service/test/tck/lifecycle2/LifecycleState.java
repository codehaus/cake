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
package org.codehaus.cake.service.test.tck.lifecycle2;

import java.util.concurrent.TimeUnit;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.Container.State;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.Test;

/**
 * Test basic functionality of a Cache. This test should be applicable for any cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LifecycleStatus.java 364 2009-05-27 21:31:45Z kasper $
 */
public class LifecycleState extends AbstractTCKTest<Container, ContainerConfiguration> {

    @Test
    public void initialStatus() {
        newContainer();
        assertEquals(State.INITIALIZED, c.getState());
    }

    private void startCache() {
        prestart();
    }

    @Test
    public void lazyStart() {
        newContainer();
        startCache();
        assertEquals(State.RUNNING, c.getState());
    }

    @Test
    public void shutdownNoOp() {
        newContainer();
        c.shutdown();
        assertState(State.SHUTDOWN, State.TERMINATED);
        c.shutdown();
        assertState(State.SHUTDOWN, State.TERMINATED);
    }

    @Test
    public void shutdownNowNoOp() {
        newContainer();
        assertState(State.INITIALIZED);
        c.shutdownNow();
        assertState(State.SHUTDOWN, State.TERMINATED);
        c.shutdownNow();
        assertState(State.SHUTDOWN, State.TERMINATED);
    }

    @Test
    public void shutdownCache() {
        newContainer();
        assertState(State.INITIALIZED);
        startCache();
        assertState(State.RUNNING);
        c.shutdown();
        assertState(State.SHUTDOWN, State.TERMINATED);
    }

    @Test
    public void shutdownNow() {
        newContainer();
        startCache();
        assertState(State.RUNNING);
        c.shutdownNow();
        assertState(State.SHUTDOWN, State.TERMINATED);
    }

    @Test
    public void shutdownTerminated() throws InterruptedException {
        newContainer();
        startCache();
        assertState(State.RUNNING);
        c.shutdown();
        assertTrue(c.awaitState(State.TERMINATED, 10, TimeUnit.SECONDS));
        assertState(State.TERMINATED);
    }

    @Test
    public void shutdownNowTerminated() throws InterruptedException {
        newContainer();
        startCache();
        assertState(State.RUNNING);
        c.shutdownNow();
        assertTrue(c.awaitState(State.TERMINATED, 10, TimeUnit.SECONDS));
        assertState(State.TERMINATED);
    }
}
