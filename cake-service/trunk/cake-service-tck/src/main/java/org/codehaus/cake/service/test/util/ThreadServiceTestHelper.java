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
package org.codehaus.cake.service.test.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.annotation.ExportAsService;
@ExportAsService(ExecutorService.class)
public class ThreadServiceTestHelper implements ServiceFactory<ExecutorService> {

    private static final int PERMITS = 100000;

    private Semaphore awaits = new Semaphore(PERMITS);

    public void awaitAllIdle() {
        awaits.acquireUninterruptibly(PERMITS);
        awaits.release(PERMITS);
    }

    private class MyRunnable implements Runnable {
        private final Runnable r;

        private final Exception calledFrom;

        public MyRunnable(Runnable r) {
            this.r = r;
            this.calledFrom = new Exception();
        }

        public void run() {
            try {
                r.run();
                if (r instanceof Future) {
                    Future ft = ((FutureTask) r);
                    if (ft.isDone()) {
                        try {
                            ft.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            calledFrom.printStackTrace();
                            e.printStackTrace();
                        }
                    }
                }
            }finally {
                awaits.release();
            }
        }
    }

    public ExecutorService lookup(
            org.codehaus.cake.service.ServiceFactory.ServiceFactoryContext<ExecutorService> context) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>())
        {
            @Override
            public void execute(Runnable command) {
                awaits.acquireUninterruptibly();
                super.execute(new MyRunnable(command));
            }
        };
    }

}
