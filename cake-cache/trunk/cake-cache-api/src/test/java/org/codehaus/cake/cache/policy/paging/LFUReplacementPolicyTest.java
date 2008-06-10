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
public class LFUReplacementPolicyTest {
    LFUReplacementPolicy<Integer, String> policy;

    @Before
    public void setUp() {
        policy = new LFUReplacementPolicy<Integer, String>();
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
        assertSame(e2, policy.evictNext());
        assertSame(e3, policy.evictNext());
        assertSame(e4, policy.evictNext());
        assertSame(e5, policy.evictNext());
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
        assertSame(e3, policy.evictNext());
        assertSame(e5, policy.evictNext());
        assertSame(e2, policy.evictNext());
        assertSame(e4, policy.evictNext());
        assertNull(policy.evictNext());
    }
}
