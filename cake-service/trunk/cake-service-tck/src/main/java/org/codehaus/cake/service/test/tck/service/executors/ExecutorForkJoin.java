package org.codehaus.cake.service.test.tck.service.executors;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import org.codehaus.cake.forkjoin.ForkJoinExecutor;
import org.codehaus.cake.service.test.tck.RequireService;
import org.codehaus.cake.service.test.util.AssertableForkJoinTask;
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

        assertTrue(c.hasService(ForkJoinExecutor.class));
        assertTrue(c.serviceKeySet().contains(ForkJoinExecutor.class));
        assertNotNull(c.getService(ForkJoinExecutor.class));
        c.getService(ForkJoinExecutor.class).execute(ar);
        c.getService(ForkJoinExecutor.class).execute(ar1);
        ar.awaitAndAssertDone();
        ar.awaitAndAssertDone();

        shutdownAndAwaitTermination();
    }

    @Test
    public void replaceDefault() {
        newConfigurationClean();
        newContainer();
        final ForkJoinExecutor e = dummy(ForkJoinExecutor.class);
        conf.addToLifecycleAndExport(ForkJoinExecutor.class, e);

        assertTrue(c.hasService(ForkJoinExecutor.class));

        ForkJoinExecutor fje = getService(ForkJoinExecutor.class);
        assertSame(e, fje);

    }

    /** Tests that an exception is throw when we attempt to execute a task on executor from a shutdown container. */
    @Test(expected = RejectedExecutionException.class)
    public void shutdown() {
        newConfigurationClean();
        newContainer();

        ForkJoinExecutor e = getService(ForkJoinExecutor.class);
        shutdownAndAwaitTermination();
        e.execute(ar);
    }

    /** Tests that an exception is throw when we attempt to execute a task on executor from a shutdown container. */
    @Test(expected = UnsupportedOperationException.class)
    public void shutdown2() {
        newConfigurationClean();
        newContainer();
        shutdownAndAwaitTermination();
        getService(ForkJoinExecutor.class);
    }
}
