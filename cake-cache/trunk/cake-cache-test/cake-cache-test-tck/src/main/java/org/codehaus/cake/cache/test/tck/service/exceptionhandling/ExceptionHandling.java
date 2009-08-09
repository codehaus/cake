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
package org.codehaus.cake.cache.test.tck.service.exceptionhandling;

import java.util.logging.LogManager;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheExceptionHandler;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.service.spi.ExceptionContext;
import org.codehaus.cake.test.util.throwables.Exception1;
import org.codehaus.cake.util.Logger;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class ExceptionHandling extends AbstractCacheTCKTest {

    Mockery context = new JUnit4Mockery();

    private Throwable failure;

    Logger logger;

    @Before
    public void setup() {
        logger = context.mock(Logger.class);
        context.checking(new Expectations() {
            {
                ignoring(logger);
            }
        });
    }

    @After
    public void resetLogging() throws Exception {
        // reset the logging system
        LogManager.getLogManager().readConfiguration();
    }

    @Test
    public void loadingDefaultExceptionHandler() {
        loader.withLoader(M1).setCause(Exception1.INSTANCE);
        conf.setDefaultLogger(logger);
        init();
        assertNull(c.get(M1.getKey()));
    }

    @Test
    public void loadingCustomExceptionHandler() {
        loader.withLoader(M1).setCause(Exception1.INSTANCE);
        conf.setDefaultLogger(logger);
        conf.withExceptionHandling().setExceptionHandler(new LoaderVerifyingExceptionHandler(logger));
        init();
        assertEquals("foo", c.get(M1.getKey()));
    }

    // @Test
    // public void exceptionLogger() {
    // final Logger logger = context.mock(Logger.class);
    // c = newCache(newConf().exceptionHandling().setExceptionHandler(
    // new LoaderVerifyingExceptionHandler(logger)).setExceptionLogger(logger).c()
    // .loading().setLoader(loader));
    // assertEquals("foo", c.get(10));
    // }
    //
    // /**
    // * Tests that the logger set by
    // * {@link CacheExceptionHandlingConfiguration#setExceptionLogger(Logger)} takes
    // * precedence over the logger set by
    // * {@link CacheConfiguration#setDefaultLogger(Logger)}
    // */
    // @Test
    // public void exceptionLoggerPreference() {
    // final Logger logger2 = context.mock(Logger.class);
    // c = newCache(newConf().setDefaultLogger(logger).exceptionHandling().setExceptionHandler(
    // new LoaderVerifyingExceptionHandler(logger2)).setExceptionLogger(logger2).c()
    // .loading().setLoader(loader));
    // assertEquals("foo", c.get(10));
    // }
    //

    // @Test
    // public void loadFailedToSystemErr() throws Exception {
    // SystemErrCatcher str2 = SystemErrCatcher.get();
    // LogManager.getLogManager().readConfiguration();
    // c = newCache(cleanConf().worker().setWorkerManager(threadHelper).c().loading().setLoader(
    // loader));
    // try {
    // loading().load(10);
    // awaitAllLoads();
    // assertTrue(str2.toString().length() > 10);
    // } finally {
    // str2.terminate();
    // }
    // LogManager.getLogManager().readConfiguration();
    // }
    //
    // @Test
    // public void loadAllFailedToSystemErr() throws Exception {
    // LogManager.getLogManager().readConfiguration();
    // c = newCache(cleanConf().worker().setWorkerManager(threadHelper).c().loading().setLoader(
    // loader));
    // SystemErrCatcher str2 = SystemErrCatcher.get();
    // try {
    // loading().loadAll(Arrays.asList(1, 10));
    // awaitAllLoads();
    // assertTrue(str2.toString().length() > 10);
    // } finally {
    // str2.terminate();
    // }
    // LogManager.getLogManager().readConfiguration();
    // }
    //
    // @Test
    // public void loadFailed() {
    // final Logger logger = TestUtil.dummy(Logger.class);
    // c = newCache(newConf().exceptionHandling().setExceptionHandler(new BaseExceptionHandler() {
    // @Override
    // public String loadingLoadValueFailed(CacheExceptionContext<Integer, String> context,
    // CacheLoader<? super Integer, ?> loader, Integer key, AttributeMap map) {
    // try {
    // assertNotNull(context);
    // assertSame(logger, context.getLogger());
    // assertSame(c, context.getCache());
    // assertEquals(10, key.intValue());
    // assertNotNull(map);
    // assertTrue(context.getCause() instanceof Exception1);
    // } catch (Error t) {
    // failure = t;
    // throw t;
    // }
    // return "foo";
    // }
    // }).setExceptionLogger(logger).c().loading().setLoader(loader));
    // loadAndAwait(M1);
    // assertSize(1);
    // loading().load(10);
    // awaitAllLoads();
    // assertSize(2);
    // assertEquals("foo", c.get(10));
    // loadAndAwait(M2);
    // assertSize(3);
    // }
    //
    // @Test
    // public void loadFailedNoValue() {
    // final Logger logger = context.mock(Logger.class);
    // c = newCache(newConf().exceptionHandling().setExceptionHandler(new BaseExceptionHandler() {
    // @Override
    // public String loadingLoadValueFailed(CacheExceptionContext<Integer, String> context,
    // CacheLoader<? super Integer, ?> loader, Integer key, AttributeMap map) {
    // try {
    // assertNotNull(context);
    // assertSame(logger, context.getLogger());
    // assertSame(c, context.getCache());
    // assertEquals(10, key.intValue());
    // assertNotNull(map);
    // assertTrue(context.getCause() instanceof Exception1);
    // } catch (Error t) {
    // failure = t;
    // throw t;
    // }
    // return null;
    // }
    // }
    //
    // ).setExceptionLogger(logger).c().loading().setLoader(loader));
    // loadAndAwait(M1);
    // assertSize(1);
    // assertNull(c.get(10));
    // assertSize(1);
    // loadAndAwait(M2);
    // assertSize(2);
    // }

    @After
    public void checkFailure() throws Throwable {
        if (failure != null) {
            throw failure;
        }
    }

    class LoaderVerifyingExceptionHandler extends BaseExceptionHandler {
        private final Logger logger;

        LoaderVerifyingExceptionHandler(Logger logger) {
            this.logger = logger;
        }

        @Override
        public String loadingOfValueFailed(ExceptionContext<Cache<Integer, String>> context, Integer key,
                AttributeMap map) {
            assertSame(c, context.getContainer());
            return "foo";
        }
    }

    public static class BaseExceptionHandler extends CacheExceptionHandler<Integer, String> {}
}
