package org.codehaus.cake.internal.codegen;

import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;

public class StaticInitializer extends AbstractMethod<StaticInitializer> {

    StaticInitializer(ClassEmitter emitter) {
        super(emitter, 0 + Opcodes.ACC_STATIC, "<clinit>", null, Type.VOID_TYPE, new Type[0]);
    }

    void finish() {
        if (last != Operation.RETURN) {
            adaptor.returnValue();
        }
        super.finish();
    }
}
