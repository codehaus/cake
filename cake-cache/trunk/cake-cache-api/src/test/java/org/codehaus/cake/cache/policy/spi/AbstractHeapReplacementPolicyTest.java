package org.codehaus.cake.cache.policy.spi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.math.MathContext;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Random;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.spi.AbstractDoubleLinkedReplacementPolicyTest.TestEntry;
import org.junit.Before;
import org.junit.Test;

public class AbstractHeapReplacementPolicyTest {

    static LongAttribute A = new LongAttribute() {};
    private int REP = 100;
    private PriorityQueue<TestEntry> matching;
    private Random r = new Random(124324234);
    private TP tp;

    @Before
    public void before() {
        tp = new TP();
        matching = new PriorityQueue<TestEntry>(1, A);
    }

    @Test
    public void addEvictPeek() {
        for (int i = 0; i < 33; i++) {
            matching = new PriorityQueue<TestEntry>(1, A);
            tp = new TP();
            for (int j = 0; j < i; j++) {
                TestEntry te = new TestEntry(r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
                matching.add(te);
            }
            while (!matching.isEmpty()) {
                TestEntry te = (TestEntry) tp.peek();
                assertSame(matching.poll(), te);
                assertSame(te, tp.evictNext());
            }
            assertNull(tp.evictNext());
            assertNull(tp.peek());
        }
    }

    @Test
    public void remove() {
        for (int i = 0; i < 129; i++) {
            matching = new PriorityQueue<TestEntry>(1, A);
            tp = new TP();
            for (int j = 0; j < i; j++) {
                TestEntry te = new TestEntry(r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
                matching.add(te);
            }
            for (int j = 0; j < Math.min(i, r.nextInt(10)); j++) {
                Object[] take = matching.toArray();
                Object o = take[r.nextInt(take.length)];
                matching.remove(o);
                tp.remove((TestEntry) o);
            }
            while (!matching.isEmpty()) {
                TestEntry te = (TestEntry) tp.peek();
                assertSame(matching.poll(), te);
                assertSame(te, tp.evictNext());
            }
            assertNull(tp.evictNext());
            assertNull(tp.peek());
        }
    }

    @Test
    public void shiftUp() {
        for (int i = 3; i < 31; i++) {
            matching = new PriorityQueue<TestEntry>(1, A);
            tp = new TP();
            for (int j = 0; j < i; j++) {
                TestEntry te = new TestEntry(r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
                matching.add(te);
            }
            Object[] take = matching.toArray();
            TestEntry te = (TestEntry) take[r.nextInt(take.length)];
            matching.remove(te);
            A.set(te, A.get(te) - r.nextInt(10));
            matching.add(te);
            tp.siftUp(te);
            emptyEquals();
        }
    }

    @Test
    public void shiftDown() {
        for (int i = 3; i < 31; i++) {
            matching = new PriorityQueue<TestEntry>(1, A);
            tp = new TP();
            for (int j = 0; j < i; j++) {
                TestEntry te = new TestEntry(r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
                matching.add(te);
            }
            Object[] take = matching.toArray();
            TestEntry te = (TestEntry) take[r.nextInt(take.length)];
            matching.remove(te);
            A.set(te, A.get(te) + r.nextInt(10));
            matching.add(te);
            tp.siftDown(te);
            emptyEquals();
        }
    }
    
    @Test
    public void replace() {
        for (int i = 0; i < 129; i++) {
            matching = new PriorityQueue<TestEntry>(1, A);
            tp = new TP();
            for (int j = 0; j < i; j++) {
                TestEntry te = new TestEntry(r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
                matching.add(te);
            }
            for (int j = 0; j < Math.min(i, r.nextInt(10)); j++) {
                Object[] take = matching.toArray();
                Object o = take[r.nextInt(take.length)];
                matching.remove(o);
                TestEntry te = new TestEntry(r.nextInt(Math.max(3, i - 3)));
                matching.add(te);
                assertSame(te, tp.replace((TestEntry) o, te));
            }
            emptyEquals();
        }
    }

    private void emptyEquals() {
        while (!matching.isEmpty()) {
            TestEntry peek = (TestEntry) tp.peek();
            TestEntry m = matching.poll();
            TestEntry evict = (TestEntry) tp.evictNext();
            assertSame(peek, evict);
            assertEquals(A.get(evict), A.get(m));
        }
        assertNull(tp.evictNext());
        assertNull(tp.peek());
    }

    @Test
    public void clear() {
        for (int i = 0; i < 33; i++) {
            tp = new TP();
            for (int j = 0; j < i; j++) {
                TestEntry te = new TestEntry(r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
            }
            tp.clear();
            assertNull(tp.peek());
            assertNull(tp.evictNext());
        }
    }

    static class TestEntry implements CacheEntry<Integer, String> {
        private final AttributeMap attributes = new DefaultAttributeMap();

        public TestEntry(long val) {
            attributes.put(A, val);
        }

        public AttributeMap getAttributes() {
            return attributes;
        }

        public Integer getKey() {
            return null;
        }

        public String getValue() {
            return null;
        }

        public String setValue(String value) {
            return null;
        }

    }

    static class TP extends AbstractHeapReplacementPolicy<Integer, String> {

        protected int compareEntry(CacheEntry<Integer, String> o1, CacheEntry<Integer, String> o2) {
            return A.compare(o1, o2);
        }
    }
}
