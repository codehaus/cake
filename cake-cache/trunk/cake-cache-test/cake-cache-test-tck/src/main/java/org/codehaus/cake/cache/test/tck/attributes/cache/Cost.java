/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.attributes.cache;

import static org.codehaus.cake.cache.CacheAttributes.ENTRY_COST;

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
        super(ENTRY_COST);
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
