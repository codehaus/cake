/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class ToString extends AbstractCacheTCKTest {

    /**
     * Just test that the toString() method works on an empty cache.
     */
    @Test
    public void toStringEmpty() {
        init();
        c.toString();
    }

    /**
     * Just test that the toString() method works.
     */
    @Test
    public void toStringX() {
        c = newCache(5);
        String s = c.toString();
        try {
            for (int i = 1; i < 6; i++) {
                assertTrue(s.indexOf(String.valueOf(i)) >= 0);
                assertTrue(s.indexOf("" + (char) (i + 64)) >= 0);
            }
        } catch (AssertionError ar) {
            System.out.println(s);
            throw ar;
        }
    }

    /**
     * Just test that the toString() does recursively call {@link Cache#toString()} if the
     * cache is put into itself as a value or a key.
     */
    @Test
    public void toStringCacheInCache() {
        Cache c = newCache();
        c.put(c, "foo");
        c.toString();
        c.put("foo", c);
        c.toString();
    }
}
