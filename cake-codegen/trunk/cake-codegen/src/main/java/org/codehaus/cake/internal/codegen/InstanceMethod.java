package org.codehaus.cake.internal.codegen;

import org.codehaus.cake.internal.asm.Label;
import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.codegen.ClassEmitter.FieldHeader;

public class InstanceMethod<T extends InstanceMethod<?>> extends AbstractMethod<T> {

    InstanceMethod(ClassEmitter emitter, int access, String name, String signature, Type returnType, Type[] types) {
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
