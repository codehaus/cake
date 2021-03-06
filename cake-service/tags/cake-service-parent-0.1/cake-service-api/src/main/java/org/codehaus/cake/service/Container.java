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
package org.codehaus.cake.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * in addition providing .
 * lifecycle, register service, 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheLifecycle.java 511 2007-12-13 14:37:02Z kasper $
 */
public interface Container extends ServiceManager {

    /**
     * Returns the name of this container. If no name has been specified while configuring the
     * container. The implementation must choose a valid name. A valid name contains no other
     * characters then alphanumeric characters and '_' or '-'.
     * 
     * @return the name of the container
     * @see ContainerConfiguration#setName(String)
     */
    String getName();

    /**
     * Blocks until all tasks within this container have completed execution after a shutdown
     * request, or the timeout occurs, or the current thread is interrupted, whichever happens
     * first.
     * 
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return <tt>true</tt> if this container terminated and <tt>false</tt> if the timeout
     *         elapsed before termination
     * @throws InterruptedException
     *             if interrupted while waiting
     */
    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Returns <tt>true</tt> if this container has been shut down.
     * 
     * @return <tt>true</tt> if this container has been shut down
     */
    boolean isShutdown();

    /**
     * Returns <tt>true</tt> if this container has been started.
     * 
     * @return <tt>true</tt> if this container has been started
     */
    boolean isStarted();

    /**
     * Returns <tt>true</tt> if all service tasks have completed following shut down. Note that
     * <tt>isTerminated</tt> is never <tt>true</tt> unless either <tt>shutdown</tt> or
     * <tt>shutdownNow</tt> was called first.
     * 
     * @return <tt>true</tt> if all tasks have completed following shut down
     */
    boolean isTerminated();

    /**
     * Initiates an orderly shutdown of the container. In which currently running tasks will be
     * executed, but no new tasks will be started. Invocation has no additional effect if already
     * shut down.
     * 
     * @throws SecurityException
     *             if a security manager exists and shutting down this container may manipulate
     *             threads that the caller is not permitted to modify because it does not hold
     *             {@link java.lang.RuntimePermission}<tt>("modifyThread")</tt>, or the security
     *             manager's <tt>checkAccess</tt> method denies access.
     */
    void shutdown();

    /**
     * Attempts to stop all actively executing tasks within the container and halts the processing
     * of waiting tasks. Invocation has no additional effect if already shut down.
     * <p>
     * There are no guarantees beyond best-effort attempts to stop processing actively executing
     * tasks in the container. For example, typical implementations will cancel via
     * {@link Thread#interrupt}, so any task that fails to respond to interrupts may never
     * terminate.
     * 
     * @throws SecurityException
     *             if a security manager exists and shutting down this container may manipulate
     *             threads that the caller is not permitted to modify because it does not hold
     *             {@link java.lang.RuntimePermission}<tt>("modifyThread")</tt>, or the security
     *             manager's <tt>checkAccess</tt> method denies access.
     */
    void shutdownNow();

    /**
     * Used on a Container implementation to document what type of services the container supports.
     */
    @Target( { ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    /* @Documented */
    @interface SupportedServices {
        /**
         * Returns the type of services the container implementation supports.
         */
        Class[] value();
    }
}
