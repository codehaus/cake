package org.codehaus.cake.cache.test.tck.service.loading;

import java.util.Arrays;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.test.util.throwables.RuntimeException1;
import org.codehaus.cake.util.Logger.Level;
import org.junit.Test;

public class LoadingErroneous extends AbstractCacheTCKTest {

    @Test
    public void loadFailed() {
        loader.withLoader(M1).setCause(RuntimeException1.INSTANCE);
        withLoading().load(1);
        awaitFinishedThreads();
        assertSize(0);
        exceptionHandler.eat(RuntimeException1.INSTANCE, Level.Error);
    }

    @Test
    public void loadAllFailed() {
        loader.withLoader(M2).setCause(RuntimeException1.INSTANCE);
        withLoading().loadAll(Arrays.asList(1, 2, 3));
        awaitFinishedThreads();
        assertSize(2);
        assertTrue(c.containsKey(1) && c.containsKey(3));
        exceptionHandler.eat(RuntimeException1.INSTANCE, Level.Error);
    }

}
