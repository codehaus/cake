/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.memorystore;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_SIZE;

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
        conf.withMemoryStore().setPolicy(Policies.newLRU());
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
        conf.withMemoryStore().setPolicy(Policies.newLRU());
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
        loader.setAttribute(ENTRY_SIZE, LongOps.add(1));// size=key+1
        conf.withAttributes().add(ENTRY_SIZE);
        conf.withMemoryStore().setPolicy(Policies.newLRU());
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
        loader.setAttribute(ENTRY_SIZE, LongOps.add(1));// size=key+1
        conf.withAttributes().add(ENTRY_SIZE);
        conf.withMemoryStore().setPolicy(Policies.newLRU());
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
