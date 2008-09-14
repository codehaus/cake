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
package org.codehaus.cake.stubber.test.tck.service.exceptionhandling;

import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.service.common.exceptionhandling.ExceptionContext;
import org.codehaus.cake.stubber.Stubber;
import org.codehaus.cake.stubber.exceptionhandling.StubberExceptionHandler;
import org.codehaus.cake.stubber.test.tck.AbstraktStubberTCKTst;
import org.codehaus.cake.test.util.throwables.Exception1;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.util.Logger;
import org.codehaus.cake.util.Logger.Level;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExceptionHandling2 extends AbstraktStubberTCKTst {

    SExceptionHandler seh;

    @Before
    public void before() {
        seh = new SExceptionHandler();
    }

    @After
    public void after() {
        seh = new SExceptionHandler();
    }

    @Test
    public void test() {
        conf.withExceptions().setExceptionHandler(seh);
        newContainer();
        c.getIt(5);

        c.fail(Level.Fatal, "foo", null);
        assertEquals(seh.last, Level.Fatal, "foo", null, null);

        c.fail(Level.Fatal, "foo1", RuntimeException1.INSTANCE);
        assertEquals(seh.last, Level.Fatal, "foo1", RuntimeException1.INSTANCE, null);

        c.fail(Level.Error, "foo2", null);
        assertEquals(seh.last, Level.Error, "foo2", null, null);

        c.fail(Level.Error, "foo3", Exception1.INSTANCE);
        assertEquals(seh.last, Level.Error, "foo3", Exception1.INSTANCE, null);

        c.fail(Level.Warn, "foo4", Exception1.INSTANCE);
        assertEquals(seh.last, Level.Warn, "foo4", null, null);

    }

    @Test
    public void testLogger() {
        Logger logger = dummy(Logger.class);
        conf.withExceptions().setExceptionHandler(seh).setExceptionLogger(logger);
        newContainer();
        c.getIt(5);

        c.fail(Level.Warn, "foo4", Exception1.INSTANCE);
        assertEquals(seh.last, Level.Warn, "foo4", null, logger);

    }

    public void assertEquals(ExceptionContext context, Level level, String message, Throwable cause, Logger logger) {
        assertSame(c, context.getContainer());
        assertSame(level, context.getLevel());
        assertSame(cause, context.getCause());
        if (logger == null) {
            assertNotNull(context.getLogger());
        } else {
            assertSame(logger, context.getLogger());// technically it could be wrapped
        }
        assertEquals(message, context.getMessage());

    }

    static class SExceptionHandler extends BaseExceptionHandler {
        public void handle(ExceptionContext<Stubber<Integer>> context) {
            last = context;
        }

        ExceptionContext last;

        public Integer tFailed(ExceptionContext<Stubber<Integer>> context, Integer key) {

            return 123;
        }
    }

    public static class BaseExceptionHandler extends StubberExceptionHandler<Integer> {}
}
