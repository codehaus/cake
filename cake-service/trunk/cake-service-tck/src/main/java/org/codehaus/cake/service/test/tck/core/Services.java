package org.codehaus.cake.service.test.tck.core;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.Test;

public class Services extends AbstractTCKTest<Container, ContainerConfiguration> {

    @Test(expected = NullPointerException.class)
    public void getServiceNPE() {
        c.getService(null);
    }
}
