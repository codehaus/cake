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
package org.codehaus.cake.internal.stubber.exceptionhandling;

import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.exceptionhandling.AbstractExceptionService;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.service.exceptionhandling.ExceptionContext;
import org.codehaus.cake.service.exceptionhandling.ExceptionHandlingConfiguration;
import org.codehaus.cake.stubber.Stubber;
import org.codehaus.cake.stubber.exceptionhandling.StubberExceptionHandler;

public class DefaultStubberExceptionService<T> extends AbstractExceptionService<Stubber<T>> {
    /** The CacheExceptionHandler configured for this cache. */
    private StubberExceptionHandler<T> exceptionHandler;

    public DefaultStubberExceptionService(Container container, Composer composer,
            ContainerConfiguration containerConfiguration,
            ExceptionHandlingConfiguration<StubberExceptionHandler<T>> configuration) {
        super(container, composer, containerConfiguration, configuration.getExceptionLogger());
        StubberExceptionHandler<T> exceptionHandler = configuration.getExceptionHandler();
        this.exceptionHandler = exceptionHandler == null ? new StubberExceptionHandler<T>() : exceptionHandler;
    }

    @Override
    protected void handle(ExceptionContext<Stubber<T>> context) {
        exceptionHandler.handle(context);
    }
}
