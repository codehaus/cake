package org.codehaus.cake.internal.util;

import org.codehaus.cake.util.Logger.Level;

public class LazyLoggerTestHelper1 {

    public static void log3(LazyLogger logger, String msg) {
        logger.log(Level.Error, msg);
    }
}
