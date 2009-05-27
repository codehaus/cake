package org.codehaus.cake.internal.cache.memorystore.openadressing;

import static org.codehaus.cake.internal.codegen.ClassDefiner.from;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.WeakHashMap;

import org.codehaus.cake.internal.asm.Label;
import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.cache.memorystore.attribute.CacheFieldDefinition;
import org.codehaus.cake.internal.cache.memorystore.attribute.CachePolicyContext;
import org.codehaus.cake.internal.cache.memorystore.attribute.CacheFieldDefinition.AccessAction;
import org.codehaus.cake.internal.cache.memorystore.attribute.CacheFieldDefinition.CreateAction;
import org.codehaus.cake.internal.cache.memorystore.attribute.CacheFieldDefinition.ModifyAction;
import org.codehaus.cake.internal.codegen.ClassDefiner;
import org.codehaus.cake.internal.codegen.ClassEmitter;
import org.codehaus.cake.internal.codegen.Method;
import org.codehaus.cake.internal.codegen.StaticInitializer;
import org.codehaus.cake.internal.codegen.attribute.AttributeMapDecoratoredEmitter;
import org.codehaus.cake.internal.codegen.attribute.FieldDefinition;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.ObjectAttribute;

public class EnhancedOpenAdressingEntryEmitter extends ClassEmitter {

    private static final java.lang.reflect.Method ACCESS = from(EnhancedOpenAdressingEntryFactory.class, "access");
    private static final java.lang.reflect.Method CREATE = from(EnhancedOpenAdressingEntryFactory.class, "create");
    static final WeakHashMap<Class<?>, Attribute<?>[]> initializers = new WeakHashMap<Class<?>, Attribute<?>[]>();
    private static final java.lang.reflect.Method UPDATE = from(EnhancedOpenAdressingEntryFactory.class, "update");

    private final Class<?>[] arguments;

    private final CacheFieldDefinition<?>[] cacheDefs;

    private final CachePolicyContext<?, ?> context;
    private int createReaders;
    private int createReadFromAttributeMapAndTakeTime;

    private Class<?> entryClass;
    private int modifyReadFromAttributeMapAndTakeTime;
    private final String name;

    public EnhancedOpenAdressingEntryEmitter(String name, Class entryClass, CachePolicyContext<?, ?> policyContext) {
        this.name = name;
        this.entryClass = entryClass;
        this.context = policyContext;
        List<CacheFieldDefinition> cacheDefs = new ArrayList<CacheFieldDefinition>();
        for (FieldDefinition def : context.getFields()) {
            if (def instanceof CacheFieldDefinition) {
                cacheDefs.add((CacheFieldDefinition) def);
            }
        }
        this.cacheDefs = cacheDefs.toArray(new CacheFieldDefinition[0]);

        List<Class<?>> constructor = new ArrayList<Class<?>>(Arrays.<Class<?>> asList(Object.class, Integer.TYPE,
                Object.class));
        for (CacheFieldDefinition<?> def : cacheDefs) {
            constructor.add(def.getAttribute().getType());
            if (def.isReadMapOnCreate() && def.getCreateAction() == CreateAction.TIMESTAMP) {
                createReadFromAttributeMapAndTakeTime++;
            }
            if (def.isReadMapOnModify() && def.getModifyAction() == ModifyAction.TIMESTAMP) {
                modifyReadFromAttributeMapAndTakeTime++;
            }
        }
        arguments = constructor.toArray(new Class[constructor.size()]);
    }

    void addAccess() {
        Method m = withMethodImplement(ACCESS);
        m.loadArg(0).checkCast(entryClass).storeLocal(2, type(entryClass));
        CacheFieldDefinition timeIndex = null;
        for (CacheFieldDefinition<?> def : cacheDefs) {
            if (def.getAccessAction() != AccessAction.NOTHING) {
                if (def.getAccessAction() == AccessAction.TIMESTAMP) {
                    m.loadLocal(2);
                    invokeClock(m);
                    m.adaptor.visitFieldInsn(Opcodes.PUTFIELD, type(entryClass).getInternalName(), def.getName(), def
                            .getTType().getDescriptor());
                } else {
                    m.loadLocal(2).dup();
                    m.adaptor.getField(type(entryClass), def.getName(), def.getTType());
                    m.adaptor.visitInsn(Opcodes.LCONST_1);
                    m.adaptor.visitInsn(Opcodes.LADD);
                    m.adaptor.visitFieldInsn(Opcodes.PUTFIELD, type(entryClass).getInternalName(), def.getName(), def
                            .getTType().getDescriptor());
                }
            }
        }
    }

