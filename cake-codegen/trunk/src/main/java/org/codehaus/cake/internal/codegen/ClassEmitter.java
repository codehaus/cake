package org.codehaus.cake.internal.codegen;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.cake.internal.asm.ClassVisitor;
import org.codehaus.cake.internal.asm.ClassWriter;
import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.asm.util.TraceClassVisitor;

public abstract class ClassEmitter {
    private static final int version = Opcodes.V1_5;

    ClassFactory c = new ClassFactory();

    AbstractMethod<?> current;
    protected final ClassVisitor cw;
    final Map<String, FieldFactory> fields = new LinkedHashMap<String, FieldFactory>();
    boolean initialValueSet;
    byte[] generated;

    private State state = new State();

    private boolean staticInitializerGenerated;

    private Type type;

    protected final ClassWriter writer;

    public ClassEmitter() {
        writer = new ClassWriter(1);
        cw = new TraceClassVisitor(writer, new PrintWriter(System.out));
    }

    public abstract void defineHeader();

    void finish() {
        if (current != null) {
            current.finish();
        }
    }

    public final byte[] generate() {
        if (generated != null) {
            return generated;
        }
        defineHeader();
        if (type == null) {
            throw new IllegalStateException("Class header has not been filled out");
        }
        // Define fields
        for (FieldFactory f : fields.values()) {
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

    public ClassFactory withClass() {
        return c;
    }

    public ConstructorFactory withConstructor() {
        finish();
        return new ConstructorFactory();
    }

    public FieldFactory withField(String name) {
        return withField(name, 0);
    }

    FieldFactory withField(String name, int access) {
        getType();
        if (fields.containsKey(name)) {
            throw new IllegalArgumentException("Field '" + name + "' already defined");
        }
        return new FieldFactory(name, access);
    }

    public FieldFactory withFieldPublic(String name) {
        return withField(name, Opcodes.ACC_PUBLIC);
    }

    MethodFactory withMethod(String name, int access) {
        getType();
        finish();
        return new MethodFactory(name, access);
    }

    public MethodFactory withMethodPublic(String name) {
        return withMethod(name, Opcodes.ACC_PUBLIC);
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

    public class AbstractMemberFactory<T extends AbstractMemberFactory<?>> {
        int access = 0;
        String name;

        void checkState() {
        }

        public final T setProtected() {
            checkState();
            access += Opcodes.ACC_PROTECTED;
            return (T) this;
        }

    }

    public class ClassFactory extends AbstractMemberFactory<ClassFactory> {
        final ArrayList<String> interfaces = new ArrayList<String>();
        String superType = "java/lang/Object";

        public ClassFactory addInterfaces(Class<?>... interfaces) {
            checkType();
            for (Class<?> c : interfaces) {
                this.interfaces.add(Type.getInternalName(c));
            }
            return this;
        }

        public ClassFactory addInterfaces(Type... interfaces) {
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

        public final ClassFactory setPublic() {
            checkState();
            access += Opcodes.ACC_PUBLIC;
            return this;
        }

        public ClassFactory setSuper(Class<?> type) {
            checkType();
            return setSuper(Type.getType(type));
        }

        public ClassFactory setSuper(String type) {
            checkType();
            superType = type;
            return this;
        }

        public ClassFactory setSuper(Type type) {
            checkType();
            return setSuper(type.getInternalName());
        }
    }

    public class ConstructorFactory extends AbstractMemberFactory<ConstructorFactory> {

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

        public void createDelegating(Class<?>... arguments) {
            createDelegating(toTypes(arguments));
        }

        public void createDelegating(Type... arguments) {
            Constructor con = create(arguments);
            con.loadThis();
            con.adaptor.loadArgs();
            con.invokeSuper(arguments);
        }

        public final ConstructorFactory setPublic() {
            checkState();
            access += Opcodes.ACC_PUBLIC;
            return this;
        }
    }

    public class FieldFactory extends AbstractMemberFactory<FieldFactory> {
        String descriptor;
        Object initialValue;
        String signature;
        Type type;

        FieldFactory(String name, int access) {
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

        public FieldFactory setFinal() {
            checkState();
            access += Opcodes.ACC_FINAL;
            return this;
        }

        public FieldFactory setInitialValue(Object value) {
            checkState();
            // Check type
            initialValue = value;
            return this;
        }

        public FieldFactory setPrivate() {
            checkState();
            access += Opcodes.ACC_PUBLIC;
            return this;
        }

        public FieldFactory setSignature(String signature) {
            checkState();
            this.signature = signature;
            return this;
        }

        public FieldFactory setStatic() {
            checkState();
            access += Opcodes.ACC_STATIC;
            return this;
        }

        public FieldFactory setVolatile() {
            checkState();
            access += Opcodes.ACC_VOLATILE;
            return this;
        }
    }

    public class MethodFactory extends AbstractMemberFactory<FieldFactory> {
        String descriptor;
        Object initialValue;
        Type returnType = Type.VOID_TYPE;
        String signature;

        MethodFactory(String name, int access) {
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

        public MethodFactory setReturnType(Class<?> type) {
            return setReturnType(type(type));
        }

        public MethodFactory setReturnType(Type type) {
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
}
