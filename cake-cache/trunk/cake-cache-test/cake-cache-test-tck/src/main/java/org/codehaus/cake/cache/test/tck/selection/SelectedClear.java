package org.codehaus.cake.cache.test.tck.selection;

import java.util.Set;
import java.util.TreeSet;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Ops.Predicate;
import org.junit.Test;

public class SelectedClear extends AbstractCacheTCKTest {

    @Test
    public void clear_() {
        int step = 1000;
        Set<Integer> keyset = new TreeSet<Integer>();
        for (int i = 0; i < step; i++) {
            c.put(i * 100, "" + i);
            if (i % 3 != 0) {
                keyset.add(i * 100);
            }
        }
        Cache<Integer, String> selected = c.select().onValue(new Predicate<String>() {
            public boolean op(String a) {
                return (Integer.parseInt(a)) % 3 == 0;
            }
        });
        assertEquals(step / 3 + 1, selected.size());
        selected.clear();
        assertSize(keyset.size());
        assertEquals(keyset, new TreeSet(c.keySet()));
    }
}
