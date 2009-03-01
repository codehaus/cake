package org.codehaus.cake.service.test.tck.service.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import org.codehaus.cake.service.test.tck.RequireService;
import org.codehaus.cake.service.test.util.AssertableForkJoinTask;
import org.codehaus.cake.util.concurrent.ForkJoinPool;
import org.junit.Before;
import org.junit.Test;

@RequireService(value = { ExecutorService.class })
public class ExecutorForkJoin extends AbstractExecutorsTckTest {
    AssertableForkJoinTask ar;
    AssertableForkJoinTask ar1;

    @Before
    public void setup() {
        ar = new AssertableForkJoinTask();
        ar1 = new AssertableForkJoinTask();
    }

    /** Tests that a default ForkJoinExecutor is available. */
    @Test
    public void availableDefault() throws InterruptedException {
        newConfigurationClean();
        newContainer();

        assertTrue(c.hasService(ForkJoinPool.class));
        assertTrue(c.serviceKeySet().contains(ForkJoinPool.class));
        assertNotNull(c.getService(ForkJoinPool.class));
        c.getService(ForkJoinPool.class).execute(ar);
        c.getService(ForkJoinPool.class).execute(ar1);
        ar.awaitAndAssertDone();
        ar.awaitAndAssertDone();

        shutdownAndAwaitTermination();
    }

    @Test
    public void replaceDefault() {
        newConfigurationClean();
        newContainer();
        final ForkJoinPool e = new ForkJoinPool();
        conf.addService(ForkJoinPool.class, e);

        assertTrue(c.hasService(ForkJoinPool.class));

        ForkJoinPool fje = getService(ForkJoinPool.class);
        assertSame(e, fje);

    }

    /** Tests that an exception is throw when we attempt to execute a task on executor from a shutdown container. */
    @Test(expected = RejectedExecutionException.class)
    public void shutdown() {
        newConfigurationClean();
        newContainer();

        ForkJoinPool e = getService(ForkJoinPool.class);
        shutdownAndAwaitTermination();
        e.execute(ar);
    }

    /** Tests that an exception is throw when we attempt to execute a task on executor from a shutdown container. */
    @Test(expected = UnsupportedOperationException.class)
    public void shutdown2() {
        newConfigurationClean();
        newContainer();
        shutdownAndAwaitTermination();
        getService(ForkJoinPool.class);
    }
}
