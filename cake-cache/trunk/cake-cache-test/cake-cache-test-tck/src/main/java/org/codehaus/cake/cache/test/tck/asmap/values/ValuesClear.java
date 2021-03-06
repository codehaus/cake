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
package org.codehaus.cake.cache.test.tck.asmap.values;

import java.util.Set;

import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class ValuesClear extends AbstractAsMapTCKTest {

    /**
     * {@link Set#clear()} removes all mappings.
     */
    @Test
    public void clearValues() {
       init(5);
        assertEquals(asMap().values().size(), 5);
        assertFalse(asMap().values().isEmpty());
        assertSize(5);
        assertIsNotEmpty();


        asMap().values().clear();

        assertEquals(asMap().values().size(), 0);
        assertTrue(asMap().values().isEmpty());
        assertSize(0);
        assertIsEmpty();
    }

    /**
     * {@link Set#clear()} lazy starts the cache.
     */
    @Test
    public void clearLazyStart() {
        init();
        assertNotStarted();
        asMap().values().clear();
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
        asMap().values().clear();
    }
}
