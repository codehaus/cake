package org.codehaus.cake.internal.codegen.attribute;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.codehaus.cake.internal.asm.Label;
import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.codegen.ClassDefiner;
import org.codehaus.cake.internal.codegen.ClassEmitter;
import org.codehaus.cake.internal.codegen.Method;
import org.codehaus.cake.internal.codegen.StaticInitializer;
import org.codehaus.cake.internal.util.attribute.AttributeHelper;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.BooleanAttribute;
import org.codehaus.cake.util.attribute.ByteAttribute;
import org.codehaus.cake.util.attribute.CharAttribute;
import org.codehaus.cake.util.attribute.DoubleAttribute;
import org.codehaus.cake.util.attribute.FloatAttribute;
import org.codehaus.cake.util.attribute.IntAttribute;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.codehaus.cake.util.attribute.ShortAttribute;

public class AttributeDecorator extends ClassEmitter {

    static final WeakHashMap<Class<?>, Attribute<?>[]> initializers = new WeakHashMap<Class<?>, Attribute<?>[]>();
    private final static String KEYSET_NAME = "keyset_ATR";
    private static final String READ_CONTAINS;
    private static final String READ_GET;
    private static final String READ_IS_EMPTY;
    private static final String READ_SIZE;
    private static final String READ_VIEW_ATTRIBUTES;
    private static final String READ_VIEW_ENTRIES;
    private static final String READ_VIEW_VALUES;

