/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache.test.tck.core.keyset;

import static org.codehaus.cake.test.util.CollectionTestUtil.M1_TO_M5_KEY_SET;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class KeySetIterator extends AbstractCacheTCKTest {

    @Test
    @SuppressWarnings("unused")
    public void iterator() {
        int count = 0;
        init();
        for (Integer entry : c.keySet()) {
            count++;
        }
        assertEquals(0, count);
        c = newCache(5);
        Iterator<Integer> iter = c.keySet().iterator();
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
        c = newCache(0);
        c.keySet().iterator();
        checkLazystart();
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorNextNSE() {
        c = newCache(1);
        Iterator<Integer> iter = c.keySet().iterator();
        iter.next();
        iter.next();
    }

    @Test
    public void iteratorCME() {
        try {
        c = newCache(1);
        Iterator<Integer> iter = c.keySet().iterator();
        Iterator<Integer> iter2 = c.keySet().iterator();
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
        c = newCache(5);
        Iterator<Integer> iter = c.keySet().iterator();
        while (iter.hasNext()) {
            int next = iter.next();
            if (next % 2 == 0) {
                iter.remove();
            }
        }
        assertEquals(3, c.size());
        assertTrue(c.keySet().contains(M1.getKey()));
        assertFalse(c.keySet().contains(M2.getKey()));
        assertTrue(c.keySet().contains(M3.getKey()));
        assertFalse(c.keySet().contains(M4.getKey()));
        assertTrue(c.keySet().contains(M5.getKey()));
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE() {
        newCache().keySet().iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE1() {
        newCache(1).keySet().iterator().remove();
    }

    @Test(expected = IllegalStateException.class)
    public void iteratorRemoveISE2() {
        c = newCache(1);
        Iterator<Integer> iter = c.keySet().iterator();
        iter.next();
        iter.remove();
        iter.remove();// should throw
    }

    /**
     * {@link Set#iterator()} fails when the cache is shutdown.
     */
    @Test
    public void iteratorShutdown() {
        c = newCache(5);
        assertTrue(c.isStarted());
        c.shutdown();
        assertFalse(c.keySet().iterator().hasNext());
    }
}
