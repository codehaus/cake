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

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests put operations for a cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class PutIfAbsent extends AbstractCacheTCKTest {

    /**
     * Tests the putIfAbsent(K key, V value) method.
     */
    @Test
    public void putIfAbsent() {
        assertNull(c.putIfAbsent(M1.getKey(), M1.getValue()));
        assertEquals(M1.getValue(), peek(M1));
        assertEquals(M1.getValue(), c.putIfAbsent(M1.getKey(), M2.getValue()));
        assertFalse(c.containsValue(M2.getValue()));
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void putIfAbsentLazyStart() {
        init();
        assertNotStarted();
        c.putIfAbsent(M1.getKey(), M1.getValue());
        checkLazystart();
    }

    /**
     * Tests that putIfAbsent(null, Object) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void putIfAbsentKeyNPE() {
        init();
        c.putIfAbsent(null, "A");
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test(expected = IllegalStateException.class)
    public void putIfAbsentShutdownISE() {
       init(5);
        assertStarted();
        shutdown();

        // should fail
        c.putIfAbsent(M1.getKey(), M1.getValue());
    }

    /**
     * Tests that putIfAbsent(Object, null) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void putIfAbsentValueNPE() {
        init();
        c.putIfAbsent(1, null);
    }
}
