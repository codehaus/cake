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
package org.codehaus.cake.service.test.tck.lifecycle;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.RunAfter;
import org.codehaus.cake.service.Container.State;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.test.util.throwables.Error1;
import org.codehaus.cake.test.util.throwables.Exception1;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.test.util.throwables.Throwable1;
import org.junit.After;
import org.junit.Test;

public class LifecycleAfterErroneous extends AbstractTCKTest<Container, ContainerConfiguration> {
    private CountDownLatch latch = new CountDownLatch(0);

    private static final boolean errorsRethrowLater = false;

    @After
    public void after() {
        assertEquals(0, latch.getCount());
    }

    // @Test(expected = IllegalStateException.class)
    // public void serviceRegistrantAfterStart() {
    // // latch = new CountDownLatch(1);
    // conf.addToLifecycle(new ServiceRegistrantAfterStart());
    // newContainer();
    // prestart();
    // }
    //
    // @Test(expected = IllegalStateException.class)
    // public void serviceRegistrantFactoryAfterStart() {
    // // latch = new CountDownLatch(1);
    // conf.addToLifecycle(new ServiceRegistrantFactoryAfterStart());
    // newContainer();
    // prestart();
    // }

    /**
     * Same as {@link #unknownObject()} except that it checks that the original exception is rethrown for subsequent
     * invocation of container methods.
     * 
     */
    @Test
    public void unknownObject() throws Throwable {
        conf.addService(new StartObject());
        newContainer();
        Throwable cause = null;
        try {
            prestart();
            fail("should fail");
        } catch (IllegalStateException ok) {
            cause = ok;
        }

        // TODO fix
        // // check that we throw the same exception again when invoking method
        if (errorsRethrowLater) {
            try {
                c.serviceKeySet();
                fail("should fail");
            } catch (IllegalStateException t) {
                assertSame(cause, t.getCause());
            }
        }
    }

    @Test
    public void startError() throws Throwable {
        conf.addService(new StartError());
        newContainer();
        try {
            prestart();
            fail("should fail");
        } catch (Error1 ok) {
            assertSame(Error1.INSTANCE, ok);
        }

        // check that we throw the same exception again when invoking method
        if (errorsRethrowLater) {
            try {
                c.serviceKeySet();
                fail("should fail");
            } catch (IllegalStateException t) {
                assertSame(Error1.INSTANCE, t.getCause());
            }
        }
    }

    @Test
    public void startRuntimeException() throws Throwable {
        conf.addService(new StartRuntimeException());
        newContainer();
        try {
            prestart();
            fail("should fail");
        } catch (RuntimeException1 ok) {
            assertSame(RuntimeException1.INSTANCE, ok);
        }

        // check that we throw the same exception again when invoking method
        if (errorsRethrowLater) {
            try {
                c.serviceKeySet();
                fail("should fail");
            } catch (IllegalStateException t) {
                assertSame(RuntimeException1.INSTANCE, t.getCause());
            }
        }
    }

    @Test
    public void startException() throws Throwable {
        conf.addService(new StartException());
        newContainer();
        try {
            prestart();
            fail("should fail");
        } catch (IllegalStateException ok) {
            assertSame(Exception1.INSTANCE, ok.getCause());
        }

        // check that we throw the same exception again when invoking method
        if (errorsRethrowLater) {
            try {
                c.serviceKeySet();
                fail("should fail");
            } catch (IllegalStateException t) {
                assertSame(Exception1.INSTANCE, t.getCause());
            }
        }
    }

    @Test
    public void unknown() throws Throwable {
        conf.addService(new StartUnknown());
        newContainer();
        Throwable cause = null;
        try {
            prestart();
            fail("should fail");
        } catch (IllegalStateException ok) {
            cause = ok;
        }

        // TODO fix
        // check that we throw the same exception again when invoking method
        if (errorsRethrowLater) {
            try {
                c.serviceKeySet();
                fail("should fail");
            } catch (IllegalStateException t) {
                assertSame(cause, t.getCause());
            }
        }
    }

    //
    // public class ServiceRegistrantAfterStart {
    // ServiceRegistrant s;
    //
    // @Startable
    // public void start1(ServiceRegistrant s) {
    // this.s = s;
    // }
    //
    // @AfterStart
    // public void start2() {
    // s.registerService(Integer.class, 1);
    // }
    // }
    //
    // public class ServiceRegistrantFactoryAfterStart {
    // ServiceRegistrant s;
    //
    // @Startable
    // public void start1(ServiceRegistrant s) {
    // this.s = s;
    // }
    //
    // @AfterStart
    // public void start2() {
    // s.registerFactory(Integer.class, dummy(ServiceFactory.class));
    // }
    // }

    public class StartRuntimeException {
        @RunAfter(State.RUNNING)
        public void start(ContainerConfiguration conf) {
            throw RuntimeException1.INSTANCE;
        }
    }

    public class StartException {
        @RunAfter(State.RUNNING)
        public void start(ContainerConfiguration conf) throws Exception {
            throw Exception1.INSTANCE;
        }
    }

    public class StartError {
        @RunAfter(State.RUNNING)
        public void start(ContainerConfiguration conf) {
            throw Error1.INSTANCE;
        }
    }

    public class StartContainer {
        @RunAfter(State.RUNNING)
        public void start(Container conf) {
        }
    }

    public class StartThrowable {
        @RunAfter(State.RUNNING)
        public void start(ContainerConfiguration conf) throws Throwable {
            throw Throwable1.INSTANCE;
        }
    }

    public class StartObject {
        @RunAfter(State.RUNNING)
        public void start(Object object) {
        }
    }

    public class StartUnknown {
        @RunAfter(State.RUNNING)
        public void start(Integer unknown) {
        }
    }

    public class StartPackageProtected {
        @RunAfter(State.RUNNING)
        void start(Integer unknown) {
        }
    }
}
