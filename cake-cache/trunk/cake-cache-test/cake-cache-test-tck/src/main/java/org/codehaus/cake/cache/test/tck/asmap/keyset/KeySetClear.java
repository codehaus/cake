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
package org.codehaus.cake.cache.test.tck.asmap.keyset;

import java.util.Set;

import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class KeySetClear extends AbstractAsMapTCKTest {

    /**
     * {@link Set#clear()} removes all mappings.
     */
    @Test
    public void clearKeySet() {
       init(5);
        assertEquals(asMap().keySet().size(), 5);
        assertFalse(asMap().keySet().isEmpty());
        assertSize(5);
        assertIsNotEmpty();

        asMap().keySet().clear();

        assertEquals(asMap().keySet().size(), 0);
        assertTrue(asMap().keySet().isEmpty());
        assertSize(0);
        assertIsEmpty();
    }

    /**
     * {@link Set#clear()} lazy starts the cache.
     */
    @Test
    public void clearLazyStart() {
        init();
        asMap().keySet().clear();
        checkLazystart();
    }

    /**
     * {@link Set#clear()} fails when the cache is shutdown.
     */
    @Test
    public void clearShutdown() {
       init(5);
        assertStarted();
        shutdown();
        asMap().keySet().clear();
    }
}