    void addCreate() {
        addCreateNoParams();

        Method m = withMethodImplement(CREATE);
        m.loadArg(3).invokeAndStore(AttributeMap.class, "size", m.adaptor.newLocal(Type.INT_TYPE));

        Label hasAttributes = new Label();

        m.loadLocal(5);
        m.getAdapter().visitJumpInsn(Opcodes.IFNE, hasAttributes);
        m.loadThis().loadArgs(0, 1, 2);
        m.invokeSpecial("create", OpenAdressingEntry.class, Object.class, Integer.TYPE, Object.class).returnValue();
        m.visitLabel(hasAttributes);

        LinkedHashSet<CacheFieldDefinition<?>> defs = new LinkedHashSet<CacheFieldDefinition<?>>(Arrays
                .asList(cacheDefs));
        LinkedHashMap<CacheFieldDefinition<?>, Integer> map = new LinkedHashMap<CacheFieldDefinition<?>, Integer>();
        int timeIndex = -1;
        int index = 4;
        for (Iterator<CacheFieldDefinition<?>> iterator = defs.iterator(); iterator.hasNext();) {
            CacheFieldDefinition<?> def = iterator.next();
            if (!def.isReadMapOnCreate()) {
                // Okay, no reading of attributes for this
                iterator.remove();
                index = m.adaptor.newLocal(def.getTType());
                map.put(def, index);
                timeIndex = onCreate(m, def, timeIndex, index);
            }
        }
        // Default values;
        for (Iterator<CacheFieldDefinition<?>> iterator = defs.iterator(); iterator.hasNext();) {
            CacheFieldDefinition<?> def = iterator.next();
            if (def.getCreateAction() != CreateAction.TIMESTAMP || timeIndex > -1
                    || createReadFromAttributeMapAndTakeTime < 2) {
                iterator.remove();
                index = m.getAdapter().newLocal(def.getTType());

                map.put(def, index);
                Label next = new Label();
                m.loadArg(3).getStatic(atrName(def)).invoke(AttributeMap.class, "contains");
                m.getAdapter().visitJumpInsn(Opcodes.IFEQ, next);
                getFromAttributeMap(def, m);
                m.storeLocal(index, def.getTType());
                Label nextnext = new Label();
                m.getAdapter().goTo(nextnext);
                m.visitLabel(next);
                timeIndex = onCreate(m, def, timeIndex, index);
                m.visitLabel(nextnext);
            }
        }

        // Initializes all timestamps
        if (defs.size() > 1) {
            Label end = new Label();
            int[] indexs = new int[defs.size()];
            CacheFieldDefinition<?>[] a = defs.toArray(new CacheFieldDefinition<?>[defs.size()]);
            for (int i = 0; i < defs.size(); i++) {
                indexs[i] = m.getAdapter().newLocal(a[i].getTType());
                map.put(a[i], indexs[i]);
            }
            clock(m, end, 0, indexs, a);
            m.visitLabel(end);
        }

        m.newInstance(entryClass).dup();
        m.loadArgs(0, 1, 2);
        for (CacheFieldDefinition<?> def : cacheDefs) {
            m.adaptor.loadLocal(map.get(def));
        }
        m.invokeConstructor(entryClass, arguments);
    }

