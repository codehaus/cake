package org.codehaus.cake.cache.test.tck.crud;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.internal.util.Pair;
import org.codehaus.cake.ops.Ops.Op;
import org.junit.Test;

public class CrudWriterPutIfAbsent extends AbstractCacheTCKTest {

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
            assertNull(c.crud().write().putIfAbsent(i, "" + i));
            assertEquals("" + i, c.get(i));
        }
        assertNull(c.crud().write().putIfAbsent(100, "value"));
        assertEquals("1", c.get(100));
    }

    @Test
    public void putIfAbsentAttributeReturnVoid() {
        fill();
        for (int i = 1; i < 99; i++) {
            assertNull(c.crud().write().putIfAbsent(i, "" + i));
            assertEquals("" + i, c.get(i));
        }
        assertNull(c.crud().write().putIfAbsent(100, "value"));
        assertEquals("1", c.get(100));
    }

    @Test(expected = NullPointerException.class)
    public void putIfAbsentLazyReturnVoidNPE1() {
        c.crud().write().putIfAbsentLazy(null, new LazyOp(1, "1"));
    }

    @Test(expected = NullPointerException.class)
    public void putIfAbsentLazyReturnVoidNPE2() {
        c.crud().write().putIfAbsentLazy(1, null);
    }

    @Test
    public void putIfAbsentLazyReturnVoid() {
        fill();
        for (int i = 1; i < 99; i++) {
            assertNull(c.crud().write().putIfAbsentLazy(i, new LazyOp(i, "" + i)));
            assertEquals("" + i, c.get(i));
        }
        assertNull(c.crud().write().putIfAbsentLazy(100, new LazyOp(100, "X")));
        assertEquals("1", c.get(100));
    }

    static class LazyOp implements Op<Integer, Pair<String, AttributeMap>> {
        private final int key;
        private final String v;
        private final AttributeMap attributes;

        public Pair<String, AttributeMap> op(Integer a) {
            assertEquals(key, a.intValue());
            return new Pair<String, AttributeMap>(v, attributes);
        }

        public LazyOp(int key, String value) {
            this(key, value, Attributes.EMPTY_ATTRIBUTE_MAP);
        }

        public LazyOp(int key, String value, AttributeMap attributes) {
            this.key = key;
            this.v = value;
            this.attributes = attributes;
        }

    }
}
