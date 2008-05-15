package org.codehaus.cake.internal.util;

import org.codehaus.cake.util.Logger.Level;

public class LazyLoggerTestHelper {

    public static void log1(LazyLogger logger, String msg) {
        logger.log(Level.Error, msg);
    }

    public static void logNext2(LazyLogger logger, String msg) {
        LazyLoggerTestHelper1.log3(logger, msg);
    }
}
