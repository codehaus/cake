package org.codehaus.cake.internal.codegen;

import org.codehaus.cake.internal.asm.Opcodes;

public enum Visibility {
    PRIVATE(Opcodes.ACC_PRIVATE), PROTECTED(Opcodes.ACC_PROTECTED), PACKAGE(0), PUBLIC(Opcodes.ACC_PUBLIC);
    private int value;

    private Visibility(int value) {
        this.value = value;
    }

    public int getModifier() {
        return value;
    }
}
