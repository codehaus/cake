/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

/**
 * Not sure these methods are to usefull though.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: EqualsHashcode.java 560 2008-01-09 16:58:56Z kasper $
 */
public class EqualsHashcode extends AbstractCacheTCKTest {

    @Test
    public void equals() {
        Cache<Integer, String> c3 = newCache(3);
        Cache<Integer, String> c4 = newCache(4);
        assertTrue(c4.equals(c4));
        assertTrue(c3.equals(c3));
        assertFalse(c3.equals(c4));
        assertFalse(c4.equals(c3));
    }

    @Test
    public void hashcode() {
        Cache<Integer, String> c3 = newCache(3);
        Cache<Integer, String> c4 = newCache(4);
        c4.hashCode();
        c3.hashCode();
    }

    @Test
    public void hashcodeShutdown() {
        Cache<Integer, String> c3 = newCache(3);
        Cache<Integer, String> c4 = newCache(4);
        c3.shutdown();
        c4.shutdown();
        c4.hashCode();
        c3.hashCode();
    }

    @Test
    public void equalsShutdown() {
        Cache<Integer, String> c3 = newCache(3);
        Cache<Integer, String> c4 = newCache(4);
        c3.shutdown();
        c4.shutdown();
        assertTrue(c4.equals(c4));
        assertTrue(c3.equals(c3));
        assertTrue(c3.equals(c4));
        assertTrue(c4.equals(c3));
    }
}
