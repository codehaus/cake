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
package org.codehaus.cake.service.test.tck.core;

import java.util.Set;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.util.attribute.Attributes;
import org.junit.Test;

public class Services extends AbstractTCKTest<Container, ContainerConfiguration> {

    @Test(expected = NullPointerException.class)
    public void getServiceNPE() {
        getService(null);
    }

    @Test(expected = NullPointerException.class)
    public void getServiceNPE1() {
        getService(null, Attributes.EMPTY_ATTRIBUTE_MAP);
    }

    @Test(expected = NullPointerException.class)
    public void getServiceNPE2() {
        getService(Integer.class, null);
    }

    @Test
    public void serviceGet() {
        conf.addService(Integer.class, new Integer(1000));
        newContainer();
        assertFalse(c.getState().isStarted());
        assertNotNull(getService(Integer.class));
        checkLazystart();
    }

    /**
     * {@link Set#clear()} fails when the cache is shutdown.
     */
    @Test
    public void serviceGetShutdown() {
        conf.addService(Integer.class, new Integer(1000));
        newContainer();
        Integer i = getService(Integer.class);
        shutdownAndAwaitTermination();
        assertSame(i, getService(Integer.class));
    }

    @Test
    public void serviceNoHas() {
        conf.addService(Integer.class, new Integer(1000));
        newContainer();
        assertFalse(c.getState().isStarted());
        assertFalse(c.hasService(Double.class));
        checkLazystart();
    }

    @Test
    public void serviceHas() {
        conf.addService(Integer.class, new Integer(1000));
        newContainer();
        assertFalse(c.getState().isStarted());
        assertTrue(c.hasService(Integer.class));
        checkLazystart();
    }

    @Test
    public void serviceHasShutdown() {
        conf.addService(Integer.class, new Integer(1000));
        newContainer();
        assertTrue(c.hasService(Integer.class));
        shutdownAndAwaitTermination();
        assertTrue(c.hasService(Integer.class));
    }

}
