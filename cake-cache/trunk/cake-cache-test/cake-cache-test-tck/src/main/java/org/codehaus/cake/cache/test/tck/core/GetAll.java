/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache.test.tck.core;

import java.util.Arrays;
import java.util.Map;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link Cache#containsKey}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class GetAll extends AbstractCacheTCKTest {

    @Before
    public void setup() {
        conf = newConfigurationClean();
    }

    @Test
    public void getAll() {
        c = newCache(4);
        Map<Integer, String> map = c.getAllOld(asList(M1.getKey(), M5.getKey(), M4.getKey()));
        assertEquals(3, map.size());
        assertEquals(M1.getValue(), map.get(M1.getKey()));
        assertTrue(map.entrySet().contains(M1));

        assertEquals(M4.getValue(), map.get(M4.getKey()));
        assertTrue(map.entrySet().contains(M4));

        assertNull(map.get(M5.getKey()));
        assertFalse(map.entrySet().contains(M5));
    }

    /**
     * {@link Cache#getAll} lazy starts the cache.
     */
    @Test
    public void getAllLazyStart() {
        init();
        assertNotStarted();
        c.getAllOld(asList(M1.getKey(), M5.getKey(), M4.getKey()));
        checkLazystart();
    }

    @Test(expected = NullPointerException.class)
    public void getAllNPE() {
       init(5);
        c.getAllOld(null);
    }

    @Test(expected = NullPointerException.class)
    public void getAllNPE1() {
       init(5);
        c.getAllOld(Arrays.asList(1, null));
    }

    /**
     * {@link Cache#get()} should not fail when cache is shutdown.
     */
    @Test(expected = IllegalStateException.class)
    public void getAllShutdownISE() {
       init(5);
        assertStarted();
        shutdown();

        // should fail
        c.getAllOld(asList(M1.getKey(), M5.getKey(), M4.getKey()));
    }

}
