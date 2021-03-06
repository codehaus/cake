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
package org.codehaus.cake.internal.service.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.forkjoin.ForkJoinExecutor;
import org.codehaus.cake.forkjoin.ForkJoinPool;
import org.codehaus.cake.service.Stoppable;
import org.codehaus.cake.service.executor.ExecutorsManager;

/**
 * This class is responsible for creating instances of {@link ExecutorService}, {@link ScheduledExecutorService} and
 * {@link ForkJoinExecutor}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheWorkerManager.java 479 2007-11-27 13:40:08Z kasper $
 */
public class DefaultExecutorsManager extends ExecutorsManager {

    /** Default executor service. */
    private volatile ExecutorService defaultExecutorService;
    /** Default fork join executor. */
    private volatile ForkJoinPool defaultForkJoinExecutor;
    /** Default scheduled executor service. */
    private volatile ScheduledExecutorService defaultScheduledExecutorService;
    /** Whether or not this service has been shutdown. */
    private boolean isShutdown;
    /** Lock for on-demand initialization of executors */
    private final Object poolLock = new Object();

    /**
     * Check that this service hasn't been shutdown. Only called when guarded by poolLock.
     */
    private void checkState() {
        if (isShutdown) {
            throw new IllegalStateException("This service has been shutdown");
        }
    }

    ExecutorService defaultExecutorService() {
        ExecutorService s = defaultExecutorService;
        if (s == null) {
            synchronized (poolLock) {
                checkState();
                s = defaultExecutorService;
                if (s == null) {
                    defaultExecutorService = s = Executors.newCachedThreadPool();
                }
            }
        }
        return s;
    }

    ForkJoinExecutor defaultForkJoinExecutor() {
        ForkJoinPool p = defaultForkJoinExecutor; // double-check
        if (p == null) {
            synchronized (poolLock) {
                checkState();
                p = defaultForkJoinExecutor;
                if (p == null) {
                    // use ceil(7/8 * ncpus)
                    int nprocs = Runtime.getRuntime().availableProcessors();
                    int nthreads = nprocs - (nprocs >>> 3);
                    defaultForkJoinExecutor = p = new ForkJoinPool(nthreads);
                }
            }
        }
        return p;
    }

    ScheduledExecutorService defaultScheduledExecutorService() {
        ScheduledExecutorService s = defaultScheduledExecutorService;
        if (s == null) {
            synchronized (poolLock) {
                checkState();
                s = defaultScheduledExecutorService;
                if (s == null) {
                    defaultScheduledExecutorService = s = Executors.newSingleThreadScheduledExecutor();
                    return s;
                }
            }
        }
        return s;
    }

    /**
     * Returns a {@link ExecutorService} that can be used to asynchronously execute tasks for the specified service.
     * 
     * @param service
     *            the service for which an ExecutorService should be returned
     * @param attributes
     *            a map of attributes that is passed to the concrete implementation of the executor manager
     * @return a ExecutorService that can be used to asynchronously execute tasks for the specified service
     */
    public ExecutorService getExecutorService(Object service, AttributeMap attributes) {
        return defaultExecutorService();
    }

    /**
     * Returns a {@link ForkJoinExecutor} that can be used to asynchronously execute tasks for the specified service.
     * 
     * @param service
     *            the service for which an ForkJoinExecutor should be returned
     * @param attributes
     *            a map of attributes that is passed to the concrete implementation of the executor manager
     * @return a ForkJoinExecutor that can be used to asynchronously execute tasks for the specified service
     */
    public ForkJoinExecutor getForkJoinExecutor(Object service, AttributeMap attributes) {
        return defaultForkJoinExecutor();
    }

    /**
     * Returns a {@link ScheduledExecutorService} that can be used to asynchronously schedule tasks for the specified
     * service.
     * 
     * @param service
     *            the service for which an ScheduledExecutorService should be returned
     * @param attributes
     *            a map of attributes that is passed to the concrete implementation of the executor manager
     * @return a ScheduledExecutorService that can be used to asynchronously schedule tasks for the specified service
     */
    public ScheduledExecutorService getScheduledExecutorService(Object service, AttributeMap attributes) {
        return defaultScheduledExecutorService();
    }

    @Stoppable
    public void shutdown() throws InterruptedException {
        synchronized (poolLock) {
            isShutdown = true;
            if (defaultExecutorService != null) {
                defaultExecutorService.shutdown();
            }
            if (defaultScheduledExecutorService != null) {
                defaultScheduledExecutorService.shutdown();
            }
            if (defaultForkJoinExecutor != null) {
                defaultForkJoinExecutor.shutdown();
            }

            if (defaultExecutorService != null) {
                defaultExecutorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            }
            if (defaultScheduledExecutorService != null) {
                defaultScheduledExecutorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            }
            if (defaultForkJoinExecutor != null) {
                defaultForkJoinExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            }
        }
    }

    //
    // public void shutdownNow() {
    // synchronized (poolLock) {
    // isShutdown = true;
    // if (defaultExecutorService != null) {
    // defaultExecutorService.shutdownNow();
    // }
    // if (defaultScheduledExecutorService != null) {
    // defaultScheduledExecutorService.shutdownNow();
    // }
    // if (defaultForkJoinExecutor != null) {
    // defaultForkJoinExecutor.shutdownNow();
    // }
    // }
    // }

    // public static DefaultExecutorsManager from(ScheduledExecutorService ses) {
    // DefaultExecutorsManager em = new DefaultExecutorsManager();
    // synchronized (em.poolLock) {
    // em.defaultScheduledExecutorService = ses;
    // }
    // return em;
    // }
    //
    // public static DefaultExecutorsManager from(ExecutorService es) {
    // DefaultExecutorsManager em = new DefaultExecutorsManager();
    // synchronized (em.poolLock) {
    // em.defaultExecutorService = es;
    // }
    // return em;
    // }
    //
    // public static DefaultExecutorsManager from(ForkJoinPool e) {
    // DefaultExecutorsManager em = new DefaultExecutorsManager();
    // synchronized (em.poolLock) {
    // em.defaultForkJoinExecutor = e;
    // }
    // return em;
    // }
}
