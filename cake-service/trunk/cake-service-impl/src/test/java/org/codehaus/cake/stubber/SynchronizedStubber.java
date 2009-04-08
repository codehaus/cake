/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.stubber;

import java.util.concurrent.ExecutorService;

import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.SynchronizedRunState;
import org.codehaus.cake.internal.service.executor.DefaultExecutorService;
import org.codehaus.cake.internal.service.executor.DefaultForkJoinPool;
import org.codehaus.cake.internal.service.executor.DefaultScheduledExecutorService;
import org.codehaus.cake.internal.service.management.DefaultManagementService;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.stubber.bubber.BubberService;
import org.codehaus.cake.util.Logger;

@Container.SupportedServices( { ExecutorService.class, BubberService.class, Manageable.class })
public class SynchronizedStubber<T> extends AbstractStubber<T> implements Stubber<T> {

    public SynchronizedStubber() {
        this(StubberConfiguration.<T> newConfiguration());
    }

    public SynchronizedStubber(StubberConfiguration<T> configuration) {
        super(createComposer(configuration));
    }

    public void fail(Logger.Level level, String message, Throwable cause) {
        synchronized (this) {
            lazyStart();
            super.fail(exceptionService, level, message, cause);
        }
    }

    public T getIt(T t) {
        synchronized (this) {
            lazyStart();
            return t;
        }
    }

    private static Composer createComposer(StubberConfiguration<?> configuration) {
        Composer composer = AbstractStubber.newComposer(configuration);

        // Common components
        composer.registerImplementation(DefaultExecutorService.class);
        composer.registerImplementation(DefaultScheduledExecutorService.class);
        composer.registerImplementation(DefaultForkJoinPool.class);

        composer.registerImplementation(SynchronizedRunState.class);
        if (configuration.withManagement().isEnabled()) {
            composer.registerImplementation(DefaultManagementService.class);
        }

        return composer;
    }
}
