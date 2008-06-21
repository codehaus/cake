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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.Container.SupportedServices;
import org.codehaus.cake.service.executor.ExecutorsService;

class TckUtil {

    static Class<? extends Container> containerImplementation;
    static Class<? extends ContainerConfiguration> configuration;

    static {
        InputStream is = TckRunner.class.getClassLoader().getResourceAsStream("defaulttestclass");
        if (is != null) {
            try {
                Properties p = new Properties();
                p.load(is);
                containerImplementation = (Class) Class.forName(p.getProperty("container"));
                configuration = (Class) Class.forName(p.getProperty("configuration"));
            } catch (IOException e) {
                throw new Error(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    static boolean isThreadSafe() {
        Set<Class> supportedServices = new HashSet(Arrays.asList(containerImplementation.getAnnotation(
                SupportedServices.class).value()));
        return supportedServices.contains(ExecutorsService.class);
    }

    public static Container newContainer(ContainerConfiguration configuration) {
        try {
            return (Container) configuration.newInstance(containerImplementation);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static ContainerConfiguration newConfiguration() {
        try {
            return configuration.newInstance();
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
