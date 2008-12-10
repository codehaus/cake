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
package org.codehaus.cake.service.test.tck.service.management;

import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.management.ManagementConfiguration;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.codehaus.cake.service.test.tck.UnsupportedServices;
import org.junit.Test;

@UnsupportedServices(value = { Manageable.class })
public class ManagementNoSupport extends AbstractTCKTest<Container, ContainerConfiguration> {
    @Test(expected = IllegalArgumentException.class)
    public void noManagementSupport() throws Throwable {
        withConf(ManagementConfiguration.class).setEnabled(true);
        cheatInstantiate();
    }
}
