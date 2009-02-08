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

import org.codehaus.cake.cache.policy.spi.PolicyContext;
import org.junit.Before;
import org.junit.Test;

public class AbstractArrayReplacementPolicyTest extends AbstractPolicyTest {
    private TP tp;

    @Before
    public void before() {
        tp = Policies.create(TP.class);
        policy = tp;
    }

    @Test
    public void addSize() {
        assertEquals(0, tp.size());
        tp.add(1);
        assertEquals(1, tp.size());
        tp.add(2);
        assertEquals(2, tp.size());
        tp.add(3);
        assertEquals(3, tp.size());
    }

    @Test
    public void addWithIndex() {
        assertEquals(0, tp.addLast(1));
        assertEquals(1, tp.addLast(2));
        assertEquals(2, tp.addLast(3));
        tp.clear();
        assertEquals(0, tp.addLast(4));
    }

    @Test
    public void getFromIndex() {
        assertEquals(1, tp.getFromIndex(tp.addLast(1)).intValue());
        assertEquals(2, tp.getFromIndex(tp.addLast(2)).intValue());
        assertEquals(3, tp.getFromIndex(tp.addLast(3)).intValue());
    }

    @Test
    public void clear() {
        tp.clear();
        assertEquals(0, tp.size());
        tp.add(1);
        tp.add(2);
        tp.add(3);
        assertEquals(3, tp.size());
        tp.clear();
        assertEquals(0, tp.size());
    }

    @Test
    public void remove() {

    }

    public static class TP extends AbstractArrayReplacementPolicy<Integer> {
        public TP(PolicyContext<Integer> context) {
            super(context);
        }

        protected void swap(int prevIndex, int newIndex) {
            // TestEntry te=matching.get(prevIndex);
            // matching.set(prevIndex, matching.get(newIndex));
            // matching.r
        }

        public Integer evictNext() {
            return null;
        }

    }
}
