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

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.LongOps;
import org.junit.Before;
import org.junit.Test;

public class MemoryStoreVolume extends AbstractCacheTCKTest {
    @Before
    public void before() {
        conf.withAttributes().add(SIZE);
        loader.setAttribute(SIZE, LongOps.add(1));// size=key+1
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
        loader.setAttribute(SIZE, LongOps.add(2));// size=key+1
        forceLoad(M3);
        assertVolume(13);
        forceLoadAll();
        awaitFinishedThreads();
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
        conf.withAttributes().add(SIZE);
        init();
        put(M1);
        assertVolume(1);
        putAll(M1, M2);
        assertVolume(2);
        loader.withLoader(M3).addAttribute(SIZE, 8l);
        get(M3);
        assertVolume(10);
        c.clear();
        assertVolume(0);
    }

    /**
     * Previous bug, where we did not trim the cache when doing a replace.
     */
    @Test
    public void replace() {
        newConfigurationClean();
        conf.withLoading().setLoader(loader);
        conf.withAttributes().add(SIZE);
        conf.withMemoryStore().setMaximumVolume(2);
        init();
        loader.setAttribute(SIZE, LongOps.add(-1));// size=key+1
        get(M1);
        get(M3);
        c.replace(M1.getKey(), "value");
        assertValidSizeAndVolume();
    }
    
    @Test
    public void volumeZeroShutdown() {
        newConfigurationClean();
        conf.withLoading().setLoader(loader);
        conf.withAttributes().add(SIZE);
        init();
        c.withCrud().write().put(1, "2", SIZE.singleton(4));
        assertVolume(4);
        shutdownAndAwaitTermination();
        assertVolume(0);
    }
}
