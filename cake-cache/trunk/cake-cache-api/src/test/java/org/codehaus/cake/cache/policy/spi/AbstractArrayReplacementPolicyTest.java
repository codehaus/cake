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
package org.codehaus.cake.cache.policy.spi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Random;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.cache.CacheEntry;
import org.junit.Before;
import org.junit.Test;

public class AbstractArrayReplacementPolicyTest {

    TestEntry TE1;
    TestEntry TE2;
    TestEntry TE3;
    TestEntry TE4;
    TestEntry TE5;

    
    private TP tp;

    @Before
    public void before() {
        tp = new TP();
        // r = new Random(1243214);

        TE1 = new TestEntry();
        TE2 = new TestEntry();
        TE3 = new TestEntry();
        TE4 = new TestEntry();
        TE5 = new TestEntry();
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

    static class TestEntry implements CacheEntry<Integer, String> {
        private final AttributeMap attributes = new DefaultAttributeMap();

        public AttributeMap getAttributes() {
            return attributes;
        }

        public Integer getKey() {
            return null;
        }

        public String getValue() {
            return null;
        }

        public String setValue(String value) {
            return null;
        }

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
