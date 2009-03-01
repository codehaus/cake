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
package org.codehaus.cake.internal.util;

import org.codehaus.cake.util.Logger;
import org.codehaus.cake.util.Logger.Level;
import org.w3c.dom.Element;

/**
 * Various {@link Logger} utilities.
 * <p>
 * NOTICE: This is an internal class and should not be directly referred. No guarantee is made to the compatibility of
 * this class between different releases.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class LogHelper {

    public static final String LOG_LEVEL_ATRB = "level";

    public static final String LOG_TYPE_ATRB = "type";

    /*
     * private static final String COMMONS_LOGGING = "commons";
     * 
     * private static final String JDK_LOGGING = "jul";
     * 
     * private static final String LOG4J = "log4j";
     * 
     * private static final String NULL_LOGGER = "null-logger";
     * 
     * private static final String SYSTEM_ERR_LOGGER = "system.err-logger";
     * 
     * private static final String SYSTEM_OUT_LOGGER = "system.out-logger";
     */
    // /CLOVER:OFF
    /** Cannot instantiate. */
    private LogHelper() {}

    // /CLOVER:ON

    static Level getLogLevel(Logger logger) {
        if (!logger.isFatalEnabled()) {
            return Level.Off;
        } else if (!logger.isErrorEnabled()) {
            return Level.Fatal;
        } else if (!logger.isWarnEnabled()) {
            return Level.Error;
        } else if (!logger.isInfoEnabled()) {
            return Level.Warn;
        } else if (!logger.isDebugEnabled()) {
            return Level.Info;
        } else if (!logger.isTraceEnabled()) {
            return Level.Debug;
        } else {
            return Level.Trace;
        }
    }

    /**
     * Converts from a {@link Level} to a {@link java.util.logging.Level}.
     * 
     * @param level
     *            the level to convert
     * @return the converted level
     */
    public static java.util.logging.Level toJdkLevel(Level level) {
        switch (level) {
        case Trace:
            return java.util.logging.Level.FINEST;
        case Debug:
            return java.util.logging.Level.FINE;
        case Error:
            return java.util.logging.Level.SEVERE;
        case Fatal:
            return java.util.logging.Level.SEVERE;
        case Info:
            return java.util.logging.Level.INFO;
        default /* Warn */:
            return java.util.logging.Level.WARNING;
        }
    }

    static Level toLevel(Element e) {
        String level = e.getAttribute(LOG_LEVEL_ATRB);
        Level l = Level.valueOf(level);
        return l;
    }

    /**
     * An AbstractLogger that all logger wrappers extend.
     */
    public abstract static class AbstractLogger implements Logger {
        /** {@inheritDoc} */
        public void debug(String message) {
            log(Level.Debug, message);
        }

        /** {@inheritDoc} */
        public void debug(String message, Throwable cause) {
            log(Level.Debug, message, cause);
        }

        /** {@inheritDoc} */
        public void error(String message) {
            log(Level.Error, message);
        }

        /** {@inheritDoc} */
        public void error(String message, Throwable cause) {
            log(Level.Error, message, cause);
        }

        /** {@inheritDoc} */
        public void fatal(String message) {
            log(Level.Fatal, message);
        }

        /** {@inheritDoc} */
        public void fatal(String message, Throwable cause) {
            log(Level.Fatal, message, cause);
        }

        /**
         * Returns the name of the logger.
         * 
         * @return the name of the logger
         */
        public abstract String getName();

        /** {@inheritDoc} */
        public void info(String message) {
            log(Level.Info, message);
        }

        /** {@inheritDoc} */
        public void info(String message, Throwable cause) {
            log(Level.Info, message, cause);
        }

        /** {@inheritDoc} */
        public boolean isDebugEnabled() {
            return isEnabled(Level.Debug);
        }

        /** {@inheritDoc} */
        public boolean isErrorEnabled() {
            return isEnabled(Level.Error);
        }

        /** {@inheritDoc} */
        public boolean isFatalEnabled() {
            return isEnabled(Level.Fatal);
        }

        /** {@inheritDoc} */
        public boolean isInfoEnabled() {
            return isEnabled(Level.Info);
        }

        /** {@inheritDoc} */
        public boolean isTraceEnabled() {
            return isEnabled(Level.Trace);
        }

        /** {@inheritDoc} */
        public boolean isWarnEnabled() {
            return isEnabled(Level.Warn);
        }

        /** {@inheritDoc} */
        public void log(Logger.Level l, String message) {
            log(l, message, null);
        }

        /** {@inheritDoc} */
        public void trace(String message) {
            log(Level.Trace, message);
        }

        /** {@inheritDoc} */
        public void trace(String message, Throwable cause) {
            log(Level.Trace, message, cause);
        }

        /** {@inheritDoc} */
        public void warn(String message) {
            log(Level.Warn, message);
        }

        /** {@inheritDoc} */
        public void warn(String message, Throwable cause) {
            log(Level.Warn, message, cause);
        }
    }
}
