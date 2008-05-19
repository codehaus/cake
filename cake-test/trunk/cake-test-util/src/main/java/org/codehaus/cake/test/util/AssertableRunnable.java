package org.codehaus.cake.test.util;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AssertableRunnable implements Runnable {
    private final CountDownLatch cdl;

    public AssertableRunnable() {
        this(1);
    }

    public AssertableRunnable(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException();
        }
        this.cdl = new CountDownLatch(count);
    }

    public synchronized void run() {
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
