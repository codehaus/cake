/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.memorystore;

import java.util.Arrays;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class MemoryStoreCachingDisabled extends AbstractCacheTCKTest {

    @Test
    public void put() {
        conf.withMemoryStore().setDisabled(true);
        init();
        assertTrue(withMemoryStore().isDisabled());
        put(M1);
        assertSize(0);
        putIfAbsent(M2);
        assertSize(0);
        put(entry(M1, "C"));
        assertSize(0);
        putAll(M2, M3);
        assertSize(0);
    }

    @Test
    public void putDisable() {
        assertFalse(withMemoryStore().isDisabled());
        put(M1);
        assertSize(1);
        putIfAbsent(M2);
        assertSize(2);
        withMemoryStore().setDisabled(true);
        assertTrue(withMemoryStore().isDisabled());
        assertSize(2);//don't remove existing items
        put(entry(M1, "C"));
        assertGet(M1);
        assertSize(2);
        putAll(M2, M3);
        assertSize(2);

    }

    @Test
    public void get() {
        conf.withMemoryStore().setDisabled(true);
        init();
        assertGet(entry(M1, null));
        assertSize(0);
        assertGet(entry(M1, null), entry(M2, null));
        assertSize(0);
        assertGetAll(entry(M2, null), entry(M3, null), entry(M4, null));
        assertSize(0);
        //assertEquals(M1.getValue(), getEntry(M1).getValue());??
        assertSize(0);
        assertEquals(6, loader.totalLoads());
    }

    @Test
    public void getDisable() {
        assertGet(M1);
        assertSize(1);
        assertGet(M1, M2);
        assertSize(2);
        withMemoryStore().setDisabled(true);
        assertTrue(withMemoryStore().isDisabled());
        assertEquals(2, loader.totalLoads());
        getAll(M2, M3, M4);
        assertSize(2);
        assertEquals(4, loader.totalLoads());
        getAll(M4, M5);
        assertSize(2);
        assertEquals(6, loader.totalLoads());
    }

    @Test
    public void loadDisable() {
        withLoading().withKey(1).load();
        awaitFinishedThreads();
        assertSize(1);
        withLoading().withKey(2).forceLoad();
        awaitFinishedThreads();
        assertSize(2);
        withMemoryStore().setDisabled(true);
        assertTrue(withMemoryStore().isDisabled());
        withLoading().withKeys(Arrays.asList(1, 2, 3, 4)).load();
        withLoading().withKeys(Arrays.asList(1, 2, 3, 4)).forceLoad();
        awaitFinishedThreads();
        assertSize(2);
    }

    @Test
    public void load() {
        conf.withMemoryStore().setDisabled(true);
        init();
        withLoading().withKey(1).load();
        awaitFinishedThreads();
        assertSize(0);
        withLoading().withKey(2).forceLoad();
        awaitFinishedThreads();
        assertSize(0);
        withLoading().withKeys(Arrays.asList(3, 4)).load();
        awaitFinishedThreads();
        assertSize(0);
    }
}
