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
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests {@link Cache#containsKey}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class GetEntry extends AbstractCacheTCKTest {

    @Test
    public void getEntry() {
       init(5);
        assertNull(c.getEntry(6));
        assertEquals(M1.getValue(), c.getEntry(M1.getKey()).getValue());
        assertEquals(M1.getKey(), c.getEntry(M1.getKey()).getKey());
        assertEquals(M5.getValue(), c.getEntry(M5.getKey()).getValue());
        assertEquals(M5.getKey(), c.getEntry(M5.getKey()).getKey());
    }

    /**
     * {@link Cache#get} lazy starts the cache.
     */
    @Test
    public void getLazyStart() {
        init();
        assertNotStarted();
        c.getEntry(M6.getKey());
        checkLazystart();
    }

    /**
     * get(null) throws NPE.
     */
    @Test(expected = NullPointerException.class)
    public void getNPE() {
       init(5);
        c.getEntry(null);
    }

    /**
     * {@link Cache#getEntry(Object)} should not fail when cache is shutdown.
     * 
     */
    @Test(expected = IllegalStateException.class)
    public void getShutdownISE() {
       init(5);
        assertStarted();
        shutdown();

        // should fail
        c.getEntry(M1.getKey());
    }

    @Test
    public void setValue() {
        init(5);
        CacheEntry<Integer, String> ce = c.getEntry(M1.getKey());
        try {
            ce.setValue("foo");
            assertEquals("foo", get(M1));
        } catch (UnsupportedOperationException ok) {}
    }

    @Test
    public void get() {
        newConfigurationClean();
        init();
        init();
        assertNull(c.getEntry(M1.getKey()));
       init(1);
        CacheEntry<Integer, String> ce = c.getEntry(M1.getKey());
        assertEquals(M1.getKey(), ce.getKey());
        assertEquals(M1.getValue(), ce.getValue());
    }

    @Test
    public void put() {
        newConfigurationClean();
        init();
        init();
        assertNull(c.getEntry(M1.getKey()));
       init(1);
        CacheEntry<Integer, String> ce = c.getEntry(M1.getKey());
        c.put(M1.getKey(), M2.getValue());
        ce = c.getEntry(M1.getKey());
        assertEquals(M1.getKey(), ce.getKey());
        assertEquals(M2.getValue(), ce.getValue());
    }
}
