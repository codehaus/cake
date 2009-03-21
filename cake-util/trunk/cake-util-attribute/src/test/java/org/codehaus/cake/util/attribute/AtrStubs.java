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
package org.codehaus.cake.util.attribute;


/**
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class AtrStubs {

    public static final ByteAttribute B_1 = new ByteAttribute("B_1", (byte) 1) {};
    public static final ByteAttribute B_2 = new ByteAttribute("B_2", (byte) 2) {};
    public static final ByteAttribute B_3 = new ByteAttribute("B_3", (byte) 3) {};
    public static final BooleanAttribute B_FALSE = new BooleanAttribute("B_FALSE", false) {};

    public static final BooleanAttribute B_FALSE1 = new BooleanAttribute("B_FALSE1", false) {};
    public static final ByteAttribute B_MAX_VALUE = new ByteAttribute("B_MAX_VALUE", Byte.MAX_VALUE) {};
    public static final ByteAttribute B_MIN_VALUE = new ByteAttribute("B_MIN_VALUE", Byte.MIN_VALUE) {};
    public static final BooleanAttribute B_TRUE = new BooleanAttribute("B_TRUE", true) {};
    public static final BooleanAttribute B_TRUE1 = new BooleanAttribute("B_TRUE1", true) {};

    public static final CharAttribute C_1 = new CharAttribute("C_1", (char) 1) {};
    public static final CharAttribute C_2 = new CharAttribute("C_2", (char) 2) {};
    public static final CharAttribute C_3 = new CharAttribute("C_3", (char) 3) {};
    public static final CharAttribute C_MAX_VALUE = new CharAttribute("C_MAX_VALUE", Character.MAX_VALUE) {};
    public static final CharAttribute C_MIN_VALUE = new CharAttribute("C_MIN_VALUE", Character.MIN_VALUE) {};

    public static final DoubleAttribute D_1 = new DoubleAttribute("D_1", 1.5) {};
    public static final DoubleAttribute D_2 = new DoubleAttribute("D_2", 2.5) {};
    public static final DoubleAttribute D_3 = new DoubleAttribute("D_3", 3.5) {};
    public static final DoubleAttribute D_MAX_VALUE = new DoubleAttribute("D_MAX_VALUE", Double.MAX_VALUE) {};
    public static final DoubleAttribute D_MIN_VALUE = new DoubleAttribute("D_MIN_VALUE", Double.MIN_VALUE) {};

    public static final FloatAttribute F_1 = new FloatAttribute("F_1", (float) 1.5) {};
    public static final FloatAttribute F_2 = new FloatAttribute("F_2", (float) 2.5) {};
    public static final FloatAttribute F_3 = new FloatAttribute("F_3", (float) 3.5) {};
    public static final FloatAttribute F_MAX_VALUE = new FloatAttribute("F_MAX_VALUE", Float.MAX_VALUE) {};
    public static final FloatAttribute F_MIN_VALUE = new FloatAttribute("F_MIN_VALUE", Float.MIN_VALUE) {};

    public static final IntAttribute I_1 = new IntAttribute("I_1", 1) {};
    public static final IntAttribute I_2 = new IntAttribute("I_2", 2) {};
    public static final IntAttribute I_3 = new IntAttribute("I_3", 3) {};
    public static final IntAttribute I_MAX_VALUE = new IntAttribute("I_MAX_VALUE", Integer.MAX_VALUE) {};
    public static final IntAttribute I_MIN_VALUE = new IntAttribute("I_MIN_VALUE", Integer.MIN_VALUE) {};
    public static final IntAttribute I_POSITIVE = new IntAttribute("I_POSITIVE", 1) {

        @Override
        public boolean isValid(int value) {
            return value>0;
        }
    };

    public static final LongAttribute L_1 = new LongAttribute("L_1", 1) {};
    public static final LongAttribute L_2 = new LongAttribute("L_2", 2) {};
    public static final LongAttribute L_3 = new LongAttribute("L_3", 3) {};
    public static final LongAttribute L_MAX_VALUE = new LongAttribute("L_MAX_VALUE", Long.MAX_VALUE) {};
    public static final LongAttribute L_MIN_VALUE = new LongAttribute("L_MIN_VALUE", Long.MIN_VALUE) {};

    public static final ObjectAttribute<String> O_1 = new ObjectAttribute<String>("O_1", String.class, "1.5") {};
    public static final ObjectAttribute<String> O_2 = new ObjectAttribute<String>("O_2", String.class, "15") {};
    public static final ObjectAttribute<String> O_3 = new ObjectAttribute<String>("O_3", String.class, null) {};
    public static final ObjectAttribute<String> O_4 = new ObjectAttribute<String>("O_4", String.class) {};

    public static final ShortAttribute S_1 = new ShortAttribute("S_1", (short) 1) {};
    public static final ShortAttribute S_2 = new ShortAttribute("S_2", (short) 2) {};

    public static final ShortAttribute S_3 = new ShortAttribute("S_3", (short) 3) {};
    public static final ShortAttribute S_MAX_VALUE = new ShortAttribute("S_MAX_VALUE", Short.MAX_VALUE) {};
    public static final ShortAttribute S_MIN_VALUE = new ShortAttribute("S_MIN_VALUE", Short.MIN_VALUE) {};


}