    void addCreateNoParams() {
        Method m = withMethodPrivate("create").setReturnType(OpenAdressingEntry.class).create(Object.class,
                Integer.TYPE, Object.class);
        int timeIndex = -1;
        int index = 4;
        for (CacheFieldDefinition<?> def : cacheDefs) {
            index = m.adaptor.newLocal(def.getTType());
            timeIndex = onCreate(m, def, timeIndex, index);
        }
        m.newInstance(entryClass).dup();
        m.loadArgs(0, 1, 2);
        index = 4;
        for (CacheFieldDefinition<?> def : cacheDefs) {
            m.adaptor.loadLocal(index);
            index += def.getTType().getSize();
        }
        m.invokeConstructor(entryClass, arguments);
    }

    void addUpdate() {
        addUpdateNoParams();

        Method m = withMethodImplement(UPDATE);
        int sizeIndex = m.adaptor.newLocal(Type.INT_TYPE);
        int entryIndex = m.adaptor.newLocal(type(entryClass));
        m.loadArg(4).checkCast(entryClass).storeLocal(entryIndex, type(entryClass));
        m.loadArg(3).invokeAndStore(AttributeMap.class, "size", sizeIndex);

        Label hasAttributes = new Label();
        m.loadLocal(sizeIndex);
        m.getAdapter().visitJumpInsn(Opcodes.IFNE, hasAttributes);
        m.loadThis().loadArgs(0, 1, 2).loadLocal(entryIndex);
        m.invokeSpecial("update", OpenAdressingEntry.class, Object.class, Integer.TYPE, Object.class, entryClass)
                .returnValue();
        m.visitLabel(hasAttributes);

        LinkedHashSet<CacheFieldDefinition<?>> defs = new LinkedHashSet<CacheFieldDefinition<?>>(Arrays
                .asList(cacheDefs));
        LinkedHashMap<CacheFieldDefinition<?>, Integer> map = new LinkedHashMap<CacheFieldDefinition<?>, Integer>();
        int timeIndex = -1;
        int index = 4;
        for (Iterator<CacheFieldDefinition<?>> iterator = defs.iterator(); iterator.hasNext();) {
            CacheFieldDefinition<?> def = iterator.next();
            if (!def.isReadMapOnModify()) {
                // Okay, no reading of attributes for this
                iterator.remove();
                index = m.adaptor.newLocal(def.getTType());
                map.put(def, index);
                timeIndex = onModify(m, entryIndex, def, timeIndex, index);
            }
        }
        // Default values;
        for (Iterator<CacheFieldDefinition<?>> iterator = defs.iterator(); iterator.hasNext();) {
            CacheFieldDefinition<?> def = iterator.next();
            if (def.getModifyAction() != ModifyAction.TIMESTAMP || timeIndex > -1
                    || modifyReadFromAttributeMapAndTakeTime < 2) {
                iterator.remove();
                index = m.getAdapter().newLocal(def.getTType());

                map.put(def, index);
                Label next = new Label();
                m.loadArg(3).getStatic(atrName(def)).invoke(AttributeMap.class, "contains");
                m.getAdapter().visitJumpInsn(Opcodes.IFEQ, next);
                getFromAttributeMap(def, m);
                m.storeLocal(index, def.getTType());
                Label nextnext = new Label();
                m.getAdapter().goTo(nextnext);
                m.visitLabel(next);
                timeIndex = onModify(m, entryIndex, def, timeIndex, index);
                m.visitLabel(nextnext);
            }
        }

        // Initializes all timestamps
        if (defs.size() > 1) {
            Label end = new Label();
            int[] indexs = new int[defs.size()];
            CacheFieldDefinition<?>[] a = defs.toArray(new CacheFieldDefinition<?>[defs.size()]);
            for (int i = 0; i < defs.size(); i++) {
                indexs[i] = m.getAdapter().newLocal(a[i].getTType());
                map.put(a[i], indexs[i]);
            }
            clock(m, end, 0, indexs, a);
            m.visitLabel(end);
        }

        m.newInstance(entryClass).dup();
        m.loadArgs(0, 1, 2);
        for (CacheFieldDefinition<?> def : cacheDefs) {
            m.adaptor.loadLocal(map.get(def));
        }
        m.invokeConstructor(entryClass, arguments);
    }

