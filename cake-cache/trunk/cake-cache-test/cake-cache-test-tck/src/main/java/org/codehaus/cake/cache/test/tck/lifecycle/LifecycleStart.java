package org.codehaus.cake.cache.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.attribute.CacheAttributeConfiguration;
import org.codehaus.cake.cache.exceptionhandling.CacheExceptionHandlingConfiguration;
import org.codehaus.cake.cache.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.cache.store.CacheStoreConfiguration;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.executor.ExecutorsConfiguration;
import org.codehaus.cake.service.management.ManagementConfiguration;
import org.junit.After;
import org.junit.Test;

public class LifecycleStart extends AbstractCacheTCKTest {
    int countdown;
    private CountDownLatch latch = new CountDownLatch(0);

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
    public void allArgs() {
        latch = new CountDownLatch(3);
        conf.addService(new Started3());
        init();
        prestart();
    }

    @Test(expected = IllegalStateException.class)
    public void unknownConfiguration() {
        conf.addService(new Started4());
        init();
        prestart();
    }

    @Test
    public void userConf() {
        conf = new TestConfiguration();
        conf.addService(new Started4());
        init();
        prestart();
    }

    public class Started1 {
        @Startable
        public void start() {
            latch.countDown();
        }
    }

    public class Started2 {
        @Startable
        public void start1() {
            latch.countDown();
        }

        @Startable
        public void start2() {
            latch.countDown();
        }
    }

    public class Started3 {
        @Startable
        public void start(CacheExceptionHandlingConfiguration a1, CacheAttributeConfiguration a2,
                CacheLoadingConfiguration a3, ManagementConfiguration a4, MemoryStoreConfiguration a5,
                ExecutorsConfiguration a6, CacheStoreConfiguration a7) {
            latch.countDown();
            assertSame(a1, conf.withExceptionHandling());
            assertSame(a2, conf.withAttributes());
            assertSame(a3, conf.withLoading());
            assertSame(a4, conf.withManagement());
            assertSame(a5, conf.withMemoryStore());
            assertSame(a6, conf.withExecutors());
            assertSame(a7, conf.withStore());
        }

        @Startable
        public void start(CacheConfiguration configuration) {
            assertSame(conf, configuration);
            latch.countDown();
        }

        @Startable
        public void start(ServiceRegistrant registrant) {
            assertNotNull(registrant);
            latch.countDown();
        }
    }

    public class Started4 {
        @Startable
        public void start(CacheConfiguration configuration) {
            assertSame(conf, configuration);
            latch.countDown();
        }

        @Startable
        public void start(TestFoo registrant) {
            assertNotNull(registrant);
            latch.countDown();
        }
    }

    static class TestConfiguration extends CacheConfiguration<Integer, String> {
        public TestConfiguration() {
            addConfiguration(new TestFoo());
        }

        TestFoo withFoo() {
            return getConfigurationOfType(TestFoo.class);
        }
    }

    public static class TestFoo {}
}
