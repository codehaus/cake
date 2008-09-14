/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.cache.test.service.exceptionhandling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.service.exceptionhandling.CacheExceptionHandler;
import org.codehaus.cake.service.common.exceptionhandling.ExceptionContext;
import org.codehaus.cake.util.Logger.Level;

public class CacheTestExceptionHandler<K, V> extends CacheExceptionHandler<K, V> {
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
