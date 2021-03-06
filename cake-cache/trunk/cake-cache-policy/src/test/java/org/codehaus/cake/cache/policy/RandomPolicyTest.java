/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache.policy;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.codehaus.cake.test.util.CollectionTestUtil.asList;
import static org.codehaus.cake.test.util.CollectionTestUtil.seq;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.policy.RandomReplacementPolicy;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of {@link RandomReplacementPolicy}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 */
public class RandomPolicyTest extends AbstractPolicyTest {

    @Before
    public void setUp() {
        policy = Policies.create(RandomReplacementPolicy.class);
    }

    /**
     * Test adding of new elements.
     */
    @Test
    public void testAdd() {
        addToPolicy(0, 9);
        assertTrue(empty().containsAll(seq(0, 9)));
    }

    @Test
    public void testIntens2() {
        Set<Integer> s = new TreeSet<Integer>();
        for (int i = 0; i < 20; i++) {
            policy.add(i);
        }
        for (int i = 0; i < 40; i++) {
            s.add(i);
        }
        // list.print();
        for (int i = 0; i < 400; i++) {
            Integer data = policy.evictNext();
            policy.add(data);
            if (i % 20 == 0)
                policy.add(i / 20 + 20);
        }
        for (;;) {
            Integer ce = policy.evictNext();
            if (ce != null)
                s.remove(ce);
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
        addToPolicy(0, 9);
        policy.touch(4);
        policy.touch(4);
        policy.touch(0);
        policy.touch(3);
        policy.touch(2);
        policy.touch(9);
        assertTrue(empty().containsAll(seq(0, 9)));
    }

    /**
     * Test removal of elements.
     */
    @Test
    public void testRemove() {
        addToPolicy(0, 9);
        assertTrue(empty().containsAll(seq(0, 9)));
    }

    @Test
    public void testRemove2() {
        Set<Integer> l = new HashSet<Integer>();
        
        policy.add(1);
        policy.add(2);
        policy.add(3);
        l.add(1);
        l.add(2);
        l.add(3);

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
        addToPolicy(0, 9);

        policy.remove(4);
        policy.remove(7);
        policy.remove(0);
        policy.remove(9);
        assertTrue(empty().containsAll(asList(1, 2, 3, 5, 6, 8)));
    }

    @Test
    public void testClear() {
        addToPolicy(0, 9);
        policy.clear();
        assertNull(policy.evictNext());
    }

    @Test
    public void testUpdate() {
        addToPolicy(0, 9);
        policy.add(15);
        policy.replace(4, 15);
        Integer ce = policy.evictNext();
        while (ce != null) {
            // System.out.println(ce);
            if (ce.equals(15)) {
                return;
            }
            ce = policy.evictNext();
        }
        assertTrue(false);
    }
}
