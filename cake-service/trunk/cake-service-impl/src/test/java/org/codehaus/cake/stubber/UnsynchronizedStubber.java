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
package org.codehaus.cake.stubber;

import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.UnsynchronizedRunState;
import org.codehaus.cake.service.Container;
import org.codehaus.cake.stubber.bubber.BubberService;
import org.codehaus.cake.util.Logger;

@Container.SupportedServices( { BubberService.class })
public class UnsynchronizedStubber<T> extends AbstractStubber<T> {


    public UnsynchronizedStubber() {
        this(StubberConfiguration.<T> newConfiguration());
    }

    public UnsynchronizedStubber(StubberConfiguration<T> configuration) {
        super(createComposer(configuration));
    }

    public void fail(Logger.Level level, String message, Throwable cause) {
        lazyStart();
        super.fail(exceptionService, level, message, cause);
    }

    public T getIt(T t) {
        lazyStart();
        return t;
    }

    private static Composer createComposer(StubberConfiguration<?> configuration) {
        Composer composer = AbstractStubber.newComposer(configuration);

        if (configuration.withManagement().isEnabled()) {
            throw new IllegalArgumentException("Cache does not support Management");
        }
        composer.registerImplementation(UnsynchronizedRunState.class);
        return composer;
    }
}
