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

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.Test;

public class LifecycleStartRegistration extends AbstractTCKTest<Container, ContainerConfiguration> {


    @Test
    public void register() {
//        conf.addToLifecycle(new Register1());
//        newContainer();
//        prestart();
//        assertEquals(1, c.getService(Integer.class).intValue());
    }
//
//    @Test(expected = NullPointerException.class)
//    public void registerNullKey() {
//        conf.addToLifecycle(new RegisterKeyNull());
//        newContainer();
//        prestart();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void registerNullService() {
//        conf.addToLifecycle(new RegisterServiceNull());
//        newContainer();
//        prestart();
//    }
//
//    @Test
//    public void registerLastIsActive() {
//        conf.addToLifecycle(new Register1());
//        conf.addToLifecycle(new Register2());
//        newContainer();
//        prestart();
//        assertEquals(2, c.getService(Integer.class).intValue()); // take last
//    }
}
