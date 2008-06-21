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
package org.codehaus.cake.cache.policy.paging;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.codehaus.cake.cache.policy.PolicyTestUtils.add;
import static org.codehaus.cake.cache.policy.PolicyTestUtils.addToPolicy;
import static org.codehaus.cake.cache.policy.PolicyTestUtils.empty;
import static org.codehaus.cake.cache.policy.PolicyTestUtils.val;
import static org.codehaus.cake.test.util.CollectionTestUtil.asList;
import static org.codehaus.cake.test.util.CollectionTestUtil.seq;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.cake.cache.CacheEntry;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of {@link RandomReplacementPolicy}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 */
public class RandomPolicyTest {

    RandomReplacementPolicy<Integer, String> policy;

    @Before
    public void setUp() {
        policy = new RandomReplacementPolicy<Integer, String>();
    }

    /**
     * Test adding of new elements.
     */
    @Test
    public void testAdd() {
        addToPolicy(policy, 0, 9);
        assertTrue(empty(policy).containsAll(seq(0, 9)));
    }

    @Test
    public void testIntens2() {
        Set<Integer> s = new TreeSet<Integer>();
        for (int i = 0; i < 20; i++) {
            add(policy, i);
        }
        for (int i = 0; i < 40; i++) {
            s.add(i);
        }
        // list.print();
        for (int i = 0; i < 400; i++) {
            CacheEntry<Integer, String> data = policy.evictNext();
            policy.add(data);
            if (i % 20 == 0)
                policy.add(val(i / 20 + 20));
        }
        for (;;) {
            CacheEntry<Integer, String> ce = policy.evictNext();
            if (ce != null)
                s.remove(ce.getKey());
            else
                break;
        }
        assertEquals(0, s.size());
        assertNull(policy.evictNext());

    }

    /**
     * Test refreshing of elements.
     */
    @Test
    public void testRefresh() {
        addToPolicy(policy, 0, 9);
        policy.touch(val(4));
        policy.touch(val(4));
        policy.touch(val(0));
        policy.touch(val(3));
        policy.touch(val(2));
        policy.touch(val(9));
        assertTrue(empty(policy).containsAll(seq(0, 9)));
    }

    /**
     * Test removal of elements.
     */
    @Test
    public void testRemove() {
        addToPolicy(policy, 0, 9);
        assertTrue(empty(policy).containsAll(seq(0, 9)));
    }

    @Test
    public void testRemove2() {
        Set l = new HashSet();
        policy.add(val(1));
        policy.add(val(2));
        policy.add(val(3));
        l.add(val(1));
        l.add(val(2));
        l.add(val(3));

        l.remove(policy.evictNext());
        assertEquals(2, l.size());

        l.remove(policy.evictNext());
        assertEquals(1, l.size());

        l.remove(policy.evictNext());
        assertEquals(0, l.size());

    }

    @Test
    public void testRemoveEmpty2() {
        assertNull(policy.evictNext());
    }

    /**
     * Test removal elements by index.
     */
    @Test
    public void testRemoveIndex() {
        addToPolicy(policy, 0, 9);
        policy.remove(val(4));
        policy.remove(val(7));
        policy.remove(val(0));
        policy.remove(val(9));
        assertTrue(empty(policy).containsAll(asList(1, 2, 3, 5, 6, 8)));
    }

    @Test
    public void testClear() {
        addToPolicy(policy, 0, 9);
        policy.clear();
        assertNull(policy.evictNext());
    }

    @Test
    public void testUpdate() {
        addToPolicy(policy, 0, 9);
        assertSame(val(15), policy.replace(val(4), val(15)));
        CacheEntry<Integer, String> ce = policy.evictNext();
        while (ce != null) {
            // System.out.println(ce);
            if (ce.getKey() == 15) {
                return;
            }
            ce = policy.evictNext();
        }
        assertTrue(false);
    }
}
