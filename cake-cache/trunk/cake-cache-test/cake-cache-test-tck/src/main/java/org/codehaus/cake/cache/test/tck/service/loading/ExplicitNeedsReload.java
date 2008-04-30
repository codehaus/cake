package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Ops.Predicate;
import org.junit.Before;
import org.junit.Test;

public class ExplicitNeedsReload extends AbstractCacheTCKTest {

    VolatilePredicate predicate;

    @Before
    public void setup() {
        predicate = new VolatilePredicate();
        conf.withLoading().setNeedsReloadFilter(predicate);
    }

    @Test
    public void withLoad() {
        assertPeek(entry(M1, null));
        load(M1);
        load(entry(M1, null));
        assertPeek(M1);
        assertLoads(1);

        predicate.result = true;
        load(M1).assertLoads(2);
        load(M1).assertLoads(3);

        assertEquals(M1.getKey(), predicate.latest.getKey());
        assertEquals(M1.getValue(), predicate.latest.getValue());
        predicate.result = false;
        load(entry(M1, null)).assertLoads(3);
    }

    @Test
    public void withLoadAll() {
        assertPeek(entry(M1, null));
        load(M1);
        load(M2);
        load(M3);
        load(entry(M1, null));
        load(entry(M3, null));
        assertLoads(3);
        withLoading().withAll().load();
        assertLoads(3);
        
        predicate.result = true;
        withLoading().withAll().load();
        assertLoads(5);
        withLoading().withAll().load();
        assertLoads(7);
        
        predicate.result = false;
        withLoading().withAll().load();
        assertLoads(7);
    }

    static class VolatilePredicate implements Predicate<CacheEntry<Integer, String>> {
        volatile boolean result;
        CacheEntry<Integer, String> latest;

        public boolean op(CacheEntry<Integer, String> a) {
            latest = a;
            return result && (a.getKey() % 2 == 1);
        }
    }

    // TODO withKeys, All
}
