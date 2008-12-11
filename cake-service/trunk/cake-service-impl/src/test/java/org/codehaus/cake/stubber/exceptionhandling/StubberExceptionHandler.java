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
package org.codehaus.cake.stubber.exceptionhandling;

import org.codehaus.cake.service.exceptionhandling.ExceptionContext;
import org.codehaus.cake.service.exceptionhandling.ExceptionHandler;
import org.codehaus.cake.stubber.Stubber;

public class StubberExceptionHandler<T> extends ExceptionHandler<Stubber<T>> {

    public T tFailed(ExceptionContext<Stubber<T>> context, T key) {
        handle(context);
        return null;
    }
}
