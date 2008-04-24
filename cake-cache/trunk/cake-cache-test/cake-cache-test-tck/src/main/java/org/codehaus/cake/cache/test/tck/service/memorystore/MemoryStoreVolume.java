package org.codehaus.cake.cache.test.tck.service.memorystore;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_SIZE;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.LongOps;
import org.junit.Before;
import org.junit.Test;

public class MemoryStoreVolume extends AbstractCacheTCKTest {
    @Before
    public void before() {
        conf.withAttributes().add(ENTRY_SIZE);
        loader.setAttribute(ENTRY_SIZE, LongOps.add(1));// size=key+1
        init();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void volume() {
        assertGet(M1);
        assertVolume(2);
        assertGet(M3);
        assertVolume(6);
        assertGet(M5);
        assertVolume(12);
    }

    public void remove() {
        assertGet(M1, M3, M5);
        assertVolume(12);
        remove(M3);
        assertSize(2);
        assertVolume(8);
        remove(M1);
        assertVolume(6);
        remove(M5);
        assertVolume(0);
    }

    @Test
    public void update() {
        assertGet(M1, M3, M5);
        assertVolume(12);
        loader.setAttribute(ENTRY_SIZE, LongOps.add(2));// size=key+1
        forceLoad(M3);
        assertVolume(13);
        forceLoadAll();
        assertVolume(15);
        assertGet(M1, M3, M5);
        assertVolume(15);
    }

    @Test
    public void clearCache() {
        assertGet(M1, M3, M5);
        assertVolume(12);
        c.clear();
        assertVolume(0);
    }

    @Test
    public void defaultSizes() {
        newConfigurationClean();
        conf.withLoading().setLoader(loader);
        conf.withAttributes().add(ENTRY_SIZE);
        init();
        put(M1);
        assertVolume(1);
        putAll(M1, M2);
        assertVolume(2);
        loader.withLoader(M3).addAttribute(ENTRY_SIZE, 8l);
        get(M3);
        assertVolume(10);
        c.clear();
        assertVolume(0);
    }
}
