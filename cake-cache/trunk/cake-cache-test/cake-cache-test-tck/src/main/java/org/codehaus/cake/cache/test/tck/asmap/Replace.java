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

import java.util.concurrent.ConcurrentMap;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.service.ContainerShutdownException;
import org.junit.Test;

/**
 * Tests {@link ConcurrentMap#replace(Object, Object)} and {@link ConcurrentMap#replace(Object, Object, Object)}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Replace.java 327 2009-04-08 09:34:27Z kasper $
 */
public class Replace extends AbstractAsMapTCKTest {

    @Test
    public void replace2() {
        init(1);
        assertNull(replace(M2.getKey(), M2.getValue()));
        init(2);
        assertEquals(M1.getValue(), replace(M1.getKey(), M3.getValue()));
        assertEquals(M3.getValue(), get(M1));
        assertNull(replace(M4.getKey(), M4.getValue()));
        assertNotContainsValue(M4);
        assertNotContainsValue(M1);
    }

    /**
     * replace(Key, null) throws {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void replace2NPE1() {
        init(1);
        replace(M1.getKey(), null);
    }

    /**
     * replace(null, Value) throws {@link NullPointerException}.
     */
    @Test(expected = NullPointerException.class)
    public void replace2NPE2() {
        init(1);
        replace(null, M2.getValue());
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void replace2LazyStart() {
        init();
        assertNotStarted();
        replace(M2.getKey(), M2.getValue());
        assertStarted();
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test(expected = ContainerShutdownException.class)
    public void replace2ShutdownISE() {
        init(5);
        assertStarted();
        shutdown();

        // should fail
        replace(M2.getKey(), M2.getValue());
    }

    @Test
    public void replace3() {
        init(2);
        assertTrue(replace(M1.getKey(), M1.getValue(), M3.getValue()));
        assertEquals(M3.getValue(), get(M1));
        assertFalse(replace(M1.getKey(), M1.getValue(), M3.getValue()));
        assertNotContainsValue(M1);
    }

    /**
     * {@link Cache#put(Object, Object)} lazy starts the cache.
     */
    @Test
    public void replace3LazyStart() {
        init();
        assertNotStarted();
        replace(M1.getKey(), M1.getValue(), M3.getValue());
        assertStarted();
    }

    /**
     * {@link Cache#containsKey()} should not fail when cache is shutdown.
     */
    @Test(expected = IllegalStateException.class)
    public void replace3ShutdownISE() {
        init(5);
        assertStarted();
        shutdown();

        // should fail
        replace(M1.getKey(), M1.getValue(), M3.getValue());
    }

    /** replace(Key,null) throws NPE. */
    @Test(expected = NullPointerException.class)
    public void replace3OldValueNPE() {
        init(1);
        replace(M1.getKey(), null, M2.getValue());
    }

    /** replace(null,Value) throws NPE. */
    @Test(expected = NullPointerException.class)
    public void replace3NewValueNPE() {
        init(1);
        replace(M1.getKey(), M1.getValue(), null);
    }

    /** replace(null,oldValue,newValue) throws NPE. */
    @Test(expected = NullPointerException.class)
    public void replace3KeyNPE() {
        init(1);
        replace(null, M1.getValue(), M2.getValue());
    }

}
