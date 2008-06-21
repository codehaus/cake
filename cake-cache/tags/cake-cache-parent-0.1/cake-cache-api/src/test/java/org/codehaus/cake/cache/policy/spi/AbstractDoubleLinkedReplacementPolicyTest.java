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
package org.codehaus.cake.cache.policy.spi;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.junit.Before;
import org.junit.Test;

public class AbstractDoubleLinkedReplacementPolicyTest {

    private int REP = 100;
    private LinkedList<TestEntry> matching;
    private Random r = new Random(124324234);
    private TP tp;

    TestEntry addFirst() {
        TestEntry ce = new TestEntry();
        tp.addFirst(ce);
        matching.addFirst(ce);
        return ce;
    }

    TestEntry addLast() {
        TestEntry ce = new TestEntry();
        tp.addLast(ce);
        matching.addLast(ce);
        return ce;
    }

    @Before
    public void before() {
        tp = new TP();
        // r = new Random(1243214);
        matching = new LinkedList<TestEntry>();
    }

    protected void populate() {
        for (int i = 0; i < REP; i++) {
            TestEntry te = r.nextBoolean() ? addFirst() : addLast();
            verify();
        }
    }

    @Test
    public void add() {
        populate();
        verify(); // tests add
    }

    @Test
    public void clear() {
        TP tp = new TP();
        populate();
        tp.clear();
        assertNull(tp.getFirst());
        assertNull(tp.getLast());
        verify();
    }

    @Test
    public void remove() {
        populate();
        while (!matching.isEmpty()) {
            int rndIndex = r.nextInt(matching.size());
            TestEntry te = matching.remove(rndIndex);
            tp.remove(te);
            verify();
        }
        verify();
    }

    @Test
    public void moveFirst() {
        populate();
        for (int i = 0; i < REP; i++) {
            int rndIndex = r.nextInt(matching.size());
            TestEntry te = matching.remove(rndIndex);
            matching.addFirst(te);
            tp.moveFirst(te);
            verify();
        }
        verify();
    }

    @Test
    public void replace0() {
        populate();
        for (int i = 0; i < 3 * REP; i++) {
            int rndIndex = r.nextInt(matching.size());
            TestEntry newTe = new TestEntry();
            TestEntry prev = matching.set(rndIndex, newTe);
            tp.replace0(prev, newTe);
            verify();
        }
        verify();
    }

    @Test
    public void replace() {
        populate();
        for (int i = 0; i < REP; i++) {
            int rndIndex = r.nextInt(matching.size());
            TestEntry newTe = new TestEntry();
            TestEntry prev = matching.set(rndIndex, newTe);
            assertSame(newTe, tp.replace(prev, newTe));
            verify();
        }
        verify();
    }

    @Test
    public void moveLast() {
        populate();
        for (int i = 0; i < REP; i++) {
            int rndIndex = r.nextInt(matching.size());
            TestEntry te = matching.remove(rndIndex);
            matching.addLast(te);
            tp.moveLast(te);
            verify();
        }
        verify();
    }

    @Test
    public void removeFirst() {
        populate();
        while (!matching.isEmpty()) {
            matching.removeFirst();
            tp.removeFirst();
            verify();
        }
        verify();
    }

    @Test
    public void removeLast() {
        populate();
        while (!matching.isEmpty()) {
            matching.removeLast();
            tp.removeLast();
            verify();
        }
        verify();
    }

    protected void verify() {
        assertSame(matching.isEmpty() ? null : matching.getFirst(), tp.getFirst());
        if (matching.size() > 0) {
            assertNull(tp.getPrevious(tp.getFirst()));
            assertNull(tp.getNext(tp.getLast()));

            // forward
            TestEntry current = matching.getFirst();
            ListIterator<TestEntry> li = matching.listIterator();
            while (li.hasNext()) {
                TestEntry m = li.next();
                assertSame(current, m);
                current = (TestEntry) tp.getNext(current);
            }

            // backward
            current = matching.getLast();
            li = matching.listIterator(matching.size());

            while (li.hasPrevious()) {
                TestEntry m = li.previous();
                assertSame(current, m);
                current = (TestEntry) tp.getPrevious(current);
            }
        }
        assertSame(matching.isEmpty() ? null : matching.getLast(), tp.getLast());
    }

    static class TestEntry implements CacheEntry<Integer, String> {
        private final AttributeMap attributes = new DefaultAttributeMap();

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

    static class TP extends AbstractDoubleLinkedReplacementPolicy<Integer, String> {
        public boolean add(CacheEntry entry) {
            return false;
        }

        public CacheEntry<Integer, String> evictNext() {
            return null;
        }
    }
}
