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
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.Test;

public class LifecycleStartRegistration extends AbstractTCKTest<Container, ContainerConfiguration> {

    @Test(expected = UnsupportedOperationException.class)
    public void registerNone() {
        newContainer();
        c.getService(Integer.class).intValue();
    }

    @Test
    public void register() {
        conf.addServiceToLifecycle(new Register());
        newContainer();
        prestart();
        assertEquals(1000, c.getService(Integer.class).intValue());
    }

    @Test(expected = NullPointerException.class)
    public void registerNullKey() {
        conf.addServiceToLifecycle(new RegisterNullKey());
        newContainer();
        prestart();
    }

    @Test(expected = NullPointerException.class)
    public void registerNullService() {
        conf.addServiceToLifecycle(new RegisterNullService());
        newContainer();
        prestart();
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerSame() {
        conf.addServiceToLifecycle(new RegisterSame());
        newContainer();
        prestart();
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerSameTwice() {
        conf.addServiceToLifecycle(new Register());
        conf.addServiceToLifecycle(new Register());
        newContainer();
        prestart();
    }

    public static class Register {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 1000);
        }
    }

    public static class RegisterNullKey {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(null, 1000);
        }
    }

    public static class RegisterNullService {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, null);
        }
    }

    public static class RegisterSame {
        @Startable
        public void start1(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 1000);
        }

        @Startable
        public void start2(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 1000);
        }
    }
}
