package org.codehaus.cake.service.executor;

import org.junit.Test;

public class ExecutorsManagerTest {

    @Test(expected = UnsupportedOperationException.class)
    public void forkjoin() {
        new ExecutorsManager() {}.getForkJoinExecutor(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void executor() {
        new ExecutorsManager() {}.getExecutorService(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void scheduled() {
        new ExecutorsManager() {}.getScheduledExecutorService(null, null);
    }
}
