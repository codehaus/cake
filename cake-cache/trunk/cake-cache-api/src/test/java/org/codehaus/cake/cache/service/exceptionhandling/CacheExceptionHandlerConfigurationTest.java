package org.codehaus.cake.cache.service.exceptionhandling;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.util.Logger;
import org.junit.Test;

public class CacheExceptionHandlerConfigurationTest {

    @Test
    public void logger() {
        CacheExceptionHandlingConfiguration<Integer, String> c = new CacheExceptionHandlingConfiguration<Integer, String>();
        assertNull(c.getExceptionLogger());
        Logger l = dummy(Logger.class);
        assertSame(c, c.setExceptionLogger(l));
        assertSame(l, c.getExceptionLogger());
    }

    @Test
    public void exceptionHandler() {
        CacheExceptionHandlingConfiguration<Integer, String> c = new CacheExceptionHandlingConfiguration<Integer, String>();
        assertNull(c.getExceptionHandler());
        CacheExceptionHandler<Integer, String> ceh = new CacheExceptionHandler<Integer, String>();
        assertSame(c, c.setExceptionHandler(ceh));
        assertSame(ceh, c.getExceptionHandler());
    }
}
