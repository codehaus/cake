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
package org.codehaus.cake.management.stubs;

import org.codehaus.cake.management.annotation.ManagedOperation;

/**
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class VariousOperations {
    public int invokeCount;

    @ManagedOperation(defaultValue = "m2")
    public void method2() {
        invokeCount++;
    }

    @ManagedOperation()
    public String method3() {
        return "m3";
    }

    @ManagedOperation()
    public String method4(String arg) {
        return arg.toUpperCase();
    }

    @ManagedOperation(description = "desca")
    public void method5() {}

    @ManagedOperation(defaultValue = "foo", description = "desc")
    public void method6() {}
}
