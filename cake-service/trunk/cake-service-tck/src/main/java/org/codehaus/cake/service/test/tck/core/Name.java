package org.codehaus.cake.service.test.tck.core;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.test.tck.AbstractTCKTest;
import org.junit.Test;

public class Name extends AbstractTCKTest<Container, ContainerConfiguration>{

    @Test
    public void defaultName() {
        assertNotNull(c.getName());
    }
    
    @Test
    public void setName() {
        conf.setName("hello");
        newContainer();
        assertEquals("hello",c.getName());
    }
}
