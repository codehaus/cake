/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.attributes.cache;

import static org.codehaus.cake.cache.CacheEntry.*;

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
public class Hits extends AbstractAttributeTest {
    public Hits() {
        super(HITS);
    }

//    /**
//     * Tests default size of 1.
//     */
//    @SuppressWarnings("unchecked")
//    @Test
//    public void put() {
//        put(M1);
//        assertAttribute(M1);
//        putAll(M1, M2);
//        assertAttribute(M1);
//        assertAttribute(M2);
//    }
//
//    /**
//     * Tests that put overrides the cost of an existing item.
//     */
//    @Test
//    public void putOverride() {
//        loader.withLoader(M1).addAttribute(atr, 4l);
//        assertGet(M1);
//        assertAttribute(M1, 4l);
//        put(M1);
//        assertAttribute(M1);
//    }
}
