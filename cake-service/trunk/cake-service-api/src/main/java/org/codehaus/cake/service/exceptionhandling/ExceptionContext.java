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
import org.codehaus.cake.util.Logger.Level;

/**
 * A ExceptionContext is created by the container whenever an exceptional state is raised and parsed along to the
 * various methods defined in {@link ExceptionHandler} .
 * <p>
 * Users will most likely never need to create instances of this class.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CacheExceptionContext.java 538 2007-12-31 00:18:13Z kasper $
 * @param <T>
 *            the type of container
 */
public abstract class ExceptionContext<T> {

    /**
     * Returns the container in which the failure occurred.
     * 
     * @return the container in which the failure occurred
     */
    public abstract T getContainer();

    /**
     * Returns the default configured logger for handling exceptions for the container in which this failure occurred.
     * 
     * @return the default configured logger for handling exceptions for the container in which this failure occurred
     */
    public abstract Logger getLogger();

    /**
     * Returns the cause of the failure, or <code>null</code> if no exception was raised.
     * 
     * @return the cause of the failure
     */
    public abstract Throwable getCause();

    /**
     * Returns the message of the failure, or <code>""</code> if no message was set.
     * 
     * @return the message of the failure
     */
    public abstract String getMessage();

    /**
     * Returns the level of the failure. The returned level is either {@link Level#Warn}, {@link Level#Error} or
     * {@link Level#Fatal}.
     * 
     * @return the level of the failure
     */
    public abstract Level getLevel();
}