    void addUpdateNoParams() {
        Method m = withMethodPrivate("update").setReturnType(OpenAdressingEntry.class).create(Object.class,
                Integer.TYPE, Object.class, entryClass);
        int timeIndex = -1;
        int index = 5;
        for (CacheFieldDefinition<?> def : cacheDefs) {
            index = m.adaptor.newLocal(def.getTType());
            timeIndex = onModify(m, -1, def, timeIndex, index);
        }
        m.newInstance(entryClass).dup();
        m.loadArgs(0, 1, 2);
        index = 5;
        for (CacheFieldDefinition<?> def : cacheDefs) {
            m.adaptor.loadLocal(index);
            index += def.getTType().getSize();
        }
        m.invokeConstructor(entryClass, arguments);
    }

    void clock(Method m, Label end, int i, int[] indexs, CacheFieldDefinition<?>[] a) {
        Label next = new Label();
        m.loadArg(3).getStatic(atrName(a[i])).invoke(AttributeMap.class, "contains");
        m.getAdapter().visitJumpInsn(Opcodes.IFEQ, next);// if contains attribute jump
        getFromAttributeMap(a[i], m);
        m.storeLocal(indexs[i], a[i].getTType());
        if (i + 1 < indexs.length) {
            clock(m, end, i + 1, indexs, a);
        }
        m.visitLabel(next);
        invokeClock(m).storeLocal(indexs[i], a[i].getTType()); // read clock once
        for (int j = i + 1; j < a.length; j++) {
            m.loadArg(3).getStatic(atrName(a[j])).invoke(AttributeMap.class, "contains");
            Label n = new Label();
            Label n1 = new Label();
            if (j + 1 == a.length) {
                n1 = end;
            }
            m.getAdapter().visitJumpInsn(Opcodes.IFEQ, n);
            getFromAttributeMap(a[j], m);
            m.storeLocal(indexs[j], a[j].getTType());
            m.getAdapter().goTo(n1);
            m.visitLabel(n);
            // assign from previous index
            m.loadLocal(indexs[i]).storeLocal(indexs[j], a[j].getTType());
            if (j + 1 < a.length) {
                m.visitLabel(n1);
            }
        }
        if (i != 0) {
            m.getAdapter().goTo(end);
        }

    }

    @Override
    public void define() {
        withClass().setPublic().setSuper(Object.class).addInterfaces(OpenAdressingEntryFactory.class).create(name);

        withField("ies").setFinal().setPrivate().create(InternalExceptionService.class);
        withField("clock").setFinal().setPrivate().create(Clock.class);

        initializeAttributes();

        withConstructor().setPublic().create(InternalExceptionService.class, Clock.class).invokeEmptySuper()
                .putFieldArg("ies", 0).putFieldArg("clock", 1);

        addCreate();
        addUpdate();
        addAccess();
    }

    Class getAClass(Class c) {
        while (c.getSuperclass() != Attribute.class) {
            c = c.getSuperclass();
        }
        return c;
    }

    private void getFromAttributeMap(CacheFieldDefinition<?> def, Method m) {
        if (getAClass(def.getAttribute().getClass()).equals(ObjectAttribute.class)) {
            m.loadArg(3).getStatic(atrName(def)).invoke(AttributeMap.class, "get", Attribute.class);
            m.checkCast(def.getType());
        } else {
            m.loadArg(3).getStatic(atrName(def)).invoke(AttributeMap.class, "get",
                    getAClass(def.getAttribute().getClass()));
        }
    }

    void initializeAttributes() {
        for (CacheFieldDefinition def : cacheDefs) {
            withField(atrName(def)).setPrivate().setStatic().setFinal()
                    .create(getAClass(def.getAttribute().getClass()));
            withField(atrDefault(def)).setPrivate().setStatic().setFinal().create(def.getAttribute().getType());

        }
        // Static constructor
        Type aarray = Type.getType(new Attribute[0].getClass());
        StaticInitializer si = withStaticInitializer();
        si.pushConst(getType());
        si.invokeStatic(EnhancedOpenAdressingEntryEmitter.class, "remove", aarray, type(Class.class));
        si.storeLocal(0, aarray);

        int count = 0;
        for (CacheFieldDefinition def : cacheDefs) {
            si.arrayLoadLocal(0, count++);
            si.checkCast(getAClass(def.getAttribute().getClass())).putStatic(atrName(def));
            // Default values;
            si.getStatic(atrName(def));
            if (def.getAttribute() instanceof ObjectAttribute) {
                si.invokeVirtual(ObjectAttribute.class, "getDefault", Object.class);
                si.checkCast(def.getAttribute().getType());
            } else {
                si.invokeVirtual(getAClass(def.getAttribute().getClass()), "getDefaultValue", def.getAttribute()
                        .getType());
            }
            si.putStatic(atrDefault(def));
        }
    }

