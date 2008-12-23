/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.cache.CacheEntry;
import org.junit.Before;
import org.junit.Test;

public class AbstractHeapReplacementPolicyTest extends AbstractPolicyTest {

    static IntAttribute A = new IntAttribute("A") {};
    private PriorityQueue matching;
    private Random r = new Random(124324234);
    private TP tp;

    @Before
    public void before() {
        tp = new TP();
        policy = tp;
        init();
        matching = new PriorityQueue(1, A);
    }

    @Test
    public void addEvictPeek() {
        for (int i = 0; i < 33; i++) {
            before();
            for (int j = 0; j < i; j++) {
                CacheEntry<Integer, String> te = createEntry(A, r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
                matching.add(te);
            }
            while (!matching.isEmpty()) {
                CacheEntry<Integer, String> te = (CacheEntry<Integer, String>) tp.peek();
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
            before();
            for (int j = 0; j < i; j++) {
                CacheEntry<Integer, String> te = createEntry(A, r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
                matching.add(te);
            }
            for (int j = 0; j < Math.min(i, r.nextInt(10)); j++) {
                Object[] take = matching.toArray();
                Object o = take[r.nextInt(take.length)];
                removeFromMatching(o);
                tp.remove((CacheEntry<Integer, String>) o);
            }
            while (!matching.isEmpty()) {
                CacheEntry<Integer, String> te = (CacheEntry<Integer, String>) tp.peek();
                assertSame(matching.poll(), te);
                assertSame(te, tp.evictNext());
            }

            assertNull(tp.evictNext());
            assertNull(tp.peek());
        }
    }

    @Test
    public void shiftUp() {
        for (int i = 3; i < 129; i++) {
            before();
            for (int j = 0; j < i; j++) {
                CacheEntry<Integer, String> te = createEntry(A, r.nextInt(Math.max(3, i - 3)));// newEntry(r.nextInt(Math.max(3,
                                                                                                // i - 3)));
                tp.add(te);
                matching.add(te);
            }
            Object[] take = matching.toArray();
            CacheEntry<Integer, String> te = (CacheEntry<Integer, String>) take[r.nextInt(take.length)];
            removeFromMatching(te);
            set(te, A, te.get(A) - r.nextInt(10));
            matching.add(te);
            tp.siftUp(te);
            emptyEquals();
        }
    }

    @Test
    public void shiftDown() {
        for (int i = 3; i < 31; i++) {
            before();
            for (int j = 0; j < i; j++) {
                CacheEntry<Integer, String> te = createEntry(A, r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
                matching.add(te);
            }
            Object[] take = matching.toArray();
            CacheEntry<Integer, String> te = (CacheEntry<Integer, String>) take[r.nextInt(take.length)];
            removeFromMatching(te);
            set(te, A, te.get(A) + r.nextInt(10));
            matching.add(te);
            tp.siftDown(te);
            emptyEquals();
        }
    }

    @Test
    public void replace() {
        for (int i = 0; i < 127; i++) {
            before();
            for (int j = 0; j < i; j++) {
                CacheEntry<Integer, String> te = createEntry(A, r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
                matching.add(te);
            }
            // if (i == 4) {
            // for (int q = 0; q < tp.queue.length; q++) {
            // System.out.println(tp.queue[q] + " " + matching.toArray()[Math.min(q, matching.size()-1)]);
            // }
            // }

            for (int j = 0; j < Math.min(i, r.nextInt(10)); j++) {
                Object[] take = matching.toArray();
                Object o = take[r.nextInt(take.length)];
                removeFromMatching(o);
                CacheEntry<Integer, String> te = createEntry(A, r.nextInt(Math.max(3, i - 3)));
                matching.add(te);
                assertSame(te, tp.replace((CacheEntry<Integer, String>) o, te));
            }
            // if (i == 4) {
            // for (int q = 0; q < tp.queue.length; q++) {
            // System.out.println(tp.queue[q] + " " + matching.toArray()[Math.min(q, matching.size()-1)]);
            // }
            // }

            emptyEquals();
        }
    }

    // bug in priorityqueue.remove() 1.5
    // which uses the comparator to compare items instead of equals
    private void removeFromMatching(Object o) {
        for (Iterator iterator = matching.iterator(); iterator.hasNext();) {
            if (o.equals(iterator.next())) {
                iterator.remove();
                return;
            }
        }
    }

    private void emptyEquals() {
        while (!matching.isEmpty()) {
            CacheEntry<Integer, String> peek = (CacheEntry<Integer, String>) tp.peek();
            CacheEntry<Integer, String> m = (CacheEntry<Integer, String>) matching.poll();
            CacheEntry<Integer, String> evict = (CacheEntry<Integer, String>) tp.evictNext();
            assertSame(peek, evict);
            assertEquals(evict.get(A), m.get(A));
        }
        assertNull(tp.evictNext());
        assertNull(tp.peek());
    }

    @Test
    public void clear() {
        for (int i = 0; i < 33; i++) {
            before();
            for (int j = 0; j < i; j++) {
                CacheEntry<Integer, String> te = createEntry(A, r.nextInt(Math.max(3, i - 3)));
                tp.add(te);
            }
            tp.clear();
            assertNull(tp.peek());
            assertNull(tp.evictNext());
        }
    }

    static final AtomicInteger count = new AtomicInteger();

    static class TP extends AbstractHeapReplacementPolicy<Integer, String> {

        protected int compareEntry(CacheEntry<Integer, String> o1, CacheEntry<Integer, String> o2) {
            int thisVal = o1.get(A);
            int anotherVal = o2.get(A);
            return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
            //
            // return A.compare(o1, o2);
        }
    }
}
