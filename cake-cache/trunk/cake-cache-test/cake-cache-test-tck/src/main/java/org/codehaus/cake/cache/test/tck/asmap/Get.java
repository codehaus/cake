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

import org.codehaus.cake.service.ContainerShutdownException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ConcurrentMap#get(Object)}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Get.java 327 2009-04-08 09:34:27Z kasper $
 */
public class Get extends AbstractAsMapTCKTest {

    @Before
    public void setup() {
        conf = newConfigurationClean();
    }

    /** Test simple get. */
    @Test
    public void get() {
        init(5);
        assertGet(M1);
        assertGet(M5);
        assertGet(M6, null);
    }

    /** {@link ConcurrentMap#get} lazy starts the cache. */
    @Test
    public void getLazyStart() {
        init();
        assertNotStarted();
        assertGet(M6, null);
        assertStarted();
    }

    /** {@link ConcurrentMap#get} throws {@link NullPointerException} for <tt>null</tt> key. */
    @Test(expected = NullPointerException.class)
    public void getNPE() {
        init(5);
        get(null);
    }

    /** {@link ConcurrentMap#containsKey()} should not fail when cache is shutdown. */
    @Test(expected = ContainerShutdownException.class)
    public void getShutdownISE() {
        init(5);
        assertStarted();
        shutdown();
        get(M1); // should fail after shutdown has been called
    }

}
