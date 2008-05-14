/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.internal.util;

import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.codehaus.cake.internal.util.LogHelper.AbstractLogger;

/**
 * Returns the exception logger configured for this cache. Or initializes the default logger if no logger has been
 * defined and the default logger has not already been initialized
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
    }

    private Logger getLogger() {
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

        public LazyLogRecord(java.util.logging.Level level, String msg, String lastCaller) {
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
            StackTraceElement stack[] = (new Throwable()).getStackTrace();
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
