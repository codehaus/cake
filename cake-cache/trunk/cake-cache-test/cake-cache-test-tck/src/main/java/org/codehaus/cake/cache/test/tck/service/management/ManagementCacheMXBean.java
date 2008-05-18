/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.management;

import org.codehaus.cake.cache.CacheMXBean;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.test.tck.RequireService;
import org.junit.Test;

@RequireService({Manageable.class})
public class ManagementCacheMXBean extends AbstractManagementTest {

    CacheMXBean cache() {
        return getManagedInstance(CacheMXBean.class);
    }

    @Test
    public void getName() {
        conf.setName("foo");
        init();
        assertEquals("foo", cache().getName());
        assertEquals(c.getName(), cache().getName());
    }

    @Test
    public void getNameUninitialized() {
        assertEquals(c.getName(), cache().getName());
    }

    @Test
    public void getSize() {
        assertEquals(c.size(), cache().getSize());
        put(M1);
        put(M2);
        assertEquals(2, cache().getSize());
        assertEquals(c.size(), cache().getSize());
    }

    @Test
    public void clearCache() {
        assertSize(0);
        put(M1);
        put(M2);
        assertSize(2);
        cache().clear();
        assertSize(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void onShutdown() {
        put(M1, M2);
        assertEquals(2, cache().getSize());
        shutdownAndAwaitTermination(); //should be removed from server
        cache().getSize();
    }
}
