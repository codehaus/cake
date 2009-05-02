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
package org.codehaus.cake.cache.test.tck.asmap.entryset;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * Tests {@link Cache#entrySet()} lazy start and shutdown.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class EntrySet extends AbstractAsMapTCKTest {

    /**
     * Calls to {@link Cache#entrySet} should not start the cache.
     */
    @Test
    public void noLazyStart() {
        init();
        asMap().entrySet();
        assertNotStarted();
    }

    /**
     * Calls to {@link Cache#entrySet} should not fail when the cache is shutdown.
     */
    @Test
    public void noFailOnShutdown() {
        init(5).assertStarted();
        shutdown();
        asMap().entrySet();
        awaitTermination();
        asMap().entrySet();
    }
}
