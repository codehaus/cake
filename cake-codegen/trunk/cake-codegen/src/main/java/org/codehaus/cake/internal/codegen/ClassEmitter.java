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

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.cake.internal.asm.ClassVisitor;
import org.codehaus.cake.internal.asm.ClassWriter;
import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;

public abstract class ClassEmitter {
    private static final int version = Opcodes.V1_5;

    ClassHeader c = new ClassHeader();

    AbstractMethod<?> current;
    protected ClassVisitor cw;
    final Map<String, FieldHeader> fields = new LinkedHashMap<String, FieldHeader>();
    boolean initialValueSet;
    byte[] generated;

    private State state = new State();

    private boolean staticInitializerGenerated;

    private Type type;

    protected final ClassWriter writer;

    public ClassEmitter() {
        writer = new ClassWriter(1);
        try {
            Class c = Class.forName("org.codehaus.cake.internal.asm.util.TraceClassVisitor");
            java.lang.reflect.Constructor<ClassVisitor> con = c.getConstructor(ClassVisitor.class, PrintWriter.class);
            cw = con.newInstance(writer, new PrintWriter(System.out));
            // cw = new TraceClassVisitor(writer, new PrintWriter(System.out));
            cw = writer;

        } catch (ClassNotFoundException e) {
            cw = writer;
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        } catch (InstantiationException e) {
            throw new Error(e);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        } catch (InvocationTargetException e) {
            throw new Error(e);
        }
    }

    /** Defines the header */
    public abstract void define();

    void finish() {
        if (current != null) {
            current.finish();
        }
    }

    protected boolean isPrimitive(Type t) {
        return t == Type.BOOLEAN_TYPE || t == Type.BYTE_TYPE || t == Type.CHAR_TYPE || t == Type.DOUBLE_TYPE
                || t == Type.FLOAT_TYPE || t == Type.INT_TYPE || t == Type.LONG_TYPE || t == Type.SHORT_TYPE;
    }

    public final byte[] generate() {
        if (generated != null) {
            return generated;
        }
        define();
        if (type == null) {
            throw new IllegalStateException("Class header has not been filled out");
        }
        // Define fields
        for (FieldHeader f : fields.values()) {
            cw.visitField(f.access, f.name, f.descriptor, f.signature, f.initialValue).visitEnd();
        }
        if (!state.constructorInvoked) {
            withConstructor().setPublic().createDefault();
        }
        // if (!staticInitializerGenerated && initialValueSet) {
        // withStaticInitializer();
        // }
        finish();
        cw.visitEnd();
        generated = writer.toByteArray();
        return generated;
    }

    public Type getType() {
        if (type == null) {
            throw new IllegalStateException("Class Header not generated yet");
        }
        return type;
    }

    public ClassHeader withClass() {
        return c;
    }

    public ConstructorHeader withConstructor() {
        finish();
        return new ConstructorHeader();
    }

    public FieldHeader withField(String name) {
        return withField(name, 0);
    }

    FieldHeader withField(String name, int access) {
        getType();
        if (fields.containsKey(name)) {
            throw new IllegalArgumentException("Field '" + name + "' already defined");
        }
        return new FieldHeader(name, access);
    }

    public FieldHeader withFieldPublic(String name) {
        return withField(name, Opcodes.ACC_PUBLIC);
    }

    MethodHeader withMethod(String name, int access) {
        getType();
        finish();
        return new MethodHeader(name, access);
    }

    public Method withMethodImplement(java.lang.reflect.Method method) {
        getType();
        finish();
        Method m = new Method(ClassEmitter.this, method, true);
        current = m;
        return m;
    }

    public MethodHeader withMethodPublic(String name) {
        return withMethod(name, Opcodes.ACC_PUBLIC);
    }

    public MethodHeader withMethodPrivate(String name) {
        return withMethod(name, Opcodes.ACC_PRIVATE);
    }

