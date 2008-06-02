package org.codehaus.cake.cache.service.exceptionhandling;

import static org.codehaus.cake.test.util.TestUtil.dummy;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.CacheEntry;
import org.codehaus.cake.service.exceptionhandling.ExceptionContext;
import org.codehaus.cake.util.Logger;
import org.codehaus.cake.util.Logger.Level;
import org.junit.Test;

public class CacheExceptionHandlerTest {

    @Test
    public void test() {
        final DummyExceptionContext ec = new DummyExceptionContext();
        final AtomicBoolean ok = new AtomicBoolean();
        CacheExceptionHandler<Integer, String> ceh = new CacheExceptionHandler<Integer, String>() {
            public void handle(ExceptionContext context) {
                assertSame(ec, context);
                ok.set(true);
            }
        };
        assertNull(ceh.loadingOfValueFailed(ec, 1, CacheEntry.SIZE.singleton(2)));
        assertTrue(ok.get());
    }

    static class DummyExceptionContext extends ExceptionContext<Cache<Integer, String>> {

        public Throwable getCause() {
            return null;
        }

        public Cache<Integer, String> getContainer() {
            return null;
        }

        public Level getLevel() {
            return null;
        }

        public Logger getLogger() {
            return null;
        }

        public String getMessage() {
            return null;
        }

    }
}
