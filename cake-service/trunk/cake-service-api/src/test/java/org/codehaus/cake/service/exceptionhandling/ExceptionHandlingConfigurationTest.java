package org.codehaus.cake.service.exceptionhandling;

import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static org.codehaus.cake.test.util.TestUtil.dummy;

import org.codehaus.cake.util.Logger;
import org.junit.Before;
import org.junit.Test;

public class ExceptionHandlingConfigurationTest {
    ExceptionHandlingConfiguration<DummyExceptionHandler> e;

    @Before
    public void setUp() {
        e = new ExceptionHandlingConfiguration<DummyExceptionHandler>();
    }

    @Test
    public void exceptionHandler() {
        DummyExceptionHandler deh = new DummyExceptionHandler();
        assertNull(e.getExceptionHandler());
        assertSame(e, e.setExceptionHandler(deh));
        assertSame(deh, e.getExceptionHandler());
    }

    @Test
    public void exceptionLogger() {
        Logger l = dummy(Logger.class);
        assertNull(e.getExceptionLogger());
        assertSame(e, e.setExceptionLogger(l));
        assertSame(l, e.getExceptionLogger());
    }

    static class DummyExceptionHandler extends ExceptionHandler<Integer> {}
}
