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
package org.codehaus.cake.internal.service.spi;

import java.util.UUID;

import org.codehaus.cake.internal.UseInternals;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;

@UseInternals
public class ContainerInfo {

    private final Class<? extends Container> clazz;
    private final String containerName;

    public ContainerInfo(Class clazz, ContainerConfiguration configuration) {
        String name = configuration.getName();
        if (name == null) {
            containerName = UUID.randomUUID().toString();
        } else {
            containerName = name;
        }
        this.clazz = clazz;
    }

    public String getContainerName() {
        return containerName;
    }

    public Class<? extends Container> getContainerType() {
        return clazz;
    }

    public String getContainerTypeName() {
        return getContainerType().getSimpleName();
    }

    public String getDefaultJMXDomain() {
        return getContainerType().getPackage().getName();
    }
}
