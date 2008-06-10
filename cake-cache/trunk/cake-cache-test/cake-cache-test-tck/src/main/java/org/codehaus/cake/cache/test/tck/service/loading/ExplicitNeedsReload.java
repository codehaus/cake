package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.cache.test.tck.service.loading.LoadingSuite.VolatilePredicate;
import org.junit.Before;
import org.junit.Test;

public class ExplicitNeedsReload extends AbstractCacheTCKTest {

    @Test
    public void withLoad() {
        VolatilePredicate predicate = new VolatilePredicate();
        conf.withLoading().setNeedsReloadFilter(predicate);
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
        VolatilePredicate predicate = new VolatilePredicate();
        conf.withLoading().setNeedsReloadFilter(predicate);

        assertPeek(entry(M1, null));
        load(M1);
        load(M2);
        load(M3);
        load(entry(M1, null));
        load(entry(M3, null));
        assertLoads(3);
        withLoading().loadAll();
        assertLoads(3);
        
        predicate.result = true;
        withLoading().loadAll();
        awaitFinishedThreads();
        assertLoads(5);
        withLoading().loadAll();
        awaitFinishedThreads();
        assertLoads(7);
        
        predicate.result = false;
        withLoading().loadAll();
        awaitFinishedThreads();
        assertLoads(7);
    }

    // TODO withKeys, All
}
