package org.codehaus.cake.internal.codegen;


public abstract class TestEmitter extends ClassEmitter {

    public TestEmitter(Class... types) {
        ClassHeader f = withClass().setPublic();
        if (types.length > 0) {
            f.setSuper(types[0]);
        }
        for (int i = 1; i < types.length; i++) {
            f.addInterfaces(types[i]);
        }
        f.create(AbstractClassEmitterTest.anyName());
    }

    @Override
    public void define() {

    }

}
