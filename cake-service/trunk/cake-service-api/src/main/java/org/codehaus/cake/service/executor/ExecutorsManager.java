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
package org.codehaus.cake.service.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.forkjoin.ForkJoinExecutor;

/**
 * This class is reponsible for creating instances of {@link ExecutorService}, {@link ScheduledExecutorService} and
 * {@link ForkJoinExecutor}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheWorkerManager.java 479 2007-11-27 13:40:08Z kasper $
 */
@Deprecated
public abstract class ExecutorsManager {

    /**
     * Returns a {@link ExecutorService} that can be used to asynchronously execute tasks for the specified service.
     * 
     * @param service
     *            the service for which an ExecutorService should be returned
     * @param attributes
     *            a map of attributes that is passed to the concrete implementation of the executor manager
     * @return a ExecutorService that can be used to asynchronously execute tasks for the specified service
     */
    public ExecutorService getExecutorService(Object service, AttributeMap attributes) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a {@link ForkJoinExecutor} that can be used to asynchronously execute tasks for the specified service.
     * 
     * @param service
     *            the service for which an ForkJoinExecutor should be returned
     * @param attributes
     *            a map of attributes that is passed to the concrete implementation of the executor manager
     * @return a ForkJoinExecutor that can be used to asynchronously execute tasks for the specified service
     */
    public ForkJoinExecutor getForkJoinExecutor(Object service, AttributeMap attributes) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a {@link ScheduledExecutorService} that can be used to asynchronously schedule tasks for the specified
     * service.
     * 
     * @param service
     *            the service for which an ScheduledExecutorService should be returned
     * @param attributes
     *            a map of attributes that is passed to the concrete implementation of the executor manager
     * @return a ScheduledExecutorService that can be used to asynchronously schedule tasks for the specified service
     */
    public ScheduledExecutorService getScheduledExecutorService(Object service, AttributeMap attributes) {
        throw new UnsupportedOperationException();
    }

}
