package org.codehaus.cake.cache.test.tck.view;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.test.util.CollectionTestUtil;
import org.junit.Test;

public class CacheViewTest extends AbstractCacheTCKTest {
    static final List<String> STRING_F5 = CollectionTestUtil.asStringList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    static final List<String> STRING_B5 = CollectionTestUtil.asStringList(12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);

    @Test
    public void test() {
        newCache(5);
        ViewAsserts.assertViewEquals(c.view().keys(), Arrays.asList(1, 2, 3, 4, 5), Integer.class);
        ViewAsserts.assertViewEquals(c.view().keys(), c.keySet(), Integer.class);
    }

    public void testSetLimit() {

    }

    public void values() {
        newCache(15);
        assertEquals(new HashSet(c.view().values().toList()), new HashSet(STRING_F5));
        
        ViewAsserts.assertViewEquals(c.view().values(), Arrays.asList("A", "B", "C", "D", "E"), String.class);
        ViewAsserts.assertViewEquals(c.view().values(), c.values(), String.class);
        //c.view().orderByValuesMax()
    }

    public void size_() {
        newCache(5);

        //Size->from last to 
    }
}