    public StaticInitializer withStaticInitializer() {
        if (staticInitializerGenerated) {
            throw new IllegalStateException("Can only generate one static initializer");
        }
        finish();
        StaticInitializer si = new StaticInitializer(this);
        // if (initialValueSet) {
        // for (FieldFactory f : fields.values()) {
        // if (f.initialValue != null) {
        // si.putStaticConst(f.name, f.initialValue);
        // }
        // }
        // }
        current = si;
        staticInitializerGenerated = true;
        return si;
    }

    public static Type[] toTypes(Class<?>... classes) {
        Type[] types = new Type[classes.length];
        for (int i = 0; i < types.length; i++) {
            types[i] = Type.getType(classes[i]);
        }
        return types;
    }

    protected static Type type(Class<?> type) {
        return Type.getType(type);
    }

    protected static Type typeAny() {
        return Type.getType(Object.class);
    }

    protected static Type typeInt() {
        return Type.INT_TYPE;
    }

    protected static Type typeString() {
        return Type.getType(String.class);
    }

    public class AbstractMemberHeader<T extends AbstractMemberHeader<?>> {
        int access;
        String name;

        void checkState() {
        }

        public final T setProtected() {
            checkState();
            access += Opcodes.ACC_PROTECTED;
            return (T) this;
        }

    }

    public class ClassHeader extends AbstractMemberHeader<ClassHeader> {
        final ArrayList<String> interfaces = new ArrayList<String>();
        String superType = "java/lang/Object";

        public ClassHeader addInterfaces(Class<?>... interfaces) {
            checkType();
            for (Class<?> c : interfaces) {
                this.interfaces.add(Type.getInternalName(c));
            }
            return this;
        }

        public ClassHeader addInterfaces(Type... interfaces) {
            checkType();
            for (Type t : interfaces) {
                this.interfaces.add(t.getInternalName());
            }
            return this;
        }

        void checkType() {
            if (type != null) {
                throw new IllegalStateException("Class header already created");
            }
        }

        public void create(String name) {
            checkType();
            this.name = name;

            type = Type.getType("L" + name + ";");
            cw.visit(version, access + Opcodes.ACC_SUPER, c.name, null, c.superType, c.interfaces
                    .toArray(new String[0]));
        }

        public final ClassHeader setPublic() {
            checkState();
            access += Opcodes.ACC_PUBLIC;
            return this;
        }

        public ClassHeader setSuper(Class<?> type) {
            return setSuper(Type.getType(type));
        }

        public ClassHeader setSuper(String type) {
            checkType();
            superType = type;
            return this;
        }

        public ClassHeader setSuper(Type type) {
            return setSuper(type.getInternalName());
        }
    }

    public class ConstructorHeader extends AbstractMemberHeader<ConstructorHeader> {

        public Constructor create(Class<?>... arguments) {
            return create(toTypes(arguments));
        }

        public Constructor create(Type... arguments) {
            Constructor con = new Constructor(ClassEmitter.this, access, arguments);
            current = con;
            state.newConstructor();
            return con;
        }

        public void createDefault() {
            createDelegating(new Type[0]);
        }

        public Constructor createDelegating(Class<?>... arguments) {
            return createDelegating(toTypes(arguments));
        }

        public Constructor createDelegating(int count, Class<?>... arguments) {
            return createDelegating(toTypes(arguments));
        }

        public Constructor createDelegating(int count, Type... arguments) {
            Constructor con = create(arguments);
            con.loadThis();
            con.adaptor.loadArgs(0, count);
            con.invokeSuper(arguments);
            return con;
        }

        public Constructor createDelegating(Type... arguments) {
            Constructor con = create(arguments);
            con.loadThis();
            con.adaptor.loadArgs();
            con.invokeSuper(arguments);
            return con;
        }

        public final ConstructorHeader setPublic() {
            checkState();
            access += Opcodes.ACC_PUBLIC;
            return this;
        }
    }

    public class FieldHeader extends AbstractMemberHeader<FieldHeader> {
        String descriptor;
        Object initialValue;
        String signature;
        Type type;

