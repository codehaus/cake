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
package org.codehaus.cake.service.test.tck.core;

import org.codehaus.cake.internal.util.LogHelper;
import org.codehaus.cake.service.AfterStart;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.OnShutdown;
import org.codehaus.cake.service.OnStart;
import org.codehaus.cake.service.OnTermination;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.test.util.throwables.Error1;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.util.Logger;
import org.junit.Test;

public class DebugTrace extends AbstractTCKTest<Container, ContainerConfiguration> {

    public static final Logger DEBUG = new LogHelper.AbstractLogger() {
        public String getName() {
            return "foo";
        }

        public boolean isEnabled(Level level) {
            return level != Level.Trace;
        }

        public void log(Level level, String message, Throwable cause) {}
    };
    public static final Logger TRACE = new LogHelper.AbstractLogger() {
        public String getName() {
            return "foo";
        }

        public boolean isEnabled(Level level) {
            return true;
        }

        public void log(Level level, String message, Throwable cause) {}
    };

    @Test
    public void debug() {
        conf.setDefaultLogger(DEBUG);
        conf.addService(new TestIt(null, null, null, null));
        newContainer();
        prestart();
        shutdownAndAwaitTermination();
    }

    @Test(expected = RuntimeException1.class)
    public void debug1() {
        conf.setDefaultLogger(DEBUG);
        conf.addService(new TestIt(RuntimeException1.INSTANCE, null, null, null));
        newContainer();
        prestart();
        shutdownAndAwaitTermination();
    }

    @Test(expected = Error1.class)
    public void debug2() {
        conf.setDefaultLogger(DEBUG);
        conf.addService(new TestIt(null, Error1.INSTANCE, null, null));
        newContainer();
        prestart();
        shutdownAndAwaitTermination();
    }

    @Test
    public void trace() {
        conf.setDefaultLogger(TRACE);
        conf.addService(new TestIt(null, null, null, null));
        newContainer();
        prestart();
        shutdownAndAwaitTermination();
    }

    public static class TestIt {

        private Throwable t1;
        private Throwable t2;
        private Throwable t3;
        private Throwable t4;

        TestIt(Throwable t1, Throwable t2, Throwable t3, Throwable t4) {
            this.t1 = t1;
            this.t2 = t2;
            this.t3 = t3;
            this.t4 = t4;
        }

        @AfterStart
        public void afterStart() throws Throwable {
            if (t2 != null) {
                throw t2;
            }
        }

        @OnTermination
        public void dispose() throws Throwable {
            if (t4 != null) {
                throw t4;
            }
        }

        @OnShutdown
        public void shutdown() throws Throwable {
            if (t3 != null) {
                throw t3;
            }
        }

        @OnStart
        public void start() throws Throwable {
            if (t1 != null) {
                throw t1;
            }
        }
    }
}
