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
package org.codehaus.cake.stubber;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.internal.stubber.UnsynchronizedInternalStubber;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.stubber.bubber.BubberService;
import org.codehaus.cake.util.Logger;

@Container.SupportedServices( { BubberService.class })
public class UnsynchronizedStubber<T> implements Stubber<T> {

    Stubber<T> container;

    public UnsynchronizedStubber() {
        this(StubberConfiguration.<T> newConfiguration());
    }

    public UnsynchronizedStubber(StubberConfiguration<T> conf) {
        this.container = new UnsynchronizedInternalStubber<T>(conf, this);
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return container.awaitTermination(timeout, unit);
    }

    public void fail(Logger.Level level, String message, Throwable cause) {
        container.fail(level, message, cause);
    }

    /** {@inheritDoc} */
    public <T> T getService(Class<T> serviceType, AttributeMap attributes) {
        return container.getService(serviceType, attributes);
    }

    public T getIt(T t) {
        return container.getIt(t);
    }

    public String getName() {
        return container.getName();
    }

    public <T> T getService(Class<T> serviceType) {
        return container.getService(serviceType);
    }

    public boolean hasService(Class<?> serviceType) {
        return container.hasService(serviceType);
    }

    public boolean isShutdown() {
        return container.isShutdown();
    }

    public boolean isStarted() {
        return container.isStarted();
    }

    public boolean isTerminated() {
        return container.isTerminated();
    }

    public void shutdown() {
        container.shutdown();
    }

    public void shutdownNow() {
        container.shutdownNow();
    }

    public Set<Class<?>> serviceKeySet() {
        return container.serviceKeySet();
    }

}
