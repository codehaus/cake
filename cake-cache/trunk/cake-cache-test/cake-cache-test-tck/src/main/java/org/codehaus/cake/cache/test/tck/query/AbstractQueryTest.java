package org.codehaus.cake.cache.test.tck.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.query.CacheQuery;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;

public class AbstractQueryTest extends AbstractCacheTCKTest {

    public void assertQueryNoOrder(Iterable<CacheEntry<Integer, String>> entries, CacheQuery<Integer, String> query) {
        List<CacheEntry<Integer, String>> l = toList(entries);
        assertEquals(new HashSet(l), new HashSet(query.all()));
        assertEquals(from(entries), query.entries());
        assertEquals(from(entries).keySet(), query.entries().keySet());
        assertEquals(from(entries).keySet(), new HashSet(query.keys()));
        assertEquals(new HashSet(from(entries).values()), new HashSet(query.values()));
        assertEquals(new HashSet(l), new HashSet(toList(query)));
        Cache<Integer, String> old = c;
        newCache();
        query.putInto(c);
        assertEquals(new HashSet(toList(c)), new HashSet(l));
        c = old;
    }

    public void assertQueryNoOrder(Iterable<CacheEntry<Integer, String>> entries, CacheQuery<Integer, String> query,
            int limit) {
        Set<CacheEntry<Integer, String>> l = new HashSet<CacheEntry<Integer, String>>(toList(entries));
        Map<Integer, String> m = from(entries);
        if (l.size() > limit) {
            assertEquals(limit, query.all().size());
            assertTrue(l.containsAll(query.all()));

            assertEquals(limit, query.entries().size());
            assertTrue(from(entries).entrySet().containsAll(query.entries().entrySet()));

            assertEquals(limit, query.keys().size());
            assertTrue(from(entries).keySet().containsAll(query.keys()));
            
            assertEquals(limit, query.values().size());
            assertTrue(from(entries).values().containsAll(query.values()));

            assertEquals(limit, new HashSet(toList(query)).size());
            assertTrue(new HashSet(l).containsAll(new HashSet(toList(query))));

            Cache<Integer, String> old = c;
            newCache();
            query.putInto(c);
            assertEquals(limit, c.size());
            for (Map.Entry<Integer, String> e : c.entrySet()) {
                assertEquals(m.get(e.getKey()), e.getValue());
            }
            c = old;
        } else {
            assertQueryNoOrder(entries, query);
        }

        // assertEquals(new HashSet(l), new HashSet(query.all()));
        // assertEquals(new HashSet(l), new HashSet(query.all()));
        // assertEquals(from(entries), query.entries());
        // assertEquals(from(entries).keySet(), query.entries().keySet());
        // assertEquals(from(entries).keySet(), query.keys());
        // assertEquals(new HashSet(from(entries).values()), new HashSet(query.values()));
        // assertEquals(new HashSet(l), new HashSet(toList(query)));
        // Cache<Integer, String> old = c;
        // newCache();
        // query.putInto(c);
        // assertEquals(new HashSet(toList(c)), new HashSet(l));
        // c = old;
    }

    public void assertQueryOrdered(Iterable<CacheEntry<Integer, String>> entries, CacheQuery<Integer, String> query) {
        List<CacheEntry<Integer, String>> l = toList(entries);
        assertEquals(new HashSet(l), new HashSet(query.all()));
        assertEquals(from(entries), query.entries());
        assertEquals(from(entries).keySet(), query.entries().keySet());
        assertEquals(from(entries).keySet(), query.keys());
        assertEquals(new HashSet(from(entries).values()), new HashSet(query.values()));
        assertEquals(new HashSet(l), new HashSet(toList(query)));
        Cache<Integer, String> old = c;
        newCache();
        query.putInto(c);
        assertEquals(new HashSet(toList(c)), new HashSet(l));
        c = old;
    }

    private Map<Integer, String> from(Iterable<CacheEntry<Integer, String>> iter) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
        for (CacheEntry<Integer, String> e : iter) {
            map.put(e.getKey(), e.getValue());
        }
        return map;
    }

    private <T> List<T> toList(Iterable<T> iter) {
        ArrayList<T> list = new ArrayList<T>();
        for (T t : iter) {
            list.add(t);
        }
        return list;
    }
}
