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
package org.codehaus.cake.cache.test.tck.core;

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_NULL;
import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN1;
import static org.codehaus.cake.test.util.CollectionTestUtil.NULL_A;
import static org.codehaus.cake.test.util.CollectionTestUtil.asMap;

import java.util.Map;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests put operations for a cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: PutAll.java 526 2007-12-27 01:32:16Z kasper $
 */
public class PutAll extends AbstractCacheTCKTest {

    @SuppressWarnings("unchecked")
    @Test
    public void putAll() {
        init();
        c.putAll(asMap(M1, M5));
        assertEquals(2, c.size());
        assertTrue(c.entrySet().contains(M1));
        assertTrue(c.entrySet().contains(M5));

        c.putAll(asMap(M1, M5));
        assertEquals(2, c.size());

        c.putAll(asMap(MNAN1, M4));
        assertEquals(3, c.size());
        assertFalse(c.entrySet().contains(M1));

    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void putAllLazyStart() {
        init();
        assertFalse(c.isStarted());
        c.putAll(asMap(M1, M5));
        checkLazystart();
    }

    /**
     * {@link Cache#putAll(Map)} should fail when the cache is shutdown.
     */
    @Test(expected = IllegalStateException.class)
    public void putAllShutdownISE() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();

        // should fail
        c.putAll(asMap(M1, M5));
    }

    @Test(expected = NullPointerException.class)
    public void putAllNPE() {
        init();
        putAll((Map.Entry) null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void putAllKeyMappingNPE() {
        init();
        c.putAll(asMap(M1, NULL_A));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void putAllValueMappingNPE() {
        init();
        c.putAll(asMap(M1, M1_NULL));
    }
}
