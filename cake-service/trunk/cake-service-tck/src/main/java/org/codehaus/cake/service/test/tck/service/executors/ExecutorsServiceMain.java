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
package org.codehaus.cake.service.test.tck.service.executors;

import java.util.concurrent.ExecutorService;

import org.codehaus.cake.service.test.tck.RequireService;
import org.junit.Test;

@RequireService( { ExecutorService.class })
public class ExecutorsServiceMain extends AbstractExecutorsTckTest {

    @Test
    public void testServiceAvailable() {
        newConfigurationClean();
        newContainer();
        assertTrue(c.hasService(ExecutorService.class));
        assertNotNull(c.getService(ExecutorService.class));
        assertTrue(c.serviceKeySet().contains(ExecutorService.class));
    }
}
