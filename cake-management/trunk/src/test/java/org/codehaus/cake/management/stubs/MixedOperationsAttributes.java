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

import org.codehaus.cake.management.annotation.ManagedAttribute;
import org.codehaus.cake.management.annotation.ManagedOperation;

public class MixedOperationsAttributes {
    public int invokeCount;

    private String string;

    @ManagedAttribute
    public String getString() {
        return string;
    }

    @ManagedOperation
    public void method1() {
        invokeCount += 1;
    }

    public void setString(String string) {
        this.string = string;
    }
}
