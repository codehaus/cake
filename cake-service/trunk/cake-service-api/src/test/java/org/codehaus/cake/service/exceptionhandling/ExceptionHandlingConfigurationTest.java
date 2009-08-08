/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.service.exceptionhandling;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.spi.ExceptionHandler;
import org.codehaus.cake.service.spi.ExceptionHandlingConfiguration;
import org.codehaus.cake.util.Logger;
import org.junit.Before;
import org.junit.Test;

public class ExceptionHandlingConfigurationTest {
    ExceptionHandlingConfiguration<DummyExceptionHandler> e;

    @Before
    public void setUp() {
        e = new ExceptionHandlingConfiguration<DummyExceptionHandler>();
    }

    @Test
    public void exceptionHandler() {
        DummyExceptionHandler deh = new DummyExceptionHandler();
        assertNull(e.getExceptionHandler());
        assertSame(e, e.setExceptionHandler(deh));
        assertSame(deh, e.getExceptionHandler());
    }

    @Test
    public void exceptionLogger() {
        Logger l = dummy(Logger.class);
        assertNull(e.getExceptionLogger());
        assertSame(e, e.setExceptionLogger(l));
        assertSame(l, e.getExceptionLogger());
    }

    static class DummyExceptionHandler extends ExceptionHandler<Container> {
    }
}
