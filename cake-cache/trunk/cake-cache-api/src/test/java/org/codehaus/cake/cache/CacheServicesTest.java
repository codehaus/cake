/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.cache;

import static org.codehaus.cake.test.util.TestUtil.dummy;
import static org.junit.Assert.assertSame;

import java.util.concurrent.ScheduledExecutorService;

import org.codehaus.cake.cache.service.loading.CacheLoadingService;
import org.codehaus.cake.cache.service.memorystore.MemoryStoreService;
import org.codehaus.cake.test.util.TestUtil;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the {@link CacheServicesOld} class.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class CacheServicesTest {

    /** The junit mockery. */
    private final Mockery context = new JUnit4Mockery();

    /** The default cache to test upon. */
    private Cache<Integer, String> cache;

    /**
     * Setup the cache mock.
     */
    @Before
    public void setupCache() {
        cache = context.mock(Cache.class);
    }

    /**
     * Tests {@link CacheServices#memoryStore()}.
     */
    @Test
    public void memoryStore() {
        final MemoryStoreService service = TestUtil.dummy(MemoryStoreService.class);
        context.checking(new Expectations() {
            {
                one(cache).with();
                will(returnValue(new CacheServices(cache)));
                one(cache).getService(MemoryStoreService.class);
                will(returnValue(service));
            }
        });
        MemoryStoreService<Integer, String> ces = cache.with().memoryStore();
        assertSame(service, ces);
    }

    /**
     * Tests {@link CacheServices#scheduledExecutor()}.
     */
    @Test
    public void scheduledExecutorService() {
        final ScheduledExecutorService service = TestUtil.dummy(ScheduledExecutorService.class);
        context.checking(new Expectations() {
            {
                one(cache).with();
                will(returnValue(new CacheServices(cache)));
                one(cache).getService(ScheduledExecutorService.class);
                will(returnValue(service));
            }
        });
        ScheduledExecutorService ces = cache.with().scheduledExecutor();
        assertSame(service, ces);
    }
    /**
     * Tests {@link CacheServices#loading()}.
     */
    @Test
    public void loadingService() {
        final CacheLoadingService service = TestUtil.dummy(CacheLoadingService.class);
        context.checking(new Expectations() {
            {
                one(cache).with();
                will(returnValue(new CacheServices(cache)));
                one(cache).getService(CacheLoadingService.class);
                will(returnValue(service));
            }
        });
        CacheLoadingService<Integer, String> ces = cache.with().loading();
        assertSame(service, ces);
    }

    /**
     * Tests {@link CacheServices#loading()}.
     */
    @Test
    public void loadingServiceForced() {
        final CacheLoadingService service = dummy(CacheLoadingService.class);
        context.checking(new Expectations() {
            {
                one(cache).with();
                will(returnValue(new CacheServices(cache)));
                one(cache).getService(CacheLoadingService.class, CacheLoadingService.IS_FORCED.singleton(true));
                will(returnValue(service));
            }
        });
        CacheLoadingService<Integer, String> ces = cache.with().loadingForced();
        assertSame(service, ces);
    }
}
