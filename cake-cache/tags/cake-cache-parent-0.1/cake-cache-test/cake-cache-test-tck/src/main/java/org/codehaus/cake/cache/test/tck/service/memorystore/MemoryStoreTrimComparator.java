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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.LongOps;
import org.junit.Test;

public class MemoryStoreTrimComparator extends AbstractCacheTCKTest {

    /**
     * Tests trimToSize.
     */
    @Test
    public void trimToSizeComparator() {
        put(10);
        assertSize(10);
        withMemoryStore().trimToSize(5, new Comparator<CacheEntry<Integer, String>>() {
            public int compare(CacheEntry<Integer, String> o1, CacheEntry<Integer, String> o2) {
                return o2.getKey() - o1.getKey();
            }
        });
        assertSize(5);
        assertEquals(c.keySet(), new HashSet(Arrays.asList(1, 2, 3, 4, 5)));
        put(10);
        assertSize(10);
        Set<Integer> s = new HashSet(c.keySet());
        withMemoryStore().trimToSize(-5, new Comparator<CacheEntry<Integer, String>>() {

            public int compare(CacheEntry<Integer, String> o1, CacheEntry<Integer, String> o2) {
                return o1.getKey() - o2.getKey();
            }
        });
        s.addAll(c.keySet());
        assertEquals(10, s.size());
        assertSize(5);
    }

    @Test(expected = NullPointerException.class)
    public void trimToSizeComparatorNPE() {
        put(10);
        assertSize(10);
        withMemoryStore().trimToSize(5, null);
    }

    /**
     * Tests trimToSize.
     */
    @Test
    public void trimToVolumeComparator() {
        loader.add(M1, M2, M3, M4, M5, M6, M7, M8, M9);
        loader.setAttribute(SIZE, LongOps.add(1));// size=key+1
        conf.withAttributes().add(SIZE);
        conf.withMemoryStore().setPolicy(Policies.newLRU());
        init();
        assertGet(M1, M2, M3, M4, M5, M6, M7, M8, M9);
        assertSize(9);
        assertGet(M2, M4, M6, M8);
        withMemoryStore().trimToVolume(19, new Comparator<CacheEntry<Integer, String>>() {

            public int compare(CacheEntry<Integer, String> o1, CacheEntry<Integer, String> o2) {
                return o2.getKey() - o1.getKey();
            }
        });
        assertSize(4);
        assertVolume(14);
        assertGet(M1, M2, M3, M4, M5, M6, M7, M8, M9);
        assertSize(9);
        withMemoryStore().trimToVolume(30, new Comparator<CacheEntry<Integer, String>>() {

            public int compare(CacheEntry<Integer, String> o1, CacheEntry<Integer, String> o2) {
                return o1.getKey() - o2.getKey();
            }
        });
        assertSize(3);
        assertVolume(27);
    }

    @Test(expected = NullPointerException.class)
    public void trimToVolumeComparatorNPE() {
        put(10);
        assertSize(10);
        withMemoryStore().trimToVolume(5, null);
    }

}
