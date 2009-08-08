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
package org.codehaus.cake.internal.util;

import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.codehaus.cake.internal.util.LogHelper.AbstractLogger;

/**
 * A LazyLogger is lazily initialised whenever the first exception is raised.
 * <p>
 * NOTICE: This is an internal class and should not be directly referred. No guarantee is made to the compatibility of
 * this class between different releases.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class LazyLogger extends AbstractLogger {

    private final String jdkLoggerName;

    private volatile Logger logger;

    private final String msg;
    private final String lastCaller;

    public LazyLogger(String jdkLoggerName, String msg, String lastCaller) {
        this.msg = msg;
        this.jdkLoggerName = jdkLoggerName;
        this.lastCaller = lastCaller;
        // lazyLoggerGenerator = new LazyGenerator<Logger>(new Generator<Logger>() {
        // public Logger op() {
        // Logger logger = LogManager.getLogManager().getLogger(LazyLogger.this.jdkLoggerName);
        // if (logger == null) {
        // logger = java.util.logging.Logger.getLogger(LazyLogger.this.jdkLoggerName);
        // logger.setLevel(java.util.logging.Level.ALL);
        // logger.log(new LazyLogRecord(java.util.logging.Level.INFO, LazyLogger.this.msg,
        // LazyLogger.this.lastCaller));
        // logger.setLevel(java.util.logging.Level.WARNING);
        // }
        // return logger;
        // }
        //
        // });
    }

    private Logger getLogger() {
        // if (false)
        // return lazyLoggerGenerator.op();
        Logger logger = this.logger;
        if (logger != null) {
            return logger;
        }
        synchronized (this) {
            if (this.logger == null) {
                logger = LogManager.getLogManager().getLogger(jdkLoggerName);
                if (logger == null) {
                    logger = java.util.logging.Logger.getLogger(jdkLoggerName);
                    logger.setLevel(java.util.logging.Level.ALL);
                    logger.log(new LazyLogRecord(java.util.logging.Level.INFO, msg, lastCaller));
                    logger.setLevel(java.util.logging.Level.WARNING);
                }
                this.logger = logger;
            }
            return logger;
        }
    }

    @Override
    public String getName() {
        return jdkLoggerName;
    }

    /** {@inheritDoc} */
    public boolean isEnabled(Level level) {
        return getLogger().isLoggable(LogHelper.toJdkLevel(level));
    }

    /** {@inheritDoc} */
    @Override
    public void log(Level level, String message) {
        Logger logger = getLogger();
        LazyLogRecord r = new LazyLogRecord(LogHelper.toJdkLevel(level), message, lastCaller);
        logger.log(r);
    }

    /** {@inheritDoc} */
    public void log(Level level, String message, Throwable cause) {
        // (new Exception()).printStackTrace();
        Logger logger = getLogger();
        LazyLogRecord r = new LazyLogRecord(LogHelper.toJdkLevel(level), message, lastCaller);
        r.setThrown(cause);
        logger.log(r);
    }

    static class LazyLogRecord extends LogRecord {
        /** serialVersionUID. */
        private static final long serialVersionUID = -5042636617771251558L;

        private boolean needToInferCaller = true;

        private String lastCaller;

        LazyLogRecord(java.util.logging.Level level, String msg, String lastCaller) {
            super(level, msg);
            this.lastCaller = lastCaller;
        }

        public String getSourceClassName() {
            if (needToInferCaller) {
                inferCaller();
            }
            return super.getSourceClassName();
        }

        public String getSourceMethodName() {
            if (needToInferCaller) {
                inferCaller();
            }
            return super.getSourceMethodName();
        }

        // Private method to infer the caller's class and method names
        private void inferCaller() {
            needToInferCaller = false;
            // Get the stack trace.
            StackTraceElement[] stack = (new Throwable()).getStackTrace();
            // First, search back to a method in the Logger class.
            int ix = 0;
            while (ix < stack.length) {
                StackTraceElement frame = stack[ix];
                String cname = frame.getClassName();
                if (cname.equals(lastCaller)) {
                    break;
                }
                ix++;
            }
            // Now search for the first frame before the "Logger" class.
            while (ix < stack.length) {
                StackTraceElement frame = stack[ix];
                String cname = frame.getClassName();
                if (!cname.equals(lastCaller)) {
                    // We've found the relevant frame.
                    setSourceClassName(cname);
                    setSourceMethodName(frame.getMethodName());
                    return;
                }
                ix++;
            }
            // We haven't found a suitable frame, so just punt. This is
            // OK as we are only committed to making a "best effort" here.
        }
    }
}
