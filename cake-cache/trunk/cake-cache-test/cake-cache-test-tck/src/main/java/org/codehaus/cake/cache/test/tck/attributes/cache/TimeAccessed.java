/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.attributes.cache;

import static org.codehaus.cake.cache.CacheEntry.TIME_ACCESSED;

import java.util.Map;

import org.codehaus.cake.cache.Cache;
import org.junit.Test;

/**
 * Tests that the creation time attribute of cache entries are working properly.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CreationTime.java 542 2008-01-02 21:50:05Z kasper $
 */
public class TimeAccessed extends AbstractAttributeTest {
    public TimeAccessed() {
        super(TIME_ACCESSED);
    }

    /**
     * Tests that clear will force new elements to have a new timestamp.
     */
    @Test
    public void clearAll() {
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
    public void loadedAttribute() {
        assertGet(M1);
        assertAttribute(M1, 10l);
    }

    /**
     * Tests that creation time can propagate via the attribute map provided to a cache loader.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void loadedAttributes() {
        loader.withLoader(M1).addAttribute(TIME_ACCESSED, 5l);
        loader.withLoader(M2).addAttribute(TIME_ACCESSED, 6l);
        loader.withLoader(M3).addAttribute(TIME_ACCESSED, 7l);
        loader.withLoader(M4).addAttribute(TIME_ACCESSED, 8l);

        assertGet(M1);
        assertAttribute(M1, 10l);

        assertGet(M2);
        assertAttribute(M2, 10l);

        assertGetAll(M3, M4);
        assertAttribute(M3, 10l);
        assertAttribute(M4, 10l);
    }

    /**
     * Tests that timestamp is set for creation date. Furthermore test that creation date is not
     * updated when replacing existing values.
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
        putAll(M1, M2);
        assertAttribute(M1, 10l);
        assertAttribute(M2, 10l);

        clock.incrementTimeOfDay();
        putAll(M1, M2);
        assertAttribute(M1, 11l);
        assertAttribute(M2, 11l);

        putAll(M3, M4);
        assertAttribute(M3, 11l);
        assertAttribute(M4, 11l);
    }

    /**
     * as {@link #put()} except that we use {@link Cache#putIfAbsent(Object, Object)} instead
     */
    @Test
    public void putIfAbsent() {
        putIfAbsent(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimeOfDay();
        putIfAbsent(M1);
        assertAttribute(M1, 11l);

        clock.incrementTimeOfDay();
        putIfAbsent(M1.getKey(), M2.getValue());
        assertAttribute(M1, 12l);

        putIfAbsent(M2);
        assertAttribute(M2, 12l);
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

        replace(M1.getKey(), M2.getValue(), M3.getValue());
        assertAttribute(M1, 12l);

        replace(M1.getKey(), M5.getValue(), M4.getValue());
        assertAttribute(M1, 12l);

    }

}
