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

import static org.codehaus.cake.internal.asm.Opcodes.ATHROW;
import static org.codehaus.cake.internal.asm.Opcodes.DUP;
import static org.codehaus.cake.internal.asm.Opcodes.INVOKESPECIAL;
import static org.codehaus.cake.internal.asm.Opcodes.NEW;

import org.codehaus.cake.internal.asm.Label;
import org.codehaus.cake.internal.asm.MethodVisitor;
import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.codegen.ClassEmitter.FieldHeader;

public class AbstractMethod<T extends AbstractMethod<?>> {
    public GeneratorAdapter adaptor;
    final Type[] arguments;
    private final String descriptor;
    final ClassEmitter emitter;
    Operation last;

    public MethodVisitor mv;
    private final String name;
    final Type returnType;

    AbstractMethod(ClassEmitter emitter, int access, String name, String signature, Type returnType, Type... types) {
        this.name = name;
        this.emitter = emitter;
        descriptor = Type.getMethodDescriptor(returnType, types);
        mv = emitter.cw.visitMethod(access, name, descriptor, signature, null);
        this.returnType = returnType;
        adaptor = new GeneratorAdapter(mv, access, name, descriptor);
        this.arguments = types;
        mv.visitCode();
    }

    public T arrayLoadLocal(int local, int index) {
        loadLocal(local);
        adaptor.push(index);
        adaptor.arrayLoad(adaptor.getLocalType(local).getElementType());
        return op(Operation.LOAD);
    }

    public T box(Class type) {
        return box(type(type));
    }

    public T box(Type t) {
        adaptor.box(t);
        return op(Operation.UNBOX);
    }

    public T checkCast(Class<?> t) {
        return checkCast(type(t));
    }

    public T checkCast(Type t) {
        adaptor.checkCast(t);
        return op(Operation.CAST);
    }

    public T dup() {
        adaptor.dup();
        return op(Operation.DUP);
    }

    void finish() {
        mv.visitMaxs(3, 3);
        mv.visitEnd();
    }

    public T get(Class<?> type, String field, Class<?> returnType) {
        adaptor.getField(type(type), field, type(returnType));
        return op(Operation.GET_STATIC);
    }

    public GeneratorAdapter getAdapter() {
        return adaptor;
    }

    FieldHeader getField(String name) {
        FieldHeader f = emitter.fields.get(name);
        if (f == null) {
            throw new IllegalStateException("Field not defined '" + name + "'");
        }
        return f;
    }

    public T getStatic(String field) {
        FieldHeader ff = getField(field);
        if (ff.isStatic()) {
            adaptor.getStatic(emitter.getType(), field, getField(field).type);
            return op(Operation.GET_STATIC);
        } else {
            adaptor.getField(emitter.getType(), field, getField(field).type);
            return op(Operation.GET);
        }
    }

    public T invoke(Class<?> type, String method, Class... args) {
        java.lang.reflect.Method m = ClassDefiner.from(type, method, args);
        MethodDef def = new MethodDef(m.getName(), Type.getType(m.getReturnType()), types(m.getParameterTypes()));
        if (type.isInterface()) {
            adaptor.invokeInterface(Type.getType(m.getDeclaringClass()), def);

        } else {
            throw new UnsupportedOperationException();
        }
        return op(Operation.INVOKE);
    }

    public T invokeAndStore(Class<?> type, String method, int locale, Class... args) {
        java.lang.reflect.Method m = ClassDefiner.from(type, method, args);
        MethodDef def = new MethodDef(m.getName(), Type.getType(m.getReturnType()), types(m.getParameterTypes()));
        if (type.isInterface()) {
            adaptor.invokeInterface(Type.getType(m.getDeclaringClass()), def);
        } else {
            throw new UnsupportedOperationException();
        }
        storeLocal(locale, type(m.getReturnType()));
        return op(Operation.STORE);
    }

    public T invokeConstructor(Class<?> target, Class<?>... arguments) {
        MethodDef def = new MethodDef("<init>", Type.VOID_TYPE, types(arguments));
        adaptor.invokeConstructor(type(target), def);
        return op(Operation.INVOKE_SPECIAL);
    }

    public T invokeInstanceAndStore(Class<?> type, int local) {
        newInstance(type);
        dup();
        invokeConstructor(type);
        return storeLocal(local, type(type));
    }

    public T invokeInterface(java.lang.reflect.Method m) {
        MethodDef def = new MethodDef(m.getName(), Type.getType(m.getReturnType()), types(m.getParameterTypes()));
        adaptor.invokeInterface(Type.getType(m.getDeclaringClass()), def);
        return op(Operation.INVOKE);
    }

