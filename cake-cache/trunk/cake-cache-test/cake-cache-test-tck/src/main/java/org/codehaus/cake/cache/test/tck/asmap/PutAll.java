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

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_NULL;
import static org.codehaus.cake.test.util.CollectionTestUtil.MNAN1;
import static org.codehaus.cake.test.util.CollectionTestUtil.NULL_A;

import java.util.Map;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.service.ContainerShutdownException;
import org.junit.Test;

/**
 * Tests putAll operations for a cache.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: PutAll.java 356 2009-05-02 11:59:51Z kasper $
 */
public class PutAll extends AbstractAsMapTCKTest {

    @Test
    public void putAll() {
        init();
        putAll(M1, M5);
        assertSize(2);
        assertTrue(entrySet().contains(M1));
        assertTrue(entrySet().contains(M5));

        putAll(M1, M5);
        assertSize(2);
        assertSize(2);

        putAll(MNAN1, M4);
        assertSize(3);
        assertFalse(entrySet().contains(M1));

    }

    /** {@link ConcurrentMap#put(Object, Object)} lazy starts the cache. */
    @Test
    public void putAllLazyStart() {
        init();
        assertNotStarted();
        putAll(M1, M5);
        assertStarted();
    }

    /** {@link ConcurrentMap#putAll(Map)} should fail when the cache is shutdown. */
    @Test(expected = ContainerShutdownException.class)
    public void putAllShutdownISE() {
        init(5);
        assertStarted();
        shutdown();

        putAll(M1, M5); // should fail
    }

    @Test(expected = NullPointerException.class)
    public void putAllNPE() {
        init();
        asMap().putAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void putAllKeyMappingNPE() {
        init();
        putAll(M1, NULL_A);
    }

    @Test(expected = NullPointerException.class)
    public void putAllValueMappingNPE() {
        init();
        putAll(M1, M1_NULL);
    }
}
