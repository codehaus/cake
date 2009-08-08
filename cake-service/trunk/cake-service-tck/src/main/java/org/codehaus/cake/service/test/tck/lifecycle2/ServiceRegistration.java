package org.codehaus.cake.service.test.tck.lifecycle2;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.Test;

public class ServiceRegistration extends AbstractTCKTest<Container, ContainerConfiguration> {

    @Test(expected = UnsupportedOperationException.class)
    public void registerNone() {
        newContainer();
        c.getService(Integer.class).intValue();
    }
}
