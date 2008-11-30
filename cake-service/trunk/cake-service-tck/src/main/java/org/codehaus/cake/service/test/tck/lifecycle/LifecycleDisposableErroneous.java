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
package org.codehaus.cake.service.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.internal.util.LogHelper;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.annotation.OnTermination;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.test.util.throwables.Error1;
import org.codehaus.cake.test.util.throwables.Exception1;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.junit.After;
import org.junit.Test;

public class LifecycleDisposableErroneous extends AbstractTCKTest<Container, ContainerConfiguration> {

    private CountDownLatch latch = new CountDownLatch(0);

    @After
    public void after() {
        assertEquals(0, latch.getCount());
    }

    /**
     * Same as {@link #unknownObject()} except that it checks that the original exception is rethrown for subsequent
     * invocation of container methods.
     * 
     */
    @Test
    public void unknownObject() throws Throwable {
        conf.addToLifecycle(new DisposeObject());
        latch = new CountDownLatch(1);

        conf.setDefaultLogger(new LogHelper.AbstractLogger() {
            public String getName() {
                return "foo";
            }

            public boolean isEnabled(Level level) {
                return level == Level.Warn || level == Level.Error || level == Level.Fatal;
            }

            public void log(Level level, String message, Throwable cause) {
                if (isEnabled(level)) {
                    assertEquals(Level.Error, level);
                    assertNotNull(message);
                    assertNull(cause);
                    latch.countDown();
                }
            }
        });
        newContainer();
        prestart();
        c.shutdown();
        awaitTermination();
    }

    @Test
    public void shutdownRuntimeException() throws Throwable {
        conf.addToLifecycle(new DisposeRuntimeException());
        latch = new CountDownLatch(1);

        conf.setDefaultLogger(new LogHelper.AbstractLogger() {
            public String getName() {
                return "foo";
            }

            public boolean isEnabled(Level level) {
                return level == Level.Warn || level == Level.Error || level == Level.Fatal;
            }

            public void log(Level level, String message, Throwable cause) {
                if (isEnabled(level)) {
                    assertEquals(Level.Error, level);
                    assertNotNull(message);
                    assertSame(RuntimeException1.INSTANCE, cause);
                    latch.countDown();
                }
            }
        });
        newContainer();
        prestart();
        c.shutdown();
        awaitTermination();
    }

    @Test
    public void shutdownException() throws Throwable {
        conf.addToLifecycle(new DisposeException());
        latch = new CountDownLatch(1);

        conf.setDefaultLogger(new LogHelper.AbstractLogger() {
            public String getName() {
                return "foo";
            }

            public boolean isEnabled(Level level) {
                return level == Level.Warn || level == Level.Error || level == Level.Fatal;
            }

            public void log(Level level, String message, Throwable cause) {
                if (isEnabled(level)) {
                    assertEquals(Level.Error, level);
                    assertNotNull(message);
                    assertSame(Exception1.INSTANCE, cause);
                    latch.countDown();
                }
            }
        });
        newContainer();
        prestart();
        c.shutdown();
        awaitTermination();
    }

    @Test(expected = Error1.class)
    public void shutdownError() throws Throwable {
        conf.addToLifecycle(new DisposeError());
        latch = new CountDownLatch(1);

        conf.setDefaultLogger(new LogHelper.AbstractLogger() {
            public String getName() {
                return "foo";
            }

            public boolean isEnabled(Level level) {
                return level == Level.Warn || level == Level.Error || level == Level.Fatal;
            }

            public void log(Level level, String message, Throwable cause) {
                if (isEnabled(level)) {
                    assertEquals(Level.Error, level);
                    assertNotNull(message);
                    assertSame(Error1.INSTANCE, cause);
                    latch.countDown();
                }
            }
        });
        newContainer();
        prestart();
        c.shutdown();
        awaitTermination();
    }

    public static class DisposeRuntimeException {
        @OnTermination
        public void dispose() {
            throw RuntimeException1.INSTANCE;
        }
    }

    public static class DisposeException {
        @OnTermination
        public void dispose() throws Exception {
            throw Exception1.INSTANCE;
        }
    }

    public static class DisposeError {
        @OnTermination
        public void dispose() {
            throw Error1.INSTANCE;
        }
    }

    public static class DisposeObject {
        @OnTermination
        public void dispose(Object object) {}
    }
}
