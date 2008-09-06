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

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.annotation.Startable;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.test.util.throwables.Error1;
import org.codehaus.cake.test.util.throwables.Exception1;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.test.util.throwables.Throwable1;
import org.junit.Test;

public class LifecycleStartErroneous extends AbstractTCKTest<Container, ContainerConfiguration> {

    /**
     * Same as {@link #unknownObject()} except that it checks that the original exception is rethrown for subsequent
     * invocation of container methods.
     * 
     */
    @Test
    public void unknownObject() throws Throwable {
        conf.addToLifecycle(new StartObject());
        newContainer();
        Throwable cause = null;
        try {
            prestart();
            fail("should fail");
        } catch (IllegalStateException ok) {
            cause = ok;
        }

        // check that we throw the same exception again when invoking method
        try {
            c.serviceKeySet();
            fail("should fail");
        } catch (IllegalStateException t) {
            assertSame(cause, t.getCause());
        }
    }

    @Test
    public void unknown() throws Throwable {
        conf.addToLifecycle(new StartUnknown());
        newContainer();
        Throwable cause = null;
        try {
            prestart();
            fail("should fail");
        } catch (IllegalStateException ok) {
            cause = ok;
        }

        // check that we throw the same exception again when invoking method
        try {
            c.serviceKeySet();
            fail("should fail");
        } catch (IllegalStateException t) {
            assertSame(cause, t.getCause());
        }
    }

    @Test
    public void startRuntimeException() throws Throwable {
        conf.addToLifecycle(new StartRuntimeException());
        newContainer();
        try {
            prestart();
            fail("should fail");
        } catch (RuntimeException1 ok) {
            assertSame(RuntimeException1.INSTANCE, ok);
        }

        // check that we throw the same exception again when invoking method
        try {
            c.serviceKeySet();
            fail("should fail");
        } catch (IllegalStateException t) {
            assertSame(RuntimeException1.INSTANCE, t.getCause());
        }
    }

    @Test
    public void startContainer() throws Throwable {
        conf.addToLifecycle(new StartContainer());
        newContainer();
        Throwable cause = null;
        try {
            prestart();
            fail("should fail");
        } catch (IllegalStateException ok) {
            cause = ok;
        }

        // check that we throw the same exception again when invoking method
        try {
            c.serviceKeySet();
            fail("should fail");
        } catch (IllegalStateException t) {
            assertSame(cause, t.getCause());
        }
    }

    @Test
    public void startException() throws Throwable {
        conf.addToLifecycle(new StartException());
        newContainer();
        try {
            prestart();
            fail("should fail");
        } catch (IllegalStateException ok) {
            assertSame(Exception1.INSTANCE, ok.getCause());
        }

        // check that we throw the same exception again when invoking method
        try {
            c.serviceKeySet();
            fail("should fail");
        } catch (IllegalStateException t) {
            assertSame(Exception1.INSTANCE, t.getCause());
        }
    }

    @Test
    public void startError() throws Throwable {
        conf.addToLifecycle(new StartError());
        newContainer();
        try {
            prestart();
            fail("should fail");
        } catch (Error1 ok) {
            assertSame(Error1.INSTANCE, ok);
        }

        // check that we throw the same exception again when invoking method
        try {
            c.serviceKeySet();
            fail("should fail");
        } catch (IllegalStateException t) {
            assertSame(Error1.INSTANCE, t.getCause());
        }
    }

    @Test
    public void startThrowable() throws Throwable {
        conf.addToLifecycle(new StartThrowable());
        newContainer();
        try {
            prestart();
            fail("should fail");
        } catch (IllegalStateException ok) {
            assertSame(Throwable1.INSTANCE, ok.getCause());
        }

        // check that we throw the same exception again when invoking method
        try {
            c.serviceKeySet();
            fail("should fail");
        } catch (IllegalStateException t) {
            assertSame(Throwable1.INSTANCE, t.getCause());
        }
    }

    public static class StartRuntimeException {
        @Startable
        public void start(ContainerConfiguration conf) {
            throw RuntimeException1.INSTANCE;
        }
    }

    public static class StartException {
        @Startable
        public void start(ContainerConfiguration conf) throws Exception {
            throw Exception1.INSTANCE;
        }
    }

    public static class StartError {
        @Startable
        public void start(ContainerConfiguration conf) {
            throw Error1.INSTANCE;
        }
    }

    public static class StartContainer {
        @Startable
        public void start(Container conf) {}
    }

    public static class StartThrowable {
        @Startable
        public void start(ContainerConfiguration conf) throws Throwable {
            throw Throwable1.INSTANCE;
        }
    }

    public static class StartObject {
        @Startable
        public void start(Object object) {}
    }

    public static class StartUnknown {
        @Startable
        public void start(Integer unknown) {}
    }

    public static class StartPackageProtected {
        @Startable
        void start(Integer unknown) {}
    }
}
