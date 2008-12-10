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
package org.codehaus.cake.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.codehaus.cake.test.util.TestUtil.dummy;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.AssertionFailedError;

import org.codehaus.cake.internal.service.ServiceList;
import org.codehaus.cake.internal.service.ServiceList.Factory;
import org.codehaus.cake.management.ManagementConfiguration;
import org.codehaus.cake.service.TstStubs.PrivateConstructorStubber;
import org.codehaus.cake.service.TstStubs.Stubber;
import org.codehaus.cake.service.TstStubs.StubberConfiguration;
import org.codehaus.cake.service.TstStubs.StubberImpl;
import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.Logger;
import org.junit.Before;
import org.junit.Test;

public class ContainerConfigurationTest {

    /** The default instanceof a ContainerConfiguration. */
    private StubberConfiguration conf;

    /**
     * Setup the ContainerConfiguration.
     */
    @Before
    public void setUp() {
        conf = StubberConfiguration.newConfiguration();
    }

    /**
     * Tests {@link ContainerConfiguration#setClock(Clock)} and {@link ContainerConfiguration#getClock()}.
     */
    @Test
    public void clock() {
        assertSame(Clock.DEFAULT_CLOCK, conf.getClock());
        Clock c = new Clock.DeterministicClock();
        assertSame(conf, conf.setClock(c));
        assertSame(c, conf.getClock());
    }

    /**
     * Tests that {@link ContainerConfiguration#setClock(Clock)} throws a {@link NullPointerException} when invoked with
     * a null argument.
     */
    @Test(expected = NullPointerException.class)
    public void clockNPE() {
        conf.setClock(null);
    }

    /**
     * Tests {@link ContainerConfiguration#setDefaultLogger(Logger)} and
     * {@link ContainerConfiguration#getDefaultLogger()}.
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
     * Tests {@link ContainerConfiguration#setName(String)} and {@link ContainerConfiguration#getName()}.
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
     * Tests {@link ContainerConfiguration#getProperties()}, {@link ContainerConfiguration#getProperty(String)},
     * {@link ContainerConfiguration#getProperty(String, Object)}and
     * {@link ContainerConfiguration#setProperty(String, Object)}.
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
     * Tests that {@link ContainerConfiguration#getProperty(String)} throws a {@link NullPointerException} when invoked
     * with a null argument.
     */
    @Test(expected = NullPointerException.class)
    public void propertiesGetNPE() {
        conf.getProperty(null);
    }

    /**
     * Tests that {@link ContainerConfiguration#getProperty(String, Object)} throws a {@link NullPointerException} when
     * invoked with a null argument as the name of the property.
     */
    @Test(expected = NullPointerException.class)
    public void propertiesGetNPE1() {
        conf.getProperty(null, "foo");
    }

    /**
     * Tests that {@link ContainerConfiguration#setProperty(String, Object)} throws a {@link NullPointerException} when
     * invoked with a null argument as the name of the property.
     */
    @Test(expected = NullPointerException.class)
    public void propertiesSetNPE() {
        conf.setProperty(null, "A");
    }

    /**
     * Tests {@link ContainerConfiguration#setType(Class)} and {@link ContainerConfiguration#getType()}.
     */
    @Test
    public void type() {
        assertNull(conf.getType());
        assertSame(conf, conf.setType(StubberImpl.class));
        assertEquals(StubberImpl.class, conf.getType());
    }

    /**
     * Tests that a configuration service added through
     * {@link ContainerConfiguration#addConfiguration(AbstractCacheServiceConfiguration)} is available when calling
     * {@link ContainerConfiguration#getAllConfigurations()}.
     */
    @Test
    public void addConfiguration() {
        ExtendConfiguration conf = new ExtendConfiguration();
        SimpleService s1 = new SimpleService();
        assertFalse(conf.getConfigurations().contains(s1));
        conf.addConfiguration(s1);
        assertSame(s1, conf.getConfigurationOfType(SimpleService.class));
        assertTrue(conf.getConfigurations().contains(s1));
        try {
            conf.getConfigurationOfType(SimpleService2.class);
            throw new AssertionError("Should fail");
        } catch (IllegalArgumentException ok) {
            /** ok */
        }
    }

    /**
     * Tests that {@link ContainerConfiguration#addConfiguration(AbstractCacheServiceConfiguration)} throws a
     * {@link NullPointerException} when invoked with a null argument.
     */
    @Test(expected = NullPointerException.class)
    public void addConfigurationNPE() {
        conf.addConfiguration(null);
    }

