package org.codehaus.cake.cache.test.tck.query;

import static org.codehaus.cake.cache.test.util.AtrStubs.I_2;
import static org.codehaus.cake.cache.test.util.AtrStubs.L_2;
import static org.codehaus.cake.cache.test.util.AtrStubs.L_3;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.query.CacheQuery;
import org.codehaus.cake.ops.Ops.Predicate;
import org.junit.Test;

public class SimpleQuery extends AbstractQueryTest {

    @Test
    public void empty() {
        assertNotNull(c.query());
        assertTrue(c.query().asList().isEmpty());
        assertTrue(c.query().map().asMap().isEmpty());
        assertFalse(c.query().iterator().hasNext());

        Cache<Integer, String> empty = newCache();
        c.query().putInto(empty);
        assertTrue(empty.isEmpty());

        assertTrue(empty.isEmpty());

        assertTrue(c.query().keys().asList().isEmpty());
        assertTrue(c.query().setLimit(5).asList().isEmpty());
        assertTrue(c.query().values().asList().isEmpty());
    }

    @Test
    public void simple() {
        put(10);
        assertQueryNoOrder(c, c.query());
        c.clear();
        put(3);
        assertQueryNoOrder(c, c.query());
    }

    @Test
    public void reuseQuery() {
        CacheQuery<Integer, String> q = c.query();
        put(3);
        assertQueryNoOrder(c, q);
        put(6);
        assertQueryNoOrder(c, q);
    }

    @Test
    public void filteredCache() {
        put(10);
        Cache<Integer, String> f = c.filter().onKey(new Predicate<Integer>() {
            public boolean op(Integer a) {
                return a.intValue() % 2 == 0;
            }
        });
        assertEquals(5, f.size());
        assertQueryNoOrder(f, f.query());
    }

    @Test
    public void limit() {
        put(10);
        assertQueryNoOrder(c, c.query().setLimit(5), 5);
        assertQueryNoOrder(c, c.query().setLimit(4), 4);
        assertQueryNoOrder(c, c.query().setLimit(20), 20);
    }

    @Test
    public void filteredLimit() {
        put(40);
        Cache<Integer, String> f = c.filter().onKey(new Predicate<Integer>() {
            public boolean op(Integer a) {
                return a.intValue() % 5 == 0;
            }
        });
        assertEquals(8, f.size());
        assertQueryNoOrder(f, f.query());
        assertQueryNoOrder(c, c.query().setLimit(7), 7);
        assertQueryNoOrder(c, c.query().setLimit(8), 8);
        assertQueryNoOrder(c, c.query().setLimit(9), 9);
    }

    @Test
    public void attributesPutIntoCache() {
        conf.addEntryAttributes(I_2, L_2);
        newCache();
        Cache<Integer, String> to = c;

        newConfiguration();
        conf.addEntryAttributes(I_2, L_3);
        newCache();
        put(M1, I_2, 5, L_3, 3L);
        put(M2, I_2, 2, L_3, 1L);
        put(M3, I_2, 3, L_3, 4L);
        put(M4, I_2, 4, L_3, 9L);

        c.query().putInto(to);
        assertEquals(5, to.getEntry(1).get(I_2));
        assertEquals(3L, to.getEntry(1).get(L_3));
        assertEquals(2L, to.getEntry(1).get(L_2));

        assertEquals(2, to.getEntry(2).get(I_2));
        assertEquals(3, to.getEntry(3).get(I_2));
        assertEquals(4, to.getEntry(4).get(I_2));

        Cache<Integer, String> f = c.filter().onKey(new Predicate<Integer>() {
            public boolean op(Integer a) {
                return a.intValue() % 2 == 0;
            }
        });
        to.clear();
        f.query().putInto(to);
        assertEquals(2, to.size());
        assertEquals(2, to.getEntry(2).get(I_2));
        assertEquals(2, to.getEntry(3).get(I_2));
        assertEquals(4, to.getEntry(4).get(I_2));

    }

}
