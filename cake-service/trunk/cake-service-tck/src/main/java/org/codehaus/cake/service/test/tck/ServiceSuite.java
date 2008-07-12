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
package org.codehaus.cake.service.test.tck;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.cake.service.Container.SupportedServices;
import org.junit.internal.runners.InitializationError;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.Suite;

public class ServiceSuite extends Suite {
    private final Set<Class> supportedServices;

    public ServiceSuite(Class<?> klass) throws InitializationError {
        super(klass);

        supportedServices = new HashSet(Arrays.asList(TckUtil.containerImplementation.getAnnotation(
                SupportedServices.class).value()));
        filter();
    }

    boolean isSupported(Object service) {
        return supportedServices.contains(service);
    }

    private void filter() {
        try {
            filter(new Filter() {
                public String describe() {
                    return "filtered";
                }

                public boolean shouldRun(Description description) {
                    RequireService rs = description.getAnnotation(RequireService.class);
                    if (rs != null) {
                        for (Class c : rs.value()) {
                            if (!isSupported(c)) {
                                return false;
                            }
                        }
                    }
                    UnsupportedServices us = description.getAnnotation(UnsupportedServices.class);
                    if (us != null) {
                        for (Class c : us.value()) {
                            if (isSupported(c)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            });
        } catch (NoTestsRemainException e) {
            e.printStackTrace();
        }
    }
}
