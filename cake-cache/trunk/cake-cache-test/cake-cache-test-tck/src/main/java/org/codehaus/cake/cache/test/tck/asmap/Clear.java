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

import org.junit.Test;

/**
 * This class tests the {@link ConcurrentMap#clear()} operation.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Clear.java 327 2009-04-08 09:34:27Z kasper $
 */
public class Clear extends AbstractAsMapTCKTest {

    /** {@link ConcurrentMap#clear()} removes all mappings. */
    @Test
    public void clearRemoves() {
        init(5);
        assertSize(5);
        clear();
        assertSize(0);
    }

    /** {@link ConcurrentMap#clear()} lazy starts the cache. */
    @Test
    public void clearLazyStart() {
        assertNotStarted();
        clear();
        assertStarted();
    }

    /** {@link ConcurrentMap#clear()} should not fail when cache has been shutdown */
    @Test
    public void clearShutdown() {
        init(5);
        shutdown();
        clear();
        awaitTermination();
        clear();
    }
}
