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
package org.codehaus.cake.service;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ExceptionContext;
import org.codehaus.cake.service.ExceptionHandler;
import org.codehaus.cake.test.util.TestUtil;
import org.codehaus.cake.test.util.throwables.Error1;
import org.codehaus.cake.test.util.throwables.Exception1;
import org.codehaus.cake.util.Logger;
import org.codehaus.cake.util.Logger.Level;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class ExceptionHandlerTest {
    Mockery context = new JUnit4Mockery();

    Container c1 = TestUtil.dummy(Container.class);

    @Test
    public void lifecycle() throws Exception {
        ExceptionHandler<Container> eh = new ExceptionHandler<Container>() {};
        eh.initialize(null);
        eh.terminated();
    }

    @Test
    public void noError() throws Exception {
        ExceptionHandler<Container> eh = new ExceptionHandler<Container>() {};
        final Logger logger = context.mock(Logger.class);
        context.checking(new Expectations() {
            {
                one(logger).log(Logger.Level.Error, "foo", Exception1.INSTANCE);
            }
        });
        eh.handle(new ExceptionContext<Container>() {
            public Throwable getCause() {
                return Exception1.INSTANCE;
            }

            @Override
            public Container getContainer() {
                return c1;
            }

            @Override
            public Level getLevel() {
                return Logger.Level.Error;
            }

            @Override
            public Logger getLogger() {
                return logger;
            }

            @Override
            public String getMessage() {
                return "foo";
            }

            @Override
            public ExceptionHandler<Container> getParent() {
                // TODO Auto-generated method stub
                return null;
            }
        });
    }

    @Test(expected = Error1.class)
    public void error() throws Exception {
        ExceptionHandler<Container> eh = new ExceptionHandler<Container>() {};
        final Logger logger = context.mock(Logger.class);
        context.checking(new Expectations() {
            {
                one(logger).log(Logger.Level.Fatal, "foo", Error1.INSTANCE);
            }
        });
        eh.handle(new ExceptionContext<Container>() {
            public Throwable getCause() {
                return Error1.INSTANCE;
            }

            @Override
            public Container getContainer() {
                return c1;
            }

            @Override
            public Level getLevel() {
                return Logger.Level.Fatal;
            }

            @Override
            public Logger getLogger() {
                return logger;
            }

            @Override
            public String getMessage() {
                return "foo";
            }

            @Override
            public ExceptionHandler<Container> getParent() {
                return null;
            }
        });
    }
}
