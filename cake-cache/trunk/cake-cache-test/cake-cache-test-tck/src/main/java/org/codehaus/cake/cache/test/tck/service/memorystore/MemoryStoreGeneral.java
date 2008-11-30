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

import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests EvictionService.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class MemoryStoreGeneral extends AbstractCacheTCKTest {

    @Test
    public void testServiceAvailable() {
        assertTrue(c.hasService(MemoryStoreService.class));
        assertNotNull(c.getService(MemoryStoreService.class));
    }

    /**
     * Tests maximum capacity.
     */
    @Test
    public void maximumVolume() {
        assertEquals(Long.MAX_VALUE, withMemoryStore().getMaximumVolume());
        withMemoryStore().setMaximumVolume(1000);
        assertEquals(1000, withMemoryStore().getMaximumVolume());

        // start value
        conf.withMemoryStore().setMaximumVolume(5000);
        init();
        assertEquals(5000, withMemoryStore().getMaximumVolume());
    }

    /**
     * Tests maximum capacity IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void maximumCapacityIAE() {
        withMemoryStore().setMaximumVolume(-1);
    }

    /**
     * Tests maximum capacity.
     */
    @Test
    public void maximumSize() {
        assertEquals(Integer.MAX_VALUE, withMemoryStore().getMaximumSize());
        withMemoryStore().setMaximumSize(1000);
        assertEquals(1000, withMemoryStore().getMaximumSize());

        // start value
        conf.withMemoryStore().setMaximumSize(5000);
        init();
        assertEquals(5000, withMemoryStore().getMaximumSize());
    }

    /**
     * Tests maximum capacity IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void maximumSizeIAE() {
        withMemoryStore().setMaximumSize(-1);
    }

}
