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

import org.codehaus.cake.util.Logger;
import org.codehaus.cake.util.Logger.Level;

/**
 * An ExceptionContext is created by a {@link Container} whenever an exceptional state is raised. The context is
 * consumed by the various methods defined in {@link ExceptionHandler}.
 * <p>
 * Normal users will most likely never need to create instances of this class.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ExceptionContext.java 225 2008-11-30 20:53:08Z kasper $
 * @param <T>
 *            the type of container
 */
public abstract class ExceptionContext<T extends Container> {

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
    
    public abstract ExceptionHandler<Container> getParent();
}
