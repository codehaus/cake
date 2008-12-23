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
package org.codehaus.cake.cache.test.tck;

import org.codehaus.cake.cache.CacheConfiguration;
import org.codehaus.cake.cache.test.tck.attributes.AttributeSuite;
import org.codehaus.cake.cache.test.tck.core.CoreSuite;
import org.codehaus.cake.cache.test.tck.crud.CrudSuite;
import org.codehaus.cake.cache.test.tck.lifecycle.LifecycleSuite;
import org.codehaus.cake.cache.test.tck.query.QuerySuite;
import org.codehaus.cake.cache.test.tck.selection.SelectionSuite;
import org.codehaus.cake.cache.test.tck.service.exceptionhandling.ExceptionHandlingSuite;
import org.codehaus.cake.cache.test.tck.service.loading.LoadingSuite;
import org.codehaus.cake.cache.test.tck.service.management.ManagementSuite;
import org.codehaus.cake.cache.test.tck.service.memorystore.SuiteMemoryStore;
import org.codehaus.cake.service.test.tck.ServiceSuite;
import org.codehaus.cake.service.test.tck.TckRunner;
import org.junit.internal.runners.InitializationError;

public class CacheTckRunner extends TckRunner {

    public CacheTckRunner(Class<?> clazz) throws InitializationError {
        super(clazz, CacheConfiguration.class);
    }

    protected void initialize() throws Exception {
        super.initialize();

        add(new ServiceSuite(AttributeSuite.class));
        add(new ServiceSuite(ExceptionHandlingSuite.class));
        add(new ServiceSuite(SelectionSuite.class));
        add(new ServiceSuite(CrudSuite.class));

        add(new ServiceSuite(ManagementSuite.class));
        add(new ServiceSuite(CoreSuite.class));
        add(new ServiceSuite(LifecycleSuite.class));
        // services
        add(new ServiceSuite(SuiteMemoryStore.class));
        add(new ServiceSuite(LoadingSuite.class));
        
        add(new ServiceSuite(QuerySuite.class));
    }
}
