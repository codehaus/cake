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
package org.codehaus.cake.cache.test.tck.asmap;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.service.ContainerShutdownException;
import org.junit.Test;

/**
 * Tests put operations for a cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Put.java 356 2009-05-02 11:59:51Z kasper $
 */
public class Put extends AbstractAsMapTCKTest {

    @Test
    public void put() {
        init();
        assertNull(put(1, "B"));
        assertSize(1);
        assertEquals("B", put(1, "C"));
        assertSize(1);
        assertEquals("C", peek(M1));
    }

    @Test
    public void putMany() {
        final int count = 500;
        Random r = new Random(1123123);
        for (int i = 0; i < count; i++) {
            int key = r.nextInt(250);
            put(key, "" + key);
        }
        assertSize(asMap().entrySet().size());
    }

    /** {@link ConcurrentMap#put(Object, Object)} lazy starts the cache. */
    @Test
    public void putLazyStart() {
        init();
        assertNotStarted();
        put(1, "B");
        assertStarted();
    }

    /** {@link ConcurrentMap#put(Object, Object)} should fail when cache is shutdown. */
    @Test(expected = ContainerShutdownException.class)
    public void putShutdownISE() {
        init(5);
        assertStarted();
        shutdown();

        put(1, "B");// should fail
    }

    @Test(expected = NullPointerException.class)
    public void putKeyNPE() {
        init();
        put(null, "A");
    }

    @Test(expected = NullPointerException.class)
    public void putValueNPE() {
        init();
        put(1, null);
    }
}
