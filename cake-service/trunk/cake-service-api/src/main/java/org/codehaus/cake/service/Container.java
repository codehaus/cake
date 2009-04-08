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
package org.codehaus.cake.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

/**
 * The main purpose of the Container interface is to control the lifecycle and publication of services registered with
 * it. Normally containers are extended with classes that provide additionally functionality then just managing
 * services. For example, {@link org.codehaus.cake.cache.Cache} which extends this interface with various methods that are relevant to a cache
 * but uses the functionality provided by this interface to allow users to control the lifecycle of the cache and the
 * services running within the cache.
 * <p>
 * The services running within a container can either be (internal) services that are hidden for users of this interface
 * and are only visible to other internal services running within the container. Or they can be services that can be
 * retrived by the user, for example, by calling {@link #getService(Class)}.
 * <p>
 * All general-purpose <tt>Container</tt> implementation classes should provide two "standard" constructors: a void
 * (no arguments) constructor, which creates an empty container with default settings, and a constructor with a single
 * argument of type {@link ContainerConfiguration}. There is no way to enforce this recommendation (as interfaces
 * cannot contain constructors) but all of the general-purpose container implementations available in Cake comply.
 * <p>
 * The lifecycle of the container is 1) A ContainerConfiguration or a subclass of it is created and configured with as
 * needed. 2) A Container or a subclass hereof is created by passing along the configuration. 3) When the constructor
 * returns the container is initialized. Which means that the configurat is ready to be started. The first request for
 * example, by calling {@link #serviceKeySet()} When the container is no longer used the user must call
 * {@link #shutdown()} which initiate an ordered shutdown of the cache. Allowing all services within the container to
 * properly shutdown.
 * <p>
 * An <tt>Container</tt> can be shut down, which will cause it to stop accepting new tasks. After being shut down, the
 * container will eventually terminate, at which point no tasks are actively executing within the container, no tasks
 * are awaiting execution, and no new tasks can be submitted through various methods.
 * 
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public interface Container {

    /**
     * Returns a service of the specified type or throws an {@link UnsupportedOperationException} if no such service
     * exists. Calling this method is equivalent to calling {@link #getService(Class, MutableAttributeMap)} with an
     * empty {@link AttributeMap} as parameter.
     * 
     * @param <T>
     *            the type of service to retrieve
     * @param serviceType
     *            the type of service to retrieve
     * @return a service of the specified type
     * @throws UnsupportedOperationException
     *             if no service of the specified type exist
     * @throws NullPointerException
     *             if the specified service type is null
     */
    <T> T getService(Class<T> serviceType);

    /**
     * Returns a service of the specified type or throws an {@link UnsupportedOperationException} if no such service
     * exists. The map of attributes will be parsed along to the {@link ServiceProvider} responsible for constructing
     * the service.
     * 
     * @param <T>
     *            the type of service to retrieve
     * @param serviceType
     *            the type of service to retrieve
     * @param attributes
     *            a map of additional attributes
     * @return a service of the specified type
     * @throws UnsupportedOperationException
     *             if no service of the specified type exist
     * @throws NullPointerException
     *             if the specified service type or attribute map is null
     */
    <T> T getService(Class<T> serviceType, AttributeMap attributes);

    /**
     * Returns whether or not this container contains a service of the specified type.
     * 
     * @param serviceType
     *            the type of service
     * @return true if this container contains a service of the specified type, otherwise false
     * @throws NullPointerException
     *             if the specified service type is null
     * 
     * @see #serviceKeySet()
     */
    boolean hasService(Class<?> serviceType);

    /**
     * Returns a {@link Set} consisting of the types of all services available.
     * 
     * @return a Set consisting of the types of all services available
     */
    Set<Class<?>> serviceKeySet();

    /**
     * Returns the name of this container. If no name has been specified while configuring the container. The container
     * chooses a valid name on contruction time. A valid name contains no other characters then alphanumeric characters
     * and '_' or '-'.
     * 
     * @return the name of the container
     * @see ContainerConfiguration#setName(String)
     */
    String getName();

    /**
     * Blocks until all services within the container have completed execution after a shutdown request, or the timeout
     * occurs, or the current thread is interrupted, whichever happens first.
     * 
     * @param timeout
     *            the maximum time to wait
     * @param unit
     *            the time unit of the timeout argument
     * @return <tt>true</tt> if this container terminated and <tt>false</tt> if the timeout elapsed before
     *         termination
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
     * Returns <tt>true</tt> if all services have been terminated following shut down. Note that <tt>isTerminated</tt>
     * is never <tt>true</tt> unless either <tt>shutdown</tt> or <tt>shutdownNow</tt> was called first.
     * 
     * @return <tt>true</tt> if all tasks have completed following shut down
     */
    boolean isTerminated();

    /**
     * Initiates an orderly shutdown of the container. In which currently running tasks will be executed, but no new
     * tasks will be started. Invocation has no additional effect if already shut down.
     */
    void shutdown();

    /**
     * Attempts to stop all actively executing tasks within the container and halts the processing of waiting tasks.
     * Invocation has no additional effect if already shut down.
     * <p>
     * There are no guarantees beyond best-effort attempts to stop processing actively executing tasks within the
     * container. For example, typical implementations will cancel via {@link Thread#interrupt}, so any task that fails
     * to respond to interrupts may never terminate.
     */
    void shutdownNow();

    /** Used on a Container implementation to document what type of services the container supports. */
    @Target( { ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface SupportedServices {
        /** Returns the type of services the container implementation supports. */
        Class<?>[] value();
    }
}
