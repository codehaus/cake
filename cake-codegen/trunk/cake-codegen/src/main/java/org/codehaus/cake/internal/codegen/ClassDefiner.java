package org.codehaus.cake.internal.codegen;

import java.util.concurrent.atomic.AtomicInteger;

public class ClassDefiner {

    private AtomicInteger prefix = new AtomicInteger();

    private TemporaryClassLoader myLoader;

    private TemporaryClassLoader lazy() {
        if (myLoader == null) {
            myLoader = new TemporaryClassLoader();
        }
        return myLoader;
    }

    public String createClassName(Package p, String prefix) {
        String name = p.getName() + "/" + prefix + this.prefix.incrementAndGet();
        return name.replace('.', '/');
    }

    public String createClassName(Class<?> friend, String prefix) {
        return createClassName(friend.getPackage(), prefix);
    }

    public Class<?> define(ClassEmitter emitter) {
        byte[] b = emitter.generate();
        String name = emitter.getType().getClassName();
        Class<?> cl = lazy().defineClass(name, b);
        return cl;
    }

    public <T> T defineAndInstantiate(ClassEmitter emitter, Object... args) {
        Class<?> clazz = define(emitter);
        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static class TemporaryClassLoader extends ClassLoader {
         protected Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}
