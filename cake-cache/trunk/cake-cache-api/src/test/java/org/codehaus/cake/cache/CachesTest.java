/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache;

import static org.codehaus.cake.cache.CacheEntry.COST;
import static org.codehaus.cake.cache.CacheEntry.HITS;
import static org.codehaus.cake.cache.CacheEntry.TIME_ACCESSED;
import static org.codehaus.cake.cache.CacheEntry.*;
import static org.codehaus.cake.cache.CacheEntry.TIME_MODIFIED;
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

import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.common.TimeInstanceAttribute;
import org.codehaus.cake.service.executor.ExecutorsService;
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
        assertEquals(0, emptyCache().getAllServices().size());
        assertFalse(emptyCache().hasService(ExecutorsService.class));
    }

    @Test(expected = NullPointerException.class)
    public void emptyCacheServicesNPE() {
        emptyCache().getService(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void emptyCacheServicesIAE() {
        emptyCache().getService(ExecutorsService.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void emptyCacheWith() {
        emptyCache().with().memoryStore();
    }

    /**
     * Tests {@link CacheEntry#TIME_CREATED}.
     */
    @Test
    public void attributeCreationTime() {
        assertTrue(TIME_CREATED instanceof TimeInstanceAttribute);
        TIME_CREATED.checkValid(0);
        TestUtil.assertSingletonSerializable(TIME_CREATED);
    }

    /**
     * Tests {@link CacheEntry#TIME_MODIFIED}.
     */
    @Test
    public void attributeModificationTime() {
        assertTrue(TIME_MODIFIED instanceof TimeInstanceAttribute);
        TIME_MODIFIED.checkValid(0);
        TestUtil.assertSingletonSerializable(TIME_MODIFIED);
    }

    /**
     * Tests {@link CacheEntry#TIME_ACCESSED}.
     */
    @Test
    public void attributeAccessTime() {
        assertTrue(TIME_ACCESSED instanceof TimeInstanceAttribute);
        TIME_ACCESSED.checkValid(0);
        TestUtil.assertSingletonSerializable(TIME_ACCESSED);
    }

    /**
     * Tests {@link CacheEntry#COST}.
     */
    @Test
    public void attributeCost() {
        assertTrue(COST instanceof DoubleAttribute);
        assertEquals(1, COST.getDefaultValue(), 0);
        COST.checkValid(-1000);
        TestUtil.assertSingletonSerializable(COST);
    }

    /**
     * Tests {@link CacheEntry#HITS}.
     */
    @Test
    public void attributeHits() {
        assertTrue(HITS instanceof LongAttribute);
        HITS.checkValid(0);
        assertFalse(HITS.isValid(-1));
        TestUtil.assertSingletonSerializable(HITS);
    }

    /**
     * Tests {@link CacheEntry#SIZE}.
     */
    @Test
    public void attributeSize() {
        assertTrue(SIZE instanceof LongAttribute);
        SIZE.checkValid(0);
        assertTrue(SIZE.isValid(0));
        assertTrue(SIZE.isValid(1));
        TestUtil.assertSingletonSerializable(SIZE);
    }

    /**
     * Tests {@link CacheEntry#SIZE}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void attributeSizeIAE() {
        SIZE.checkValid(-1);
    }

    /**
     * Tests {@link CacheEntry#VERSION}.
     */
    @Test
    public void attributeVersion() {
        assertTrue(VERSION instanceof LongAttribute);
        VERSION.checkValid(1);
        assertFalse(VERSION.isValid(0));
        TestUtil.assertSingletonSerializable(VERSION);
    }
}
