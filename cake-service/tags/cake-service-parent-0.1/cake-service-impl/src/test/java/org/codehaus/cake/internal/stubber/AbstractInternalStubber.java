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

import org.codehaus.cake.internal.service.AbstractInternalContainer;
import org.codehaus.cake.internal.service.Composer;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.internal.stubber.bubber.DefaultBubberService;
import org.codehaus.cake.internal.stubber.exceptionhandling.DefaultStubberExceptionService;
import org.codehaus.cake.internal.stubber.tubber.DefaultTubber1Service;
import org.codehaus.cake.internal.stubber.tubber.DefaultTubberService;
import org.codehaus.cake.service.ContainerConfiguration;
import org.codehaus.cake.stubber.Stubber;
import org.codehaus.cake.util.Logger;

public abstract class AbstractInternalStubber<T> extends AbstractInternalContainer implements Stubber<T> {

    public AbstractInternalStubber(Composer composer) {
        super(composer);
    }

    static Composer newComposer(ContainerConfiguration configuration) {
        Composer composer = new Composer(Stubber.class, configuration);
        composer.registerImplementation(DefaultStubberExceptionService.class);
        composer.registerImplementation(DefaultBubberService.class);
        composer.registerImplementation(DefaultTubberService.class);
        composer.registerImplementation(DefaultTubber1Service.class);
        return composer;
    }

    void fail(InternalExceptionService ies, Logger.Level level, String message, Throwable cause) {
        if (level == Logger.Level.Fatal) {
            if (cause == null) {
                ies.fatal(message);
            } else {
                ies.fatal(message, cause);
            }
        } else if (level == Logger.Level.Error) {
            if (cause == null) {
                ies.error(message);
            } else {
                ies.error(message, cause);
            }
        } else if (level == Logger.Level.Warn) {
            ies.warning(message);
        }

    }
}
