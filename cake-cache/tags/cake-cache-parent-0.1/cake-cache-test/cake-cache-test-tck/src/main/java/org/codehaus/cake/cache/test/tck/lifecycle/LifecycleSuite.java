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
package org.codehaus.cake.cache.test.tck.lifecycle;

import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A test suite consisting of all loading service test classes.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: LoadingSuite.java 522 2007-12-24 11:24:35Z kasper $
 */
@RunWith(ServiceSuite.class)
@Suite.SuiteClasses( { LifecycleRegistration.class, LifecycleStart.class, LifecycleStarted.class })
public class LifecycleSuite {}
