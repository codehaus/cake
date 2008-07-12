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
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.forkjoin.ForkJoinExecutor;
import org.codehaus.cake.service.executor.ExecutorsConfiguration;
import org.codehaus.cake.service.executor.ExecutorsManager;
import org.codehaus.cake.service.test.tck.RequireService;
import org.codehaus.cake.service.test.tck.UnsupportedServices;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
@RequireService(value = { ExecutorService.class })
@Ignore
public class ExecutorsForkJoin extends AbstractExecutorsTckTest {

    Mockery context = new JUnit4Mockery();

    AssertableForkJoinTask ar;
    AssertableForkJoinTask ar1;
    AssertableForkJoinTask ar2;

    @Before
    public void setup() {
        ar = new AssertableForkJoinTask();
        ar1 = new AssertableForkJoinTask();
        ar2 = new AssertableForkJoinTask();
    }

    @Test
    public void executeWithDefault() throws Exception {
        newConfigurationClean();
        newContainer();
        c.getService(ForkJoinExecutor.class).execute(ar);
        ar.awaitAndAssertDone();
//        ar1.awaitAndAssertDone();
//        ar2.awaitAndAssertDone();
        shutdownAndAwaitTermination();
    }

    @Test
    public void executeWithCustom() throws Exception {
        final AttributeMap am = dummy(AttributeMap.class);
        final AtomicInteger step = new AtomicInteger();
        final ForkJoinExecutor ses = context.mock(ForkJoinExecutor.class);
        context.checking(new Expectations() {
            {
                one(ses).execute(ar);
//                one(ses).execute(ar1);
//                one(ses).execute(ar2);
            }
        });

        withConf(ExecutorsConfiguration.class).setExecutorManager(new ExecutorsManager() {
            public ForkJoinExecutor getForkJoinExecutor(Object service, AttributeMap attributes) {
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
        c.getService(ForkJoinExecutor.class).execute(ar);
//        withExecutors().getForkJoinExecutor(1).execute(ar1);
        //withExecutors().getForkJoinExecutor(2, am).execute(ar2);
        assertEquals(1 /* 3 */, step.get());
        shutdownAndAwaitTermination();
    }

    @Test(expected = IllegalStateException.class)
    public void shutdown() {
        newConfigurationClean();
        newContainer();
        
        ForkJoinExecutor e = c.getService(ForkJoinExecutor.class);
        shutdownAndAwaitTermination();
        e.execute(ar1);
    }
}
