package org.codehaus.cake.cache.test.tck.asmap;

import java.util.concurrent.ConcurrentMap;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;

public class AbstractAsMapTCKTest extends AbstractCacheTCKTest {

    protected ConcurrentMap<Integer, String> asMap() {
        return c.asMap();
    }
}
