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

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.annotation.OnShutdown;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.After;
import org.junit.Test;

public class LifecycleStoppable extends AbstractTCKTest<Container, ContainerConfiguration<?>> {
    private CountDownLatch latch = new CountDownLatch(0);

    @After
    public void after() {
        assertEquals(0, latch.getCount());
    }

    @Test
    public void noArg() {
        latch = new CountDownLatch(1);
        conf.addToLifecycle(new Stoppable1());
        newContainer();
        assertFalse(c.isStarted());
        assertFalse(c.isShutdown());
        assertFalse(c.isTerminated());
        prestart();
        assertTrue(c.isStarted());
        assertFalse(c.isShutdown());
        assertFalse(c.isTerminated());
        c.shutdown();
        assertTrue(c.isStarted());
        assertTrue(c.isShutdown());
        awaitTermination();
        assertTrue(c.isTerminated());
    }

    public class Stoppable1 {
        @OnShutdown
        public void stop() {
            latch.countDown();
        }
    }

}
