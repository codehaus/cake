/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.tck.service.loading;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.loading.BlockingCacheLoader;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.test.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests whether or not the cache loading service is available at runtime.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class LoadingService extends AbstractCacheTCKTest {

    @Before
    public void setup() {
        newConfigurationClean();
    }

    /**
     * Tests that if a cache has no configured cache loader there is no {@link CacheLoadingService} available.
     */
    @Test
    public void noLoadingServiceIfNoLoaderConfigured() {
        assertNull(conf.withLoading().getLoader());
        init();
        assertFalse(c.hasService(CacheLoadingService.class));
    }

    /**
     * Tests that if a cache has no configured cache loader there is no {@link CacheLoadingService} available.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getLoadingServiceFailIfNoLoaderConfigured() {
        assertNull(conf.withLoading().getLoader());
        init();
        c.getService(CacheLoadingService.class);
    }

    /**
     * Tests that a cache that has a configured cache loader. Will have a {@link CacheLoadingService} available.
     */
    @Test
    public void loadingServiceAvailableOp() {
        conf.withLoading().setLoader(TestUtil.dummy(Op.class));
        init();
        assertTrue(c.hasService(CacheLoadingService.class));
        // check that it doesn't fail with a classcast exception
        assertNotNull(c.getService(CacheLoadingService.class));
        assertTrue(c.serviceKeySet().contains(CacheLoadingService.class));
        assertTrue(c.getService(CacheLoadingService.class, Attributes.EMPTY_ATTRIBUTE_MAP) instanceof CacheLoadingService);
    }

    /**
     * Tests that a cache that has a configured cache loader. Will have a {@link CacheLoadingService} available.
     */
    @Test
    public void loadingServiceAvailableSimpleCacheLoader() {
        conf.withLoading().setLoader(TestUtil.dummy(BlockingCacheLoader.class));
        init();
        assertTrue(c.hasService(CacheLoadingService.class));
        // check that it doesn't fail with a classcast exception
        assertNotNull(c.getService(CacheLoadingService.class));
        assertTrue(c.serviceKeySet().contains(CacheLoadingService.class));
        assertTrue(c.getService(CacheLoadingService.class, Attributes.EMPTY_ATTRIBUTE_MAP) instanceof CacheLoadingService);
    }
}
