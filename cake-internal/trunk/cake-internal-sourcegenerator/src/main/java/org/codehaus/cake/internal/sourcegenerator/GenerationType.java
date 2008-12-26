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
package org.codehaus.cake.internal.sourcegenerator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import org.apache.velocity.context.Context;
import org.codehaus.cake.internal.sourcegenerator.math.Complex;

public class GenerationType {
    public static final GenerationType BOOLEAN = new GenerationType(Boolean.TYPE);
    public static final GenerationType BYTE = new GenerationType(Byte.TYPE);
    public static final GenerationType CHAR = new GenerationType(Character.TYPE);
    public static final GenerationType DOUBLE = new GenerationType(Double.TYPE);
    public static final GenerationType FLOAT = new GenerationType(Float.TYPE);
    public static final GenerationType INT = new GenerationType(Integer.TYPE);
    public static final GenerationType LONG = new GenerationType(Long.TYPE);
    public static final GenerationType SHORT = new GenerationType(Short.TYPE);

    public static final GenerationType BIG_DECIMAL = new GenerationType(BigDecimal.class);
    public static final GenerationType BIG_INTEGER = new GenerationType(BigInteger.class);
    public static final GenerationType COMPLEX = new GenerationType(Complex.class);

    public static final Collection<GenerationType> ALL = Arrays.asList(BOOLEAN, BYTE, CHAR, DOUBLE, FLOAT, INT, LONG,
            SHORT);
    public static final Collection<GenerationType> ALL_NUMBERS = Arrays.asList(BYTE, CHAR, DOUBLE, FLOAT, INT, LONG,
            SHORT);
    /** BigInteger, Integer.class, .. */
    private final Class type;

    GenerationType(Class type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

    public String getTypeCap() {
        return capitalize(type.getSimpleName());
    }

    public String add(String a, String b) {
        if (isObject()) {
            return a + ".add(" + b + ")";
        } else {
            return a + " + " + b;
        }
    }

    public String eq(String a, String b) {
        if (isObject()) {
            return a + ".equals(" + b + ")";
        } else {
            return a + " == " + b;
        }
    }

    public String neq(String a, String b) {
        if (isObject()) {
            return "!" + a + ".equals(" + b + ")";
        } else {
            return a + " != " + b;
        }
    }

    public String subtract(String a, String b) {
        if (isObject()) {
            return a + ".subtract(" + b + ")";
        } else {
            return a + " - " + b;
        }
    }

    public String divide(String a, String b) {
        if (isObject()) {
            return a + ".divide(" + b + ")";
        } else {
            return a + " / " + b;
        }
    }

    public String multiply(String a, String b) {
        if (isObject()) {
            return a + ".multiply(" + b + ")";
        } else {
            return a + " * " + b;
        }
    }

    public String op(String type) {
        if (isObject()) {
            if (type.contains("*")) {
                int c = countOccurrences(type, "*");
                type = type.replace("*", "Object") + "<";
                if (c == 1) {
                    return type + this.type.getSimpleName() + ">";
                } else if (c == 2) {
                    return type + this.type.getSimpleName() + ", " + this.type.getSimpleName() + ">";
                }
            }
            if (type.equals("Op")) {
                return type + "<" + this.type.getSimpleName() + ", " + this.type.getSimpleName() + ">";
            } else {
                return type + "<" + this.type.getSimpleName() + ">";
            }
        } else {
            if (type.contains("*")) {
                return type.replace("*", getTypeCap());
            }
            return getTypeCap() + type;
        }
    }

    public boolean isObject() {
        return type == fromPrimitive(type);
    }

    public void add(Context context) {

        context.put("type", type.getSimpleName());
        context.put("Type", capitalize(type.getSimpleName()));
        context.put("a", type.getSimpleName().startsWith("I") ? "an" : "a");
        context.put("object", fromPrimitive(type).getSimpleName());
        context.put("isReal", type == Double.TYPE || type == Float.TYPE);
        context.put("is16Or8Bit", type == Character.TYPE || type == Short.TYPE || type==Byte.TYPE);
        context.put("equalTest", type == Double.TYPE || type == Float.TYPE ? ",0" : "");
        context.put("isObject", isObject());
        context.put("isPrimitive", !isObject());
        context.put("util", this);
    }

    public String valNoCast(int i) {
        if (type == Boolean.TYPE) {
            return (i == 0) ? "false" : "true";
        } else {
            return "" + i;
        }
    }

    public String val(int i) {
        if (type == Boolean.TYPE) {
            return (i == 0) ? "false" : "true";
        } else if (type == Byte.TYPE) {
            return "(byte) " + i;
        } else if (type == Character.TYPE) {
            return "(char) " + i;
        } else if (type == Double.TYPE) {
            return i + "D";
        } else if (type == Float.TYPE) {
            return i + "F";
        } else if (type == Integer.TYPE) {
            return i + "";
        } else if (type == Long.TYPE) {
            return i + "L";
        } else if (type == Short.TYPE) {
            return "(short) " + i;
        }
        if (i == 0) {
            return type.getSimpleName() + ".ZERO";
        }
        if (type == BigInteger.class) {
            return "BigInteger.valueOf(" + i + "L)";
        }
        if (type == Complex.class) {
            return "new " + type.getSimpleName() + "(" + i + "," + i + ")";
        } else {
            return "new " + type.getSimpleName() + "(" + i + ")";
        }

    }

    /**
     * Converts the specified primitive class to the corresponding Object based class. Or returns the specified class if
     * it is not a primitive class.
     * 
     * @param c
     *            the class to convert
     * @return the converted class
     */
    public static Class fromPrimitive(Class c) {
        if (c.equals(Integer.TYPE)) {
            return Integer.class;
        } else if (c.equals(Double.TYPE)) {
            return Double.class;
        } else if (c.equals(Byte.TYPE)) {
            return Byte.class;
        } else if (c.equals(Float.TYPE)) {
            return Float.class;
        } else if (c.equals(Long.TYPE)) {
            return Long.class;
        } else if (c.equals(Short.TYPE)) {
            return Short.class;
        } else if (c.equals(Boolean.TYPE)) {
            return Boolean.class;
        } else if (c.equals(Character.TYPE)) {
            return Character.class;
        } else if (c.equals(Void.TYPE)) {
            return Void.class;
        } else {
            return c;
        }
    }

    public static String capitalize(String s) {
        if (s.length() == 0)
            return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static int countOccurrences(String arg1, String arg2) {
        int count = 0;
        int index = 0;
        while ((index = arg1.indexOf(arg2, index)) != -1) {
            ++index;
            ++count;
        }
        return count;
    }
}
