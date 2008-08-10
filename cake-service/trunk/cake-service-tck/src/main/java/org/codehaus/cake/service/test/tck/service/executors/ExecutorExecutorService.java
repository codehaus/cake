package org.codehaus.cake.service.test.tck.service.executors;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import org.codehaus.cake.service.test.tck.RequireService;
import org.codehaus.cake.test.util.AssertableRunnable;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@RequireService(value = { ExecutorService.class })
public class ExecutorExecutorService extends AbstractExecutorsTckTest {
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

        assertTrue(c.hasService(ExecutorService.class));
        assertTrue(c.serviceKeySet().contains(ExecutorService.class));
        assertNotNull(c.getService(ExecutorService.class));
        c.getService(ExecutorService.class).execute(ar);
        c.getService(ExecutorService.class).execute(ar1);
        
        ar.awaitAndAssertDone();
        ar1.awaitAndAssertDone();
        shutdownAndAwaitTermination();
    }

    @Test @Ignore
    public void replaceDefault() {
        newConfigurationClean();
        newContainer();
        final ExecutorService e = dummy(ExecutorService.class);
        conf.addService(ExecutorService.class, e);

        assertTrue(c.hasService(ExecutorService.class));

        ExecutorService fje = getService(ExecutorService.class);
        assertSame(e, fje);

    }

    /** Tests that an exception is throw when we attempt to execute a task on executor from a shutdown container. */
    @Test(expected = RejectedExecutionException.class) @Ignore
    public void shutdown() {
        newConfigurationClean();
        newContainer();

        ExecutorService e = getService(ExecutorService.class);
        shutdownAndAwaitTermination();
        e.execute(ar);
    }
}
