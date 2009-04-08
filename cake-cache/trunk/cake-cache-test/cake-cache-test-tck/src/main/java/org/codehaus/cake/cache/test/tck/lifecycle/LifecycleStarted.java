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
package org.codehaus.cake.cache.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.service.AfterStart;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.test.util.throwables.Exception1;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.util.Logger.Level;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

public class LifecycleStarted extends AbstractCacheTCKTest {

    int countdown;
    private CountDownLatch latch = new CountDownLatch(1);

    @After
    public void after() {
        assertEquals(0, latch.getCount());
    }

    @Test
    public void noArg() {
        latch = new CountDownLatch(1);
        conf.addService(new Started1());
        init();
        prestart();
    }

    @Test
    public void twoMethod() {
        latch = new CountDownLatch(2);
        conf.addService(new Started2());
        init();
        prestart();
    }

    @Test
    public void cacheArg() {
        latch = new CountDownLatch(2);
        conf.addService(new Started8());
        init();
        prestart();
    }

    @Test @Ignore
    public void cacheAndServiceArg() {
        latch = new CountDownLatch(1);
        conf.addService(new Started4());
        init();
        prestart();
    }

    @Test(expected = IllegalStateException.class)
    public void noLoader() {
        newConfigurationClean();
        latch = new CountDownLatch(0);
        conf.addService(new Started5());
        init();
        prestart();
    }

    @Test
    @Ignore
    public void runtimeExceptionMethod() {
        try {
            latch = new CountDownLatch(0);
            conf.addService(new Started6());
            conf.withExceptionHandling().setExceptionHandler(exceptionHandler);
            init();
            prestart();
        } finally {
            exceptionHandler.eat(RuntimeException1.INSTANCE, Level.Error);
        }
    }

    @Test
    @Ignore
    public void exceptionMethod() {
        try {
            latch = new CountDownLatch(0);
            conf.addService(new Started7());
            conf.withExceptionHandling().setExceptionHandler(exceptionHandler);
            init();
            prestart();
        } finally {
            exceptionHandler.eat(Exception1.INSTANCE, Level.Error);
        }

    }

    public class Started1 {
        @AfterStart
        public void start() {
            latch.countDown();
        }
    }

    public class Started2 {
        @AfterStart
        public void start1() {
            latch.countDown();
        }

        @AfterStart
        public void start2() {
            latch.countDown();
        }
    }

    public class Started8 {
        @AfterStart
        public void start(Cache<?, ?> cache) {
            assertSame(c, cache);
            latch.countDown();
        }

        @AfterStart
        public void start(Container cache) {
            assertSame(c, cache);
            latch.countDown();
        }
    }

    public class Started4 {
        @AfterStart
        public void hullabulla(Cache<?, ?> cache) {
            assertSame(c, cache);
            assertSame(withMemoryStore(), cache.with().memoryStore());
            assertSame(withLoading(), cache.with().loading());
            latch.countDown();
        }
    }

    public class Started5 {
        @AfterStart
        public void hullabulla(Cache<?, ?> cache, CacheLoadingService<?, ?> ls) {
            fail("should not have been run");
        }
    }

    public class Started6 {
        @AfterStart
        public void hullabulla(Cache<?, ?> cache, CacheLoadingService<?, ?> ls) {
            throw RuntimeException1.INSTANCE;
        }
    }

    public class Started7 {
        @AfterStart
        public void hullabulla(Cache<?, ?> cache, CacheLoadingService<?, ?> ls) throws Exception {
            throw Exception1.INSTANCE;
        }
    }
}