    public T invokeSpecial(String method, Class<?> returnType, Class<?>... arguments) {
        MethodDef def = new MethodDef(method, Type.getType(returnType), types(arguments));
        adaptor.invokeConstructor(emitter.getType(), def);
        return op(Operation.INVOKE);
    }

    public T invokeStatic(Class<?> target, String method, Class<?> returnType, Class<?>... arguments) {
        MethodDef def = new MethodDef(method, Type.getType(returnType), types(arguments));
        adaptor.invokeStatic(Type.getType(target), def);
        return op(Operation.INVOKE_STATIC);
    }

    public T invokeStatic(Class<?> target, String method, Type returnType, Type... arguments) {
        MethodDef def = new MethodDef(method, returnType, arguments);
        adaptor.invokeStatic(Type.getType(target), def);
        return op(Operation.INVOKE_STATIC);
    }

    public T invokeStaticAndStore(int local, Class<?> target, String method, Class<?> returnType, Class<?>... arguments) {
        invokeStatic(target, method, returnType, arguments);
        adaptor.storeLocal(local, Type.getType(returnType));
        return op(Operation.STORE);
    }

    public T invokeVirtual(Class<?> target, String method, Class<?> returnType, Class<?>... arguments) {
        MethodDef def = new MethodDef(method, Type.getType(returnType), types(arguments));
        adaptor.invokeVirtual(Type.getType(target), def);
        return op(Operation.INVOKE);
    }

    public T loadLocal(int local) {
        adaptor.loadLocal(local);
        return op(Operation.LOAD);
    }

    public T newArrays(Class c, int length) {
        pushConst(length);
        adaptor.newArray(type(c));
        return op(Operation.NEW_ARRAY);
    }

    public T newInstance(Class c) {
        adaptor.newInstance(type(c));
        return op(Operation.NEW_INSTANCE);
    }

    T op(Operation op) {
        last = op;
        return (T) this;
    }

    public T pop() {
        adaptor.pop();
        return op(Operation.POP);
    }

    public T pushConst(Object value) {
        if (value == null) {
            adaptor.push((String) null);
        } else if (value instanceof Boolean) {
            adaptor.push((Boolean) value);
        } else if (value instanceof Integer) {
            adaptor.push((Integer) value);
        } else if (value instanceof Long) {
            adaptor.push((Long) value);
        } else if (value instanceof String) {
            adaptor.push((String) value);
        } else if (value instanceof Class) {
            adaptor.push(type((Class) value));
        } else if (value instanceof Type) {
            adaptor.push((Type) value);
        } else {
            // Add more types if needed;
            throw new IllegalArgumentException();
        }
        return op(Operation.PUSH);
    }

    public T putStatic(String field) {
        mv.visitFieldInsn(Opcodes.PUTSTATIC, emitter.getType().getInternalName(), field, getField(field).descriptor);
        return op(Operation.PUT_STATIC);
    }

    public T putStaticConst(String field, Object value) {
        pushConst(value);
        mv.visitFieldInsn(Opcodes.PUTSTATIC, emitter.getType().getInternalName(), field, getField(field).descriptor);
        last = Operation.PUT_STATIC;
        return (T) this;
    }

    public T returnValue() {
        adaptor.returnValue();
        return op(Operation.RETURN);
    }

    public T returnValue(Object value) {
        pushConst(value);
        returnValue();
        return op(Operation.RETURN);
    }

    public T storeArray(Class type) {
        adaptor.arrayStore(type(type));
        return op(Operation.STORE);
    }

    public T storeLocal(int local, Type c) {
        adaptor.storeLocal(local, c);
        return op(Operation.STORE);
    }

    public T storeLocalConst(int field, Object value) {
        pushConst(value);
        adaptor.storeLocal(field);
        return op(Operation.STORE);
    }

    public T storeLocalConst(int field, Type type, Object value) {
        if (field < 0) {
            throw new IllegalArgumentException("must be >=0");
        }
        pushConst(value);

        adaptor.storeLocal(field, type);
        return op(Operation.STORE);
    }

    public T throwUnsupportedOperationException(String msg) {
        mv.visitTypeInsn(NEW, "java/lang/UnsupportedOperationException");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(msg);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/UnsupportedOperationException", "<init>", "(Ljava/lang/String;)V");
        mv.visitInsn(ATHROW);
        return op(Operation.THROW);
    }

    public T unbox(Class type) {
        return unbox(type(type));
    }

    public T unbox(Type t) {
        adaptor.unbox(t);
        return op(Operation.UNBOX);
    }

    public T visitLabel(Label label) {
        adaptor.visitLabel(label);
        return (T) this;
    }

    static Type type(Class<?> c) {
        return Type.getType(c);
    }

    static Type[] types(Class<?>... classes) {
        Type[] types = new Type[classes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = Type.getType(classes[i]);
        }
        return types;
    }
}
