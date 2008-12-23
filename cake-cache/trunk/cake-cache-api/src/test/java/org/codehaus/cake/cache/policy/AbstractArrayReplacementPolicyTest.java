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
package org.codehaus.cake.cache.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.cache.CacheEntry;
import org.junit.Before;
import org.junit.Test;

public class AbstractArrayReplacementPolicyTest extends AbstractPolicyTest{
    private TP tp;

    @Before
    public void before() {
        tp = new TP();
        policy = tp;
        init();
    }

    @Test
    public void addSize() {
        assertEquals(0, tp.size());
        assertTrue(tp.add(TE1));
        assertEquals(1, tp.size());
        assertTrue(tp.add(TE2));
        assertEquals(2, tp.size());
        assertTrue(tp.add(TE3));
        assertEquals(3, tp.size());
    }

    @Test
    public void addWithIndex() {
        assertEquals(0, tp.add0(TE1));
        assertEquals(1, tp.add0(TE2));
        assertEquals(2, tp.add0(TE3));
        tp.clear();
        assertEquals(0, tp.add0(TE4));
    }

    @Test
    public void getFromIndex() {
        assertEquals(TE1, tp.getFromIndex(tp.add0(TE1)));
        assertEquals(TE2, tp.getFromIndex(tp.add0(TE2)));
        assertEquals(TE3, tp.getFromIndex(tp.add0(TE3)));
    }

    @Test
    public void getIndexOf() {
        assertEquals(tp.add0(TE1), tp.getIndexOf(TE1));
        assertEquals(tp.add0(TE2), tp.getIndexOf(TE2));
        assertEquals(tp.add0(TE3), tp.getIndexOf(TE3));
    }

    @Test
    public void clear() {
        tp.clear();
        assertEquals(0, tp.size());
        assertTrue(tp.add(TE1));
        assertTrue(tp.add(TE2));
        assertTrue(tp.add(TE3));
        assertEquals(3, tp.size());
        tp.clear();
        assertEquals(0, tp.size());
    }

    @Test
    public void remove() {

    }


    static class TP extends AbstractArrayReplacementPolicy<Integer, String> {
        protected void swap(int prevIndex, int newIndex) {
            // TestEntry te=matching.get(prevIndex);
            // matching.set(prevIndex, matching.get(newIndex));
            // matching.r
        }

        public CacheEntry<Integer, String> evictNext() {
            return null;
        }

    }
}
