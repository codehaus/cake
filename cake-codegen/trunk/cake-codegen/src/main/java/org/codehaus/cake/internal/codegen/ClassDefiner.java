/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.internal.codegen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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
        return (T) instantiate(clazz, args);//javac needs the cast (T)
    }

    public <T> T instantiate(Class<?> clazz, Object... args) {
        try {
            if (args.length == 0) {
                return (T) clazz.newInstance();
            } else {
                return (T) findConstructor(clazz, args).newInstance(args);
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> Constructor<T> findConstructor(Class<T> from, Object... args) {
        Constructor candidate = null;
        for (Constructor c : from.getConstructors()) {
            Class[] constructorArgs = c.getParameterTypes();
            if (constructorArgs.length == args.length) {
                for (int i = 0; i < args.length; i++) {
                    boolean isAssignable = constructorArgs[i].isAssignableFrom(args[i].getClass());
                    if (!isAssignable) {
                        c = null;
                    }
                }
                if (c != null) {
                    if (candidate != null) {
                        throw new IllegalArgumentException("Found more then 1 constructor matching the args ["
                                + Arrays.toString(args) + "]");
                    }
                    candidate = c;
                }
            }
        }
        if (candidate != null) {
            return candidate;
        }
        throw new IllegalArgumentException("Could not find matching constructor [" + Arrays.toString(args) + "]");
    }

    static class TemporaryClassLoader extends ClassLoader {
        protected Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }

    public static Method from(Class<?> from, String methodName, Class<?>... args) {
        try {
            return from.getMethod(methodName, args);
        } catch (NoSuchMethodException e) {}

        // See if we get just one hit for method name
        if (args.length == 0) {
            Method onehit = null;
            for (Method m : from.getMethods()) {
                if (m.getName().equals(methodName)) {
                    if (onehit != null) {
                        throw new Error("Internal Error (This should never happen), method does not exist [name="
                                + methodName + "]");
                    }
                    onehit = m;
                }
            }
            if (onehit != null) {
                return onehit;
            }
        }
        throw new Error("Internal Error (This should never happen), method does not exist [name=" + methodName + ", args= " + Arrays.toString(args) + "]");
    }
}
