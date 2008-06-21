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
package org.codehaus.cake.cache;

import static org.codehaus.cake.cache.Caches.emptyCache;
import static org.codehaus.cake.test.util.TestUtil.assertIsSerializable;
import static org.codehaus.cake.test.util.TestUtil.serializeAndUnserialize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.service.loading.CacheLoadingService;
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
 * @version $Id: CacheServicesTest.java 427 2007-11-10 13:15:25Z kasper $
 */
@SuppressWarnings("unchecked")
@RunWith(JMock.class)
public class CachesTest {

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
     * Tests {@link Caches#runClear(Cache)}.
     */
    @Test
    public void runClear() {
        context.checking(new Expectations() {
            {
                one(cache).clear();
            }
        });
        Caches.clearAsRunnable(cache).run();
    }

    /**
     * Tests that {@link Caches#runClear(Cache)} throws a {@link NullPointerException} when invoked with a null
     * argument.
     */
    @Test(expected = NullPointerException.class)
    public void runClearNPE() {
        Caches.clearAsRunnable(null);
    }

    @Test
    public void emptyCache_() throws Exception {
        Cache<Integer, String> c = Caches.emptyCache();
        assertSame(c, Caches.emptyCache());
        assertFalse(emptyCache().awaitTermination(1, TimeUnit.NANOSECONDS));
        assertFalse(Caches.emptyCache().containsKey(1));
        assertFalse(Caches.emptyCache().containsValue(2));
        assertEquals(0, Caches.emptyCache().entrySet().size());
        assertEquals(new HashMap(), Caches.emptyCache());
        assertEquals("emptymap", emptyCache().getName());
        assertEquals(new HashMap().hashCode(), Caches.emptyCache().hashCode());
        assertFalse(emptyCache().isShutdown());
        assertFalse(emptyCache().isStarted());
        assertFalse(emptyCache().isTerminated());
        assertEquals(0, Caches.emptyCache().size());
        assertTrue(Caches.emptyCache().isEmpty());
        assertEquals(0, Caches.emptyCache().keySet().size());
        assertEquals(0, Caches.emptyCache().values().size());
        assertNull(emptyCache().get(1));
        assertNull(emptyCache().getEntry(2));
        assertNull(emptyCache().peek(2));
        assertNull(emptyCache().peekEntry(2));
        assertEquals(2, emptyCache().getAll(Arrays.asList(1, 2)).size());
        assertTrue(emptyCache().getAll(Arrays.asList(1, 2)).containsKey(1));
        assertNull(emptyCache().getAll(Arrays.asList(1, 2)).get(1));
        assertFalse(Caches.emptyCache().equals(new HashSet()));

        try {
            assertNull(emptyCache().put(1, 2));
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */}

        try {
            emptyCache().putAll(Collections.singletonMap(1, 2));
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */}

        try {
            assertNull(emptyCache().putIfAbsent(1, 2));
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */}

        assertNull(emptyCache().remove(1));
        assertFalse(emptyCache().remove(1, 2));
        emptyCache().removeAll(Arrays.asList(1, 2, 3));
        try {
            assertNull(emptyCache().replace(1, 2));
            fail("should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException ok) {/* ok */}

        assertFalse(emptyCache().replace(1, 2, 3));
        emptyCache().clear();
        emptyCache().shutdown();
        emptyCache().shutdownNow();
        assertTrue(emptyCache().with() instanceof CacheServices);
    }

    /**
     * Tests that EMPTY_MAP is serializable and maintains the singleton property.
     * 
     * @throws Exception
     *             something went wrong
     */
    @Test
    public void emptyCacheSerialization() throws Exception {
        assertIsSerializable(Caches.emptyCache());
        assertSame(Caches.emptyCache(), serializeAndUnserialize(new Caches.EmptyCache()));
    }

    @Test
    public void emptyCacheServices() {
        assertEquals(0, emptyCache().serviceKeySet().size());
        assertFalse(emptyCache().hasService(CacheLoadingService.class));
    }

    @Test(expected = NullPointerException.class)
    public void emptyCacheServicesNPE() {
        emptyCache().getService(null);
    }

    @Test(expected = NullPointerException.class)
    public void emptyCacheServicesNPE1() {
        emptyCache().getService(null, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void emptyCacheServicesNPE2() {
        emptyCache().getService(Integer.class, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void emptyCacheServicesIAE() {
        emptyCache().getService(CacheLoadingService.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void emptyCacheWith() {
        emptyCache().with().memoryStore();
    }

}
