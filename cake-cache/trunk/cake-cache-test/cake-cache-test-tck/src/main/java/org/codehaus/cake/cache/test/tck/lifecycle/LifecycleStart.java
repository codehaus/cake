package org.codehaus.cake.cache.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.service.attribute.CacheAttributeConfiguration;
import org.codehaus.cake.cache.service.loading.CacheLoadingConfiguration;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreConfiguration;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.exceptionhandling.ExceptionHandlingConfiguration;
import org.codehaus.cake.service.executor.ExecutorsConfiguration;
import org.codehaus.cake.service.management.ManagementConfiguration;
import org.junit.After;
import org.junit.Test;

public class LifecycleStart extends AbstractCacheTCKTest {
    private CountDownLatch latch = new CountDownLatch(0);

    @After
    public void after() {
        assertEquals(0, latch.getCount());
    }

    @Test
    public void allConfigurations() {
        latch = new CountDownLatch(1);
        conf.addService(new AllConfigurations());
        init();
        prestart();
    }

    @Test(expected = IllegalStateException.class)
    public void unknownConfiguration() {
        conf.addService(new CustomConfiguration());
        init();
        prestart();
    }

    @Test
    public void customConfiguration() {
        latch = new CountDownLatch(1);
        conf = new TestConfiguration();
        conf.addService(new CustomConfiguration());
        init();
        prestart();
    }

    public class AllConfigurations {
        @Startable
        public void start(CacheConfiguration configuration, ExceptionHandlingConfiguration a1,
                CacheAttributeConfiguration a2, CacheLoadingConfiguration a3, ManagementConfiguration a4,
                MemoryStoreConfiguration a5, ExecutorsConfiguration a6/*, CacheStoreConfiguration a7*/) {
            latch.countDown();
            assertSame(conf, configuration);
            assertSame(a1, conf.withExceptionHandling());
            assertSame(a2, conf.withAttributes());
            assertSame(a3, conf.withLoading());
            assertSame(a4, conf.withManagement());
            assertSame(a5, conf.withMemoryStore());
            assertSame(a6, conf.withExecutors());
       //     assertSame(a7, conf.withStore());
        }
    }

    public class CustomConfiguration {
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
