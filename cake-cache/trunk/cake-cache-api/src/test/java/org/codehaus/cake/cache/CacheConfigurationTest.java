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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.codehaus.cake.test.util.TestUtil.dummy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.AssertionFailedError;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.internal.service.ServiceList;
import org.codehaus.cake.internal.service.ServiceList.Factory;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.common.management.ManagementConfiguration;
import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link CacheConfiguration} class.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class CacheConfigurationTest {

    /** The default instanceof a CacheConfiguration. */
    private CacheConfiguration<Number, Collection> conf;

    /**
     * Setup the CacheConfiguration.
     */
    @Before
    public void setUp() {
        conf = CacheConfiguration.newConfiguration();
    }

    /**
     * Tests {@link CacheConfiguration#setClock(Clock)} and {@link CacheConfiguration#getClock()}.
     */
    @Test
    public void clock() {
        assertSame(Clock.DEFAULT_CLOCK, conf.getClock());
        Clock c = new Clock.DeterministicClock();
        assertSame(conf, conf.setClock(c));
        assertSame(c, conf.getClock());
    }

    /**
     * Tests that a configuration service added through
     * {@link CacheConfiguration#addConfiguration(AbstractCacheServiceConfiguration)} is available when calling
     * {@link CacheConfiguration#getAllConfigurations()}.
     */
    @Test
    public void addConfiguration() {
        ExtendConfiguration conf = new ExtendConfiguration();
        SimpleService s1 = new SimpleService();
        assertFalse(conf.getConfigurations().contains(s1));
        conf.addConfiguration2(s1);
        assertSame(s1, conf.getConfigurationOfType(SimpleService.class));
        assertTrue(conf.getConfigurations().contains(s1));
        try {
            conf.getConfigurationOfType(SimpleService2.class);
            throw new AssertionError("Should fail");
        } catch (IllegalArgumentException ok) {
            /** ok */
        }
    }
    static final Attribute A1 = new LongAttribute() {};
    static final Attribute A2 = new BooleanAttribute() {};

    @Test
    public void addAttribute() {
        CacheConfiguration c = new CacheConfiguration();
        assertSame(c, c.addEntryAttributes(A1));
        assertSame(c, c.addEntryAttributes(A2));
        assertEquals(2, c.getAllEntryAttributes().size());
        assertSame(A1, c.getAllEntryAttributes().get(0));
        assertSame(A2, c.getAllEntryAttributes().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addSame_IAE() {
        new CacheConfiguration().addEntryAttributes(A1).addEntryAttributes(A1);
    }

    @Test
    public void addService() {
        assertFalse(((ServiceList) conf.getServices()).getServices().iterator().hasNext());
        ServiceFactory sf = dummy(ServiceFactory.class);
        conf.addToLifecycle(5);
        conf.addToLifecycleAndExport(Integer.class, 10);
        conf.addToLifecycleAndExport(Long.class, sf);
        ServiceList sl = (ServiceList) conf.getServices();
        Iterator<Object> iter = sl.getServices().iterator();
        assertEquals(5, iter.next());
        ServiceList.Factory f10 = (Factory) iter.next();
        assertEquals(Integer.class, f10.getKey());
        assertEquals(10, f10.getFactory());
        f10 = (Factory) iter.next();
        assertEquals(Long.class, f10.getKey());
        assertEquals(sf, f10.getFactory());
        assertFalse(iter.hasNext());
    }

//    /**
//     * Tests that {@link CacheConfiguration#addConfiguration(AbstractCacheServiceConfiguration)} throws a
//     * {@link NullPointerException} when invoked with a null argument.
//     */
//    @Test(expected = NullPointerException.class)
//    public void addConfigurationNPE() {
//        conf.addConfiguration(null);
//    }
//
//    /**
//     * Tests that {@link CacheConfiguration#addConfiguration(AbstractCacheServiceConfiguration)} throws a
//     * {@link IllegalArgumentException} when we try to register a configuration service that is registered as default.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void addConfigurationIAE() {
//        conf.addConfiguration(new ManagementConfiguration());
//    }
//
//    /**
//     * Tests that {@link CacheConfiguration#addConfiguration(AbstractCacheServiceConfiguration)} throws a
//     * {@link IllegalArgumentException} when we try to register the same configuration service twice.
//     */
//    @Test(expected = IllegalArgumentException.class)
//    public void addConfigurationIAE2() {
//        try {
//            conf.addConfiguration(new SimpleService());
//        } catch (Throwable t) {
//            throw new AssertionFailedError("Should not throw " + t.getMessage());
//        }
//        conf.addConfiguration(new SimpleService());
//    }

    /**
     * Tests that {@link CacheConfiguration#setClock(Clock)} throws a {@link NullPointerException} when invoked with a
     * null argument.
     */
    @Test(expected = NullPointerException.class)
    public void clockNPE() {
        conf.setClock(null);
    }

    /**
     * Tests {@link CacheConfiguration#setCacheType(Class)} and {@link CacheConfiguration#getCacheType()}.
     */
    @Test
    public void cacheType() {
        assertNull(conf.getType());
        assertSame(conf, conf.setType(DummyCache.class));
        assertEquals(DummyCache.class, conf.getType());
    }

    /**
     * Tests {@link CacheConfiguration#setDefaultLogger(Logger)} and {@link CacheConfiguration#getDefaultLogger()}.
     */
    @Test
    public void defaultLogger() {
        assertNull(conf.getDefaultLogger());
        Logger log = TestUtil.dummy(Logger.class);
        assertSame(conf, conf.setDefaultLogger(log));
        assertSame(log, conf.getDefaultLogger());
        conf.setDefaultLogger(null);
        assertNull(conf.getDefaultLogger());
    }

    /**
     * Tests {@link CacheConfiguration#setName(String)} and {@link CacheConfiguration#getName()}.
     */
    @Test
    public void name() {
        assertNull(conf.getName());
        assertSame(conf, conf.setName("foo-123_"));
        assertEquals("foo-123_", conf.getName());
        conf.setName(null);
        assertNull(conf.getName());
    }

    /**
     * Tests that we cannot use the empty string as the name of a cache.
     */
    @Test(expected = IllegalArgumentException.class)
    public void nameNoEmptyStringIAE() {
        conf.setName("");
    }

    /**
     * Tests that we cannot use an invalid String as the name of the cache.
     */
    @Test(expected = IllegalArgumentException.class)
    public void nameInvalidIAE() {
        conf.setName("&asdad");
    }

    /**
     * Tests {@link CacheConfiguration#getProperties()}, {@link CacheConfiguration#getProperty(String)},
     * {@link CacheConfiguration#getProperty(String, Object)}and {@link CacheConfiguration#setProperty(String, Object)}.
     */
    @Test
    public void properties() {
        conf.setProperty("a", "A");
        conf.setProperty("b", "B");
        assertEquals("A", conf.getProperty("a"));
        assertEquals("B", conf.getProperty("b"));
        assertNull(conf.getProperty("c"));
        assertNull(conf.getProperty("c", null));
        assertEquals("A", conf.getProperties().get("a"));
        assertEquals("B", conf.getProperties().get("b"));
        assertNull(conf.getProperties().get("c"));
        assertEquals("B", conf.getProperty("b", "C"));
        assertEquals("B", conf.getProperty("b", null));
        assertEquals("C", conf.getProperty("c", "C"));
    }

    /**
     * Tests that {@link CacheConfiguration#getProperty(String)} throws a {@link NullPointerException} when invoked with
     * a null argument.
     */
    @Test(expected = NullPointerException.class)
    public void propertiesGetNPE() {
        conf.getProperty(null);
    }

    /**
     * Tests that {@link CacheConfiguration#getProperty(String, Object)} throws a {@link NullPointerException} when
     * invoked with a null argument as the name of the property.
     */
    @Test(expected = NullPointerException.class)
    public void propertiesGetNPE1() {
        conf.getProperty(null, "foo");
    }

    /**
     * Tests that {@link CacheConfiguration#setProperty(String, Object)} throws a {@link NullPointerException} when
     * invoked with a null argument as the name of the property.
     */
    @Test(expected = NullPointerException.class)
    public void propertiesSetNPE() {
        conf.setProperty(null, "A");
    }

    // /**
    // * Tests the {@link CacheConfiguration#toString()} method.
    // */
    // @Test
    // public void toString_() {
    // conf.setName("foo");
    // CacheConfiguration conf2 = CacheConfiguration.newConfiguration();
    // conf2.setName("foo");
    // assertEquals(conf.toString(), conf2.toString());
    // conf2.setName("foo1");
    // assertFalse(conf.toString().equals(conf2.toString()));
    // }
    //
    // /**
    // * Tests the {@link CacheConfiguration#toString()} does not throw an exception even if
    // * it cannot persist a configuration.
    // */
    // @Test
    // public void toStringISE() {
    // conf.setName("foo");
    // conf.addConfiguration(new SimpleServiceAE());
    // assertTrue(conf.toString().contains(ArithmeticException.class.getName()));
    // }

    /**
     * Tests that we can create a specific cache instance from a configuration via the
     * {@link CacheConfiguration#newCacheInstance(Class)} method.
     * 
     * @throws Exception
     *             some exception while constructing the cache
     */
    @Test
    public void newInstance() throws Exception {
        Cache c = conf.setName("foo").newInstance(DummyCache.class);
        assertTrue(c instanceof DummyCache);
        assertEquals("foo", c.getName());
    }

    @Test
    public void newInstanceType() throws Exception {
        conf.setType(DummyCache.class);
        assertTrue(conf.newInstance() instanceof DummyCache);
    }

    /**
     * Tests the {@link CacheConfiguration#create(String)} method.
     */
    @Test
    public void createWithName() {
        CacheConfiguration<?, ?> conf = CacheConfiguration.newConfiguration("foo");
        assertEquals("foo", conf.getName());
    }

    /**
     * Tests the default service are available.
     */
    @Test
    public void defaultService() {
        CacheConfiguration<?, ?> conf = CacheConfiguration.newConfiguration();
        assertNotNull(conf.withExceptionHandling());
        assertNotNull(conf.withLoading());
        assertNotNull(conf.withManagement());
        assertNotNull(conf.withMemoryStore());
        // assertNotNull(conf.withStore());
    }

    /**
     * Tests that {@link CacheConfiguration#newCacheInstance(Class)} throws a {@link NullPointerException} when invoked
     * with a null argument.
     * 
     * @throws Exception
     *             some exception while constructing the cache
     */
    @Test(expected = NullPointerException.class)
    public void newInstanceNPE() throws Exception {
        conf.newInstance(null);
    }

    /**
     * Tests that {@link CacheConfiguration#newCacheInstance(Class)} throws a {@link IllegalArgumentException} when
     * invoked with a class that does not have a constructor taking a single {@link CacheConfiguration} argument.
     * 
     * @throws Exception
     *             some exception while constructing the cache
     */
    @Test(expected = IllegalArgumentException.class)
    public void newInstanceNoConstructor() throws Exception {
        conf.newInstance(TestUtil.dummy(Cache.class).getClass());
    }

    /**
     * Tests that {@link CacheConfiguration#newCacheInstance(Class)} throws a {@link IllegalArgumentException} when
     * invoked with an abstract class.
     * 
     * @throws Exception
     *             some exception while constructing the cache
     */
    @Test(expected = IllegalArgumentException.class)
    public void newInstanceAbstractClass() throws Exception {
        conf.newInstance(DummyCache.CannotInstantiateAbstractCache.class);
    }

    /**
     * Tests that {@link CacheConfiguration#newCacheInstance(Class)} throws a {@link RuntimeException} when invoked with
     * class that throws an RuntimeException in the constructor.
     * 
     * @throws Throwable
     *             some exception while constructing the cache
     */
    @Test(expected = ArithmeticException.class)
    public void newInstanceConstructorRuntimeThrows() throws Throwable {
        try {
            conf.newInstance(DummyCache.ConstructorRuntimeThrowingCache.class);
        } catch (IllegalArgumentException e) {
            throw e.getCause();
        }
    }

    /**
     * Tests that {@link CacheConfiguration#newInstance(Class)} throws a {@link Error} when invoked with class that
     * throws an Error in the constructor.
     * 
     * @throws Throwable
     *             some exception while constructing the cache
     */
    @Test(expected = AbstractMethodError.class)
    public void newInstanceConstructorErrorThrows() throws Throwable {
        try {
            conf.newInstance(DummyCache.ConstructorErrorThrowingCache.class);
        } catch (IllegalArgumentException e) {
            throw e.getCause();
        }
    }

    /**
     * Tests that {@link CacheConfiguration#newInstance(Class)} throws a {@link IllegalArgumentException} when invoked
     * with class that throws an {@link Exception} in the constructor.
     * 
     * @throws Throwable
     *             some exception while constructing the cache
     */
    @Test(expected = IOException.class)
    public void newInstanceConstructorExceptionThrows() throws Throwable {
        try {
            conf.newInstance(DummyCache.ConstructorExceptionThrowingCache.class);
            throw new AssertionError("should fail");
        } catch (IllegalArgumentException e) {
            throw e.getCause();
        }
    }

    /**
     * Tests that {@link CacheConfiguration#newInstance(Class)} throws a {@link IllegalArgumentException} when invoked
     * with a class where the constructor taking a single {@link CacheConfiguration} argument is private.
     * 
     * @throws Throwable
     *             some exception while constructing the cache
     */
    @Test(expected = IllegalAccessException.class)
    public void newInstancePrivateConstructorIAE() throws Throwable {
        try {
            conf.newInstance(DummyCache.PrivateConstructorCache.class);
        } catch (IllegalArgumentException e) {
            throw e.getCause();
        }
    }

    /**
     * Tests {@link CacheConfiguration#CacheConfiguration(Collection)}.
     */
    @Test
    public void cacheConfiguration() {
        ExtendConfiguration conf = new ExtendConfiguration(Arrays.asList(new SimpleService(), new SimpleService2()));
        assertEquals(1, getInstancesOfType(conf.getConfigurations(), SimpleService.class).size());
        assertEquals(1, getInstancesOfType(conf.getConfigurations(), SimpleService2.class).size());
        assertEquals(0, getInstancesOfType(conf.getConfigurations(), SimpleServiceAE.class).size());
        assertNotNull(conf.getConfigurationOfType(SimpleService.class));
        assertNotNull(conf.getConfigurationOfType(SimpleService2.class));
        try {
            conf.getConfigurationOfType(SimpleServiceAE.class);
            throw new AssertionError("Should fail");
        } catch (IllegalArgumentException ok) {
            /** ok */
        }
    }

    private <K> Collection<K> getInstancesOfType(Collection<?> col, Class<K> type) {
        ArrayList<K> list = new ArrayList<K>();
        for (Object o : col) {
            if (o.getClass().equals(type)) {
                list.add((K) o);
            }
        }
        return list;
    }

    /**
     * An extension of CacheConfiguration that exposes {@link #getConfiguration(Class)} as a public method.
     */
    public static class ExtendConfiguration<K, V> extends CacheConfiguration<K, V> {

        /** Create a new ExtendConfiguration. */
        public ExtendConfiguration() {
        }

        /**
         * Create a new ExtendConfiguration.
         * 
         * @param additionalConfigurationTypes
         *            service types to instantiate
         */
        public ExtendConfiguration(Collection additionalConfigurationTypes) {
            for (Object o : additionalConfigurationTypes) {
                super.addConfiguration(o);
            }
        }

        public ExtendConfiguration<K, V> addConfiguration2(Object o) {
            super.addConfiguration(o);
            return this;
        }

        @Override
        public <U> U getConfigurationOfType(Class<U> configurationType) {
            return super.getConfigurationOfType(configurationType);
        }
    }

    /**
     * A simple implementation of AbstractCacheServiceConfiguration.
     */
    public static class SimpleService {
        /** Creates a new SimpleService with the name 'foo'. */

    }

    /**
     * A simple implementation of AbstractCacheServiceConfiguration.
     */
    public static class SimpleService2 {
        /** Creates a new SimpleService2 with the name 'foo1'. */

    }

    /**
     * A simple implementation of AbstractCacheServiceConfiguration that throws an exception when it is being persisted.
     */
    public static class SimpleServiceAE {

    }
}
