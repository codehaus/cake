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

import org.codehaus.cake.internal.asm.Label;
import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.codegen.ClassEmitter.FieldHeader;

public abstract class MethodOrConstructor<T extends MethodOrConstructor<?>> extends AbstractMethod<T> {

    MethodOrConstructor(ClassEmitter emitter, int access, String name, String signature, Type returnType, Type[] types) {
        super(emitter, access, name, signature, returnType, types);
    }

    public Type getReturnType() {
        return returnType;
    }

    T loadArg(int... args) {
        for (int i : args) {
            adaptor.loadArg(i);
        }
        return (T) this;
    }

    public T loadThis() {
        adaptor.loadThis();
        return (T) this;
    }

    public void jumpIfNotEqual(Class type, Label label) {
        adaptor.ifCmp(type(type), GeneratorAdapter.NE, label);
    }

    public void jumpIfEqual(Class<?> type, Label label) {
        adaptor.ifCmp(type(type), GeneratorAdapter.EQ, label);
    }

    public T loadArgs(int... args) {
        for (int arg : args) {
            adaptor.loadArg(arg);
        }
        return op(Operation.LOAD);
    }

    public T loadArg(int arg) {
        adaptor.loadArg(arg);
        return op(Operation.LOAD);
    }

    public T putFieldArg(Class<?> target, String field, Class<?> targetType, int arg, boolean checkCast) {
        loadArg(arg);
        if (checkCast) {
            checkCast(targetType);
        }
        Type t = arguments[arg];
        mv.visitFieldInsn(Opcodes.PUTFIELD, type(target).getInternalName(), field, type(targetType).getDescriptor());
        last = Operation.PUT_FIELD;
        return (T) this;
    }

    public T returnField(String name) {
        getStatic(name);
        return returnValue();
    }

    public T putFieldArg(String name, int arg) {
        loadThis().loadArg(arg);
        FieldHeader f = emitter.fields.get(name);
        if (f == null) {
            throw new IllegalStateException("Field not defined + '" + name + "'");
        }
        Type t = arguments[arg];
        boxUnbox(t, f.type);
        mv.visitFieldInsn(Opcodes.PUTFIELD, emitter.getType().getInternalName(), name, f.descriptor);
        last = Operation.PUT_FIELD;
        return (T) this;
    }

    void boxUnbox(Type from, Type to) {
        // More types
        if (from == Type.INT_TYPE && isNumberType(to, GeneratorAdapter.INTEGER_TYPE)) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
        }
        // Currently we only handle boxing of ints
    }

    private static boolean isNumberType(Type a, Type b) {
        return a.equals(b) || a.equals(GeneratorAdapter.NUMBER_TYPE);
    }
}
