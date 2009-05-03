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

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_SET;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class EntrySetIterator extends AbstractAsMapTCKTest {

    @Test
    @SuppressWarnings("unused")
    public void iterator() {
        int count = 0;
        init();
        for (Integer entry : asMap().keySet()) {
            count++;
        }
        assertEquals(0, count);
       init(5);
        Iterator<Map.Entry<Integer, String>> iter = asMap().entrySet().iterator();
        while (iter.hasNext()) {
            assertTrue(M1_TO_M5_SET.contains(iter.next()));
            count++;
        }
        assertEquals(5, count);
    }

    /**
     * {@link Set#iterator()} lazy starts the cache.
     */
    @Test
    public void iteratorLazyStart() {
        init();
        asMap().entrySet().iterator();
        checkLazystart();
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorNextNSE() {
       init(1);
        Iterator<Map.Entry<Integer, String>> iter = asMap().entrySet().iterator();
        iter.next();
        iter.next();
    }

    @Test
    public void iteratorRemove() {
       init(5);
        Iterator<Map.Entry<Integer, String>> iter = asMap().entrySet().iterator();
        while (iter.hasNext()) {
            int next = iter.next().getKey();
            if (next % 2 == 0) {
                iter.remove();
            }
        }
        assertSize(3);
        assertTrue(asMap().entrySet().contains(M1));
        assertFalse(asMap().entrySet().contains(M2));
        assertTrue(asMap().entrySet().contains(M3));
        assertFalse(asMap().entrySet().contains(M4));
        assertTrue(asMap().entrySet().contains(M5));
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE() {
        newCache();
        asMap().entrySet().iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE1() {
        newCache(1);asMap().entrySet().iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE2() {
       init(1);
        Iterator<Map.Entry<Integer, String>> iter = asMap().entrySet().iterator();
        iter.next();
        iter.remove();
        iter.remove();// should throw
    }

    /**
     * {@link Set#iterator()} fails when the cache is shutdown.
     */
    @Test
    public void iteratorShutdown() {
       init(5);
        assertStarted();
        shutdown();
        asMap().entrySet().iterator();
    }
}
