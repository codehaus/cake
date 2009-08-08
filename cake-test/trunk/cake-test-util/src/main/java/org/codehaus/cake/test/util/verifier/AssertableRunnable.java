/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.test.util.verifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AssertableRunnable implements Runnable, Verifier {
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

    public void awaitAndAssertDone() throws InterruptedException {
        cdl.await(10, TimeUnit.SECONDS);
        verify();
    }

    public void verify() {
        assertEquals(0, cdl.getCount());
    }
}
