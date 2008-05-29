/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.service.test.tck.service.executors;

import org.codehaus.cake.service.executor.ExecutorsService;
import org.codehaus.cake.service.test.tck.RequireService;
import org.junit.Test;

@RequireService( { ExecutorsService.class })
public class ExecutorsServiceMain extends AbstractExecutorsTckTest {

    @Test
    public void testServiceAvailable() {
        newConfigurationClean();
        newContainer();
        assertTrue(c.hasService(ExecutorsService.class));
        assertNotNull(c.getService(ExecutorsService.class));
        assertTrue(c.serviceKeySet().contains(ExecutorsService.class));
    }
}
