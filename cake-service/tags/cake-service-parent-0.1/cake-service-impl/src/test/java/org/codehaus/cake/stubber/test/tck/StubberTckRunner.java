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
package org.codehaus.cake.stubber.test.tck;

import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.codehaus.cake.service.test.tck.TckRunner;
import org.codehaus.cake.stubber.StubberConfiguration;
import org.codehaus.cake.stubber.test.tck.core.CoreSuite;
import org.codehaus.cake.stubber.test.tck.service.exceptionhandling.ExceptionHandlingSuite;
import org.junit.internal.runners.InitializationError;

public class StubberTckRunner extends TckRunner {

    public StubberTckRunner(Class<?> clazz) throws InitializationError {
        super(clazz, StubberConfiguration.class);
    }

    protected void initialize() throws Exception {
        super.initialize();
        add(new ServiceSuite(CoreSuite.class));
        add(new ServiceSuite(ExceptionHandlingSuite.class));
        // if (isThreadSafe()) {
        // add(new ServiceSuite(ExecutorsSuite.class));
        // add(new ServiceSuite(ManagementSuite.class));
        // } else {
        // add(new JUnit4ClassRunner(ManagementNoSupport.class));
        // }
        // add(new ServiceSuite(AttributeSuite.class));

        // add(new ServiceSuite(CoreSuite.class));
        // add(new ServiceSuite(LifecycleSuite.class));
        // services

        // add(new ServiceSuite(ManagementSuite.class));

    }
}
