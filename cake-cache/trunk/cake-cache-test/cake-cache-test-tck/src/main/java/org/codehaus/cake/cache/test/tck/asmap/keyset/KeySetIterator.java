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

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_KEY_SET;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.codehaus.cake.cache.test.tck.asmap.AbstractAsMapTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class KeySetIterator extends AbstractAsMapTCKTest {

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
        Iterator<Integer> iter = asMap().keySet().iterator();
        while (iter.hasNext()) {
            assertTrue(M1_TO_M5_KEY_SET.contains(iter.next()));
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
        asMap().keySet().iterator();
        checkLazystart();
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorNextNSE() {
       init(1);
        Iterator<Integer> iter = asMap().keySet().iterator();
        iter.next();
        iter.next();
    }

    @Test
    public void iteratorCME() {
        try {
       init(1);
        Iterator<Integer> iter = asMap().keySet().iterator();
        Iterator<Integer> iter2 = asMap().keySet().iterator();
        iter.next();
        iter.remove();
        //It is actually not stricly required that we throw
        //An CME but all implementations does it for now.
        //Let us keep it in
        iter2.next();
        } catch (ConcurrentModificationException ok) {
            
        }
    }

    @Test
    public void iteratorRemove() {
       init(5);
        Iterator<Integer> iter = asMap().keySet().iterator();
        while (iter.hasNext()) {
            int next = iter.next();
            if (next % 2 == 0) {
                iter.remove();
            }
        }
        assertSize(3);
        assertTrue(asMap().keySet().contains(M1.getKey()));
        assertFalse(asMap().keySet().contains(M2.getKey()));
        assertTrue(asMap().keySet().contains(M3.getKey()));
        assertFalse(asMap().keySet().contains(M4.getKey()));
        assertTrue(asMap().keySet().contains(M5.getKey()));
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE() {
        newCache();asMap().keySet().iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE1() {
        newCache(1);asMap().keySet().iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE2() {
       init(1);
        Iterator<Integer> iter = asMap().keySet().iterator();
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
        assertFalse(asMap().keySet().iterator().hasNext());
    }
}
