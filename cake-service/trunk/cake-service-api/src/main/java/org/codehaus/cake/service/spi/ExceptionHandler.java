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
package org.codehaus.cake.service.spi;

import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;

/**
 * The purpose of this class is to have one central place where all exceptions that arise within a container or one of
 * its associated services are handled. Normally an ExceptionHandler just logs any exception that is raised. However, an
 * implementation might instead shutdown the container whenever an exception is raised (can be useful in development
 * environments). To allow for easily extending this class with new methods at a later time this class is a class
 * instead of an interface.
 * <p>
 * This class is normally extended by an implementation for a specific container type.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ExceptionHandler.java 225 2008-11-30 20:53:08Z kasper $
 * @param <T>
 *            the type of the container
 */
public class ExceptionHandler<T extends Container> {

    /**
     * Called to initialize the ExceptionHandler. This method will be called as the first operation from within the
     * constructor of the container. The default implementation does nothing.
     * <p>
     * Any exception thrown by this method will not be handled by the container
     * 
     * @param configuration
     *            the configuration of the container
     */
    public void initialize(ContainerConfiguration configuration) {
    }

    /**
     * The default exception handler. Logs all exceptions and rethrows any errors.
     * 
     * @param context
     *            the context
     */
    public void handle(ExceptionContext<T> context) {
        Throwable cause = context.getCause();
        context.getLogger().log(context.getLevel(), context.getMessage(), cause);
        if (cause instanceof Error) {
            throw (Error) cause;
        }
    }

    /**
     * Called as the last action by the container once it has been fully terminated. The default implementation does
     * nothing.
     */
    public void terminated() {
    }
}
