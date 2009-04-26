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
package org.codehaus.cake.internal.codegen;

import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;

public class Method extends MethodOrConstructor<Method> {

    Method(ClassEmitter emitter, int access, String name, String signature, Type returnType, Type... types) {
        super(emitter, access, name, signature, returnType, types);
    }

    Method(ClassEmitter emitter, java.lang.reflect.Method method, boolean implement) {
        super(emitter, method.getModifiers() & ~Opcodes.ACC_ABSTRACT, method.getName(), Type.getMethodDescriptor(method), Type.getReturnType(method), Type
                .getArgumentTypes(method));
    }

    void finish() {
        if (last != Operation.RETURN && last != Operation.THROW) {
            adaptor.returnValue();
        }
        super.finish();
    }

}
