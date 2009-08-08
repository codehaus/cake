package org.codehaus.cake.internal.codegen.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.codehaus.cake.internal.codegen.ClassDefiner;
import org.codehaus.cake.util.Maps;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.BooleanAttribute;
import org.codehaus.cake.util.attribute.LongAttribute;
import org.junit.Test;

public class AttributeDecoratorTest {
    public static final BooleanAttribute ba = new BooleanAttribute("ba", true) {};

    public static final LongAttribute la = new LongAttribute("la", 123) {};

    @Test
    public void singleBoolean() throws Exception {
        ClassDefiner def = new ClassDefiner();

        FieldDefinition conf = new FieldDefinition(ba.getType(), ba.getName()).setAttribute(ba);
        Class<AttributeMap> c = AttributeMapDecoratoredEmitter.decorate(def, "any", SingleElement.class, Arrays.asList(conf));
        assertEquals(2, c.getConstructors().length);
        assertNotNull(c.getConstructor(Boolean.TYPE));
        assertNotNull(c.getConstructor(Object.class, Boolean.TYPE));

        SingleElement sf = (SingleElement) c.getConstructor(Boolean.TYPE).newInstance(false);
        SingleElement st = (SingleElement) c.getConstructor(Boolean.TYPE).newInstance(true);

        AttributeMapChecker.assertAttributeMap(sf, Maps.from(ba, false));
        AttributeMapChecker.assertAttributeMap(st, Maps.from(ba, true));
    }

