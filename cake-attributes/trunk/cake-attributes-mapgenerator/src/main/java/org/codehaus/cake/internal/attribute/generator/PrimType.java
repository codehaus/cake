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
package org.codehaus.cake.internal.attribute.generator;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.ByteAttribute;
import org.codehaus.cake.attribute.CharAttribute;
import org.codehaus.cake.attribute.DoubleAttribute;
import org.codehaus.cake.attribute.FloatAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.codehaus.cake.attribute.ObjectAttribute;
import org.codehaus.cake.attribute.ShortAttribute;
import org.codehaus.cake.internal.asm.Opcodes;
import org.codehaus.cake.internal.asm.Type;

public enum PrimType {
    BOOLEAN(0), BYTE(1), CHAR(2), DOUBLE(3), FLOAT(4), INT(5), LONG(6), OBJECT(7), SHORT(8);

    private final int loadCode;
    private final Type object;// java.lang.Integer
    private final Type primType;
    private final int returncode;
    private final int storeCode;
    private final Type type;

    private PrimType(int index) {
        type = Type.getType(Info.CLASSES_ATT[index]);
        this.object = Type.getType(Info.CLASSES[index]);
        this.primType = Info.TYPES[index];
        this.returncode = Info.RETURN_OPCODES[index];
        this.loadCode = Info.LOAD_OPCODES[index];
        this.storeCode = Info.STORE_OPCODES[index];

    }

    public String getDescriptor() {
        return type.getDescriptor();
    }

    // java.lang.Integer
    public Type getObjectType() {
        return object;
    }

    public String getPrimDescriptor() {
        return getPrimType().getDescriptor();
    }

    public Type getPrimType() {
        return primType;
    }

    public Type getType() {
        return type;
    }

    public int indexInc() {
        return this == LONG || this == DOUBLE ? 2 : 1;
    }

    public int loadCode() {
        return loadCode;
    }

    public int returnCode() {
        return returncode;
    }

    public int storeCode() {
        return storeCode;
    }

    public static PrimType from(Attribute a) {
        if (a instanceof LongAttribute) {
            return LONG;
        } else if (a instanceof DoubleAttribute) {
            return DOUBLE;
        } else if (a instanceof IntAttribute) {
            return INT;
        } else if (a instanceof ShortAttribute) {
            return SHORT;
        } else if (a instanceof ByteAttribute) {
            return BYTE;
        } else if (a instanceof FloatAttribute) {
            return FLOAT;
        } else if (a instanceof CharAttribute) {
            return CHAR;
        } else if (a instanceof BooleanAttribute) {
            return BOOLEAN;
        } else {
            // if (a instanceof ObjectAttribute) {
            return OBJECT;
        }
    }
    

    static class Info {
        final static Class[] CLASSES = new Class[] { Boolean.class, Byte.class, Character.class, Double.class,
                Float.class, Integer.class, Long.class, Object.class, Short.class };
        final static Class[] CLASSES_ATT = new Class[] { BooleanAttribute.class, ByteAttribute.class,
                CharAttribute.class, DoubleAttribute.class, FloatAttribute.class, IntAttribute.class,
                LongAttribute.class, ObjectAttribute.class, ShortAttribute.class };
        final static Type[] TYPES = new Type[] { Type.BOOLEAN_TYPE, Type.BYTE_TYPE, Type.CHAR_TYPE, Type.DOUBLE_TYPE,
                Type.FLOAT_TYPE, Type.INT_TYPE, Type.LONG_TYPE, Type.getType(Object.class), Type.SHORT_TYPE };
        final static int[] LOAD_OPCODES = new int[] { Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.ILOAD, Opcodes.DLOAD,
                Opcodes.FLOAD, Opcodes.ILOAD, Opcodes.LLOAD, Opcodes.ALOAD, Opcodes.ILOAD };
        final static int[] STORE_OPCODES = new int[] { Opcodes.ISTORE, Opcodes.ISTORE, Opcodes.ISTORE, Opcodes.DSTORE,
                Opcodes.FSTORE, Opcodes.ISTORE, Opcodes.LSTORE, Opcodes.ASTORE, Opcodes.ISTORE };
        final static int[] RETURN_OPCODES = new int[] { Opcodes.IRETURN, Opcodes.IRETURN, Opcodes.IRETURN,
                Opcodes.DRETURN, Opcodes.FRETURN, Opcodes.IRETURN, Opcodes.LRETURN, Opcodes.ARETURN, Opcodes.IRETURN };
    }
}
