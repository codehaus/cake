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

import static org.codehaus.cake.internal.cache.CacheEntryAttributes.HITS;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.codehaus.cake.cache.policy.AbstractPolicyTest;
import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.policy.ReplacementPolicy;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of the MRU policy.
 * <p>
 * This test relies on the assumption that AbstractHeapReplacementPolicy has been thoroughly tested.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen </a>
 */
public class LFUReplacementPolicyTest extends AbstractPolicyTest {

    protected ReplacementPolicy<AttributeMap> policy;

    @Before
    public void setUp() {
        policy = Policies.create(LFUReplacementPolicy.class);
    }

    @Test
    public void addEvict() {
        AttributeMap e2 = createEntry(HITS, 2L);
        AttributeMap e3 = createEntry(HITS, 3L);
        AttributeMap e4 = createEntry(HITS, 4L);
        AttributeMap e5 = createEntry(HITS, 5L);

        policy.add(e3);
        policy.add(e4);
        policy.add(e2);
        policy.add(e5);
        assertSame(e2, policy.evictNext());
        assertSame(e3, policy.evictNext());
        assertSame(e4, policy.evictNext());
        assertSame(e5, policy.evictNext());
        assertNull(policy.evictNext());
    }

    @Test
    public void addTouchEvict() {
        AttributeMap e2 = createEntry(HITS, 2L);
        AttributeMap e3 = createEntry(HITS, 3L);
        AttributeMap e4 = createEntry(HITS, 4L);
        AttributeMap e5 = createEntry(HITS, 5L);

        policy.add(e3);
        policy.add(e4);
        policy.add(e2);
        policy.add(e5);

        set(e2, HITS, 7L);
        policy.touch(e2);
        set(e4, HITS, 8L);
        policy.touch(e4);
        assertSame(e3, policy.evictNext());
        assertSame(e5, policy.evictNext());
        assertSame(e2, policy.evictNext());
        assertSame(e4, policy.evictNext());
        assertNull(policy.evictNext());
    }
}
