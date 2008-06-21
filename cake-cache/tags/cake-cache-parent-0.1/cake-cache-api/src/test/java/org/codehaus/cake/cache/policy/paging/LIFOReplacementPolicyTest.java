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
import static org.codehaus.cake.cache.policy.PolicyTestUtils.addToPolicy;
import static org.codehaus.cake.cache.policy.PolicyTestUtils.empty;
import static org.codehaus.cake.cache.policy.PolicyTestUtils.evict;
import static org.codehaus.cake.cache.policy.PolicyTestUtils.val;
import static org.codehaus.cake.test.util.CollectionTestUtil.asList;
import static org.codehaus.cake.test.util.CollectionTestUtil.seq;

import org.junit.Before;
import org.junit.Test;

/**
 * Test of the LIFO policy.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 */
public class LIFOReplacementPolicyTest {

    LIFOReplacementPolicy<Integer, String> policy;

    @Before
    public void setUp() {
        policy = new LIFOReplacementPolicy<Integer, String>();
    }

    /**
     * Test adding of new elements.
     */
    @Test
    public void testAdd() {
        addToPolicy(policy, 0, 9);
        assertTrue(empty(policy).containsAll(seq(0, 9)));
    }

    /**
     * Test removal of elements.
     */
    @Test
    public void testRemove() {
        addToPolicy(policy, 0, 9);
        assertEquals(seq(0, 9), empty(policy));
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
        assertEquals(asList(1, 2, 3, 5, 6, 8), empty(policy));
    }

    /**
     * Test refreshing of elements.
     */
    @Test
    public void testRefresh() {
        addToPolicy(policy, 0, 9);

        policy.touch(val(4));
        policy.touch(val(4));
        policy.touch(val(3));
        policy.touch(val(2));
        policy.touch(val(9));
        // LIFO queues doesn't care about refreshes
        assertEquals(seq(0, 9), empty(policy));
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
        assertSame(val(123), policy.replace(val(4), val(123)));
        Integer[] i = evict(policy, 10);
        assertEquals(9, i[9].intValue());
        assertEquals(3, i[3].intValue());
        assertEquals(123, i[4].intValue());
    }
}
