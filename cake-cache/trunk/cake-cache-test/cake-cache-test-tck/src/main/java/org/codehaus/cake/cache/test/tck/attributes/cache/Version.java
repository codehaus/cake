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
package org.codehaus.cake.cache.test.tck.attributes.cache;

import static org.codehaus.cake.cache.CacheEntry.VERSION;

import java.util.Iterator;

import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.ops.PrimitiveOps;
import org.junit.Test;

/**
 * Tests the size attribute of {@link CacheEntry}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class Version extends AbstractAttributeTest {
    public Version() {
        super(VERSION);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void iterator() {
        loader.setAttribute(VERSION, PrimitiveOps.longAdd(5));// size=key+1
        init();
        assertGet(M1);
        Iterator iter = c.asMap().values().iterator();
        iter.next();
        iter.remove();
        assertVolume(0);
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
        loader.withLoader(M1).addAttribute(atr, 1l);
        assertGet(M1);
        assertAttribute(M1, 1l);

        loader.withLoader(M2).addAttribute(atr, 2l);
        assertGet(M2);
        assertAttribute(M2, 2l);

        loader.withLoader(M3).addAttribute(atr, 3l);
        loader.withLoader(M4).addAttribute(atr, 4l);
        assertGet(M3, M4);
        assertAttribute(M3, 3l);
        assertAttribute(M4, 4l);

        c.clear();
        loader.withLoader(M1).addAttribute(atr, 5l);
        assertGet(M1);
        assertAttribute(M1, 5l);

        // tests that a loaded value will override the cost
        loader.withLoader(M1).addAttribute(atr, 6l);
        forceLoad(M1, Attributes.singleton(atr, 6l));
        assertAttribute(M1, 6l);
    }

    /**
     * Tests version increment.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void put() {
        put(M1);
        assertAttribute(M1);
        putAll(M1, M2);
        assertAttribute(M1, 2L);
        assertAttribute(M2);
    }

    /**
     * Tests that put increments the version of an item loaded with a specific version.
     */
    @Test
    public void putOverride() {
        loader.withLoader(M1).addAttribute(atr, 4l);
        assertGet(M1);
        assertAttribute(M1, 4l);
        put(M1);
        assertAttribute(M1, 5L);
    }
}
