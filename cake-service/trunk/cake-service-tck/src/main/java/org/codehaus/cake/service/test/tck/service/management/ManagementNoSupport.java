/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.service.test.tck.service.management;

import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.management.ManagementConfiguration;
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
