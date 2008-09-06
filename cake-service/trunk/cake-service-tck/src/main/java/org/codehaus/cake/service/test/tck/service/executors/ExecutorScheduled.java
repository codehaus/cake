package org.codehaus.cake.service.test.tck.service.executors;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;

import org.codehaus.cake.service.test.tck.RequireService;
import org.codehaus.cake.test.util.AssertableRunnable;
import org.junit.Before;
import org.junit.Test;

@RequireService(value = { ExecutorService.class })
public class ExecutorScheduled extends AbstractExecutorsTckTest {
    AssertableRunnable ar;
    AssertableRunnable ar1;

    @Before
    public void setup() {
        ar = new AssertableRunnable();
        ar1 = new AssertableRunnable();
    }

    /** Tests that a default ForkJoinExecutor is available. */
    @Test
    public void availableDefault() throws InterruptedException {
        newConfigurationClean();
        newContainer();

        assertTrue(c.hasService(ScheduledExecutorService.class));
        assertTrue(c.serviceKeySet().contains(ScheduledExecutorService.class));
        assertNotNull(c.getService(ScheduledExecutorService.class));
        c.getService(ScheduledExecutorService.class).execute(ar);
        c.getService(ScheduledExecutorService.class).execute(ar1);
        ar.awaitAndAssertDone();
        ar1.awaitAndAssertDone();
        shutdownAndAwaitTermination();
    }

    @Test
    public void replaceDefault() {
        newConfigurationClean();
        newContainer();
        final ScheduledExecutorService e = dummy(ScheduledExecutorService.class);
        conf.addToLifecycleAndExport(ScheduledExecutorService.class, e);

        assertTrue(c.hasService(ScheduledExecutorService.class));

        ScheduledExecutorService fje = getService(ScheduledExecutorService.class);
        assertSame(e, fje);

    }

    /** Tests that an exception is throw when we attempt to execute a task on executor from a shutdown container. */
    @Test(expected = RejectedExecutionException.class)
    public void shutdown() {
        newConfigurationClean();
        newContainer();

        ScheduledExecutorService e = getService(ScheduledExecutorService.class);
        shutdownAndAwaitTermination();
        e.execute(ar);
    }
}
