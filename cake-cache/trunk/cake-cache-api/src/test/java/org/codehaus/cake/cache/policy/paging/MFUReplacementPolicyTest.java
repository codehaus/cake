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

import static org.codehaus.cake.cache.CacheEntry.HITS;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.PolicyTestUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of the MRU policy.
 * <p>
 * This test relies on the assumption that AbstractHeapReplacementPolicy has been thoroughly tested.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 */
public class MFUReplacementPolicyTest {
    MFUReplacementPolicy<Integer, String> policy;

    @Before
    public void setUp() {
        policy = new MFUReplacementPolicy<Integer, String>();
    }

    @Test
    public void addEvict() {
        CacheEntry<Integer, String> e2 = PolicyTestUtils.create(1, HITS, 2L);
        CacheEntry<Integer, String> e3 = PolicyTestUtils.create(1, HITS, 3L);
        CacheEntry<Integer, String> e4 = PolicyTestUtils.create(1, HITS, 4L);
        CacheEntry<Integer, String> e5 = PolicyTestUtils.create(1, HITS, 5L);

        policy.add(e3);
        policy.add(e4);
        policy.add(e2);
        policy.add(e5);
        assertSame(e5, policy.evictNext());
        assertSame(e4, policy.evictNext());
        assertSame(e3, policy.evictNext());
        assertSame(e2, policy.evictNext());
        assertNull(policy.evictNext());
    }

    @Test
    public void addTouchEvict() {
        CacheEntry<Integer, String> e2 = PolicyTestUtils.create(1, HITS, 2L);
        CacheEntry<Integer, String> e3 = PolicyTestUtils.create(1, HITS, 3L);
        CacheEntry<Integer, String> e4 = PolicyTestUtils.create(1, HITS, 4L);
        CacheEntry<Integer, String> e5 = PolicyTestUtils.create(1, HITS, 5L);

        policy.add(e3);
        policy.add(e4);
        policy.add(e2);
        policy.add(e5);

        HITS.set(e2, 7);
        policy.touch(e2);
        HITS.set(e4, 8);
        policy.touch(e4);
        assertSame(e4, policy.evictNext());
        assertSame(e2, policy.evictNext());
        assertSame(e5, policy.evictNext());
        assertSame(e3, policy.evictNext());
        assertNull(policy.evictNext());
    }
}
