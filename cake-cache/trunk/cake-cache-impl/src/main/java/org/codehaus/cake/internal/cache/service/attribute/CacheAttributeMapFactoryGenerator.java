/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.internal.cache.service.attribute;

import static org.codehaus.cake.internal.asm.Type.getMethodDescriptor;
import static org.codehaus.cake.internal.asm.Type.getType;

import java.lang.reflect.Constructor;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.WeakHashMap;

import org.codehaus.cake.internal.asm.ClassVisitor;
import org.codehaus.cake.internal.asm.ClassWriter;
import org.codehaus.cake.internal.asm.Label;
import org.codehaus.cake.internal.asm.MethodVisitor;
import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;
import org.codehaus.cake.internal.attribute.generator.DefaultMapGenerator;
import org.codehaus.cake.internal.attribute.generator.PrimType;
import org.codehaus.cake.internal.attribute.generator.DefaultMapGenerator.MyLoader;
import org.codehaus.cake.internal.cache.service.attribute.CacheAttributeMapConfiguration.AccessAction;
import org.codehaus.cake.internal.cache.service.attribute.CacheAttributeMapConfiguration.CreateAction;
import org.codehaus.cake.internal.cache.service.attribute.CacheAttributeMapConfiguration.ModifyAction;
import org.codehaus.cake.internal.service.exceptionhandling.InternalExceptionService;
import org.codehaus.cake.internal.util.attribute.SecurityTools;
import org.codehaus.cake.util.Clock;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.attribute.MutableAttributeMap;

public class CacheAttributeMapFactoryGenerator implements Opcodes {
    private static final String ATTRIBUTEMAP_DESCRIPTOR = Type.getType(MutableAttributeMap.class).getDescriptor();
    private static final String GET_ATTRIBUTER_DESCRIPTOR = Type.getType(AttributeMap.class).getDescriptor();
    private static final String ATTRIBUTE_DESCRIPTOR = Type.getType(Attribute.class).getDescriptor();
    private static final String CLOCK_DESCRIPTOR = Type.getType(Clock.class).getDescriptor();
    private static final String IES_DESCRIPTOR = Type.getType(InternalExceptionService.class).getDescriptor();
    static final WeakHashMap<Class<?>, Attribute<?>[]> initializers = new WeakHashMap<Class<?>, Attribute<?>[]>();
    private static final String INTERFACE_DESCRIPTOR = Type.getType(CacheAttributeMapFactory.class).getDescriptor();
    private static final String INTERFACE_INTERNAL_NAME = Type.getType(CacheAttributeMapFactory.class)
            .getInternalName();
    private static final Object lock = new Object();
    private final String classDescriptor;
    private final String classDescriptorMap;
    ClassVisitor cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

    static final CacheAttributeMapFactory NO_ATTRIBUTES_FACTORY = new NoAttributes();
    private final Info[] info;

    CacheAttributeMapFactoryGenerator(String classDescriptor, List<? extends CacheAttributeMapConfiguration> infos) {
        this.classDescriptor = classDescriptor;
        this.classDescriptorMap = classDescriptor + "Map";
        info = new Info[infos.size()];
        for (int i = 0; i < info.length; i++) {
            info[i] = new Info(i, infos.get(i));
        }
    }

