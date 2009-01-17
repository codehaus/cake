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
    private final String name;
    public GeneratorAdapter adaptor;
    public MethodVisitor mv;
    private final String descriptor;
    final ClassEmitter emitter;

    final Type[] arguments;
    final Type returnType;
    Operation last;

    static Type[] types(Class<?>... classes) {
        Type[] types = new Type[classes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = Type.getType(classes[i]);
        }
        return types;
    }

    static Type type(Class<?> c) {
        return Type.getType(c);
    }

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

    void finish() {
        mv.visitMaxs(3, 3);
        mv.visitEnd();
    }

    public T pop() {
        adaptor.pop();
        return op(Operation.POP);
    }
    public T invokeVirtual(Class<?> target, String method, Class<?> returnType, Class<?>... arguments) {
        MethodDef def = new MethodDef(method, Type.getType(returnType), types(arguments));
        adaptor.invokeVirtual(Type.getType(target), def);
        return op(Operation.INVOKE);
    }

    public T storeLocal(int local, Type c) {
        adaptor.storeLocal(local, c);
        return op(Operation.STORE);
    }

    public T storeArray(Class type) {
      adaptor.arrayStore(type(type));
      return op(Operation.STORE);
    }
    public T invokeStatic(Class<?> target, String method, Type returnType, Type... arguments) {
        MethodDef def = new MethodDef(method, returnType, arguments);
        adaptor.invokeStatic(Type.getType(target), def);
        return op(Operation.INVOKE_STATIC);
    }

    public T invokeInstanceAndStore(Class<?> type, int local) {
        newInstance(type);
        dup();
        invokeConstructor(type);
        return storeLocal(local, type(type));
    }

    public T invokeConstructor(Class<?> target, Class<?>... arguments) {
        MethodDef def = new MethodDef("<init>", Type.VOID_TYPE, types(arguments));
        adaptor.invokeConstructor(type(target), def);
        return op(Operation.INVOKE_SPECIAL);
    }

    public T invokeStatic(Class<?> target, String method, Class<?> returnType, Class<?>... arguments) {
        MethodDef def = new MethodDef(method, Type.getType(returnType), types(arguments));
        adaptor.invokeStatic(Type.getType(target), def);
        return op(Operation.INVOKE_STATIC);
    }

    public T dup() {
        adaptor.dup();
        return op(Operation.DUP);
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
    public T invokeStaticAndStore(int local, Class<?> target, String method, Class<?> returnType, Class<?>... arguments) {
        invokeStatic(target, method, returnType, arguments);
        adaptor.storeLocal(local, Type.getType(returnType));
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

    public T storeLocalConst(int field, Type type, Object value) {
        if (field < 0) {
            throw new IllegalArgumentException("must be >=0");
        }
        pushConst(value);

        adaptor.storeLocal(field, type);
        return op(Operation.STORE);
    }

    public T storeLocalConst(int field, Object value) {
        pushConst(value);
        adaptor.storeLocal(field);
        return op(Operation.STORE);
    }

    public T loadLocal(int local) {
        adaptor.loadLocal(local);
        return op(Operation.LOAD);
    }

   
    public T arrayLoadLocal(int local, int index) {
        loadLocal(local);
        adaptor.push(index);
        adaptor.arrayLoad(adaptor.getLocalType(local).getElementType());
        return op(Operation.LOAD);
    }

    public T checkCast(Class<?> t) {
        return checkCast(type(t));
    }

    public T checkCast(Type t) {
        adaptor.checkCast(t);
        return op(Operation.CAST);
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

    public T box(Type t) {
        adaptor.box(t);
        return op(Operation.UNBOX);
    }

    public T box(Class type) {
        return box(type(type));
    }

    public T unbox(Type t) {
        adaptor.unbox(t);
        return op(Operation.UNBOX);
    }

    public T unbox(Class type) {
        return unbox(type(type));
    }

    public T visitLabel(Label label) {
        adaptor.visitLabel(label);
        return (T) this;
    }

    public T get(Class<?> type, String field, Class<?> returnType) {
        adaptor.getField(type(type), field, type(returnType));
        return op(Operation.GET_STATIC);
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

    T op(Operation op) {
        last = op;
        return (T) this;
    }

    public T returnValue(Object value) {
        pushConst(value);
        returnValue();
        return op(Operation.RETURN);
    }

    public T returnValue() {
        adaptor.returnValue();
        return op(Operation.RETURN);
    }

    public T pushConst(Object value) {
        if (value==null) {
            adaptor.push((String) null);
        } else
        if (value instanceof Boolean) {
            adaptor.push((Boolean) value);
        } else if (value instanceof Integer) {
            adaptor.push((Integer) value);
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

    FieldHeader getField(String name) {
        FieldHeader f = emitter.fields.get(name);
        if (f == null) {
            throw new IllegalStateException("Field not defined '" + name + "'");
        }
        return f;
    }
}
