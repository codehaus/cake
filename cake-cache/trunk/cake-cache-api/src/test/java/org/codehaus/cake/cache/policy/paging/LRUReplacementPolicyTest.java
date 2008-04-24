/* Copyright 2004 - 2007 Kasper Nielsen <kasper@codehaus.org> Licensed under 
 * the Apache 2.0 License, see http://coconut.codehaus.org/license.
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
 * Test of the LRU policy.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 */
public class LRUReplacementPolicyTest {

    LRUReplacementPolicy<Integer, String> policy;

    @Before
    public void setUp() {
        policy = new LRUReplacementPolicy<Integer, String>();
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
        // assertEquals(asList(0, 1, 2, 3, 5, 6, 7, 8, 9, 4), policy.peekAll());
        policy.touch(val(4));
        // assertEquals(asList(0, 1, 2, 3, 5, 6, 7, 8, 9, 4), policy.peekAll());
        //((LRUReplacementPolicy) policy).print();
        policy.touch(val(0));
        //((LRUReplacementPolicy) policy).print();
        // assertEquals(asList(1, 2, 3, 5, 6, 7, 8, 9, 4, 0), policy.peekAll());
        policy.touch(val(3));
        policy.touch(val(2));
        policy.touch(val(9));
        //((LRUReplacementPolicy) policy).print();
        //System.out.println(policy.evictNext());
        //((LRUReplacementPolicy) policy).print();
        assertEquals(asList(1, 5, 6, 7, 8, 4, 0, 3, 2, 9), empty(policy));
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
        Integer[] i = evict(policy, 9);
        assertEquals(3, i[3].intValue());
        assertEquals(5, i[4].intValue());
        assertEquals(123, policy.evictNext().getKey().intValue());
    }
}
