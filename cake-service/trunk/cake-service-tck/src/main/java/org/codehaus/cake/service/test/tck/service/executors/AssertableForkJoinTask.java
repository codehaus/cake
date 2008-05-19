package org.codehaus.cake.service.test.tck.service.executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.forkjoin.RecursiveAction;

public class AssertableForkJoinTask extends RecursiveAction {
    private final CountDownLatch cdl;

    public AssertableForkJoinTask() {
        this(1);
    }

    public AssertableForkJoinTask(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException();
        }
        this.cdl = new CountDownLatch(count);
    }

    public synchronized void compute() {
        long c = cdl.getCount();
        if (c <= 0) {
            System.err.println("Count was " + c);
        }
        assertTrue("Count was " + c, c > 0);
        cdl.countDown();
    }

    public void assertDone() {
        assertEquals(0, cdl.getCount());
    }

    public void awaitAndAssertDone() throws InterruptedException {
        cdl.await(10, TimeUnit.SECONDS);
        assertDone();
    }
}
