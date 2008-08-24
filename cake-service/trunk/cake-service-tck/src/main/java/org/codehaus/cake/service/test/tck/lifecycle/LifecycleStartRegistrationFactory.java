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

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.service.test.tck.lifecycle.Services.Register2;
import org.codehaus.cake.service.test.tck.lifecycle.Services.RegisterFactory0;
import org.codehaus.cake.service.test.tck.lifecycle.Services.RegisterFactory1;
import org.codehaus.cake.service.test.tck.lifecycle.Services.RegisterFactoryKeyNull;
import org.codehaus.cake.service.test.tck.lifecycle.Services.RegisterFactoryServiceNull;
import org.junit.Test;

public class LifecycleStartRegistrationFactory extends AbstractTCKTest<Container, ContainerConfiguration> {

    /**
     * Tests that {@link UnsupportedOperationException} is thrown if a requested service is not available.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void noneRegistered() {
        newContainer();
        c.getService(Integer.class, Attributes.EMPTY_ATTRIBUTE_MAP).intValue();
    }

    @Test
    public void register() {
        conf.addServiceToLifecycle(new RegisterFactory0());
        newContainer();
        assertEquals(0, c.getService(Integer.class).intValue());
        assertEquals(1, c.getService(Integer.class, Services.I.singleton(1)).intValue());
    }

    @Test(expected = NullPointerException.class)
    public void registerNullKey() {
        conf.addServiceToLifecycle(new RegisterFactoryKeyNull());
        newContainer();
        prestart();
    }

    @Test(expected = NullPointerException.class)
    public void registerNullService() {
        conf.addServiceToLifecycle(new RegisterFactoryServiceNull());
        newContainer();
        prestart();
    }

    @Test
    public void registerAskLatest() {
        conf.addServiceToLifecycle(new RegisterFactory0());
        conf.addServiceToLifecycle(new RegisterFactory1());
        newContainer();
        assertEquals(1, c.getService(Integer.class).intValue());
        assertEquals(0, c.getService(Integer.class, Services.I.singleton(-1)).intValue());
    }

    @Test
    public void registerDelegateToNextFactory() {
        conf.addServiceToLifecycle(new RegisterFactory1());
        conf.addServiceToLifecycle(new RegisterFactory0());
        newContainer();
        assertEquals(0, c.getService(Integer.class).intValue());
        assertEquals(1, c.getService(Integer.class, Services.I.singleton(1)).intValue());
        assertEquals(0, c.getService(Integer.class, Services.I.singleton(-1)).intValue());// delegate to
                                                                                          // RegisterFactory1
    }

    @Test
    public void registerDelegateToNextService() {
        conf.addServiceToLifecycle(new Register2());
        conf.addServiceToLifecycle(new RegisterFactory0());
        newContainer();
        assertEquals(0, c.getService(Integer.class).intValue());
        assertEquals(1, c.getService(Integer.class, Services.I.singleton(1)).intValue());
        assertEquals(2, c.getService(Integer.class, Services.I.singleton(-1)).intValue());// delegate to
                                                                                          // RegisterFactory1
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delegateToUnknown() {
        conf.addServiceToLifecycle(new RegisterFactory0());
        newContainer();
        c.getService(Integer.class, Services.I.singleton(-1));
    }

}
