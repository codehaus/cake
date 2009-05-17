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
package org.codehaus.cake.service.test.tck;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.Container.SupportedServices;
import org.codehaus.cake.service.test.tck.core.CoreSuite;
import org.codehaus.cake.service.test.tck.lifecycle.LifecycleSuite;
import org.codehaus.cake.service.test.tck.service.executors.ExecutorsSuite;
import org.codehaus.cake.service.test.tck.service.management.ManagementSuite;
import org.junit.internal.runners.CompositeRunner;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;

public abstract class TckRunner extends CompositeRunner {

    Class<? extends Container> containerImplementation;
    Class<? extends ContainerConfiguration> configuration;

    @Override
    public void run(RunNotifier notifier) {
        TckUtil.containerImplementation = containerImplementation;
        TckUtil.configuration = configuration;
        super.run(notifier);
    }

    private final Set<Class<?>> supportedServices;

    /**
     * If the file test-tck/src/main/resources/defaulttestclass exists. We will try to open it and read which cache
     * implementation should be tested by default.
     * <p>
     * This is very usefull if you just want to run a subset of the tests in an IDE.
     */

    @SuppressWarnings("unchecked")
    public TckRunner(Class<?> clazz, Class<? extends ContainerConfiguration> configuration) throws InitializationError {
        super(clazz.getSimpleName() + " TCK");
        TckUtil.containerImplementation = clazz.getAnnotation(TckImplementationSpecifier.class).value();
        TckUtil.configuration = configuration;
        this.containerImplementation = clazz.getAnnotation(TckImplementationSpecifier.class).value();
        this.configuration = configuration;
        try {
            add(new JUnit4ClassRunner(clazz));
        } catch (InitializationError e) {
            // ignore, no valid tests in clazz
        }
        supportedServices = new HashSet(Arrays.asList(TckUtil.containerImplementation.getAnnotation(
                SupportedServices.class).value()));
        try {
            initialize();
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error(e);
        }
        // filter();
        super.sort(new Sorter(new Comparator<Description>() {
            public int compare(Description o1, Description o2) {
                return o1.getDisplayName().compareTo(o2.getDisplayName());
            }
        }));
    }

    boolean isSupported(Object service) {
        return supportedServices.contains(service);
    }

    protected boolean isThreadSafe() {
        return supportedServices.contains(ExecutorService.class);
    }

    protected void initialize() throws Exception {
        add(new ServiceSuite(LifecycleSuite.class));
        add(new ServiceSuite(CoreSuite.class));
        add(new ServiceSuite(ExecutorsSuite.class));
        add(new ServiceSuite(ManagementSuite.class));
    }
}