    /**
     * Tests that {@link ContainerConfiguration#addConfiguration(AbstractCacheServiceConfiguration)} throws a
     * {@link IllegalArgumentException} when we try to register a configuration service that is registered as default.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addConfigurationIAE() {
        conf.addConfiguration(new ManagementConfiguration());
    }

    /**
     * Tests that {@link ContainerConfiguration#addConfiguration(AbstractCacheServiceConfiguration)} throws a
     * {@link IllegalArgumentException} when we try to register the same configuration service twice.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addConfigurationIAE2() {
        try {
            conf.addConfiguration(new SimpleService());
        } catch (Throwable t) {
            throw new AssertionFailedError("Should not throw " + t.getMessage());
        }
        conf.addConfiguration(new SimpleService());
    }

    @Test
    public void addService() {
        assertFalse(((ServiceList) conf.getServices()).getServices().iterator().hasNext());
        ServiceFactory sf = dummy(ServiceFactory.class);
        conf.addService(5);
        conf.addService(Integer.class, 10);
        conf.addService(Long.class, sf);
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

    @Test
    public void addGetServices() {
        for (int i = 0; i < 100; i++) {
            conf.addService(i);
        }
        ServiceList l = (ServiceList) conf.getServices();
        Iterator<Object> iter = l.getServices().iterator();
        for (int i = 0; i < 100; i++) {
            assertEquals(i, iter.next());
        }
    }

    @Test(expected = NullPointerException.class)
    public void addServicesNPE() {
        conf.addService(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addServicesSame() {
        conf.addService(1);
        conf.addService(1);
    }

    @Test(expected = NullPointerException.class)
    public void addToLifecycleAndExportNPE1() {
        conf.addService(null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void addToLifecycleAndExportNPE2() {
        conf.addService(Integer.class, null);
    }

    @Test(expected = NullPointerException.class)
    public void addToLifecycleAndExportNPE3() {
        conf.addService(null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void addToLifecycleAndExportNPE4() {
        conf.addService(Integer.class,(ServiceFactory) null);
    }

    @Test
    public void newInstance() {
        conf.setType(StubberImpl.class);
        assertTrue(conf.newInstance() instanceof StubberImpl);
    }

    @Test(expected = IllegalStateException.class)
    public void newInstanceISE() {
        conf.newInstance();
    }

    /**
     * Tests that {@link ContainerConfiguration#newCacheInstance(Class)} throws a {@link NullPointerException} when
     * invoked with a null argument.
     * 
     * @throws Exception
     *             some exception while constructing the cache
     */
    @Test(expected = NullPointerException.class)
    public void newInstanceNPE() throws Exception {
        conf.newInstance(null);
    }

    /**
     * Tests that {@link ContainerConfiguration#newCacheInstance(Class)} throws a {@link IllegalArgumentException} when
     * invoked with a class that does not have a constructor taking a single {@link ContainerConfiguration} argument.
     * 
     * @throws Exception
     *             some exception while constructing the cache
     */
    @Test(expected = IllegalArgumentException.class)
    public void newInstanceNoConstructor() throws Exception {
        conf.newInstance(TestUtil.dummy(Stubber.class).getClass());
    }

    /**
     * Tests that {@link ContainerConfiguration#newStubberInstance(Class)} throws a {@link IllegalArgumentException}
     * when invoked with an abstract class.
     * 
     * @throws Exception
     *             some exception while constructing the Stubber
     */
    @Test(expected = IllegalArgumentException.class)
    public void newInstanceAbstractClass() throws Exception {
        conf.newInstance(TstStubs.CannotInstantiateAbstractStubber.class);
    }

    /**
     * Tests that {@link ContainerConfiguration#newStubberInstance(Class)} throws a {@link RuntimeException} when
     * invoked with class that throws an RuntimeException in the constructor.
     * 
     * @throws Throwable
     *             some exception while constructing the Stubber
     */
    @Test(expected = ArithmeticException.class)
    public void newInstanceConstructorRuntimeThrows() throws Throwable {
        try {
            conf.newInstance(TstStubs.ConstructorRuntimeThrowingStubber.class);
        } catch (IllegalArgumentException e) {
            throw e.getCause();
        }
    }

    /**
     * Tests that {@link ContainerConfiguration#newInstance(Class)} throws a {@link Error} when invoked with class that
     * throws an Error in the constructor.
     * 
     * @throws Throwable
     *             some exception while constructing the Stubber
     */
    @Test(expected = AbstractMethodError.class)
    public void newInstanceConstructorErrorThrows() throws Throwable {
        try {
            conf.newInstance(TstStubs.ConstructorErrorThrowingStubber.class);
        } catch (IllegalArgumentException e) {
            throw e.getCause();
        }
    }

    /**
     * Tests that {@link ContainerConfiguration#newInstance(Class)} throws a {@link IllegalArgumentException} when
     * invoked with class that throws an {@link Exception} in the constructor.
     * 
     * @throws Throwable
     *             some exception while constructing the Stubber
     */
    @Test(expected = IOException.class)
    public void newInstanceConstructorExceptionThrows() throws Throwable {
        try {
            conf.newInstance(TstStubs.ConstructorExceptionThrowingStubber.class);
            throw new AssertionError("should fail");
        } catch (IllegalArgumentException e) {
            throw e.getCause();
        }
    }

    /**
     * Tests that {@link ContainerConfiguration#newInstance(Class)} throws a {@link IllegalArgumentException} when
     * invoked with a class where the constructor taking a single {@link ContainerConfiguration} argument is private.
     * 
     * @throws Throwable
     *             some exception while constructing the Stubber
     */
    @Test(expected = IllegalAccessException.class)
    public void newInstancePrivateConstructorIAE() throws Throwable {
        try {
            conf.newInstance(PrivateConstructorStubber.class);
        } catch (IllegalArgumentException e) {
            throw e.getCause();
        }
    }

    /**
     * An extension of ContainerConfiguration that exposes {@link #getConfiguration(Class)} as a public method.
     */
    public static class ExtendConfiguration extends ContainerConfiguration<Integer> {

        /** Create a new ExtendConfiguration. */
        public ExtendConfiguration() {
            addConfiguration(new ManagementConfiguration());
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

        @Override
        public <U> U getConfigurationOfType(Class<U> configurationType) {
            return super.getConfigurationOfType(configurationType);
        }
    }

    /**
     * A simple implementation of AbstractStubberServiceConfiguration.
     */
    public static class SimpleService {}

    /**
     * A simple implementation of AbstractStubberServiceConfiguration.
     */
    public static class SimpleService2 {}

    /**
     * A simple implementation of AbstractStubberServiceConfiguration that throws an exception when it is being
     * persisted.
     */
    public static class SimpleServiceAE {}

}
