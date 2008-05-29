/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.loading.SimpleCacheLoader;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.test.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests whether or not the cache loading service is available at runtime.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LoadingService.java 536 2007-12-30 00:14:25Z kasper $
 */
@SuppressWarnings("unchecked")
public class LoadingService extends AbstractCacheTCKTest {

    @Before
    public void setup() {
        newConfigurationClean();
    }
    /**
     * Tests that if a cache has no configured cache loader there is no {@link CacheLoadingService}
     * available.
     */
    @Test
    public void noLoadingServiceIfNoLoaderConfigured() {
        assertNull(conf.withLoading().getLoader());
        init();
        assertFalse(c.hasService(CacheLoadingService.class));
    }

    /**
     * Tests that if a cache has no configured cache loader there is no {@link CacheLoadingService}
     * available.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getLoadingServiceFailIfNoLoaderConfigured() {
        assertNull(conf.withLoading().getLoader());
        init();
        c.getService(CacheLoadingService.class);
    }

    /**
     * Tests that a cache that has a configured cache loader. Will have a
     * {@link CacheLoadingService} available.
     */
    @Test
    public void loadingServiceAvailableOp() {
        conf.withLoading().setLoader(TestUtil.dummy(Op.class));
        init();
        assertTrue(c.hasService(CacheLoadingService.class));
        // check that it doesn't fail with a classcast exception
        assertNotNull(c.getService(CacheLoadingService.class));
        assertTrue(c.getAllServices().containsKey(CacheLoadingService.class));
        assertTrue(c.getAllServices().get(CacheLoadingService.class) instanceof CacheLoadingService);
    }
    
    /**
     * Tests that a cache that has a configured cache loader. Will have a
     * {@link CacheLoadingService} available.
     */
    @Test
    public void loadingServiceAvailableSimpleCacheLoader() {
        conf.withLoading().setLoader(TestUtil.dummy(SimpleCacheLoader.class));
        init();
        assertTrue(c.hasService(CacheLoadingService.class));
        // check that it doesn't fail with a classcast exception
        assertNotNull(c.getService(CacheLoadingService.class));
        assertTrue(c.getAllServices().containsKey(CacheLoadingService.class));
        assertTrue(c.getAllServices().get(CacheLoadingService.class) instanceof CacheLoadingService);
    }
}
