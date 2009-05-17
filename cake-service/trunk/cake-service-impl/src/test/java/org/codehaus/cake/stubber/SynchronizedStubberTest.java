/*
 * Copyright 2008, 2009 Kasper Nielsen.
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
package org.codehaus.cake.stubber;

import org.codehaus.cake.service.test.tck.TckImplementationSpecifier;
import org.codehaus.cake.stubber.test.tck.AbstraktStubberTCKTst;
import org.codehaus.cake.stubber.test.tck.StubberTckRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(StubberTckRunner.class)
@TckImplementationSpecifier(SynchronizedStubber.class)
public class SynchronizedStubberTest extends AbstraktStubberTCKTst {

    @Test
    public void testInstance() {
        assertTrue(c instanceof SynchronizedStubber<?>);
    }
}
