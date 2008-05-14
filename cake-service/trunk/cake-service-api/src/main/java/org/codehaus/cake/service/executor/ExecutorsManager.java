/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
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
    public abstract ExecutorService getExecutorService(Object service, AttributeMap attributes);

    /**
     * Returns a {@link ForkJoinExecutor} that can be used to asynchronously execute tasks for the specified service.
     * 
     * @param service
     *            the service for which an ForkJoinExecutor should be returned
     * @param attributes
     *            a map of attributes that is passed to the concrete implementation of the executor manager
     * @return a ForkJoinExecutor that can be used to asynchronously execute tasks for the specified service
     */
    public abstract ForkJoinExecutor getForkJoinExecutor(Object service, AttributeMap attributes);

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
    public abstract ScheduledExecutorService getScheduledExecutorService(Object service, AttributeMap attributes);

}
