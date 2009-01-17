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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.concurrent.RecursiveAction;

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
        if (c < 0) {
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
