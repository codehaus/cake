/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org>
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.cache.test.service.exceptionhandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.exceptionhandling.CacheExceptionHandler;
import org.codehaus.cake.service.exceptionhandling.ExceptionContext;
import org.codehaus.cake.util.Logger.Level;

public class TestExceptionHandler<K, V> extends CacheExceptionHandler<K, V> {
    private final Queue<ExceptionContext<Cache<K, V>>> q = new ConcurrentLinkedQueue<ExceptionContext<Cache<K, V>>>();

    @Override
    public synchronized void handle(ExceptionContext<Cache<K, V>> context) {
        q.add(context);
    }

    public void eat(Throwable cause, Level level) {
        ExceptionContext context = q.poll();
        assertSame(cause, context.getCause());
        assertSame(level, context.getLevel());
    }

    public void assertCleared() {
        assertEquals(0, q.size());
    }

}
