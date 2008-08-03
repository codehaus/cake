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

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.service.exceptionhandling.ExceptionHandlingConfiguration;
import org.codehaus.cake.service.executor.ExecutorsConfiguration;
import org.codehaus.cake.service.management.ManagementConfiguration;

public class TstStubs {
    public interface Stubber<T> extends Container {}

    public static class StubberConfiguration extends ContainerConfiguration<Stubber> {
        public StubberConfiguration() {
            addConfiguration(new ExceptionHandlingConfiguration());
            addConfiguration(new ManagementConfiguration());
            addConfiguration(new ExecutorsConfiguration());
        }

        public static StubberConfiguration newConfiguration() {
            return new StubberConfiguration();
        }
    }

    public static class StubberImpl extends AbstractStubber {
        StubberConfiguration conf;

        public StubberImpl(StubberConfiguration conf) {
            this.conf = conf;
        }
    }

    /**
     * A Cache that is abstract.
     */
    public static abstract class CannotInstantiateAbstractStubber extends AbstractStubber {

        /**
         * Create a new CannotInstantiateAbstractCache.
         * 
         * @param configuration
         *            the cache configuration
         */
        public CannotInstantiateAbstractStubber(StubberConfiguration configuration) {}
    }

    /**
     * A Stubber that throws an {@link ArithmeticException} in the constructor.
     */
    public static class ConstructorRuntimeThrowingStubber extends AbstractStubber {

        /**
         * Create a new ConstructorThrowingStubber.
         * 
         * @param configuration
         *            the Stubber configuration
         */
        public ConstructorRuntimeThrowingStubber(StubberConfiguration configuration) {
            throw new ArithmeticException();
        }
    }

    /**
     * A Stubber that throws an {@link ArithmeticException} in the constructor.
     */
    public static class ConstructorErrorThrowingStubber extends AbstractStubber {

        /**
         * Create a new ConstructorThrowingStubber.
         * 
         * @param configuration
         *            the Stubber configuration
         */
        public ConstructorErrorThrowingStubber(StubberConfiguration configuration) {
            throw new AbstractMethodError();
        }
    }

    /**
     * A Stubber that throws an {@link ArithmeticException} in the constructor.
     */
    public static class ConstructorExceptionThrowingStubber extends AbstractStubber {

        /**
         * Create a new ConstructorThrowingStubber.
         * 
         * @param configuration
         *            the Stubber configuration
         * @throws Exception
         *             construction failed
         */
        public ConstructorExceptionThrowingStubber(StubberConfiguration configuration) throws Exception {
            throw new IOException();
        }
    }

    /**
     * A Stubber that has a private constructor.
     */
    public static final class PrivateConstructorStubber extends AbstractStubber {

        /**
         * Create a new PrivateConstructorStubber.
         * 
         * @param configuration
         *            the Stubber configuration
         */
        private PrivateConstructorStubber(StubberConfiguration configuration) {}
    }

    public static class AbstractStubber implements Stubber<Integer> {

        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }

        public String getName() {
            return null;
        }

        public boolean isShutdown() {
            return false;
        }

        public boolean isStarted() {
            return false;
        }

        public boolean isTerminated() {
            return false;
        }

        public void shutdown() {}

        public void shutdownNow() {}

        public Map<Class<?>, Object> getAllServices() {
            return null;
        }

        public <T> T getService(Class<T> serviceType) {
            return null;
        }

        public boolean hasService(Class<?> serviceType) {
            return false;
        }

        public <T> T getService(Class<T> serviceType, AttributeMap attributes) {
            return null;
        }

        public Set<Class<?>> serviceKeySet() {
            return null;
        }
    }
}
