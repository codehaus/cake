package org.codehaus.cake.internal.codegen;

import java.lang.reflect.Constructor;

import org.codehaus.cake.internal.codegen.ClassEmitter;

public class AbstractClassEmitterTest {

    protected ClassEmitter emitter;

    protected Class generated;

    static String anyName() {
        return "org/codehaus/cake/test/Class" + System.nanoTime();
    }

    protected <T> Class<T> generate() {
        if (generated != null) {
            return generated;
        }
        byte[] b = emitter.generate();
        generated = myLoader.defineClass(emitter.getType().getInternalName().replace('/', '.'), b);
        return generated;
    }

    protected <T> T newInstance(Object... types) throws Exception {
        Constructor con=generate().getConstructor();
        return (T) con.newInstance();
    }

    static MyLoader myLoader = new MyLoader();

    static class MyLoader extends ClassLoader {
        public Class defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}
