package org.codehaus.cake.cache;

import static org.codehaus.cake.cache.CacheDataExtractor.IS_NOT_NULL;
import static org.codehaus.cake.cache.CacheDataExtractor.ONLY_KEY;
import static org.codehaus.cake.cache.CacheDataExtractor.ONLY_VALUE;
import static org.codehaus.cake.cache.CacheDataExtractor.WHOLE_ENTRY;
import static org.codehaus.cake.cache.CacheDataExtractor.toAttribute;
import static org.codehaus.cake.test.util.TestUtil.serializeAndUnserialize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.Serializable;

import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.ops.ObjectOps;
import org.codehaus.cake.ops.Ops.Op;
import org.junit.Test;

public class CacheDataExtractorTest {
    public static final IntAttribute I_1 = new IntAttribute("I_1", 1) {};

    private final static CacheEntry<Integer, String> ce1 = Caches.newEntry(1, "A");
    private final static CacheEntry<Integer, String> ce2 = Caches.newEntry(2, "BB", I_1.singleton(2));

    @Test
    public void entryExtractor() {
        assertSame(WHOLE_ENTRY, ObjectOps.CONSTANT_OP);
        assertSame(WHOLE_ENTRY, serializeAndUnserialize(WHOLE_ENTRY));
        assertNull(WHOLE_ENTRY.op(null));
        assertSame(WHOLE_ENTRY.op(ce1), ce1);
    }

    @Test
    public void valueExtractor() {
        assertSame(ONLY_VALUE, serializeAndUnserialize(ONLY_VALUE));
        assertNull(ONLY_VALUE.op(null));
        assertEquals(ONLY_VALUE.op(ce1), "A");
        assertEquals(ONLY_VALUE.op(ce2), "BB");
    }

    @Test
    public void keyExtractor() {
        assertSame(ONLY_KEY, serializeAndUnserialize(ONLY_KEY));
        assertNull(ONLY_KEY.op(null));
        assertEquals(ONLY_KEY.op(ce1), 1);
        assertEquals(ONLY_KEY.op(ce2), 2);
    }

    @Test
    public void isNotNull() {
        assertSame(IS_NOT_NULL, serializeAndUnserialize(IS_NOT_NULL));
        assertSame(Boolean.FALSE, IS_NOT_NULL.op(null));
        assertSame(Boolean.TRUE, IS_NOT_NULL.op(ce1));
        assertSame(Boolean.TRUE, IS_NOT_NULL.op(ce2));
    }

    @Test(expected = NullPointerException.class)
    public void attributeNPE() {
        toAttribute(null);
    }

    @Test
    public void attribute() {
        IntAttribute I_2 = new IntAttribute("I_2", 2) {};

        Op o1 = toAttribute(I_1);
        Op o2 = toAttribute(I_2);
        assertEquals(1, o1.op(ce1));
        assertEquals(2, o1.op(ce2));
        assertEquals(1, o1.op(null));
        
        assertEquals(2, o2.op(ce1));
        assertEquals(2, o2.op(ce2));
        assertEquals(2, o2.op(null));
        
        assertEquals(2, serializeAndUnserialize(o1.op(ce2)));
    }

    @Test
    public void extractValue() {
        assertEquals(1, CacheDataExtractor.<Integer, String, Integer> extractFromValue(new StringLength()).op(ce1)
                .intValue());
        assertEquals(2, CacheDataExtractor.<Integer, String, Integer> extractFromValue(new StringLength()).op(ce2)
                .intValue());
        assertEquals(2, CacheDataExtractor.<Integer, String, Integer> extractFromValue(
                serializeAndUnserialize(new StringLength())).op(ce2).intValue());
    }


    @Test
    public void extractKey() {
        assertEquals(4, CacheDataExtractor.<Integer, String, Integer> extractFromKey(new Multiply4()).op(ce1)
                .intValue());
        assertEquals(8, CacheDataExtractor.<Integer, String, Integer> extractFromKey(new Multiply4()).op(ce2)
                .intValue());
        assertEquals(8, CacheDataExtractor.<Integer, String, Integer> extractFromKey(
                serializeAndUnserialize(new Multiply4())).op(ce2).intValue());
    }

    static class Multiply4 implements Op<Integer, Integer>, Serializable {
        public Integer op(Integer a) {
            return a * 4;
        }
    }

    static class StringLength implements Op<String, Integer>, Serializable {
        public Integer op(String a) {
            return a.length();
        }
    }
}
