/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.core;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.junit.Test;

public class Size extends AbstractCacheTCKTest {

    /**
     * size returns the correct values.
     */
    @Test
    public void sizeBasic() {
        assertEquals(0, newCache().size());
        assertEquals(5, newCache(5).size());
    }

    /**
     * {@link Cache#size()} lazy starts the cache.
     */
    @Test
    public void sizeLazyStart() {
        init().assertNotStarted();
        assertSize(0).assertStarted();
    }

    /**
     * {@link Cache#isEmpty()} should not fail when cache is shutdown.
     * 
     * @throws InterruptedException
     *             was interrupted
     */
    @Test
    public void sizeShutdown() throws InterruptedException {
        init(5).assertStarted();
        shutdown().size();
        assertEquals(0, awaitTermination().size());
    }

}
