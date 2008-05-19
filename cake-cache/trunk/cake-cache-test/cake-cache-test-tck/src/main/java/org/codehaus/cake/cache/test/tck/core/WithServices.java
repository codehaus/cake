package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.memorystore.MemoryStoreService;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class WithServices extends AbstractCacheTCKTest {

    @Test
    public void withServices() {
        assertNotNull(c.with());
        assertNotNull(c.with().memoryStore());
        assertNotNull(c.getService(MemoryStoreService.class));
        assertSame(c.getService(MemoryStoreService.class), c.with().memoryStore());
    }
}
