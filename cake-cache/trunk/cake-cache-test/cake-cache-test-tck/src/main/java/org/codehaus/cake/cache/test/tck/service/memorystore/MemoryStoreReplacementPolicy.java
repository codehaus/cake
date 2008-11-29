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
package org.codehaus.cake.cache.test.tck.service.memorystore;

import static org.codehaus.cake.cache.CacheEntry.SIZE;
import static org.codehaus.cake.test.util.CollectionTestUtil.asMap_;
import static org.codehaus.cake.test.util.CollectionTestUtil.asSet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.policy.paging.LRUReplacementPolicy;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests that an instance of a CacheEntry is passed to the specified cache policy.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheEntryToPolicy.java 511 2007-12-13 14:37:02Z kasper $
 */
public class MemoryStoreReplacementPolicy extends AbstractCacheTCKTest {

    @Test
    public void testSimpleSize() {
        conf.withMemoryStore().setPolicy(Policies.newLRU()).setMaximumSize(5);
        init();
        for (int i = 0; i < 5; i++) {
            put(entry(i, Integer.toString(i)));
        }
        assertEquals(5, c.size());
        for (int i = 5; i < 10; i++) {
            put(entry(i, Integer.toString(i)));
            assertEquals(5, c.size());
        }
    }

    @Test
    public void testEviction() {
        conf.withMemoryStore().setPolicy(Policies.newLRU()).setMaximumSize(5);
        init();
        for (int i = 0; i < 5; i++) {
            c.put(i, Integer.toString(i));
        }
        assertEquals(asSet(0, 1, 2, 3, 4), c.keySet());
        c.put(5, "");
        assertEquals(asSet(1, 2, 3, 4, 5), c.keySet());
        c.put(6, "");
        assertEquals(asSet(2, 3, 4, 5, 6), c.keySet());

        c.put(7, "");
        assertEquals(asSet(3, 4, 5, 6, 7), c.keySet());

        c.put(8, "");
        assertEquals(asSet(4, 5, 6, 7, 8), c.keySet());
    }

    @Test
    public void testTouch() {
        conf.withMemoryStore().setPolicy(Policies.newLRU()).setMaximumSize(10);
        init();
        for (int i = 0; i < 10; i++) {
            c.put(i, Integer.toString(i));
        }
        c.get(4);
        c.get(4);
        c.get(0);
        c.get(3);
        c.get(2);
        c.get(9);

        replaceAndCheck(10, 1);
        replaceAndCheck(11, 5);
        replaceAndCheck(12, 6);
        replaceAndCheck(13, 7);
        replaceAndCheck(14, 8);
        replaceAndCheck(15, 4);
        replaceAndCheck(16, 0);
        replaceAndCheck(17, 3);
        replaceAndCheck(18, 2);
        replaceAndCheck(19, 9);
    }

    @Test
    public void testPeek() {
        conf.withMemoryStore().setPolicy(Policies.newLRU()).setMaximumSize(5);
        init();
        for (int i = 0; i < 5; i++) {
            c.put(i, Integer.toString(i));
        }
        peek(0);
        peek(M2);
        assertEquals(asSet(0, 1, 2, 3, 4), c.keySet());
        c.put(5, "");
        assertEquals(asSet(1, 2, 3, 4, 5), c.keySet());
        c.put(6, "");
        assertEquals(asSet(2, 3, 4, 5, 6), c.keySet());
    }

    void replaceAndCheck(Integer newKey, Integer oldKey) {
        assertNull(c.put(newKey, "foo"));
        assertFalse(c.containsKey(oldKey));
    }

    @Test
    public void testRejectEntry() {
        RejectEntriesPolicy rep = new RejectEntriesPolicy();
        conf.withMemoryStore().setPolicy(rep).setMaximumSize(5);
        init();
        c.put(1, "A");
        rep.rejectAdd = true;
        c.put(2, "B");
        rep.rejectAdd = false;
        c.put(3, "C");
        assertEquals(2, c.size());
        assertFalse(c.containsKey(2));
    }

