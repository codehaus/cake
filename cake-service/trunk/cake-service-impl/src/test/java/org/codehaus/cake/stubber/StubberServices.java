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
package org.codehaus.cake.stubber;

import org.codehaus.cake.service.ServiceManager;
import org.codehaus.cake.service.executor.ExecutorsService;
import org.codehaus.cake.stubber.bubber.BubberService;

public class StubberServices<T> {
    /** The service manager to extract cache services from. */
    private final ServiceManager serviceManager;

    /**
     * Creates a new {@link StubberServices} from the specified {@link Stubber}
     * 
     * @param cache
     *            the cache to retrieve services from
     */
    public StubberServices(Stubber<?> stubber) {
        this.serviceManager = stubber;
    }

    /**
     * Returns the worker service.
     * 
     * @return the worker service for the cache
     * @throws UnsupportedOperationException
     *             if no worker service is available
     */
    public ExecutorsService executors() {
        return getService(ExecutorsService.class);
    }

    /**
     * Returns the memory store service.
     * 
     * @return the memory store service for the cache
     * @throws UnsupportedOperationException
     *             if no memory store service is available
     * @param <K>
     *            the type of keys maintained by the cache
     * @param <V>
     *            the type of mapped values
     */
    public BubberService<T> bubber() {
        return getService(BubberService.class);
    }

    /**
     * This method can be called by subclasses to retrieve services from the cache that this object is wrapping.
     * 
     * @param <T>
     *            the type of service
     * @param serviceType
     *            the type of services
     * @return an instance of the specified type
     * @throws UnsupportedOperationException
     *             if no service of the specified type is available
     */
    protected <T> T getService(Class<T> serviceType) {
        return serviceManager.getService(serviceType);
    }

}
