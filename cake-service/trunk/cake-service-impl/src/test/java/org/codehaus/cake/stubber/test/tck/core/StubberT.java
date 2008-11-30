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
package org.codehaus.cake.stubber.test.tck.core;

import org.codehaus.cake.service.annotation.OnStart;
import org.codehaus.cake.stubber.test.tck.AbstraktStubberTCKTst;
import org.junit.Test;

public class StubberT extends AbstraktStubberTCKTst {
    
    @Test(expected = IllegalStateException.class)
    public void t() {
        conf.addToLifecycleAndExport(Register.class, new Register());
        newContainer();
        c.getIt(5);
        assertTrue(c.hasService(Integer.class));
        shutdownAndAwaitTermination();
        assertTrue(c.hasService(Integer.class));
    }

    public class Register {
        @OnStart
        public void start() {
            c.getIt(5);
        }
    }
}