    int onCreate(Method m, CacheFieldDefinition<?> def, int clockIndex, int index) {
        switch (def.getCreateAction()) {
        case SET_VALUE:
            m.pushConst(def.getInitialValue());
            break;
        case DEFAULT:
            m.getStatic(atrDefault(def));
            break;
        case TIMESTAMP:
            if (clockIndex == -1) {
                invokeClock(m);
                clockIndex = index;
            } else {
                m.loadLocal(clockIndex);
            }
        }
        m.storeLocal(index, def.getTType());
        return clockIndex;
    }

    int onModify(Method m, int entryIndex, CacheFieldDefinition<?> def, int clockIndex, int index) {
        switch (def.getModifyAction()) {
        case INCREMENT:
            if (entryIndex > 0) {
                m.loadLocal(entryIndex);
            } else {
                m.loadArg(3);
            }
            m.adaptor.getField(type(entryClass), def.getName(), def.getTType());
            m.adaptor.visitInsn(Opcodes.LCONST_1);
            m.adaptor.visitInsn(Opcodes.LADD);
            break;
        case DEFAULT:
            m.getStatic(atrDefault(def));
            break;
        case TIMESTAMP:
            if (clockIndex == -1) {
                invokeClock(m);
                clockIndex = index;
            } else {
                m.loadLocal(clockIndex);
            }
            break;
        case KEEP_EXISTING:
            if (entryIndex > 0) {
                m.loadLocal(entryIndex);
            } else {
                m.loadArg(3);
            }
            m.adaptor.getField(type(entryClass), def.getName(), def.getTType());
            break;
        }
        m.storeLocal(index, def.getTType());
        return clockIndex;
    }

    private static String atrDefault(FieldDefinition conf) {
        return conf.getName() + "_ATR_DEFAULT";
    }

    private static String atrName(FieldDefinition conf) {
        return conf.getName() + "_ATR";
    }

    static <K, V> OpenAdressingEntryFactory<K, V> create(ClassDefiner definer, CachePolicyContext<K, V> policyContext,
            InternalExceptionService<?> exceptionService, Clock clock) {
        Package p = EnhancedOpenAdressingEntryFactory.class.getPackage();
        Class<?> entryClass = AttributeMapDecoratoredEmitter.create(definer, OpenAdressingEntry.class, p, "Entry",
                policyContext.getFields());

        EnhancedOpenAdressingEntryEmitter emitter = new EnhancedOpenAdressingEntryEmitter(definer.createClassName(p,
                "EntryFactory"), entryClass, policyContext);
        List<Attribute> attributes = new ArrayList<Attribute>();
        for (FieldDefinition def : policyContext.getFields()) {
            if (def instanceof CacheFieldDefinition) {
                attributes.add(((CacheFieldDefinition) def).getAttribute());
            }
        }
        Class<?> factoryClass = definer.define(emitter);
        initializers.put(factoryClass, attributes.toArray(new Attribute[attributes.size()]));
        return definer.instantiate(factoryClass, exceptionService, clock);
        //TODO should we assert that class has been removed from initializers??
        
    }

    static Method invokeClock(Method m) {
        m.loadThis().getStatic("clock").invokeVirtual(Clock.class, "timeOfDay", Long.TYPE);
        return m;
    }

    public static Attribute<?>[] remove(Class<?> clazz) {
        Attribute<?>[] a = initializers.remove(clazz);
        return a;
    }
}
