package org.codehaus.cake.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ContainerAlreadyShutdownExceptionTest {

    @Test
    public void test() {
        assertNull(new ContainerAlreadyShutdownException().getCause());
        assertNull(new ContainerAlreadyShutdownException("foo").getCause());
        assertNull(new ContainerAlreadyShutdownException().getMessage());
        assertEquals("foo", new ContainerAlreadyShutdownException("foo").getMessage());
    }

}