        FieldHeader(String name, int access) {
            this.name = name;
            this.access = access;
        }

        void checkState() {

        }

        public void create(Class<?> type) {
            create(Type.getType(type));
        }

        public void create(Type type) {
            this.type = type;
            descriptor = type.getDescriptor();
            fields.put(name, this);
        }

        void create(Type type, Object initial) {
            this.type = type;
            descriptor = type.getDescriptor();
            initialValueSet = true;
            this.initialValue = initial;
            fields.put(name, this);

        }

        public void createBoolean() {
            create(Type.BOOLEAN_TYPE);
        }

        public void createByte() {
            create(Type.BYTE_TYPE);
        }

        public void createChar() {
            create(Type.CHAR_TYPE);
        }

        public void createDouble() {
            create(Type.DOUBLE_TYPE);
        }

        public void createFloat() {
            create(Type.FLOAT_TYPE);
        }

        public void createInt() {
            create(Type.INT_TYPE);
        }

        public void createLong() {
            create(Type.LONG_TYPE);
        }

        public void createShort() {
            create(Type.SHORT_TYPE);
        }

        public void createString() {
            create(Type.getType(String.class));
        }

        public void createString(String initialValue) {
            create(Type.getType(String.class), initialValue);
        }

        public FieldHeader setFinal() {
            return setFinal(true);
        }

        public FieldHeader setFinal(boolean isFinal) {
            checkState();
            access = isFinal ? access |= Opcodes.ACC_FINAL : access & ~Opcodes.ACC_FINAL;
            return this;
        }

        public FieldHeader setInitialValue(Object value) {
            checkState();
            // Check type
            initialValue = value;
            return this;
        }

        public FieldHeader setPrivate() {
            checkState();
            access += Opcodes.ACC_PRIVATE;
            return this;
        }

        boolean isStatic() {
            return (access & Opcodes.ACC_STATIC) != 0;
        }

        public FieldHeader setSignature(String signature) {
            checkState();
            this.signature = signature;
            return this;
        }

        public FieldHeader setStatic() {
            return setStatic(true);
        }

        public FieldHeader setStatic(boolean isStatic) {
            checkState();
            access = isStatic ? access |= Opcodes.ACC_STATIC : access & ~Opcodes.ACC_STATIC;
            return this;
        }

        public FieldHeader setVolatile() {
            checkState();
            access += Opcodes.ACC_VOLATILE;
            return this;
        }
    }

    public class MethodHeader extends AbstractMemberHeader<FieldHeader> {
        String descriptor;
        Object initialValue;
        Type returnType = Type.VOID_TYPE;
        String signature;

        MethodHeader(String name, int access) {
            this.name = name;
            this.access = access;
        }

        void checkState() {

        }

        public Method create() {
            return create(new Class[0]);
        }

        public Method create(Class<?>... arguments) {
            return create(toTypes(arguments));
        }

        public Method create(Type... arguments) {
            Method m = new Method(ClassEmitter.this, access, name, signature, returnType, arguments);
            current = m;
            return m;
        }

        public MethodHeader setReturnType(Class<?> type) {
            return setReturnType(type(type));
        }

        public MethodHeader setReturnType(Type type) {
            returnType = type;
            return this;
        }
    }

    class State {
        boolean constructorInvoked;
        private int state;

        void newConstructor() {
            constructorInvoked = true;
        }

        void newMethod() {
            if (!constructorInvoked) {
                withConstructor().createDefault();
            }
        }
    }

    public java.lang.reflect.Method findMetod(Class<?> from, String methodName, Class<?>... args) {
        try {
            return from.getMethod(methodName, args);
        } catch (NoSuchMethodException e) {}

        // See if we get just one hit for method name
        if (args.length == 0) {
            java.lang.reflect.Method onehit = null;
            for (java.lang.reflect.Method m : from.getMethods()) {
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
        throw new Error("Internal Error (This should never happen), method does not exist [name=" + methodName + "]");
    }
}
