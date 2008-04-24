/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.memorystore;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_SIZE;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.LongOps;
import org.junit.Test;

/**
 * This class tests that even if a policy is not defined in the configuration the cache will still
 * be able to evict elements.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EvictionNoPolicy.java 526 2007-12-27 01:32:16Z kasper $
 */
public class MemoryStoreReplacementPolicyNone extends AbstractCacheTCKTest {

    @Test
    public void maximumSize() {
        conf.withMemoryStore().setMaximumSize(3);
        init();
        put(5);
        assertSize(3);
        putAll(10, 15);
        assertSize(3);
    }

    @Test
    public void maximumSizeChange() {
        conf.withMemoryStore().setMaximumSize(3);
        init();
        put(5);
        assertSize(3);
        assertValidSizeAndVolume();
        withMemoryStore().setMaximumSize(6);
        putAll(10, 15);
        assertSize(6);
    }

    @Test
    public void maximumVolumeDefaultSizes() {
        conf.withAttributes().add(ENTRY_SIZE);
        conf.withMemoryStore().setMaximumVolume(3);
        init();
        put(M1, M2, M3, M4);
        assertValidSizeAndVolume();
        putAll(10, 15);
        //System.out.println(c.size() + ", " + withMemoryStore().getVolume());
        assertValidSizeAndVolume();
        assertSize(3);
        assertVolume(3);
    }

    @Test
    public void maximumVolume() {
        loader.setAttribute(ENTRY_SIZE, LongOps.add(1));// size=key+1
        conf.withAttributes().add(ENTRY_SIZE);
        conf.withMemoryStore().setMaximumVolume(7);
        init();
        assertGet(M1, M2, M3);
        assertValidSizeAndVolume();
        putAll(10, 15);
        assertValidSizeAndVolume();
    }
}
