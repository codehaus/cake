package org.codehaus.cake.internal.codegen;

import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.codegen.ClassEmitter.FieldFactory;

public class InstanceMethod<T extends InstanceMethod<?>> extends AbstractMethod<T> {

    InstanceMethod(ClassEmitter emitter, int access, String name, String signature, Type returnType, Type[] types) {
        super(emitter, access, name, signature, returnType, types);
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

    public T loadArg(int arg) {
        adaptor.loadArg(arg);
        return (T) this;
    }

    public T putFieldArg(Class<?> target, String field, Class<?> targetType, int arg) {
        loadArg(arg);
        Type t = arguments[arg];
        mv.visitFieldInsn(Opcodes.PUTFIELD, type(target).getInternalName(), field, type(targetType).getDescriptor());
        last = Operation.PUT_FIELD;
        return (T) this;
    }

    public T putFieldArg(String name, int arg) {
        loadThis().loadArg(arg);
        FieldFactory f = emitter.fields.get(name);
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
