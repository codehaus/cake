package org.codehaus.cake.cache.test.tck.crud;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.cache.test.tck.crud.CrudSuite.LazyOp;
import org.junit.Test;

public class WriterPutIfAbsent extends AbstractCacheTCKTest {

    private void fill() {
        int step = 1000;
        for (int i = 0; i < step; i++) {
            c.put(i * 100, "" + i);
        }
    }

    @Test
    public void putIfAbsentReturnVoid() {
        fill();
        for (int i = 1; i < 99; i++) {
            assertNull(c.withCrud().write().putIfAbsent(i, "" + i));
            assertEquals("" + i, c.get(i));
        }
        assertNull(c.withCrud().write().putIfAbsent(100, "value"));
        assertEquals("1", c.get(100));
    }

    @Test
    public void putIfAbsentAttributeReturnVoid() {
        fill();
        for (int i = 1; i < 99; i++) {
            assertNull(c.withCrud().write().putIfAbsent(i, "" + i));
            assertEquals("" + i, c.get(i));
        }
        assertNull(c.withCrud().write().putIfAbsent(100, "value"));
        assertEquals("1", c.get(100));
    }


    @Test
    public void putIfAbsentLazyReturnVoid() {
        fill();
        for (int i = 1; i < 99; i++) {
            assertNull(c.withCrud().write().putIfAbsentLazy(i, new LazyOp(i, "" + i)));
            assertEquals("" + i, c.get(i));
        }
        assertNull(c.withCrud().write().putIfAbsentLazy(100, new LazyOp(100, "X")));
        assertEquals("1", c.get(100));
    }

}
