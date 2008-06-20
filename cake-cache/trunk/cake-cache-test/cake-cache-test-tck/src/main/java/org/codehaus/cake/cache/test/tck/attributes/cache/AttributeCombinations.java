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
package org.codehaus.cake.cache.test.tck.attributes.cache;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.util.Clock.DeterministicClock;
import org.junit.Test;

public class AttributeCombinations extends AbstractCacheTCKTest {

    /**
     * Tests that the creation time and modification is always equal when creating. ie clock is only read once.
     */
    @Test
    public void creationTimeAndModificationEqual() {
        conf.withAttributes().add(CacheEntry.TIME_CREATED, CacheEntry.TIME_MODIFIED);
        DeterministicClock dc = new DeterministicClock() {
            public long timeOfDay() {
                this.incrementTimeOfDay();
                return super.timeOfDay();
            }
        };
        conf.setClock(dc);
        dc.setTimeOfDay(34);
        init();
        put(M1);
        assertEquals(35, c.getEntry(M1.getKey()).getAttributes().get(CacheEntry.TIME_CREATED));
        assertEquals(35, c.getEntry(M1.getKey()).getAttributes().get(CacheEntry.TIME_MODIFIED));
    }

    @Test
    public void testToString() {
        conf.withAttributes().add(CacheEntry.TIME_CREATED, CacheEntry.TIME_MODIFIED);
        clock.setTimeOfDay(1000);
        init();
        put(M1);
        assertTrue("1=A [CreationTime=1000, ModificationTime=1000]".equals(c.getEntry(M1.getKey()).toString())
                || "1=A [ModificationTime=1000, CreationTime=1000]".equals(c.getEntry(M1.getKey()).toString()));
        // assertEquals("1=A [CreationTime=1000, ModificationTime=1000]", c.getEntry(M1.getKey()).toString());
    }

}