    @Test
    public void singleLong() throws Exception {
        ClassDefiner def = new ClassDefiner();

        FieldDefinition conf = new FieldDefinition(la.getType(), la.getName()).setAttribute(la);
        Class<AttributeMap> c = AttributeMapDecoratoredEmitter.decorate(def, "any", SingleElement.class, Arrays.asList(conf));
        assertEquals(2, c.getConstructors().length);
        assertNotNull(c.getConstructor(Long.TYPE));
        assertNotNull(c.getConstructor(Object.class, Long.TYPE));

        SingleElement s123 = (SingleElement) c.getConstructor(Long.TYPE).newInstance(123L);
        SingleElement sm23 = (SingleElement) c.getConstructor(Long.TYPE).newInstance(-23L);

        AttributeMapChecker.assertAttributeMap(s123, Maps.from(la, 123L));
        AttributeMapChecker.assertAttributeMap(sm23, Maps.from(la, -23L));
    }
        //bytecode for long version
//    public class any extends org/codehaus/cake/internal/codegen/attribute/SingleElement  {
//
//
//        // access flags 1
//        public <init>(J)V
//          ALOAD 0
//          INVOKESPECIAL org/codehaus/cake/internal/codegen/attribute/SingleElement.<init> ()V
//          ALOAD 0
//          LLOAD 1
//          PUTFIELD any.la : J
//          RETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public <init>(Ljava/lang/Object;J)V
//          ALOAD 0
//          ALOAD 1
//          INVOKESPECIAL org/codehaus/cake/internal/codegen/attribute/SingleElement.<init> (Ljava/lang/Object;)V
//          ALOAD 0
//          LLOAD 2
//          PUTFIELD any.la : J
//          RETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 8
//        static <clinit>()V
//          LDC Lany;.class
//          INVOKESTATIC org/codehaus/cake/internal/codegen/attribute/AttributeDecorator.remove (Ljava/lang/Class;)[Lorg/codehaus/cake/util/attribute/Attribute;
//          ASTORE 0
//          NEW java/util/HashSet
//          DUP
//          ALOAD 0
//          INVOKESTATIC java/util/Arrays.asList ([Ljava/lang/Object;)Ljava/util/List;
//          CHECKCAST java/util/Collection
//          INVOKESPECIAL java/util/HashSet.<init> (Ljava/util/Collection;)V
//          INVOKESTATIC java/util/Collections.unmodifiableSet (Ljava/util/Set;)Ljava/util/Set;
//          PUTSTATIC any.keyset_ATR : Ljava/util/Set;
//          ALOAD 0
//          ICONST_0
//          AALOAD
//          CHECKCAST org/codehaus/cake/util/attribute/LongAttribute
//          PUTSTATIC any.la_ATR : Lorg/codehaus/cake/util/attribute/LongAttribute;
//          RETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public contains(Lorg/codehaus/cake/util/attribute/Attribute;)Z
//          ALOAD 1
//          GETSTATIC any.la_ATR : Lorg/codehaus/cake/util/attribute/LongAttribute;
//          IF_ACMPEQ L0
//          ICONST_0
//          IRETURN
//         L0
//          ICONST_1
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public isEmpty()Z
//          ICONST_0
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public size()I
//          ICONST_1
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public attributes()Ljava/util/Set;
//          GETSTATIC any.keyset_ATR : Ljava/util/Set;
//          ARETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public values()Ljava/util/Collection;
//          ICONST_1
//          ANEWARRAY java/lang/Object
//          DUP
//          ICONST_0
//          ALOAD 0
//          GETFIELD any.la : J
//          NEW java/lang/Long
//          DUP_X2
//          DUP_X2
//          POP
//          INVOKESPECIAL java/lang/Long.<init> (J)V
//          AASTORE
//          INVOKESTATIC java/util/Arrays.asList ([Ljava/lang/Object;)Ljava/util/List;
//          CHECKCAST java/util/Collection
//          INVOKESTATIC java/util/Collections.unmodifiableCollection (Ljava/util/Collection;)Ljava/util/Collection;
//          ARETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public entrySet()Ljava/util/Set;
//          NEW java/util/HashSet
//          DUP
//          INVOKESPECIAL java/util/HashSet.<init> ()V
//          ASTORE 1
//          ALOAD 1
//          NEW org/codehaus/cake/internal/util/attribute/AttributeHelper$SimpleImmutableEntry
//          DUP
//          GETSTATIC any.la_ATR : Lorg/codehaus/cake/util/attribute/LongAttribute;
//          ALOAD 0
//          GETFIELD any.la : J
//          NEW java/lang/Long
//          DUP_X2
//          DUP_X2
//          POP
//          INVOKESPECIAL java/lang/Long.<init> (J)V
//          INVOKESPECIAL org/codehaus/cake/internal/util/attribute/AttributeHelper$SimpleImmutableEntry.<init> (Ljava/lang/Object;Ljava/lang/Object;)V
//          INVOKEVIRTUAL java/util/HashSet.add (Ljava/lang/Object;)Z
//          POP
//          ALOAD 1
//          INVOKESTATIC java/util/Collections.unmodifiableSet (Ljava/util/Set;)Ljava/util/Set;
//          ARETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/BooleanAttribute;)Z
//          ALOAD 1
//          INVOKEVIRTUAL org/codehaus/cake/util/attribute/BooleanAttribute.getDefaultValue ()Z
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/BooleanAttribute;Z)Z
//          ILOAD 2
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/ByteAttribute;)B
//          ALOAD 1
//          INVOKEVIRTUAL org/codehaus/cake/util/attribute/ByteAttribute.getDefaultValue ()B
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/ByteAttribute;B)B
//          ILOAD 2
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/CharAttribute;)C
//          ALOAD 1
//          INVOKEVIRTUAL org/codehaus/cake/util/attribute/CharAttribute.getDefaultValue ()C
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/CharAttribute;C)C
//          ILOAD 2
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/DoubleAttribute;)D
//          ALOAD 1
//          INVOKEVIRTUAL org/codehaus/cake/util/attribute/DoubleAttribute.getDefaultValue ()D
//          DRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/DoubleAttribute;D)D
//          DLOAD 2
//          DRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/FloatAttribute;)F
//          ALOAD 1
//          INVOKEVIRTUAL org/codehaus/cake/util/attribute/FloatAttribute.getDefaultValue ()F
//          FRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/FloatAttribute;F)F
//          FLOAD 2
//          FRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/IntAttribute;)I
//          ALOAD 1
//          INVOKEVIRTUAL org/codehaus/cake/util/attribute/IntAttribute.getDefaultValue ()I
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/IntAttribute;I)I
//          ILOAD 2
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/LongAttribute;)J
//          ALOAD 1
//          GETSTATIC any.la_ATR : Lorg/codehaus/cake/util/attribute/LongAttribute;
//          IF_ACMPNE L0
//          ALOAD 0
//          GETFIELD any.la : J
//          LRETURN
//         L0
//          ALOAD 1
//          INVOKEVIRTUAL org/codehaus/cake/util/attribute/LongAttribute.getDefaultValue ()J
//          LRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/LongAttribute;J)J
//          ALOAD 1
//          GETSTATIC any.la_ATR : Lorg/codehaus/cake/util/attribute/LongAttribute;
//          IF_ACMPNE L0
//          ALOAD 0
//          GETFIELD any.la : J
//          LRETURN
//         L0
//          LLOAD 2
//          LRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/ShortAttribute;)S
//          ALOAD 1
//          INVOKEVIRTUAL org/codehaus/cake/util/attribute/ShortAttribute.getDefaultValue ()S
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/ShortAttribute;S)S
//          ILOAD 2
//          IRETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/Attribute;)Ljava/lang/Object;
//          ALOAD 1
//          GETSTATIC any.la_ATR : Lorg/codehaus/cake/util/attribute/LongAttribute;
//          IF_ACMPNE L0
//          ALOAD 0
//          GETFIELD any.la : J
//          NEW java/lang/Long
//          DUP_X2
//          DUP_X2
//          POP
//          INVOKESPECIAL java/lang/Long.<init> (J)V
//          ARETURN
//         L0
//          ALOAD 1
//          INVOKEVIRTUAL org/codehaus/cake/util/attribute/Attribute.getDefault ()Ljava/lang/Object;
//          ARETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 1
//        public get(Lorg/codehaus/cake/util/attribute/Attribute;Ljava/lang/Object;)Ljava/lang/Object;
//          ALOAD 1
//          GETSTATIC any.la_ATR : Lorg/codehaus/cake/util/attribute/LongAttribute;
//          IF_ACMPNE L0
//          ALOAD 0
//          GETFIELD any.la : J
//          NEW java/lang/Long
//          DUP_X2
//          DUP_X2
//          POP
//          INVOKESPECIAL java/lang/Long.<init> (J)V
//          ARETURN
//         L0
//          ALOAD 2
//          ARETURN
//          MAXSTACK = 3
//          MAXLOCALS = 3
//
//        // access flags 25
//        public final static Ljava/util/Set; keyset_ATR
//
//        // access flags 0
//        J la
//
//        // access flags 25
//        public final static Lorg/codehaus/cake/util/attribute/LongAttribute; la_ATR
//      }

    private void print(Object o) throws Exception {
        System.out.println("----");
        for (Field f : o.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            String s = f.get(o) == null ? "null" : f.get(o).getClass().toString();
            System.out.println(f.getName() + " '" + s + "'");
        }
    }
}
