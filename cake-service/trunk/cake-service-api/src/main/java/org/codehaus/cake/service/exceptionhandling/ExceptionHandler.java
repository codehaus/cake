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
package org.codehaus.cake.service.exceptionhandling;

import org.codehaus.cake.service.ContainerConfiguration;

/**
 * The purpose of this class is to have one central place where all exceptions
 * that arise within a container or one of its associated services are handled.
 * One implementation of this class might shutdown the container for any raised
 * exception. This is often usefull in development environments. Another
 * implementation might just log the exception and continue serving other
 * requests. To allow for easily extending this class with new methods at a
 * later time this class is a class instead of an interface.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: containerExceptionHandler.java 538 2007-12-31 00:18:13Z kasper $
 * @param <T>
 *            the type of the container
 */
public class ExceptionHandler<T> {

    /**
     * Called to initialize the ExceptionHandler. This method must be called as
     * the first operation from within the constructor of the container.
     * Exceptions thrown by this method will not be handled by the container.
     * The default implementation does nothing
     * 
     * @param configuration
     *            the configuration of the container
     */
    public void initialize(ContainerConfiguration<T> configuration) {
    }

    /**
     * The default exception handler. Logs all exceptions and rethrows any
     * errors.
     * 
     * @param context
     *            the context
     */
    public void handle(ExceptionContext<T> context) {
        Throwable cause = context.getCause();
        context.getLogger()
                .log(context.getLevel(), context.getMessage(), cause);
        if (cause instanceof Error) {
            throw (Error) cause;
        }
    }

    /**
     * Called as the last action by the container once it has been fully
     * terminated.
     */
    public void terminated() {
    }
}
