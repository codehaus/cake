package org.codehaus.cake.internal.codegen;

import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.util.ArrayUtils;

public class Constructor extends InstanceMethod<Constructor> {

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
