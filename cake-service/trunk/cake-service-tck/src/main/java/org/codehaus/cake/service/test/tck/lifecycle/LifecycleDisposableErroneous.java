package org.codehaus.cake.service.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.internal.util.LogHelper;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.Disposable;
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
        conf.addService(new DisposeObject());
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
        conf.addService(new DisposeRuntimeException());
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
        conf.addService(new DisposeException());
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
        conf.addService(new DisposeError());
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
        @Disposable
        public void dispose() {
            throw RuntimeException1.INSTANCE;
        }
    }

    public static class DisposeException {
        @Disposable
        public void dispose() throws Exception {
            throw Exception1.INSTANCE;
        }
    }

    public static class DisposeError {
        @Disposable
        public void dispose() {
            throw Error1.INSTANCE;
        }
    }

    public static class DisposeObject {
        @Disposable
        public void dispose(Object object) {}
    }
}
