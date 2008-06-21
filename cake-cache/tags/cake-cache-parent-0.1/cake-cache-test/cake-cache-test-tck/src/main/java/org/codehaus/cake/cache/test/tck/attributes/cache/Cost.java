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

import static org.codehaus.cake.cache.CacheEntry.COST;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.CacheEntry;
import org.junit.Test;

/**
 * Tests {@link CacheEntry#getCost()}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Cost.java 536 2007-12-30 00:14:25Z kasper $
 */
public class Cost extends AbstractAttributeTest {
    public Cost() {
        super(COST);
    }

    /**
     * Tests default cost of 1 for new entries.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void put() {
        put(M1);
        assertAttribute(M1);
        putAll(M1, M2);
        assertAttribute(M1);
        assertAttribute(M2);
    }

    /**
     * Tests that loaded valus has a default cost of 1.
     */
    @Test
    public void loaded() {
        assertGet(M1);
        assertAttribute(M1);
    }

    /**
     * Test a cache loader where entries has a specific cost.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void loadedCost() {
        loader.withLoader(M1).addAttribute(atr, 1.5);
        assertGet(M1);
        assertAttribute(M1, 1.5);

        loader.withLoader(M2).addAttribute(atr, 2.5);
        assertGet(M2);
        assertAttribute(M2, 2.5);

        loader.withLoader(M3).addAttribute(atr, 3.5);
        loader.withLoader(M4).addAttribute(atr, 4.5);
        assertGet(M3, M4);
        assertAttribute(M3, 3.5);
        assertAttribute(M4, 4.5);

        c.clear();
        loader.withLoader(M1).addAttribute(atr, 5.5);
        assertGet(M1);
        assertAttribute(M1, 5.5);

        // tests that a loaded value will override the cost
        loader.withLoader(M1).addAttribute(atr, 6.5);
        forceLoad(M1, Attributes.singleton(atr, 6.5));
        assertAttribute(M1, 6.5);
    }

    /**
     * Tests that put overrides the cost of an existing item.
     */
    @Test
    public void putOverride() {
        loader.withLoader(M1).addAttribute(atr, 1.5);
        assertGet(M1);
        assertAttribute(M1, 1.5);
        put(M1);
        assertAttribute(M1);
    }

}
