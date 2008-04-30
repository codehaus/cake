/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.attributes.cache;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_DATE_CREATED;

import org.junit.Test;

/**
 * Tests that the creation time attribute of cache entries are working properly.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CreationTime.java 542 2008-01-02 21:50:05Z kasper $
 */
public class CreationTime extends AbstractAttributeTest {
    public CreationTime() {
        super(ENTRY_DATE_CREATED);
    }

    /**
     * Tests that clear will force new elements to have a new timestamp.
     */
    @Test
    public void clearAll() {
        put(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimestamp();
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
        loader.withLoader(M1).addAttribute(ENTRY_DATE_CREATED, 5l);
        loader.withLoader(M2).addAttribute(ENTRY_DATE_CREATED, 6l);
        loader.withLoader(M3).addAttribute(ENTRY_DATE_CREATED, 7l);
        loader.withLoader(M4).addAttribute(ENTRY_DATE_CREATED, 8l);

        assertGet(M1);
        assertAttribute(M1, 5l);

        assertGet(M2);
        assertAttribute(M2, 6l);

        assertGetAll(M3, M4);
        assertAttribute(M3, 7l);
        assertAttribute(M4, 8l);
    }

    /**
     * Tests that timestamp is set for creation date. Furthermore test that creation date is not
     * updated when replacing existing values.
     */
    @Test
    public void put() {
        put(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimestamp();
        put(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimestamp();
        c.put(M1.getKey(), M2.getValue());
        assertAttribute(M1, 10l);

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

        clock.incrementTimestamp();
        putAll(M1, M2);
        assertAttribute(M1, 10l);
        assertAttribute(M2, 10l);

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

        clock.incrementTimestamp();
        putIfAbsent(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimestamp();
        putIfAbsent(M1.getKey(), M2.getValue());
        assertAttribute(M1, 10l);

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

        clock.incrementTimestamp();
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

        clock.incrementTimestamp();
        replace(M1);
        assertAttribute(M1, 10l);

        clock.incrementTimestamp();
        replace(M1.getKey(), M2.getValue());
        assertAttribute(M1, 10l);

        replace(M1.getKey(), M2.getValue(), M3.getValue());
        assertAttribute(M1, 10l);

        replace(M1.getKey(), M5.getValue(), M4.getValue());
        assertAttribute(M1, 10l);

    }

}
