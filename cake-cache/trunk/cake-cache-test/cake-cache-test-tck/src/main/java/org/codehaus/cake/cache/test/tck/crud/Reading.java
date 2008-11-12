package org.codehaus.cake.cache.test.tck.crud;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class Reading extends AbstractCacheTCKTest{

    @Test
    public void read() {
        newCache(5);
        assertEquals("A", c.crud().value().get(1));
    }
}
