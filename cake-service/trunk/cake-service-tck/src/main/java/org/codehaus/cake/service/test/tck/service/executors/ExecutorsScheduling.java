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
package org.codehaus.cake.service.test.tck.service.executors;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.service.executor.ExecutorsConfiguration;
import org.codehaus.cake.service.executor.ExecutorsManager;
import org.codehaus.cake.service.test.tck.RequireService;
import org.codehaus.cake.test.util.AssertableRunnable;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
@RequireService( { ExecutorService.class })
@Ignore
public class ExecutorsScheduling extends AbstractExecutorsTckTest {

    Mockery context = new JUnit4Mockery();

    AssertableRunnable ar;
    AssertableRunnable ar1;
    AssertableRunnable ar2;
    ScheduledExecutorService ses;

    @Before
    public void setup() {
        ar = new AssertableRunnable();
        ar1 = new AssertableRunnable();
        ar2 = new AssertableRunnable();
        ses = c.getService(ScheduledExecutorService.class);
    }

    @Test
    public void executeWithDefault() throws Exception {
        newConfigurationClean();
        newContainer();
        
        ses.execute(ar);
        ar.awaitAndAssertDone();
        shutdownAndAwaitTermination();
    }

    @Test
    public void executeWithCustom() throws Exception {
        final AttributeMap am = dummy(AttributeMap.class);
        final AtomicInteger step = new AtomicInteger();
        final ScheduledExecutorService ses = context.mock(ScheduledExecutorService.class);
        context.checking(new Expectations() {
            {
                one(ses).execute(ar);
//                one(ses).execute(ar1);
//                one(ses).execute(ar2);
            }
        });

        withConf(ExecutorsConfiguration.class).setExecutorManager(new ExecutorsManager() {
            public ScheduledExecutorService getScheduledExecutorService(Object service, AttributeMap attributes) {
                if (step.get() == 0) {
                    assertNull(service);
                    assertTrue(attributes.isEmpty());
//                } else if (step.get() == 1) {
//                    assertEquals(1, service);
//                    assertTrue(attributes.isEmpty());
//                } else if (step.get() == 2) {
//                    assertEquals(2, service);
//                    assertSame(am, attributes);
                } else {
                    fail("Unknown step");
                }
                step.incrementAndGet();
                return ses;
            }
        });
        newContainer();
        ses.execute(ar);
//        withExecutors().getScheduledExecutorService(1).execute(ar1);
//        withExecutors().getScheduledExecutorService(2, am).execute(ar2);
        assertEquals(1, step.get());
        shutdownAndAwaitTermination();
    }

    @Test
    public void executeScheduledWithDefault() throws Exception {
        newConfigurationClean();
        newContainer();
        ses.schedule(ar, 1, TimeUnit.MICROSECONDS);
        ar.awaitAndAssertDone();
        shutdownAndAwaitTermination();
    }

    @Test(expected = IllegalStateException.class)
    public void shutdown() {
        newConfigurationClean();
        newContainer();
        shutdownAndAwaitTermination();
        ses.execute(ar2);
    }
}
