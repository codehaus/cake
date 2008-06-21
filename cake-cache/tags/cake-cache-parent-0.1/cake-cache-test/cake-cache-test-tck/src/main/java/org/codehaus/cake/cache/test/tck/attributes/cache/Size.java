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

import static org.codehaus.cake.cache.CacheEntry.SIZE;

import java.util.Iterator;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.ops.LongOps;
import org.junit.Test;

/**
 * Tests the size attribute of {@link CacheEntry}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Size.java 555 2008-01-09 04:52:48Z kasper $
 */
public class Size extends AbstractAttributeTest {
    public Size() {
        super(SIZE);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void iterator() {
        loader.setAttribute(SIZE, LongOps.add(5));// size=key+1
        init();
        assertGet(M1);
        Iterator iter = c.values().iterator();
        iter.next();
        iter.remove();
        assertVolume(0);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void volume() {
        loader.setAttribute(SIZE, LongOps.add(1));// size=key+1
        init();
        assertGet(M1);
        assertVolume(2);
        assertGet(M3);
        assertVolume(6);
        remove(M1);
        assertVolume(4);
        assertGet(M5);
        assertVolume(10);
        assertSize(2);// element size unaffected
        c.clear();
        assertVolume(0);

        init();
        put(M1);
        assertVolume(1);
        putAll(M1, M2);
        assertVolume(2);
        c.clear();
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
     * Tests default size of 1.
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
     * Tests that put overrides the cost of an existing item.
     */
    @Test
    public void putOverride() {
        loader.withLoader(M1).addAttribute(atr, 4l);
        assertGet(M1);
        assertAttribute(M1, 4l);
        put(M1);
        assertAttribute(M1);
    }
}
