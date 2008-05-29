package org.codehaus.cake.cache.service.loading;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Predicate;
import org.junit.Test;

public class CacheLoadingConfigurationTest {

    @Test
    public void filter() {
        CacheLoadingConfiguration<Integer, String> c = new CacheLoadingConfiguration<Integer, String>();
        assertNull(c.getNeedsReloadFilter());
        Predicate<CacheEntry<Integer, String>> p = dummy(Predicate.class);

        assertSame(c, c.setNeedsReloadFilter(p));
        assertSame(p, c.getNeedsReloadFilter());

        Predicate<Object> p1 = dummy(Predicate.class);
        c.setNeedsReloadFilter(p1);
        Predicate<CacheEntry> p2 = dummy(Predicate.class);
        c.setNeedsReloadFilter(p2);

        Predicate<CacheEntry<Object, Object>> p3 = dummy(Predicate.class);
        // c.setNeedsReloadFilter(p3);

    }

    @Test
    public void loaderOp() {
        CacheLoadingConfiguration<Number, Number> c = new CacheLoadingConfiguration<Number, Number>();
        assertNull(c.getLoader());
        Op<Number, Number> op = dummy(Op.class);
        
        assertSame(c, c.setLoader(op));
        assertSame(op, c.getLoader());

        Op<Number, Integer> op1 = dummy(Op.class);
        c.setLoader(op1);
        
        Op<Object, Number> op2 = dummy(Op.class);
        c.setLoader(op2);
    }
    
    @Test
    public void loaderSimple() {
        CacheLoadingConfiguration<Number, Number> c = new CacheLoadingConfiguration<Number, Number>();
        assertNull(c.getLoader());
        SimpleCacheLoader<Number, Number> scl = dummy(SimpleCacheLoader.class);
        
        assertSame(c, c.setLoader(scl));
        assertSame(scl, c.getLoader());

        SimpleCacheLoader<Number, Integer> SimpleCacheLoader1 = dummy(SimpleCacheLoader.class);
        c.setLoader(SimpleCacheLoader1);
        
        SimpleCacheLoader<Object, Number> SimpleCacheLoader2 = dummy(SimpleCacheLoader.class);
        c.setLoader(SimpleCacheLoader2);
    }
}
