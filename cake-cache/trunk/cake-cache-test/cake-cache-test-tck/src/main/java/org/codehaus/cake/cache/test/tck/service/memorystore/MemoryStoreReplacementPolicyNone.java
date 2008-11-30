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
import org.junit.Test;

/**
 * This class tests that even if a policy is not defined in the configuration the cache will still be able to evict
 * elements.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
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
        conf.addEntryAttributes(SIZE);
        conf.withMemoryStore().setMaximumVolume(3);
        init();
        put(M1, M2, M3, M4);
        assertValidSizeAndVolume();
        putAll(10, 15);
        // System.out.println(c.size() + ", " + withMemoryStore().getVolume());
        assertValidSizeAndVolume();
        assertSize(3);
        assertVolume(3);
    }

    @Test
    public void maximumVolume() {
        loader.setAttribute(SIZE, LongOps.add(1));// size=key+1
        conf.addEntryAttributes(SIZE);
        conf.withMemoryStore().setMaximumVolume(7);
        init();
        assertGet(M1, M2, M3);
        assertValidSizeAndVolume();
        putAll(10, 15);
        assertValidSizeAndVolume();
    }
}
