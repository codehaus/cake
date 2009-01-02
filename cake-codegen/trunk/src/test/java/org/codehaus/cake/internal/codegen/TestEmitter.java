package org.codehaus.cake.internal.codegen;

import org.codehaus.cake.internal.codegen.ClassEmitter;

public abstract class TestEmitter extends ClassEmitter {

    public TestEmitter(Class<?>... types) {
        ClassFactory f = withClass().setPublic();
        if (types.length > 0) {
            f.setSuper(types[0]);
        }
        for (int i = 1; i < types.length; i++) {
            f.addInterfaces(types[i]);
        }
        f.create(AbstractClassEmitterTest.anyName());
    }

    @Override
    public void defineHeader() {

    }

}
