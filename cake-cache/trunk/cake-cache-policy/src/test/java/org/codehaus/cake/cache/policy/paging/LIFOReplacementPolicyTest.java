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
package org.codehaus.cake.cache.policy.paging;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.codehaus.cake.test.util.CollectionTestUtil.asList;
import static org.codehaus.cake.test.util.CollectionTestUtil.seq;

import org.codehaus.cake.cache.policy.AbstractPolicyTest;
import org.codehaus.cake.cache.policy.Policies;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of the LIFO policy.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 */
public class LIFOReplacementPolicyTest extends AbstractPolicyTest {

    @Before
    public void setUp() {
        policy = Policies.create(LIFOReplacementPolicy.class);
    }

    /**
     * Test adding of new elements.
     */
    @Test
    public void testAdd() {
        addToPolicy(0, 9);
        assertTrue(empty().containsAll(seq(0, 9)));
    }

    /**
     * Test removal of elements.
     */
    @Test
    public void testRemove() {
        addToPolicy(0, 9);
        assertEquals(seq(0, 9), empty());
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
        assertEquals(asList(1, 2, 3, 5, 6, 8), empty());
    }

    /**
     * Test refreshing of elements.
     */
    @Test
    public void testRefresh() {
        addToPolicy(0, 9);

        policy.touch(4);
        policy.touch(4);
        policy.touch(3);
        policy.touch(2);
        policy.touch(9);
        // LIFO queues doesn't care about refreshes
        assertEquals(seq(0, 9), empty());
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
        policy.replace(4, 123);
        Integer[] i = evict(10);
        assertEquals(9, i[9].intValue());
        assertEquals(3, i[3].intValue());
        assertEquals(123, i[4].intValue());
    }
}
