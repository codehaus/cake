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

import static org.codehaus.cake.test.util.TestUtil.dummy;

import java.util.concurrent.CountDownLatch;

import org.codehaus.cake.service.AfterStart;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.test.util.throwables.Error1;
import org.codehaus.cake.test.util.throwables.Exception1;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.test.util.throwables.Throwable1;
import org.junit.After;
import org.junit.Test;

public class LifecycleAfterErroneous extends AbstractTCKTest<Container, ContainerConfiguration> {
    private CountDownLatch latch = new CountDownLatch(0);

    @After
    public void after() {
        assertEquals(0, latch.getCount());
    }

    @Test(expected = IllegalStateException.class)
    public void serviceRegistrantAfterStart() {
        // latch = new CountDownLatch(1);
        conf.addService(new ServiceRegistrantAfterStart());
        newContainer();
        prestart();
    }

    @Test(expected = IllegalStateException.class)
    public void serviceRegistrantFactoryAfterStart() {
        // latch = new CountDownLatch(1);
        conf.addService(new ServiceRegistrantFactoryAfterStart());
        newContainer();
        prestart();
    }

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
        // try {
        // c.getAllServices();
        // fail("should fail");
        // } catch (IllegalStateException t) {
        // assertSame(cause, t.getCause());
        // }
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
        // try {
        // c.getAllServices();
        // fail("should fail");
        // } catch (IllegalStateException t) {
        // assertSame(cause, t.getCause());
        // }
    }

    public class ServiceRegistrantAfterStart {
        ServiceRegistrant s;

        @Startable
        public void start1(ServiceRegistrant s) {
            this.s = s;
        }

        @AfterStart
        public void start2() {
            s.registerService(Integer.class, 1);
        }
    }

    public class ServiceRegistrantFactoryAfterStart {
        ServiceRegistrant s;

        @Startable
        public void start1(ServiceRegistrant s) {
            this.s = s;
        }

        @AfterStart
        public void start2() {
            s.registerFactory(Integer.class, dummy(ServiceFactory.class));
        }
    }

    public class StartRuntimeException {
        @AfterStart
        public void start(ContainerConfiguration conf) {
            throw RuntimeException1.INSTANCE;
        }
    }

    public class StartException {
        @AfterStart
        public void start(ContainerConfiguration conf) throws Exception {
            throw Exception1.INSTANCE;
        }
    }

    public class StartError {
        @AfterStart
        public void start(ContainerConfiguration conf) {
            throw Error1.INSTANCE;
        }
    }

    public class StartContainer {
        @AfterStart
        public void start(Container conf) {}
    }

    public class StartThrowable {
        @AfterStart
        public void start(ContainerConfiguration conf) throws Throwable {
            throw Throwable1.INSTANCE;
        }
    }

    public class StartObject {
        @AfterStart
        public void start(Object object) {}
    }

    public class StartUnknown {
        @AfterStart
        public void start(Integer unknown) {}
    }

    public class StartPackageProtected {
        @AfterStart
        void start(Integer unknown) {}
    }
}
