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

import static org.codehaus.cake.cache.CacheEntry.TIME_MODIFIED;

import java.util.Map;

import org.junit.Test;

public class TimeModified extends AbstractAttributeTest {
    public TimeModified() {
        super(TIME_MODIFIED);
    }

    /**
     * Tests that timestamp is set for modification date. This also tests that put of the same values constitutes a new
     * put ie modification time is updated.
     */
    @Test
    public void put() {
        put(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimeOfDay();
        put(M1);
        assertAttribute(M1, 11l);

        clock.incrementTimeOfDay();
        c.put(M1.getKey(), M2.getValue());
        assertAttribute(M1, 12l);

        put(M2);
        assertAttribute(M2, 12l);
    }

    /**
     * as {@link #put()} except that we use {@link Map#putAll(Map)} instead
     */
    @Test
    public void putAll() {
        putAll(M1, M2, M3);
        assertAttribute(M1, 10l);
        assertAttribute(M2, 10l);
        assertAttribute(M3, 10l);

        clock.incrementTimeOfDay();
        putAll(M1, M2);
        assertAttribute(M1, 11l);
        assertAttribute(M2, 11l);
        assertAttribute(M3, 10l);

        putAll(M3, M4);
        assertAttribute(M3, 11l);
        assertAttribute(M4, 11l);
    }

    /**
     * as {@link #put()} except that we use {@link java.util.ConcurrentMap#putIfAbsent(Map)} instead
     */
    @Test
    public void putIfAbsent() {
        putIfAbsent(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimeOfDay();
        putIfAbsent(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimeOfDay();
        putIfAbsent(M1.getKey(), M2.getValue());
        assertAttribute(M1, 10l);

        putIfAbsent(M2);
        assertAttribute(M2, 12l);
    }

    /**
     * as {@link #put()} except that we use {@link java.util.ConcurrentMap#putIfAbsent(Map)} instead
     */
    @Test
    public void replace() {
        put(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimeOfDay();
        replace(M1);
        assertAttribute(M1, 11l);

        clock.incrementTimeOfDay();
        replace(M1.getKey(), M2.getValue());
        assertAttribute(M1, 12l);

        clock.incrementTimeOfDay();
        replace(M1.getKey(), M2.getValue(), M3.getValue());
        assertAttribute(M1, 13l);

        replace(M1.getKey(), M5.getValue(), M4.getValue());
        assertAttribute(M1, 13l);

    }

    /**
     * Tests that remove will force new elements to have a new timestamp.
     */
    @Test
    public void remove() {
        put(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimeOfDay();
        remove(M1);
        put(M1);
        assertAttribute(M1, 11l);
    }

    /**
     * Tests that clear will force new elements to have a new timestamp.
     */
    @Test
    public void clearCache() {
        put(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimeOfDay();
        c.clear();
        put(M1);
        assertAttribute(M1, 11l);
    }

    /**
     * Tests that timestamp is set for creation date when loading values
     */
    @Test
    public void loadedNoAttribute() {
        assertGet(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimeOfDay();
        forceLoad(M1);
        assertAttribute(M1, 11l);
    }

    /**
     * Tests that creation time can propagate via the attribute map provided to a cache loader.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void loadedAttribute() {
        loader.withLoader(M1).addAttribute(TIME_MODIFIED, 5l);
        loader.withLoader(M2).addAttribute(TIME_MODIFIED, 6l);
        loader.withLoader(M3).addAttribute(TIME_MODIFIED, 7l);
        loader.withLoader(M4).addAttribute(TIME_MODIFIED, 8l);

        assertGet(M1);
        assertAttribute(M1, 5l);

        assertGet(M2);
        assertAttribute(M2, 6l);

        assertGetAll(M3, M4);
        assertAttribute(M3, 7l);
        assertAttribute(M4, 8l);

        assertGet(M5);
        assertAttribute(M5, 10l);// default
    }
}
