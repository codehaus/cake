package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Ops.Predicate;
import org.codehaus.cake.service.AfterStart;
import org.junit.Test;

public class LoadingLifecycle extends AbstractCacheTCKTest {

    @Test
    public void startedLoader() {
        TestLoader loader = new TestLoader();
        conf.withLoading().setLoader(loader);
        init().prestart();
        assertEquals(1, loader.count);
    }

    @Test
    public void startedPredicate() {
        TestPredicate loader = new TestPredicate();
        conf.withLoading().setNeedsReloadFilter(loader);
        init().prestart();
        assertEquals(1, loader.count);
    }

    public static class TestLoader implements SimpleCacheLoader {

        volatile int count;

        public Object load(Object key, AttributeMap attributes) throws Exception {
            return "foo";
        }

        @AfterStart
        public void started() {
            count++;
        }
    }

    public class TestPredicate implements Predicate {

        volatile int count;

        public boolean op(Object a) {
            return true;
        }

        @AfterStart
        public void started(Cache cache) {
            count++;
            assertSame(c, cache);
        }
    }
}
