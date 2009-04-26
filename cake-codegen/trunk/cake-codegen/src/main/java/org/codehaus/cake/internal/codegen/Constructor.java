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
import org.codehaus.cake.internal.util.ArrayUtils;

public class Constructor extends MethodOrConstructor<Constructor> {

    Constructor(ClassEmitter emitter, int access, Type... types) {
        super(emitter, access, "<init>", null, Type.VOID_TYPE, types);
    }

    void finish() {
        if (last != Operation.RETURN) {
            adaptor.returnValue();
        }
        super.finish();
    }

    public Constructor invokeEmptySuper() {
        loadThis();
        return invokeSuper();
    }

    public Constructor invokeSuper(Type... types) {
        String descriptor = Type.getMethodDescriptor(Type.VOID_TYPE, types);
        return invokeSuper(descriptor);
    }

    public Constructor loadAndInvokeSuperWithArgs(int count) {
        adaptor.loadThis();
        adaptor.loadArgs(0, count);
        String descriptor = Type.getMethodDescriptor(Type.VOID_TYPE, ArrayUtils.copyOf(arguments, count));
        return invokeSuper(descriptor);
    }

    public Constructor invokeSuper(String descriptor) {
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, emitter.c.superType, "<init>", descriptor);
        return this;
    }
}
