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
package org.codehaus.cake.internal.stubber;

import java.util.concurrent.ExecutorService;

import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.SynchronizedRunState;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.internal.service.executor.DefaultExecutorsService;
import org.codehaus.cake.internal.service.management.DefaultManagementService;
import org.codehaus.cake.management.Manageable;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.stubber.Stubber;
import org.codehaus.cake.stubber.StubberConfiguration;
import org.codehaus.cake.stubber.bubber.BubberService;
import org.codehaus.cake.util.Logger;

@Container.SupportedServices( { ExecutorService.class, BubberService.class, Manageable.class })
public class SynchronizedInternalStubber<T> extends AbstractInternalStubber<T> {

    private final InternalExceptionService exceptionService;

    public SynchronizedInternalStubber(StubberConfiguration<T> configuration) {
        this(createComposer(configuration));
    }

    public SynchronizedInternalStubber(StubberConfiguration<T> configuration, Stubber<T> wrapper) {
        this(createComposer(configuration, wrapper));
    }

    public SynchronizedInternalStubber(Composer composer) {
        super(composer);
        exceptionService = composer.get(InternalExceptionService.class);
    }

    public void fail(Logger.Level level, String message, Throwable cause) {
        lazyStart();
        super.fail(exceptionService, level, message, cause);
    }

    public T getIt(T t) {
        lazyStart();
        return t;
    }

    private static Composer createComposer(StubberConfiguration<?> configuration, Stubber<?> stubber) {
        Composer composer = createComposer(configuration);
        composer.registerInstance(Stubber.class, stubber);
        composer.registerInstance(Container.class, stubber);
        return composer;
    }

    private static Composer createComposer(StubberConfiguration<?> configuration) {
        Composer composer = AbstractInternalStubber.newComposer(configuration);

        // Common components
        composer.registerImplementation(DefaultExecutorsService.class);
        composer.registerImplementation(SynchronizedRunState.class);
        if (configuration.withManagement().isEnabled()) {
            composer.registerImplementation(DefaultManagementService.class);
        }

        return composer;
    }
}
