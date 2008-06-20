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
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.forkjoin.ForkJoinExecutor;
import org.codehaus.cake.service.ServiceManager;

/**
 * This is the main service interface for scheduling and executing tasks at runtime.
 * <p>
 * An instance of this interface can be retrieved by using {@link ServiceManager#getService(Class)} to look it up.
 * 
 * <pre>
 * ServiceManager&lt;?, ?&gt; sm = someContainer;
 * ExecutorManagerService executorManager = sm.getService(ExecutorManagerService.class);
 * executorManager.getExecutor(someService)
 * </pre>
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheStatisticsService.java 430 2007-11-11 14:50:09Z kasper $
 */
public interface ExecutorsService {

    /**
     * Returns a {@link ExecutorService}.
     * 
     * @return a ExecutorService for the specified service
     */
    ExecutorService getExecutorService();

    /**
     * Returns a {@link ExecutorService} for the specified service.
     * 
     * @param service
     *            the service that needs a ExecutorService
     * @return a ExecutorService for the specified service
     */
    ExecutorService getExecutorService(Object service);

    /**
     * Returns a ExecutorService for the specified service.
     * 
     * @param service
     *            the service that needs a ExecutorService
     * @param attributes
     *            a map of attributes that can be used to determine which type of executor service should be returned
     * @return a scheduled for the specified service
     */
    ExecutorService getExecutorService(Object service, AttributeMap attributes);

    ForkJoinExecutor getForkJoinExecutor();

    /**
     * Returns a {@link ForkJoinExecutor} for the specified service.
     * 
     * @param service
     *            the service that needs a ForkJoinExecutor
     * @return a ForkJoinExecutor for the specified service
     */
    ForkJoinExecutor getForkJoinExecutor(Object service);

    /**
     * Returns a {@link ForkJoinExecutor} for the specified service.
     * 
     * @param service
     *            the service that needs a ForkJoinExecutor
     * @param attributes
     *            a map of attributes that can be used to determine which type of forkjoin executor should be returned
     * @return a ForkJoinExecutor for the specified service
     */
    ForkJoinExecutor getForkJoinExecutor(Object service, AttributeMap attributes);

    /**
     * Returns the default ScheduledExecutorService. A call to this method is equivalent to calling
     * <tt>getScheduledExecutorService(null)</tt>.
     * 
     * @return the default ScheduledExecutorService
     */
    ScheduledExecutorService getScheduledExecutorService();

    /**
     * Returns a ScheduledExecutorService for the specified service. A call to this method is equivalent to calling
     * <tt>getScheduledExecutorService(service, {@link Attributes#EMPTY_ATTRIBUTE_MAP}</tt>
     * 
     * @param service
     *            the service that needs a ScheduledExecutorService
     * @return a ScheduledExecutorService for the specified service
     */
    ScheduledExecutorService getScheduledExecutorService(Object service);

    /**
     * Returns a {@link ScheduledExecutorService} for the specified service.
     * 
     * @param service
     *            the service that needs a ScheduledExecutorService
     * @param attributes
     *            a map of attributes that can be used to determine which type of scheduled executor service should be
     *            returned
     * @return a ScheduledExecutorService for the specified service
     */
    ScheduledExecutorService getScheduledExecutorService(Object service, AttributeMap attributes);
}
