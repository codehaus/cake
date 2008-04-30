/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Tests loading through one of the {@link Cache#get(Object)} or
 * {@link Cache#getAll(java.util.Collection)} methods.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ImplicitLoading.java 511 2007-12-13 14:37:02Z kasper $
 */
public class ImplicitLoading extends AbstractCacheTCKTest {

    @Test
    public void testSimpleLoading() {
        assertPeek(entry(M1, null));
        assertNotContainsKey(M1);
        assertNotContainsValue(M1);
        assertSize(0);

        assertGet(M1);
        assertSize(1); // M1 loaded
        assertPeek(M1);
        assertContainsKey(M1);
        assertContainsValue(M1);

        assertGet(M2);
        assertSize(2); // M2 loaded
        assertPeek(M2);
        assertContainsKey(M2);
        assertContainsValue(M2);
    }

    @Test
    public void testAggregateLoading() {
        assertGetAll(M1, M2, M3, M4, M5);
        assertSize(5);
        assertPeek(M1, M2, M3, M4, M5);
    }

    @Test
    public void testNullLoading() {
        assertGet(entry(M6, null));
        assertSize(0);
        assertFalse(containsKey(M6));

        assertGet(M5);
        assertSize(1);
    }

    @Test
    public void testAggregateNullLoading() {
        assertGetAll(M1, M2, entry(M6, null), entry(M7, null));

        assertSize(2);
        assertPeek(M1, M2);
    }

    @Test
    public void keepLoadedValue() {
        assertGet(M1);
        assertEquals(1, loader.totalLoads());
        assertGet(M1);
        assertEquals(1, loader.totalLoads());
    }
}
