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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import org.codehaus.cake.cache.CacheEntry;
import org.junit.Before;
import org.junit.Test;

public class AbstractDoubleLinkedReplacementPolicyTest extends AbstractPolicyTest {

    private int REP = 100;
    private LinkedList<CacheEntry<Integer, String>> matching;
    private Random r = new Random(124324234);
    private TP tp;

    CacheEntry<Integer, String> addFirst() {
        CacheEntry<Integer, String> ce = createEntry(1, "");
        tp.addFirst(ce);
        matching.addFirst(ce);
        return ce;
    }

    CacheEntry<Integer, String> addLast() {
        CacheEntry<Integer, String> ce = createEntry(1, "");
        tp.addLast(ce);
        matching.addLast(ce);
        return ce;
    }

    @Before
    public void before() {
        tp = new TP();
        policy = tp;
        init();
        // r = new Random(1243214);
        matching = new LinkedList<CacheEntry<Integer, String>>();
    }

    protected void populate() {
        for (int i = 0; i < REP; i++) {
            CacheEntry<Integer, String> te = r.nextBoolean() ? addFirst() : addLast();
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
            CacheEntry<Integer, String> te = matching.remove(rndIndex);
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
            CacheEntry<Integer, String> te = matching.remove(rndIndex);
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
            CacheEntry<Integer, String> newTe = createEntry(1, "");
            CacheEntry<Integer, String> prev = matching.set(rndIndex, newTe);
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
            CacheEntry<Integer, String> newTe = createEntry(1, "");
            CacheEntry<Integer, String> prev = matching.set(rndIndex, newTe);
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
            CacheEntry<Integer, String> te = matching.remove(rndIndex);
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
            CacheEntry<Integer, String> current = matching.getFirst();
            ListIterator<CacheEntry<Integer, String>> li = matching.listIterator();
            while (li.hasNext()) {
                CacheEntry<Integer, String> m = li.next();
                assertSame(current, m);
                current = (CacheEntry<Integer, String>) tp.getNext(current);
            }

            // backward
            current = matching.getLast();
            li = matching.listIterator(matching.size());

            while (li.hasPrevious()) {
                CacheEntry<Integer, String> m = li.previous();
                assertSame(current, m);
                current = (CacheEntry<Integer, String>) tp.getPrevious(current);
            }
        }
        assertSame(matching.isEmpty() ? null : matching.getLast(), tp.getLast());
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
