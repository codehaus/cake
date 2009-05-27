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
package org.codehaus.cake.service.test.tck;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import junit.framework.AssertionFailedError;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.Container.State;
import org.codehaus.cake.service.test.util.ThreadServiceTestHelper;
import org.codehaus.cake.util.Clock.DeterministicClock;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class AbstractTCKTest<C extends Container, T extends ContainerConfiguration> extends Assert {
    protected C c;

    protected T conf;

    protected DeterministicClock clock;

    protected ThreadServiceTestHelper threadHelper;

    private volatile Throwable failure;

    private volatile String failText;

    @Before
    public void setupConf() {
        failure = null;
        failText = null;
        clock = new DeterministicClock();
        clock.setTimeOfDay(10);
        clock.setNanoTime(1000);
        conf = newConfiguration();
        conf.setClock(clock);
        c = newContainer();
    }

    @After
    public final void noFailures() throws Throwable {
        // exceptionHandler.assertCleared();
        if (failText != null) {
            throw new AssertionError(failText);
        }
        if (failure != null) {
            if (failure instanceof AssertionFailedError) {
                throw failure;
            }
            failure = null;
            failure.printStackTrace();
            throw new AssertionError("Test failed");
        }
    }

    /**
     * Await all loads that currently active.
     */
    protected void awaitFinishedThreads() {
        if (threadHelper != null) {
            threadHelper.awaitAllIdle();
        }
    }

    protected void failed(Throwable cause) {
        this.failure = cause;
        if (cause instanceof Error) {
            throw (Error) cause;
        } else if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
        }
    }

    protected void failed(String text) {
        this.failText = text;
    }

    public AbstractTCKTest<C,T> awaitTermination() {
        try {
            long start = System.nanoTime();
            assertTrue(c.awaitState(State.TERMINATED, 10, TimeUnit.SECONDS));
            long finish = System.nanoTime();
            if (finish - start > 1000000) {
                // System.out.println(finish - start);
                // new Exception().printStackTrace();
            }
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
        return this;
    }

    public void checkLazystart() {
        assertTrue(c.getState().isStarted());
    }

    protected void shutdownAndAwaitTermination() {
        c.shutdown();
        awaitTermination();
    }

    @SuppressWarnings("unchecked")
    protected final C newContainer() {
        c = (C) TckUtil.newContainer(conf);
        return c;
    }

    @SuppressWarnings("unchecked")
    protected final T newConfigurationClean() {
        conf = (T) TckUtil.newConfiguration();
        return (T) conf;
    }

    public <S> S getService(Class<S> type) {
        return c.getService(type);
    }

    public <S> S getService(Class<S> type, AttributeMap attributes) {
        return c.getService(type, attributes);
    }

    public final void prestart() {
        c.hasService(Object.class);
    }

    protected T newConfiguration() {
        newConfigurationClean();
        if (TckUtil.isThreadSafe()) {
            threadHelper = new ThreadServiceTestHelper();
            conf.addService(threadHelper);
            //conf.addServiceFactory(ExecutorService.class, threadHelper);
        }
        return (T) conf;
    }

    @SuppressWarnings("unchecked")
    public <S> S withConf(Class<S> confType) {
        for (Object o : conf.getConfigurations()) {
            if (confType.isAssignableFrom(o.getClass())) {
                return (S) o;
            }
        }
        throw new IllegalArgumentException("Unknown Type " + confType);
    }

    public Class<?> getContainerInterface() {
        Class<?> someImpl = c.getClass();
        while (someImpl != null) {
            for (Class<?> c : allInterfaces(someImpl)) {
                if (c != Container.class && Arrays.asList(c.getInterfaces()).contains(Container.class)) {
                    return c;
                }
            }
            someImpl = someImpl.getSuperclass();
        }
        throw new IllegalStateException("Unknown type");
    }

    Set<Class<?>> allInterfaces(Class<?> c) {
        Set<Class<?>> s = new HashSet<Class<?>>();
        for (Class<?> cl : c.getInterfaces()) {
            s.add(cl);
            s.addAll(allInterfaces(cl));
        }
        return s;
    }

    /**
     * Bypasses some checks in {@link ContainerConfiguration#newInstance(Class)}.
     */
    public C cheatInstantiate() throws Throwable {

        for (Constructor<C> con : c.getClass().getConstructors()) {
            if (con.getParameterTypes().length == 1
                    && ContainerConfiguration.class.isAssignableFrom(con.getParameterTypes()[0])) {
                try {
                    return con.newInstance(conf);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                } catch (Exception e) {
                    System.out.println(c.getClass());
                    e.printStackTrace();
                    throw new Error(e);
                }
            }
        }
        throw new IllegalArgumentException("missing constructor");
    }
}