    void _constructor() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + CLOCK_DESCRIPTOR + IES_DESCRIPTOR + ")V", null,
                null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, classDescriptor, "clock", CLOCK_DESCRIPTOR);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitFieldInsn(PUTFIELD, classDescriptor, "ies", IES_DESCRIPTOR);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 3);
        mv.visitEnd();
    }

    void _fields() {
        cw.visitField(ACC_PRIVATE + ACC_FINAL, "ies", Type.getType(InternalExceptionService.class).getDescriptor(),
                null, null).visitEnd();
        cw.visitField(ACC_PRIVATE + ACC_FINAL, "clock", Type.getType(Clock.class).getDescriptor(), null, null)
                .visitEnd();
    }

    void _static_fields() {
        // add static fields, each containing an attribute
        for (Info i : info) {
            cw.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, i.getFieldStaticName(), i.attributeDescriptor, null,
                    null).visitEnd();
        }
    }

    void _static_init() {
        MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
        mv.visitCode();
        mv.visitLdcInsn(Type.getType("L" + classDescriptor + ";"));
        mv.visitMethodInsn(INVOKESTATIC, getType(CacheAttributeMapFactoryGenerator.class).getInternalName(), "init",
                getMethodDescriptor(getType(Attribute[].class), new Type[] { getType(Class.class) }));
        mv.visitVarInsn(ASTORE, 0);
        for (Info i : info) {
            mv.visitVarInsn(ALOAD, 0);
            mv.visitIntInsn(BIPUSH, i.index);
            mv.visitInsn(AALOAD);
            mv.visitTypeInsn(CHECKCAST, i.vType.getType().getInternalName());
            mv.visitFieldInsn(PUTSTATIC, classDescriptor, i.getFieldStaticName(), i.attributeDescriptor);
        }
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
    }

    private void addCreate(MethodVisitor mv, int startIndex, boolean checkMap, boolean isCreate) {
        int index = startIndex;
        mv.visitCode();
        Label l0 = new Label();
        int timeStampIndex = 0;// used for only calculating timestamp once
        for (Info info : this.info) {
            mv.visitLabel(l0);
            l0 = new Label();
            if (checkMap
                    && ((isCreate && info.impl.isReadMapOnCreate()) || (!isCreate && info.impl.isReadMapOnModify()))) {
                mv.visitVarInsn(ALOAD, 3);
                info.visitStaticGet(mv);
                mv.visitMethodInsn(INVOKEINTERFACE, "org/codehaus/cake/attribute/AttributeMap", "contains",
                        "(Lorg/codehaus/cake/attribute/Attribute;)Z");
                Label lN = new Label();
                mv.visitJumpInsn(IFEQ, lN);

                mv.visitVarInsn(ALOAD, 3);
                info.visitStaticGet(mv);
                if (info.vType == PrimType.OBJECT) {
                    mv.visitMethodInsn(INVOKEINTERFACE, "org/codehaus/cake/attribute/AttributeMap", "get",
                            "(Lorg/codehaus/cake/attribute/Attribute;)Ljava/lang/Object;");
                    mv.visitTypeInsn(CHECKCAST, info.type.getInternalName());
                } else {
                    mv.visitMethodInsn(INVOKEINTERFACE, "org/codehaus/cake/attribute/AttributeMap", "get", "("
                            + info.attributeDescriptor + ")" + info.descriptor);
                }
                mv.visitVarInsn(info.vType.storeCode(), index);

                info.visitStaticGet(mv);
                mv.visitVarInsn(info.vType.loadCode(), index);
                mv.visitMethodInsn(INVOKEVIRTUAL, info.vType.getType().getInternalName(), "isValid", "("
                        + info.descriptor + ")Z");
                mv.visitJumpInsn(IFNE, l0);

                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, classDescriptor, "ies",
                        "Lorg/codehaus/cake/internal/service/exceptionhandling/InternalExceptionService;");
                mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
                mv.visitInsn(DUP);
                mv.visitLdcInsn("Illegal Value for attribute ");
                mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
                mv.visitVarInsn(ALOAD, 1);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                        "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
                mv.visitLdcInsn("[value=");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
                mv.visitVarInsn(info.vType.loadCode(), index);
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(" + info.descriptor
                        + ")Ljava/lang/StringBuilder;");
                mv.visitLdcInsn("]");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
                mv.visitMethodInsn(INVOKEINTERFACE,
                        "org/codehaus/cake/internal/service/exceptionhandling/InternalExceptionService", "warning",
                        "(Ljava/lang/String;)V");

                // mv.visitJumpInsn(GOTO, l0);

                // normal action
                mv.visitLabel(lN);
            }
            if ((isCreate && info.impl.getCreateAction() == CreateAction.TIMESTAMP)
                    || (!isCreate && info.impl.getModifyAction() == ModifyAction.TIMESTAMP)) {
                if (timeStampIndex == 0) {
                    readClock(mv, index);
                    info.visitStaticGet(mv);
                    mv.visitVarInsn(LLOAD, index);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "org/codehaus/cake/attribute/LongAttribute", "isValid", "(J)Z");
                    mv.visitJumpInsn(IFNE, l0);
                    // else
                    warnTimeStamp(mv, index);
                    info.visitStaticGet(mv);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "org/codehaus/cake/attribute/LongAttribute", "getDefaultValue",
                            "()J");
                    mv.visitVarInsn(LSTORE, index);
                    timeStampIndex = index;
                } else {
                    mv.visitVarInsn(LLOAD, timeStampIndex);
                    mv.visitVarInsn(LSTORE, index);
                }
            } else if (isCreate && info.impl.getCreateAction() == CreateAction.SET_VALUE) {
                mv.visitLdcInsn(info.impl.getCreateSetValue());
                mv.visitVarInsn(info.vType.storeCode(), index);
            } else if ((isCreate && info.impl.getCreateAction() == CreateAction.DEFAULT)
                    || (!isCreate && info.impl.getModifyAction() == ModifyAction.DEFAULT)) { // default
                if (info.vType == PrimType.OBJECT) {
                    info.visitStaticGet(mv);
                    // System.out.println(classDescriptor);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "org/codehaus/cake/attribute/Attribute", "getDefault",
                            "()Ljava/lang/Object;");
                    mv.visitTypeInsn(CHECKCAST, info.type.getInternalName());
                } else {
                    mv.visitLdcInsn(info.impl.getAttribute().getDefault());
                }
                mv.visitVarInsn(info.vType.storeCode(), index);
            } else if (!isCreate && info.impl.getModifyAction() == ModifyAction.KEEP_EXISTING) {
                // TODO we should make field package protected and then access it directly in the map
                // System.out.println("keep" + checkMap);
                int i = checkMap ? 4 : 3;
                mv.visitVarInsn(ALOAD, i);
                info.visitStaticGet(mv);
                // System.out.println(info.vType.getPrimDescriptor());
                if (info.vType == PrimType.OBJECT) {
                    mv.visitMethodInsn(INVOKEINTERFACE, "org/codehaus/cake/attribute/AttributeMap", "get", "("
                            + ATTRIBUTE_DESCRIPTOR + ")" + info.vType.getPrimDescriptor());

                } else {
                    mv.visitMethodInsn(INVOKEINTERFACE, "org/codehaus/cake/attribute/AttributeMap", "get", "("
                            + info.attributeDescriptor + ")" + info.vType.getPrimDescriptor());
                }
                mv.visitVarInsn(info.vType.storeCode(), index);
            } else if (!isCreate && info.impl.getModifyAction() == ModifyAction.INCREMENT) {
                int i = checkMap ? 4 : 3;
                mv.visitVarInsn(ALOAD, i);
                info.visitStaticGet(mv);
                mv.visitMethodInsn(INVOKEINTERFACE, "org/codehaus/cake/attribute/AttributeMap", "get", "("
                        + info.attributeDescriptor + ")" + info.descriptor);
                mv.visitInsn(LCONST_1);
                mv.visitInsn(LADD);
                mv.visitVarInsn(info.vType.storeCode(), index);
            }
            index += info.vType.indexInc();

        }
        mv.visitLabel(l0);
        createMap(mv, l0, startIndex);
        mv.visitMaxs(10, 10);
        mv.visitEnd();
    }

    private void addCreateEntry() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "create", "(Ljava/lang/Object;Ljava/lang/Object;"
                + GET_ATTRIBUTER_DESCRIPTOR + GET_ATTRIBUTER_DESCRIPTOR + ")" + ATTRIBUTEMAP_DESCRIPTOR, "(TK;TV;"
                + GET_ATTRIBUTER_DESCRIPTOR + GET_ATTRIBUTER_DESCRIPTOR + ")" + ATTRIBUTEMAP_DESCRIPTOR, null);

        mv.visitCode();
        mv.visitVarInsn(ALOAD, 4);
        Label l0 = new Label();
        mv.visitJumpInsn(IFNONNULL, l0);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEINTERFACE, "org/codehaus/cake/attribute/AttributeMap", "isEmpty", "()Z");
        Label l1 = new Label();
        mv.visitJumpInsn(IFEQ, l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKESPECIAL, classDescriptor, "onCreate", "(Ljava/lang/Object;Ljava/lang/Object;)"
                + ATTRIBUTEMAP_DESCRIPTOR);
        mv.visitInsn(ARETURN);
        mv.visitLabel(l1);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKESPECIAL, classDescriptor, "onCreate", "(Ljava/lang/Object;Ljava/lang/Object;"
                + ATTRIBUTEMAP_DESCRIPTOR + ")" + ATTRIBUTEMAP_DESCRIPTOR);
        mv.visitInsn(ARETURN);
        mv.visitLabel(l0);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEINTERFACE, "org/codehaus/cake/attribute/AttributeMap", "isEmpty", "()Z");
        Label l2 = new Label();
        mv.visitJumpInsn(IFEQ, l2);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(INVOKESPECIAL, classDescriptor, "onModify", "(Ljava/lang/Object;Ljava/lang/Object;"
                + ATTRIBUTEMAP_DESCRIPTOR + ")" + ATTRIBUTEMAP_DESCRIPTOR);
        mv.visitInsn(ARETURN);
        mv.visitLabel(l2);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(INVOKESPECIAL, classDescriptor, "onModify", "(Ljava/lang/Object;Ljava/lang/Object;"
                + ATTRIBUTEMAP_DESCRIPTOR + ATTRIBUTEMAP_DESCRIPTOR + ")" + ATTRIBUTEMAP_DESCRIPTOR);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(5, 5);
        mv.visitEnd();
    }

    private void addCreate() {
        MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, "onCreate", "(Ljava/lang/Object;Ljava/lang/Object;)"
                + ATTRIBUTEMAP_DESCRIPTOR, "(TK;TV;)" + ATTRIBUTEMAP_DESCRIPTOR, null);
        addCreate(mv, 3, false, true);
    }

    private void addCreateWithAttributes() {
        MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, "onCreate", "(Ljava/lang/Object;Ljava/lang/Object;"
                + ATTRIBUTEMAP_DESCRIPTOR + ")" + ATTRIBUTEMAP_DESCRIPTOR, "(TK;TV;" + ATTRIBUTEMAP_DESCRIPTOR + ")"
                + ATTRIBUTEMAP_DESCRIPTOR, null);
        addCreate(mv, 4, true, true);
    }

    private void addModify() {
        MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, "onModify", "(Ljava/lang/Object;Ljava/lang/Object;"
                + ATTRIBUTEMAP_DESCRIPTOR + ")" + ATTRIBUTEMAP_DESCRIPTOR, "(TK;TV;" + ATTRIBUTEMAP_DESCRIPTOR + ")"
                + ATTRIBUTEMAP_DESCRIPTOR, null);
        addCreate(mv, 4, false, false);
    }

    private void addModifyWithAttributes() {
        MethodVisitor mv = cw.visitMethod(ACC_PRIVATE, "onModify", "(Ljava/lang/Object;Ljava/lang/Object;"
                + ATTRIBUTEMAP_DESCRIPTOR + ATTRIBUTEMAP_DESCRIPTOR + ")" + ATTRIBUTEMAP_DESCRIPTOR, "(TK;TV;"
                + ATTRIBUTEMAP_DESCRIPTOR + ATTRIBUTEMAP_DESCRIPTOR + ")" + ATTRIBUTEMAP_DESCRIPTOR, null);
        addCreate(mv, 5, true, false);
    }

    private void createClass() {
        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, classDescriptor, "<K:Ljava/lang/Object;V:Ljava/lang/Object;>"
                + INTERFACE_DESCRIPTOR + "<TK;TV;>;", "java/lang/Object", new String[] { INTERFACE_INTERNAL_NAME });
        _fields();
        _static_fields();
        _static_init();
        _constructor();
        addCreateEntry();
        addCreate();
        addCreateWithAttributes();
        addModify();
        addModifyWithAttributes();
        addAccess();
        cw.visitEnd();
    }

    private void createMap(MethodVisitor mv, Label l0, int paramCount) {
        mv.visitLabel(l0);
        mv.visitTypeInsn(NEW, classDescriptor + "Map");
        mv.visitInsn(DUP);
        int index = paramCount;
        Type[] types = new Type[info.length];
        for (int i = 0; i < this.info.length; i++) {
            Info in = this.info[i];
            mv.visitVarInsn(in.vType.loadCode(), index);
            index += in.vType.indexInc();
            if (in.vType == PrimType.OBJECT) {
                types[i] = Type.getType(in.attribute.getType());
            } else {
                types[i] = in.vType.getPrimType();
            }
        }
        String str = getMethodDescriptor(Type.VOID_TYPE, types);
        // System.out.println(str);
        mv
                .visitMethodInsn(INVOKESPECIAL, classDescriptor + "Map", "<init>", getMethodDescriptor(Type.VOID_TYPE,
                        types));
        mv.visitInsn(ARETURN);
    }

    private void readClock(MethodVisitor mv, int index) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, classDescriptor, "clock", "Lorg/codehaus/cake/util/Clock;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "org/codehaus/cake/util/Clock", "timeOfDay", "()J");
        mv.visitVarInsn(LSTORE, index);
    }

    private void warnTimeStamp(MethodVisitor mv, int index) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, classDescriptor, "ies",
                "Lorg/codehaus/cake/internal/service/exceptionhandling/InternalExceptionService;");
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitLdcInsn("Clock returned an illegal timestamp, resorting to default value [illegal timestamp=");
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
        mv.visitVarInsn(LLOAD, index);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;");
        mv.visitLdcInsn("]");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
        mv.visitMethodInsn(INVOKEINTERFACE,
                "org/codehaus/cake/internal/service/exceptionhandling/InternalExceptionService", "warning",
                "(Ljava/lang/String;)V");
    }

    private boolean checkAccess() {
        for (Info info : this.info) {
            if (info.impl.getAccessAction() != AccessAction.NOTHING) {
                return true;
            }
        }
        return false;
    }

    private void addAccess() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "access", "(Lorg/codehaus/cake/attribute/AttributeMap;)V", null,
                null);
        mv.visitCode();
        if (checkAccess()) {
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, classDescriptor + "Map");
            mv.visitVarInsn(ASTORE, 2);
            for (Info info : this.info) {
                if (info.impl.getAccessAction() == AccessAction.TIMESTAMP) {
                    mv.visitVarInsn(ALOAD, 2);
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitFieldInsn(GETFIELD, classDescriptor, "clock", "Lorg/codehaus/cake/util/Clock;");
                    mv.visitMethodInsn(INVOKEVIRTUAL, "org/codehaus/cake/util/Clock", "timeOfDay", "()J");
                    mv.visitFieldInsn(PUTFIELD, classDescriptorMap, info.getFieldName(), "J");
                } else if (info.impl.getAccessAction() == AccessAction.INCREMENT) {
                    mv.visitVarInsn(ALOAD, 2);
                    mv.visitInsn(DUP);
                    mv.visitFieldInsn(GETFIELD, classDescriptorMap, info.getFieldName(), "J");
                    mv.visitInsn(LCONST_1);
                    mv.visitInsn(LADD);
                    mv.visitFieldInsn(PUTFIELD, classDescriptorMap, info.getFieldName(), "J");
                }
            }
        }
        mv.visitInsn(RETURN);
        mv.visitMaxs(3, 3);
        mv.visitEnd();
    }

  //  static MyLoader ml;

    public static <K, V> CacheAttributeMapFactory<K, V> generate(String className,
            List<? extends CacheAttributeMapConfiguration> infos, Clock clock, InternalExceptionService ies)
            throws Exception {
        String descriptor = className.replace('.', '/');
        MyLoader ml=null;
        if (ml == null) {
            ml = (MyLoader) SecurityTools.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    return new MyLoader();
                }
            });
        }
        Class mapClass = DefaultMapGenerator.generate(ml, className + "Map", infos);
        // System.out.println(mapClass.getConstructors()[0]);
        CacheAttributeMapFactoryGenerator g = new CacheAttributeMapFactoryGenerator(descriptor, infos);
        // g.cw = new ASMifierClassVisitor(new PrintWriter(System.out));

        g.createClass();
        Class c = ml.defineClass(className, ((ClassWriter) g.cw).toByteArray());
        Attribute[] attributes = new Attribute[g.info.length];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = g.info[i].attribute;
        }
        synchronized (lock) {
            initializers.put(c, attributes);
        }
        Constructor con = (Constructor) c.getConstructors()[0];
        return (CacheAttributeMapFactory) con.newInstance(clock, ies);
    }

    public static Attribute[] init(Class c) {
        synchronized (lock) {
            return initializers.remove(c);
        }
    }

    public class Info {
        public final Attribute<?> attribute;
        /** For example <code>Lorg/codehaus/cake/attribute/DoubleAttribute; </code> */
        final String attributeDescriptor;
        final String descriptor;
        final CacheAttributeMapConfiguration impl;
        private final int index;

        final Type type;

        final PrimType vType;

        Info(int index, CacheAttributeMapConfiguration info) {
            this.impl = info;
            this.index = index;

            attribute = info.getAttribute();
            type = Type.getType(attribute.getType());
            vType = PrimType.from(attribute);
            // System.out.println(type);
            descriptor = vType == PrimType.OBJECT ? Type.getType(Object.class).getDescriptor() : type.getDescriptor();
            attributeDescriptor = vType.getDescriptor();
        }

        public String getFieldName() {
            return "v" + index;
        }

        String getFieldStaticName() {
            return "A" + index;
        }

        void visitStaticGet(MethodVisitor mv) {
            mv.visitFieldInsn(GETSTATIC, classDescriptor, getFieldStaticName(), vType.getDescriptor());
        }
    }

    static class NoAttributes<K, V> implements CacheAttributeMapFactory<K, V> {
        public void access(AttributeMap map) {
        }

        public MutableAttributeMap create(K key, V value, AttributeMap params, AttributeMap previous) {
            return Attributes.EMPTY_ATTRIBUTE_MAP;
        }

    }
}
