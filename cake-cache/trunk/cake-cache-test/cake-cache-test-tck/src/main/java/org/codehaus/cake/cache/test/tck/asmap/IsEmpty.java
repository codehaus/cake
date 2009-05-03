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
import org.junit.Test;

/**
 * Tests {@link ConcurrentMap#IsEmpty(Object)}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Get.java 327 2009-04-08 09:34:27Z kasper $
 */
public class IsEmpty extends AbstractAsMapTCKTest {

    /** {@link ConcurrentMap#IsEmpty(Object)} is true for empty map and false for non-empty. */
    @Test
    public void isEmptyCache() {
        init(0);
        assertIsEmpty();
        init(5);
        assertIsNotEmpty();
    }

    /** {@link ConcurrentMap#isEmpty()} lazy starts the cache. */
    @Test
    public void isEmptyLazyStart() {
        init();
        asMap().isEmpty();
        assertStarted();
    }

    /** {@link ConcurrentMap#isEmpty()} should not fail when cache is shutdown. */
    @Test
    public void isEmptyShutdown() {
        init(5);
        assertStarted();
        shutdown();
        isEmpty();// shouldnt fail
        awaitTermination();
        assertIsEmpty();
    }
}
