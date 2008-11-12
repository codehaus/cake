package org.codehaus.cake.cache.test.tck.selection;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Predicates;
import org.junit.Test;

public class SelectedCache extends AbstractCacheTCKTest {

    /** Tests that we don't load an item that is filtered */
    @Test
    public void noLoading() {
        c.put(1, "foo");
        assertEquals("foo", c.get(1));
        c.select().on(Predicates.FALSE).get(1);
        assertEquals("foo", c.get(1));
    }

    /** Tests that we don't load an item that is filtered */
    @Test
    public void loading() {
        c.remove(1);
        assertNull(c.peek(1));
        assertEquals("B", c.get(2));
        assertNull(c.peek(10));
        
        assertNull(c.select().on(Predicates.FALSE).get(1));
        assertNull(c.select().on(Predicates.FALSE).get(2));
        assertNull(c.select().on(Predicates.FALSE).get(10));

        assertEquals("A", c.get(1));
        assertEquals("B", c.get(2));
        assertNull(c.get(10));
    }

    @Test
    public void test() {
        for (SelectedCacheType sc : SelectedCacheType.values()) {
            newConfiguration().withAttributes().add(sc.getAttribute().toArray(new Attribute[0]));
            c = (Cache) sc.fillAndSelect((Cache) newCache());
            test2();
        }
    }

    private void test2() {
        assertSize(3);
        assertNotNull(c.getName());
        assertContainsKey(M1).assertContainsKey(M3).assertContainsKey(M5);
        assertNotContainsKey(M2).assertNotContainsKey(M4);
        assertContainsValue(M1).assertContainsValue(M3).assertContainsValue(M5);
        assertNotContainsValue(M2).assertNotContainsValue(M4);
    }

    @Test
    public void clear_() {
        for (SelectedCacheType sc : SelectedCacheType.values()) {
            newConfiguration().withAttributes().add(sc.getAttribute().toArray(new Attribute[0]));
            Cache orig = newCache();
            c = (Cache) sc.fillAndSelect((Cache) c);
            int initSize = orig.size();
            clear();
            assertSize(0);
            assertEquals(initSize - 3, orig.size());
        }
    }
}