    @Test
    public void testRejectReplaceEntry() {
        RejectEntriesPolicy rep = new RejectEntriesPolicy();
        conf.withMemoryStore().setPolicy(rep).setMaximumSize(5);
        init();

        c.put(1, "A");
        c.put(2, "B");
        assertEquals(2, c.size());
        c.put(2, "C");
        assertEquals(1, c.size());
        rep.rejectUpdate = false;
        c.put(2, "B");
        c.put(3, "C");
        assertGet(entry(M3, "C"));
        c.put(3, "D");
        assertGet(entry(M3, "D"));
        assertEquals(3, c.size());
        rep.rejectUpdate = true;
        c.put(2, "E");
        assertGet(entry(M2, "B"));
        c.put(3, "F");
        assertGet(entry(M3, "D"));
        assertEquals(3, c.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRejectEntryPutAll() {
        Reject2EntriesPolicy rep = new Reject2EntriesPolicy();
        conf.withMemoryStore().setPolicy(rep).setMaximumSize(5);
        init();
        // the reject2EntriesPolicy is kind of a hack until we are
        // clear what type og object goes into add() for the policy
        c.putAll(asMap_(M1, M2, M3));
        assertEquals(2, c.size());
        assertFalse(c.containsKey(2));
    }

    // put of elements

    //
    /**
     * Currently put is ignored with regards to cache policies, but should just inherit the previous entry.
     */
    @Test
    public void testPutOverridesPreviousValue() {
        conf.withMemoryStore().setPolicy(Policies.newLRU()).setMaximumSize(2);
        init();
        c.put(M1.getKey(), M1.getValue());
        c.put(M2.getKey(), M2.getValue());
        c.get(M1.getKey());
        c.put(M2.getKey(), M3.getValue());
        c.get(M2.getKey());
        replaceAndCheck(10, M1.getKey());
        replaceAndCheck(11, M2.getKey());
    }

    @Test
    public void testPutAllOverridesPreviousValue() {
        conf.withMemoryStore().setPolicy(Policies.newLRU()).setMaximumSize(3);
        init();
        put(M1);
        put(M2);
        put(M3);
        get(M3);
        get(M2);
        get(M1);
        remove(M2);
        put(M4);
        get(M4);
        assertTrue(c.containsKey(M4.getKey()));
        assertTrue(c.containsKey(M3.getKey()));
        assertFalse(c.containsKey(M2.getKey()));
        assertTrue(c.containsKey(M1.getKey()));

        replaceAndCheck(10, M3.getKey());
        replaceAndCheck(11, M1.getKey());
        replaceAndCheck(12, M4.getKey());

    }

    @Test
    public void testRemoveEntry() {
        conf.withMemoryStore().setPolicy(Policies.newLRU()).setMaximumSize(2);
        init();
        c.put(M1.getKey(), M1.getValue());
        c.put(M2.getKey(), M2.getValue());
        c.get(M1.getKey());
        c.remove(M1.getKey());
        c.put(M3.getKey(), M3.getValue());
    }

    @SuppressWarnings( { "serial" })
    static class RejectEntriesPolicy<K, V> extends LRUReplacementPolicy<K, V> {
        @Override
        public boolean add(CacheEntry<K, V> entry) {
            if (!rejectAdd) {
                return super.add(entry);
            } else {
                return false;
            }
        }

        @Override
        public CacheEntry<K, V> replace(CacheEntry<K, V> previous, CacheEntry<K, V> newEntry) {
            if (rejectUpdate == null) {
                super.remove(previous);
                return null;
            } else if (rejectUpdate) {
                return previous;
            } else {
                return super.replace(previous, newEntry);
            }
        }

        volatile boolean rejectAdd;

        volatile Boolean rejectUpdate;

    }

    @SuppressWarnings( { "unchecked", "serial" })
    static class Reject2EntriesPolicy<K, V> extends LRUReplacementPolicy<K, V> {
        @Override
        public boolean add(CacheEntry<K, V> entry) {
            if (entry.getKey().equals(2)) {
                return false;
            } else {
                return super.add(entry);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void isCacheEntry() throws InterruptedException {
        final BlockingQueue<Object> q = new LinkedBlockingQueue<Object>();

        conf.addEntryAttributes(SIZE);
        loader.withLoader(M1).addAttribute(SIZE, 4l);
        conf.withMemoryStore().setPolicy(new LRUReplacementPolicy<Integer, String>() {
            @Override
            public boolean add(CacheEntry<Integer, String> entry) {
                q.add(entry);
                return super.add(entry);
            }
        });
        conf.withMemoryStore().setMaximumSize(5);
        init();

        assertGet(M1);

        Object o = q.poll(1, TimeUnit.SECONDS);
        assertNotNull("No object was handed off to the queue from PolicyMock", o);
        assertTrue(o instanceof CacheEntry);
        CacheEntry<Integer, String> ce = (CacheEntry<Integer, String>) o;
        assertSame(M1.getKey(), ce.getKey());
        assertSame(M1.getValue(), ce.getValue());
        assertEquals(ce.getAttributes(), SIZE.singleton(4));
    }

}
