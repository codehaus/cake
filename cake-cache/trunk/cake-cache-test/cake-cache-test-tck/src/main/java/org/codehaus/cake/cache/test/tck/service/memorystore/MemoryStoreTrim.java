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

import org.codehaus.cake.cache.policy.Policies;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.LongOps;
import org.junit.Test;

public class MemoryStoreTrim extends AbstractCacheTCKTest {

    /**
     * Tests trimToSize.
     */
    @Test
    public void trimToSize() {
        conf.withMemoryStore().setPolicy(Policies.LRU);
        init();
        put(5);
        assertSize(5);
        withMemoryStore().trimToSize(6);
        assertSize(5);

        withMemoryStore().trimToSize(5);
        assertSize(5);
        withMemoryStore().trimToSize(3);
        assertSize(3);
        assertFalse(c.containsKey(1));
        assertFalse(c.containsKey(2));
        assertTrue(c.containsKey(3));
        c.get(3);
        withMemoryStore().trimToSize(1);
        assertSize(1);
        assertTrue(c.containsKey(3));
    }

    /**
     * Tests trimToSize.
     */
    @Test
    public void trimToSize2() {
        init();
        put(5);
        assertSize(5);
        withMemoryStore().trimToSize(3);
        assertSize(3);
        put(10, 15);
        assertSize(9);
        withMemoryStore().trimToSize(1);
        assertSize(1);
    }

    /**
     * Tests trimToSize.
     */
    @Test
    public void trimToSizeNegative() {
        conf.withMemoryStore().setPolicy(Policies.LRU);
        init();
        put(5);
        assertSize(5);
        withMemoryStore().trimToSize(-2);
        assertSize(3);
        assertFalse(c.containsKey(1));
        assertFalse(c.containsKey(2));
        assertTrue(c.containsKey(3));
        c.get(3);
        withMemoryStore().trimToSize(Integer.MIN_VALUE);
        assertSize(0);
    }

    @Test
    public void trimToVolume() {
        loader.setAttribute(SIZE, LongOps.add(1));// size=key+1
        conf.addEntryAttributes(SIZE);
        conf.withMemoryStore().setPolicy(Policies.LRU);
        init();
        c.get(1);
        assertVolume(2);
        c.get(2);
        assertVolume(5);
        c.get(4);
        assertVolume(10);
        c.get(3);
        c.get(5);
        assertVolume(20);

        withMemoryStore().trimToVolume(21);
        assertVolume(20);

        withMemoryStore().trimToVolume(20);
        assertVolume(20);

        withMemoryStore().trimToVolume(19);
        assertFalse(c.containsKey(1));
        assertVolume(18);

        withMemoryStore().trimToVolume(12);
        assertVolume(10);

        withMemoryStore().trimToVolume(3);
        assertVolume(0);

        withMemoryStore().trimToVolume(0);
        assertVolume(0);

    }

    @Test
    public void trimToVolumeNegative() {
        loader.setAttribute(SIZE, LongOps.add(1));// size=key+1
        conf.addEntryAttributes(SIZE);
        conf.withMemoryStore().setPolicy(Policies.LRU);
        init();
        c.get(1);
        assertVolume(2);
        c.get(2);
        assertVolume(5);
        c.get(4);
        assertVolume(10);
        c.get(3);
        c.get(5);
        assertVolume(20);

        withMemoryStore().trimToVolume(-4);
        assertVolume(15);

        withMemoryStore().trimToVolume(-2);
        assertVolume(10);

        withMemoryStore().trimToVolume(Long.MIN_VALUE);
        assertVolume(0);
    }
}
