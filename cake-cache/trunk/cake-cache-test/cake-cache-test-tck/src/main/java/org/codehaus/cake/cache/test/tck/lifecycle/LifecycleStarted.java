package org.codehaus.cake.cache.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.container.Container;
import org.codehaus.cake.container.lifecycle.Started;
import org.codehaus.cake.test.util.throwables.Exception1;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.util.Logger.Level;
import org.junit.After;
import org.junit.Test;

public class LifecycleStarted extends AbstractCacheTCKTest {

    int countdown;
    private CountDownLatch latch = new CountDownLatch(1);

    @After
    public void after() {
    // assertEquals(0, latch.getCount());
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
        conf.addService(new Started3());
        init();
        prestart();
    }

    @Test
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

    @Test(expected = RuntimeException1.class)
    public void runtimeExceptionMethod() {
        try {
            latch = new CountDownLatch(0);
            conf.addService(new Started6());
            conf.withExceptionHandling().setExceptionHandler(exceptionHandler);
            init();
            prestart();
        } finally {
            exceptionHandler.eat(RuntimeException1.INSTANCE, Level.Fatal);
        }
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionMethod() {
        try {
            latch = new CountDownLatch(0);
            conf.addService(new Started7());
            conf.withExceptionHandling().setExceptionHandler(exceptionHandler);
            init();
            prestart();
        } finally {
            exceptionHandler.eat(Exception1.INSTANCE, Level.Fatal);
        }

    }

    public class Started1 {
        @Started
        public void start() {
            latch.countDown();
        }
    }

    public class Started2 {
        @Started
        public void start1() {
            latch.countDown();
        }

        @Started
        public void start2() {
            latch.countDown();
        }
    }

    public class Started3 {
        @Started
        public void start(Cache cache) {
            assertSame(c, cache);
            latch.countDown();
        }

        @Started
        public void start(Container cache) {
            assertSame(c, cache);
            latch.countDown();
        }
    }

    public class Started4 {
        @Started
        public void hullabulla(Cache cache, MemoryStoreService ms, CacheLoadingService ls) {
            assertSame(c, cache);
            assertSame(withMemoryStore(), ms);
            assertSame(withLoading(), ls);
            latch.countDown();
        }
    }

    public class Started5 {
        @Started
        public void hullabulla(Cache cache, CacheLoadingService ls) {
            fail("should not have been run");
        }
    }

    public class Started6 {
        @Started
        public void hullabulla(Cache cache, CacheLoadingService ls) {
            throw RuntimeException1.INSTANCE;
        }
    }

    public class Started7 {
        @Started
        public void hullabulla(Cache cache, CacheLoadingService ls) throws Exception {
            throw Exception1.INSTANCE;
        }
    }
}
