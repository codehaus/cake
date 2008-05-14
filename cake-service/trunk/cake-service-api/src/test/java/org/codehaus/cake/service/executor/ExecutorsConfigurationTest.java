/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.service.executor;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.forkjoin.ForkJoinExecutor;
import org.junit.Test;

public class ExecutorsConfigurationTest {
    @Test
    public void domain() {
        ExecutorsManager em = new DummyExecutorsManager() {};
        ExecutorsConfiguration emb = new ExecutorsConfiguration();
        assertNull(emb.getExecutorManager());
        assertSame(emb, emb.setExecutorManager(em));
        assertSame(em, emb.getExecutorManager());
    }

    static class DummyExecutorsManager extends ExecutorsManager {

        public ExecutorService getExecutorService(Object service, AttributeMap attributes) {
            return null;
        }

        public ForkJoinExecutor getForkJoinExecutor(Object service, AttributeMap attributes) {
            return null;
        }

        public ScheduledExecutorService getScheduledExecutorService(Object service, AttributeMap attributes) {
            return null;
        }

    }
}
