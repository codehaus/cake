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

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_NULL;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class EntrySetRetain extends AbstractAsMapTCKTest {

    /**
     * {@link Set#clear()} lazy starts the cache.
     */
    @Test
    public void retainAllLazyStart() {
        c = newCache(0);
        assertFalse(c.isStarted());
        asMap().entrySet().retainAll(Collections.singleton(M1));
        checkLazystart();
    }

    @Test
    public void retainAll() {
        c = newCache(1);
        asMap().entrySet().retainAll(Collections.singleton(M1));
        assertSize(1);
        asMap().entrySet().retainAll(Collections.singleton(M1_NULL));
        assertSize(0);
        c = newCache(1);
        asMap().entrySet().retainAll(Collections.singleton(M2));
        assertSize(0);
        c = newCache(5);
        asMap().entrySet().retainAll(Arrays.asList(M1, "F", M3, "G", M5));
        assertSize(3);
        assertTrue(asMap().entrySet().contains(M1) && asMap().entrySet().contains(M3) && asMap().entrySet().contains(M5));

    }

    @Test(expected = NullPointerException.class)
    public void retainAllNPE() {
        newCache(5);asMap().entrySet().retainAll(null);
    }
}
