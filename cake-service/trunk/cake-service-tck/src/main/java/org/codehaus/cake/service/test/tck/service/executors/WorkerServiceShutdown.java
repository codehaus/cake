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

import java.security.Permission;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.service.test.tck.RequireService;
import org.junit.Ignore;
import org.junit.Test;

@RequireService( { ExecutorService.class })
public class WorkerServiceShutdown extends AbstractExecutorsTckTest {

    @Ignore
    @Test
    public void tt() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        newConfigurationClean();
        newContainer();

        ScheduledExecutorService ses = c.getService(ScheduledExecutorService.class);
        ses.execute(new Runnable() {
            public void run() {
                for (;;) {
                    try {
                        new CountDownLatch(1).await(Long.MAX_VALUE, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        latch.countDown();
                        return;
                    }
                }
            }
        });
        c.shutdown();
        assertEquals(1, latch.getCount());
        c.shutdownNow();
        assertTrue(latch.await(1, TimeUnit.SECONDS));
    }

    /**
     * Tests that newly created threads with the default workerservice and a security manager set. Creates threads in
     * the threadgroup of the security manager
     */
    @Test
    public void securityGroupScheduledExecutor() {
        final ThreadGroup tg = new ThreadGroup("myGroup");
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkPermission(Permission perm) {}

            @Override
            public ThreadGroup getThreadGroup() {
                return tg;
            }
        });
        try {
            newConfigurationClean();
            newContainer();

            // manager
            c.getService(ScheduledExecutorService.class).execute(new Runnable() {
                public void run() {
                    if (tg != Thread.currentThread().getThreadGroup()) {
                        doFail("wrong threadgroup");
                    }
                }
            });
        } finally {
            System.setSecurityManager(null);
        }
        shutdownAndAwaitTermination();
    }

    /**
     * Tests that newly created threads with the default workerservice and a security manager set. Creates threads in
     * the threadgroup of the security manager
     * 
     * @throws InterruptedException
     */
    @Test
    public void threadDeamonPriority() throws InterruptedException {
        newConfigurationClean();
        newContainer();

        // manager
        Thread t = new Thread(new Runnable() {
            public void run() {
                c.getService(ScheduledExecutorService.class).execute(new Runnable() {
                    public void run() {
                        if (Thread.currentThread().isDaemon()) {
                            doFail("should not be deamon");
                        }
                        if (Thread.currentThread().getPriority() != Thread.NORM_PRIORITY) {
                            doFail("wrong priority");
                        }
                    }
                });
            }
        });
        t.setDaemon(true);
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        t.join();
        shutdownAndAwaitTermination();
    }
}
