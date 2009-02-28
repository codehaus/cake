package org.codehaus.cake.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ContainerShutdownExceptionTest {

    @Test
    public void test() {
        assertNull(new ContainerShutdownException().getCause());
        assertNull(new ContainerShutdownException("foo").getCause());
        assertNull(new ContainerShutdownException().getMessage());
        assertEquals("foo", new ContainerShutdownException("foo").getMessage());
    }

}
