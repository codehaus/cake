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

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.ServiceFactory;
import org.codehaus.cake.service.ServiceRegistrant;
import org.codehaus.cake.service.Startable;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.Test;

public class LifecycleStartRegistrationFactory extends AbstractTCKTest<Container, ContainerConfiguration> {

    static final IntAttribute I = new IntAttribute() {};

    @Test(expected = UnsupportedOperationException.class)
    public void registerNone() {
        newContainer();
        c.getService(Integer.class, Attributes.EMPTY_ATTRIBUTE_MAP).intValue();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void registerUnknown() {
        conf.addService(new Register());
        newContainer();
        c.getService(Integer.class, I.singleton(-1)).intValue();
    }

    @Test
    public void register() {
        conf.addService(new Register());
        newContainer();
        prestart();
        assertEquals(100, c.getService(Integer.class, I.singleton(100)).intValue());
    }

    @Test(expected = NullPointerException.class)
    public void registerNullKey() {
        conf.addService(new RegisterNullKey());
        newContainer();
        prestart();
    }

    @Test(expected = NullPointerException.class)
    public void registerNullService() {
        conf.addService(new RegisterNullService());
        newContainer();
        prestart();
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerSame() {
        conf.addService(new RegisterSame());
        newContainer();
        prestart();
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerSameOdd() {
        conf.addService(new RegisterSameOdd());
        newContainer();
        prestart();
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerSameTwice() {
        conf.addService(new Register());
        conf.addService(new Register());
        newContainer();
        prestart();
    }

    public static class Register {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerFactory(Integer.class, new ServiceFactoryStub());
        }
    }

    public static class RegisterNullKey {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerFactory(null, new ServiceFactoryStub());
        }
    }

    public static class RegisterNullService {
        @Startable
        public void start(ServiceRegistrant registrant) {
            registrant.registerFactory(Integer.class, null);
        }
    }

    public static class RegisterSame {
        @Startable
        public void start1(ServiceRegistrant registrant) {
            registrant.registerFactory(Integer.class, new ServiceFactoryStub());
        }

        @Startable
        public void start2(ServiceRegistrant registrant) {
            registrant.registerFactory(Integer.class, new ServiceFactoryStub());
        }
    }

    public static class RegisterSameOdd {
        @Startable
        public void start1(ServiceRegistrant registrant) {
            registrant.registerService(Integer.class, 4);
        }

        @Startable
        public void start2(ServiceRegistrant registrant) {
            registrant.registerFactory(Integer.class, new ServiceFactoryStub());
        }
    }

    static class ServiceFactoryStub implements ServiceFactory<Integer> {

        public Integer lookup(AttributeMap attributes) {
            int val = attributes.get(I);
            return val >= 0 ? val : null;
        }
    }
}
