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

import org.codehaus.cake.util.Logger;

/**
 * This class is used to configure the exception handling service prior to usage.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: containerExceptionHandler.java 538 2007-12-31 00:18:13Z kasper $
 * 
 * @param <T>
 *            the type ExceptionHandler that is used
 */
public class ExceptionHandlingConfiguration<T extends ExceptionHandler> {

    /** The exception handler used for handling erroneous conditions in the container. */
    private T exceptionHandler;

    /** The default exception logger to log to. */
    private Logger logger;

    /**
     * Returns the exception handler that should be used to handle all exceptions and warnings. If no exception handler
     * has been the container will use a default exception handler.
     * 
     * @return the exceptionHandler that is configured for the container
     * @see #setExceptionHandler(ExceptionHandler)
     */
    public T getExceptionHandler() {
        return exceptionHandler;
    }

    /**
     * Returns the logger that is used for exception handling, or <tt>null</tt> if no such logger has been set.
     * 
     * @return the logger that is used for exception handling, or <tt>null</tt> if no such logger has been set
     * @see #setExceptionLogger(Logger)
     */
    public Logger getExceptionLogger() {
        return logger;
    }

    /**
     * Sets the exception handler that should be used to handle all exceptions and warnings. If no exception handler is
     * set using this method the container will automatically use a default exception handler. That logs any output to
     * the configured logger, or {@link System#err} if no logger has been set.
     * 
     * @param exceptionHandler
     *            the exceptionHandler to use for handling exceptions and warnings
     * @return this configuration
     */
    public ExceptionHandlingConfiguration<T> setExceptionHandler(T exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * Sets the log that will be used for logging information whenever the cache or any of its services fails in some
     * way.
     * <p>
     * If no logger has been set using this method. The exception handling service will used the default logger returned
     * from {@link org.codehaus.cake.container.AbstractConfiguration#getDefaultLogger()}. If no default logger has been
     * set, output will be sent to {@link System#err}.
     * 
     * @param logger
     *            the logger to use for exception handling
     * @return this configuration
     */
    public ExceptionHandlingConfiguration<T> setExceptionLogger(Logger logger) {
        this.logger = logger;
        return this;
    }
}
