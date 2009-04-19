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

import static org.codehaus.cake.cache.Caches.emptyCache;
import static org.codehaus.cake.test.util.TestUtil.assertIsSerializable;
import static org.codehaus.cake.test.util.TestUtil.dummy;
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

import org.codehaus.cake.cache.loading.CacheLoadingService;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.attribute.BooleanAttribute;
import org.codehaus.cake.util.attribute.ByteAttribute;
import org.codehaus.cake.util.attribute.CharAttribute;
import org.codehaus.cake.util.attribute.DoubleAttribute;
import org.codehaus.cake.util.attribute.FloatAttribute;
import org.codehaus.cake.util.attribute.IntAttribute;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.codehaus.cake.util.attribute.ShortAttribute;
import org.codehaus.cake.util.ops.Predicates;
import org.codehaus.cake.util.ops.Ops.BinaryPredicate;
import org.codehaus.cake.util.ops.Ops.BytePredicate;
import org.codehaus.cake.util.ops.Ops.CharPredicate;
import org.codehaus.cake.util.ops.Ops.DoublePredicate;
import org.codehaus.cake.util.ops.Ops.FloatPredicate;
import org.codehaus.cake.util.ops.Ops.IntPredicate;
import org.codehaus.cake.util.ops.Ops.LongPredicate;
import org.codehaus.cake.util.ops.Ops.ShortPredicate;
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
        assertFalse(Caches.emptyCache().iterator().hasNext());
        assertEquals(0, Caches.emptyCache().keySet().size());
        assertEquals(0, Caches.emptyCache().values().size());
        assertNull(emptyCache().get(1));
        assertNull(emptyCache().getEntry(2));
        assertNull(emptyCache().peek(2));
        assertNull(emptyCache().peekEntry(2));
        assertEquals(2, emptyCache().getAllOld(Arrays.asList(1, 2)).size());
        assertTrue(emptyCache().getAllOld(Arrays.asList(1, 2)).containsKey(1));
        assertNull(emptyCache().getAllOld(Arrays.asList(1, 2)).get(1));
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

//    @Test(expected=UnsupportedOperationException.class)
//    public void emptyCrud() {
//        ObjectAttribute<String> oa=new ObjectAttribute<String>(String.class,"foo"){};
//        assertEquals("foo", emptyCache().withCrud().attribute(oa));
//    }
    @Test
    public void emptyCacheSelection() {
        assertSame(emptyCache(), emptyCache().filter().on(new BinaryPredicate() {
            public boolean op(Object a, Object b) {
                return true;
            }
        }));
        assertSame(emptyCache(),emptyCache().filter().on(Predicates.TRUE));
        assertSame(emptyCache(),emptyCache().filter().on(((Attribute) new IntAttribute() {}), Predicates.TRUE));
        assertSame(emptyCache(),emptyCache().filter().on((new BooleanAttribute() {}), true));
        assertSame(emptyCache(),emptyCache().filter().on((new ByteAttribute() {}), dummy(BytePredicate.class)));
        assertSame(emptyCache(),emptyCache().filter().on((new CharAttribute() {}), dummy(CharPredicate.class)));
        assertSame(emptyCache(),emptyCache().filter().on((new DoubleAttribute() {}), dummy(DoublePredicate.class)));
        assertSame(emptyCache(),emptyCache().filter().on((new FloatAttribute() {}), dummy(FloatPredicate.class)));
        assertSame(emptyCache(),emptyCache().filter().on((new IntAttribute() {}), dummy(IntPredicate.class)));
        assertSame(emptyCache(),emptyCache().filter().on((new LongAttribute() {}), dummy(LongPredicate.class)));
        assertSame(emptyCache(),emptyCache().filter().on((new ShortAttribute() {}), dummy(ShortPredicate.class)));
        assertSame(emptyCache(),emptyCache().filter().onKey(Predicates.TRUE));
        assertSame(emptyCache(),emptyCache().filter().onKeyType(Predicates.class));
        assertSame(emptyCache(),emptyCache().filter().onValue(Predicates.TRUE));
        assertSame(emptyCache(),emptyCache().filter().onValueType(Predicates.class));

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
