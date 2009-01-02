package org.codehaus.cake.internal.codegen;

import org.codehaus.cake.internal.asm.MethodVisitor;
import org.codehaus.cake.internal.asm.Opcodes;
import static org.codehaus.cake.internal.asm.Opcodes.*;
import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.codegen.ClassEmitter.FieldFactory;

public class AbstractMethod<T extends AbstractMethod<?>> {
    private final String name;
    GeneratorAdapter adaptor;
    public MethodVisitor mv;
    private final String descriptor;
    final ClassEmitter emitter;

    final Type[] arguments;

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
        adaptor = new GeneratorAdapter(mv, access, name, descriptor);
        this.arguments = types;
        mv.visitCode();
    }

    void finish() {
        mv.visitMaxs(3, 3);
        mv.visitEnd();
    }

    public T invokeStatic(Class<?> target, String method, Class<?> returnType, Class<?>... arguments) {
        MethodDef def = new MethodDef(method, Type.getType(returnType), types(arguments));
        adaptor.invokeStatic(Type.getType(target), def);
        return op(Operation.INVOKE_STATIC);
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
        adaptor.getStatic(emitter.getType(), field, getField(field).type);
        return op(Operation.GET_STATIC);
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

    private T op(Operation op) {
        last = op;
        return (T) this;
    }

    public T pushConst(Object value) {
        if (value instanceof Integer) {
            adaptor.push((Integer) value);
        } else if (value instanceof String) {
            adaptor.push((String) value);
        } else if (value instanceof Class) {
            adaptor.push(type((Class) value));
        }

        else {
            // Add more types if needed;
            throw new IllegalArgumentException();
        }
        return op(Operation.PUSH);
    }

    FieldFactory getField(String name) {
        FieldFactory f = emitter.fields.get(name);
        if (f == null) {
            throw new IllegalStateException("Field not defined '" + name + "'");
        }
        return f;
    }
}
