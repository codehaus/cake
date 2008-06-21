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
        assertSize(2);// don't remove existing items
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
        // assertEquals(M1.getValue(), getEntry(M1).getValue());??
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
        withLoading().load(1);
        awaitFinishedThreads();
        assertSize(1);
        withLoadingForced().load(2);
        awaitFinishedThreads();
        assertSize(2);
        withMemoryStore().setDisabled(true);
        assertTrue(withMemoryStore().isDisabled());
        withLoading().loadAll(Arrays.asList(1, 2, 3, 4));
        withLoadingForced().loadAll(Arrays.asList(1, 2, 3, 4));
        awaitFinishedThreads();
        assertSize(2);
    }

    @Test
    public void load() {
        conf.withMemoryStore().setDisabled(true);
        init();
        withLoading().load(1);
        awaitFinishedThreads();
        assertSize(0);
        withLoadingForced().load(2);
        awaitFinishedThreads();
        assertSize(0);
        withLoading().loadAll(Arrays.asList(3, 4));
        awaitFinishedThreads();
        assertSize(0);
    }
}
