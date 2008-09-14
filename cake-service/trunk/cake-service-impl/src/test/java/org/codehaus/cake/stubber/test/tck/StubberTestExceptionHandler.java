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
package org.codehaus.cake.stubber.test.tck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.codehaus.cake.service.common.exceptionhandling.ExceptionContext;
import org.codehaus.cake.stubber.Stubber;
import org.codehaus.cake.stubber.exceptionhandling.StubberExceptionHandler;
import org.codehaus.cake.util.Logger.Level;

public class StubberTestExceptionHandler<T> extends StubberExceptionHandler<T> {
    private final Queue<ExceptionContext<Stubber<T>>> q = new ConcurrentLinkedQueue<ExceptionContext<Stubber<T>>>();

    @Override
    public synchronized void handle(ExceptionContext<Stubber<T>> context) {
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
