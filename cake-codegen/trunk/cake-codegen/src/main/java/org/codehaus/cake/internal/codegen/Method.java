package org.codehaus.cake.internal.codegen;

import org.codehaus.cake.internal.asm.Type;

public class Method extends InstanceMethod<Method> {

  Method(ClassEmitter emitter, int access, String name, String signature, Type returnType,
    Type... types) {
    super(emitter, access, name, signature, returnType, types);
  }

  void finish() {
    if (last != Operation.RETURN && last != Operation.THROW) {
      adaptor.returnValue();
    }
    super.finish();
  }

}
