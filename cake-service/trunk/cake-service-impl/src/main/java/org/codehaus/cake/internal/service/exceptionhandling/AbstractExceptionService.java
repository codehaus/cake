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
package org.codehaus.cake.internal.service.exceptionhandling;

import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.util.LazyLogger;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.spi.ExceptionContext;
import org.codehaus.cake.util.Logger;
import org.codehaus.cake.util.Loggers;
import org.codehaus.cake.util.Logger.Level;

public abstract class AbstractExceptionService<T extends Container> implements InternalDebugService,
        InternalExceptionService<T> {
    /** The cache for which exceptions should be handled. */
    private final T container;

    private Logger exceptionLogger;// never null

    private final Logger infoLogger;// might be null

    public AbstractExceptionService(Container container, Composer composer,
            ContainerConfiguration containerConfiguration, Logger exceptionLogger) {
        this.container = (T) container;
        Logger infoLogger = containerConfiguration.getDefaultLogger();
        this.infoLogger = infoLogger == null ? Loggers.NULL_LOGGER : infoLogger;

        Logger logger = exceptionLogger;
        if (logger == null) {
            logger = infoLogger;
        }
        if (logger == null) {
            String loggerName = composer.getContainerType().getPackage().getName() + "." + composer.getContainerName();
            logger = new LazyLogger(loggerName, "no logger defined", AbstractExceptionService.class.getName());
        }
        this.exceptionLogger = logger;
    }

    protected ExceptionContext<T> createContext(String message, Level level) {
        return createContext(null, message, level);
    }

    protected ExceptionContext<T> createContext(Throwable cause, String message, Level level) {
        return new DefaultExceptionContext(cause, message, level);
    }

    public void debug(String str) {
        infoLogger.debug(str);
    }

    /** {@inheritDoc} */
    public void error(String msg) {
        handle(createContext(msg, Level.Error));
    }

    /** {@inheritDoc} */
    public void error(String msg, Throwable cause) {
        handle(createContext(cause, msg, Level.Error));
    }

    /** {@inheritDoc} */
    public void fatal(String msg) {
        handle(createContext(msg, Level.Fatal));
    }

    /** {@inheritDoc} */
    public void fatal(String msg, Throwable cause) {
        handle(createContext(cause, msg, Level.Fatal));
    }

    protected abstract void handle(ExceptionContext<T> context);
    /** {@inheritDoc} */
    public void info(String str) {
        infoLogger.info(str);
    }

    /** {@inheritDoc} */
    public boolean isDebugEnabled() {
        return infoLogger.isDebugEnabled();
    }

    /** {@inheritDoc} */
    public boolean isTraceEnabled() {
        return infoLogger.isTraceEnabled();
    }

    /** {@inheritDoc} */
    public void trace(String str) {
        infoLogger.trace(str);
    }
    /** {@inheritDoc} */
    public void warning(String warning) {
        handle(createContext(warning, Level.Warn));
    }

    private class DefaultExceptionContext extends ExceptionContext<T> {
        private final Throwable cause;

        private final Level level;

        private final String message;

        public DefaultExceptionContext(Throwable cause, String message, Level level) {
            this.cause = cause;
            this.message = message;
            this.level = level;
        }

        @Override
        public Throwable getCause() {
            return cause;
        }

        @Override
        public T getContainer() {
            return container;
        }

        @Override
        public Level getLevel() {
            return level;
        }

        @Override
        public Logger getLogger() {
            return exceptionLogger;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
    /** {@inheritDoc} */
    public void terminated() {}
}
