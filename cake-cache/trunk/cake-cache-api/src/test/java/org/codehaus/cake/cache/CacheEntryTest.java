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
package org.codehaus.cake.cache;

import static org.codehaus.cake.cache.CacheEntry.COST;
import static org.codehaus.cake.cache.CacheEntry.HITS;
import static org.codehaus.cake.cache.CacheEntry.SIZE;
import static org.codehaus.cake.cache.CacheEntry.TIME_ACCESSED;
import static org.codehaus.cake.cache.CacheEntry.TIME_CREATED;
import static org.codehaus.cake.cache.CacheEntry.TIME_MODIFIED;
import static org.codehaus.cake.cache.CacheEntry.VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.attribute.DoubleAttribute;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.codehaus.cake.util.attribute.TimeInstanceAttribute;
import org.junit.Test;

public class CacheEntryTest {
    /**
     * Tests {@link CacheEntry#TIME_CREATED}.
     */
    @Test
    public void attributeCreationTime() {
        assertTrue(TIME_CREATED instanceof TimeInstanceAttribute);
        TIME_CREATED.checkValid(0);
        TestUtil.assertSingletonSerializable(TIME_CREATED);
    }

    /**
     * Tests {@link CacheEntry#TIME_MODIFIED}.
     */
    @Test
    public void attributeModificationTime() {
        assertTrue(TIME_MODIFIED instanceof TimeInstanceAttribute);
        TIME_MODIFIED.checkValid(0);
        TestUtil.assertSingletonSerializable(TIME_MODIFIED);
    }

    /**
     * Tests {@link CacheEntry#TIME_ACCESSED}.
     */
    @Test
    public void attributeAccessTime() {
        assertTrue(TIME_ACCESSED instanceof TimeInstanceAttribute);
        TIME_ACCESSED.checkValid(0);
        TestUtil.assertSingletonSerializable(TIME_ACCESSED);
    }

    /**
     * Tests {@link CacheEntry#COST}.
     */
    @Test
    public void attributeCost() {
        assertTrue(COST instanceof DoubleAttribute);
        assertEquals(1, COST.getDefaultValue(), 0);
        COST.checkValid(-1000);
        TestUtil.assertSingletonSerializable(COST);
    }

    /**
     * Tests {@link CacheEntry#HITS}.
     */
    @Test
    public void attributeHits() {
        assertTrue(HITS instanceof LongAttribute);
        HITS.checkValid(0);
        assertFalse(HITS.isValid(-1));
        TestUtil.assertSingletonSerializable(HITS);
    }

    /**
     * Tests {@link CacheEntry#SIZE}.
     */
    @Test
    public void attributeSize() {
        assertTrue(SIZE instanceof LongAttribute);
        SIZE.checkValid(0);
        assertTrue(SIZE.isValid(0));
        assertTrue(SIZE.isValid(1));
        TestUtil.assertSingletonSerializable(SIZE);
    }

    /**
     * Tests {@link CacheEntry#SIZE}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void attributeSizeIAE() {
        SIZE.checkValid(-1);
    }

    /**
     * Tests {@link CacheEntry#VERSION}.
     */
    @Test
    public void attributeVersion() {
        assertTrue(VERSION instanceof LongAttribute);
        VERSION.checkValid(1);
        assertFalse(VERSION.isValid(0));
        TestUtil.assertSingletonSerializable(VERSION);
    }

}
