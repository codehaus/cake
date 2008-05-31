package org.codehaus.cake.service.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.internal.util.LogHelper;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.Stoppable;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.test.util.throwables.Error1;
import org.codehaus.cake.test.util.throwables.Exception1;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.junit.After;
import org.junit.Test;

public class LifecycleStoppableErroneous extends AbstractTCKTest<Container, ContainerConfiguration> {

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
        conf.addService(new StopObject());
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
    }

    @Test
    public void shutdownRuntimeException() throws Throwable {
        conf.addService(new StopRuntimeException());
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
    }

    @Test
    public void shutdownException() throws Throwable {
        conf.addService(new StopException());
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
    }

    @Test(expected = Error1.class)
    public void shutdownError() throws Throwable {
        conf.addService(new StopError());
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
    }
    @Test
    public void shutdownPackageProteced() throws Throwable {
        conf.addService(new StopPackageProtected());
      //  latch = new CountDownLatch(1);

        conf.setDefaultLogger(new LogHelper.AbstractLogger() {
            public String getName() {
                return "foo";
            }

            public boolean isEnabled(Level level) {
                return level == Level.Warn || level == Level.Error || level == Level.Fatal;
            }

            public void log(Level level, String message, Throwable cause) {
                if (isEnabled(level)) {
                    System.out.println(cause.getClass());
                    assertEquals(Level.Error, level);
                    assertNotNull(message);
                    //assertSame(Exception1.INSTANCE, cause);
                    latch.countDown();
                }
            }
        });
        newContainer();
        prestart();
        c.shutdown();
    }
    public static class StopRuntimeException {
        @Stoppable
        public void stop() {
            throw RuntimeException1.INSTANCE;
        }
    }

    public static class StopException {
        @Stoppable
        public void stop() throws Exception {
            throw Exception1.INSTANCE;
        }
    }

    public static class StopError {
        @Stoppable
        public void stop() {
            throw Error1.INSTANCE;
        }
    }

    public static class StopObject {
        @Stoppable
        public void stop(Object object) {}
    }

    public static class StopPackageProtected {
        @Stoppable
        void stop() {
            System.out.println("ran");
        }
    }
}