    static {
        Class<?> READ_MAP = AttributeMap.class;
        try {
            READ_GET = READ_MAP.getMethod("get", Attribute.class).getName();
            READ_CONTAINS = READ_MAP.getMethod("contains", Attribute.class).getName();
            READ_IS_EMPTY = READ_MAP.getMethod("isEmpty").getName();
            READ_SIZE = READ_MAP.getMethod("size").getName();
            READ_VIEW_ATTRIBUTES = READ_MAP.getMethod("attributes").getName();
            READ_VIEW_ENTRIES = READ_MAP.getMethod("entrySet").getName();
            READ_VIEW_VALUES = READ_MAP.getMethod("values").getName();
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }
    private final Map<Attribute, FieldDefinition> attributes = new LinkedHashMap<Attribute, FieldDefinition>();
    private final Class<? extends AttributeMap> classToExtend;
    private final List<FieldDefinition> fields;
    private final String name;

    private int visibleAttributeCount = 0;

    public AttributeDecorator(String name, Class<? extends AttributeMap> classToExtend, List<FieldDefinition> fields) {
        this.name = name;
        this.classToExtend = classToExtend;
        this.fields = fields;

        for (FieldDefinition f : fields) {
            if (f.getAttribute() != null) {
                attributes.put(f.getAttribute(), f);
            }
        }
    }

    private void checkGet(Method m, FieldDefinition f) {
        Label label = new Label();
        m.loadArg(0);
        m.getStatic(atrName(f));
        m.jumpIfNotEqual(Attribute.class, label); // jump if equal
        m.loadThis();
        m.getStatic(f.getName());
        if (!isPrimitive(m.getReturnType())) {
            m.box(f.getType());
        }
        m.returnValue();
        m.visitLabel(label);
    }

    private void constructors() {
        // Constructor(
        // We create a new constructor for each of the existing constructors
        for (Constructor con : classToExtend.getConstructors()) {
            int count = con.getParameterTypes().length;
            List<Class> parameters = new ArrayList<Class>(Arrays.asList(con.getParameterTypes()));
            for (FieldDefinition f : fields) {
                parameters.add(f.getType());
            }
            // We delegate the first x parameters to the super constructor
            org.codehaus.cake.internal.codegen.Constructor c = withConstructor().setPublic().create(
                    parameters.toArray(new Class[0])).loadAndInvokeSuperWithArgs(con.getParameterTypes().length);
            // Create ikke delegating med alle
            // assign parameters
            int i = 0;
            for (FieldDefinition f : fields) {
                c.putFieldArg(f.getName(), count + i++);
            }
        }
        Type aarray = Type.getType(new Attribute[0].getClass());

        // Static constructor
        StaticInitializer si = withStaticInitializer();
        si.pushConst(getType());
        si.invokeStatic(AttributeDecorator.class, "remove", aarray, type(Class.class));
        si.storeLocal(0, aarray);

        // Create keyset
        si.newInstance(HashSet.class);
        si.dup();
        si.loadLocal(0);
        si.invokeStatic(Arrays.class, "asList", List.class, (new Object[0]).getClass());
        si.checkCast(Collection.class);
        si.invokeConstructor(HashSet.class, Collection.class);
        si.invokeStatic(Collections.class, "unmodifiableSet", Set.class, Set.class);
        si.putStatic(KEYSET_NAME);

        int count = 0;
        for (Map.Entry<Attribute, FieldDefinition> m : attributes.entrySet()) {
            si.arrayLoadLocal(0, count++);
            si.checkCast(getAClass(m.getKey().getClass())).putStatic(atrName(m.getValue()));
        }
    }

    @Override
    public void define() {
        withClass().setPublic().setSuper(classToExtend).create(name);

        fields();
        constructors();
        methods();
        getters();
    }

    private void fields() {
        // A set of all attributes, as returned by attributeSet();
        withField(KEYSET_NAME).setPrivate().setStatic().setFinal().create(Set.class);
        for (FieldDefinition f : fields) {
            // Set private, volatile
            withField(f.getName()).setFinal(f.isFinal()).create(f.getType());
            if (f.getAttribute() != null) {
                withField(atrName(f)).setPrivate().setStatic().setFinal()
                        .create(getAClass(f.getAttribute().getClass()));
                visibleAttributeCount++;
            }
        }
    }

    private void get(Class<? extends Attribute> attributeType, Class number) {
        // Get with no default;
        Method m = withMethodPublic(READ_GET).setReturnType(number).create(attributeType);
        for (FieldDefinition f : attributes.values()) {
            if (f.getType() == number) {
                checkGet(m, f);
            }
        }
        m.loadArg(0).invokeVirtual(attributeType, "getDefaultValue", number);

        // Get with default
        m = withMethodPublic(READ_GET).setReturnType(number).create(attributeType, number);
        for (FieldDefinition f : attributes.values()) {
            if (f.getType() == number) {
                checkGet(m, f);
            }
        }
        m.loadArg(1);
    }

    private void getters() {
        // Primitive getter
        get(BooleanAttribute.class, Boolean.TYPE);
        get(ByteAttribute.class, Byte.TYPE);
        get(CharAttribute.class, Character.TYPE);
        get(DoubleAttribute.class, Double.TYPE);
        get(FloatAttribute.class, Float.TYPE);
        get(IntAttribute.class, Integer.TYPE);
        get(LongAttribute.class, Long.TYPE);
        get(ShortAttribute.class, Short.TYPE);

        // Object getters
        Method m = withMethodPublic(READ_GET).setReturnType(Object.class).create(Attribute.class);
        // First we find all non primitives
        for (FieldDefinition f : attributes.values()) {
            if (!f.getType().isPrimitive()) {
                checkGet(m, f);
            }
        }
        // Then all primitive attributes
        for (FieldDefinition f : attributes.values()) {
            if (f.getType().isPrimitive()) {
                checkGet(m, f);
            }
        }
        m.loadArg(0).invokeVirtual(Attribute.class, "getDefault", Object.class);

        // Object getters
        m = withMethodPublic(READ_GET).setReturnType(Object.class).create(Attribute.class, Object.class);
        // First we find all non primitives
        for (FieldDefinition f : attributes.values()) {
            if (!f.getType().isPrimitive()) {
                checkGet(m, f);
            }
        }
        // Then all primitive attributes
        for (FieldDefinition f : attributes.values()) {
            if (f.getType().isPrimitive()) {
                checkGet(m, f);
            }
        }
        m.loadArg(1);
    }

    private void methods() {
        // Contains
        Method m = withMethodPublic(READ_CONTAINS).setReturnType(Boolean.TYPE).create(Attribute.class);
        Label l = new Label();
        for (FieldDefinition f : attributes.values()) {
            m.loadArg(0);
            m.getStatic(atrName(f));
            m.jumpIfEqual(Attribute.class, l); // jump if equal
        }
        m.returnValue(false);
        if (attributes.size() > 0) {
            m.visitLabel(l);
            m.returnValue(true);
        }

        // isEmpty
        m = withMethodPublic(READ_IS_EMPTY).setReturnType(Boolean.TYPE).create();
        m.returnValue(attributes.size() == 0);

        // Size
        m = withMethodPublic(READ_SIZE).setReturnType(Integer.TYPE).create();
        m.returnValue(attributes.size());

        // Attributes

        // Keys
        m = withMethodPublic(READ_VIEW_ATTRIBUTES).setReturnType(Set.class).create();
        m.getStatic(KEYSET_NAME);

        // Values
        m = withMethodPublic(READ_VIEW_VALUES).setReturnType(Collection.class).create();
        m.newArrays(Object.class, attributes.size());
        int count = 0;
        for (FieldDefinition fc : attributes.values()) {
            m.dup();
            m.pushConst(count++);
            m.loadThis();
            m.getStatic(fc.getName());
            if (isPrimitive(type(fc.getType()))) {
                m.box(fc.getType());
            }
            m.storeArray(Object.class);
        }
        m.invokeStatic(Arrays.class, "asList", List.class, (new Object[0]).getClass());
        m.checkCast(Collection.class);
        m.invokeStatic(Collections.class, "unmodifiableCollection", Collection.class, Collection.class);

        // Entries
        Type aarray = Type.getType(new Attribute[0].getClass());
        m = withMethodPublic(READ_VIEW_ENTRIES).setReturnType(Set.class).create();
        m.invokeInstanceAndStore(HashSet.class, 1);

        count = 0;
        for (FieldDefinition fc : attributes.values()) {
            m.loadLocal(1);
            m.newInstance(AttributeHelper.SimpleImmutableEntry.class);
            m.dup();
            m.getStatic(atrName(fc));
            m.loadThis();
            m.getStatic(fc.getName());
            if (isPrimitive(type(fc.getType()))) {
                m.box(fc.getType());
            }
            m.invokeConstructor(AttributeHelper.SimpleImmutableEntry.class, Object.class, Object.class);
            m.invokeVirtual(HashSet.class, "add", Boolean.TYPE, Object.class);
            m.pop();

        }
        m.loadLocal(1);
        m.invokeStatic(Collections.class, "unmodifiableSet", Set.class, Set.class);

    }

    private static String atrName(FieldDefinition conf) {
        return conf.getName() + "_ATR";
    }

    public static Class<AttributeMap> decorate(ClassDefiner factory, String name, Class<? extends AttributeMap> clazz,
            List<FieldDefinition> fields) {
        AttributeDecorator decorator = new AttributeDecorator(name, clazz, fields);
        Class<AttributeMap> c = (Class<AttributeMap>) factory.define(decorator);
        initializers.put(c, decorator.attributes.keySet().toArray(new Attribute[0]));
        return c;
    }

    public static Attribute<?>[] remove(Class<?> clazz) {
        Attribute<?>[] a = initializers.remove(clazz);
        return a;
    }

    public Class getAClass(Class c) {
        while (c.getSuperclass() != Attribute.class) {
            c = c.getSuperclass();
        }
        return c;
    }
}
