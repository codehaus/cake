package org.codehaus.cake.internal.codegen.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Map;

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
import org.codehaus.cake.util.collection.Maps;

public class AttributeMapChecker {

    public static final LongAttribute L_1 = new LongAttribute("L_1", 1) {};
    public static final LongAttribute L_2 = new LongAttribute("L_2", 2) {};
    public static final LongAttribute L_3 = new LongAttribute("L_3", 3) {};
    public static final LongAttribute L_MAX_VALUE = new LongAttribute("L_MAX_VALUE", Long.MAX_VALUE) {};
    public static final LongAttribute L_MIN_VALUE = new LongAttribute("L_MIN_VALUE", Long.MIN_VALUE) {};

    public static void assertAttributeMap(AttributeMap attributes, Map<? extends Attribute, ? extends Object> map) {
        checkBoolean(attributes, Maps.filterMapOnTypes(map, BooleanAttribute.class, Boolean.class));
        checkByte(attributes, Maps.filterMapOnTypes(map, ByteAttribute.class, Byte.class));
        checkChar(attributes, Maps.filterMapOnTypes(map, CharAttribute.class, Character.class));
        checkDouble(attributes, Maps.filterMapOnTypes(map, DoubleAttribute.class, Double.class));
        checkFloat(attributes, Maps.filterMapOnTypes(map, FloatAttribute.class, Float.class));
        checkInt(attributes, Maps.filterMapOnTypes(map, IntAttribute.class, Integer.class));
        checkLong(attributes, Maps.filterMapOnTypes(map, LongAttribute.class, Long.class));
        checkShort(attributes, Maps.filterMapOnTypes(map, ShortAttribute.class, Short.class));

        assertEquals(map.keySet(), attributes.attributes());
        for (Attribute a : map.keySet()) {
            assertTrue(attributes.contains(a));
        }
        assertEquals(map.entrySet(), attributes.entrySet());
        assertEquals(map.isEmpty(), attributes.isEmpty());
        assertEquals(map.size(), attributes.size());
        assertEquals(new HashSet(map.values()), new HashSet(attributes.values()));

    }

    public static void checkBoolean(AttributeMap attributes, Map<BooleanAttribute, Boolean> map) {
        BooleanAttribute B_FALSE = new BooleanAttribute("B_FALSE", false) {};
        BooleanAttribute B_TRUE = new BooleanAttribute("B_TRUE", true) {};
        for (Map.Entry<BooleanAttribute, Boolean> e : map.entrySet()) {
            BooleanAttribute a = e.getKey();
            boolean value = e.getValue();
            assertEquals(value, attributes.get(a));
            assertEquals(value, attributes.get(a, value));
            assertEquals(value, attributes.get(a, !value));

            assertEquals(value, attributes.get((Attribute) a));
            assertEquals(value, attributes.get((Attribute) a, value));
            assertEquals(value, attributes.get((Attribute) a, !value));
        }
        assertEquals(false, attributes.get(B_FALSE));
        assertEquals(true, attributes.get(B_TRUE));
        assertEquals(true, attributes.get(B_FALSE, true));
        assertEquals(false, attributes.get(B_TRUE, false));

        assertEquals(false, attributes.get((Attribute) B_FALSE));
        assertEquals(true, attributes.get((Attribute) B_TRUE));
        assertEquals(true, attributes.get((Attribute) B_FALSE, true));
        assertEquals(false, attributes.get((Attribute) B_TRUE, false));
    }

    public static void checkByte(AttributeMap attributes, Map<ByteAttribute, Byte> map) {

    }

    public static void checkChar(AttributeMap attributes, Map<CharAttribute, Character> map) {

    }

    public static void checkDouble(AttributeMap attributes, Map<DoubleAttribute, Double> map) {

    }

    public static void checkFloat(AttributeMap attributes, Map<FloatAttribute, Float> map) {

    }

    public static void checkInt(AttributeMap attributes, Map<IntAttribute, Integer> map) {

    }

    public static void checkLong(AttributeMap attributes, Map<LongAttribute, Long> map) {
        for (Map.Entry<LongAttribute, Long> e : map.entrySet()) {
            LongAttribute a = e.getKey();
            long value = e.getValue();
            assertEquals(value, attributes.get(a));
            assertEquals(value, attributes.get(a, value));
            assertEquals(value, attributes.get(a, -123434L));

            assertEquals(value, attributes.get((Attribute) a));
            assertEquals((Long) value, attributes.get((Attribute) a, value));
            assertEquals((Long) value, attributes.get((Attribute) a, 213123L));
        }
        assertEquals(3L, attributes.get(L_3));
        assertEquals(123L, attributes.get(L_3, 123L));
        assertEquals(2L, attributes.get(L_2, 2L));

        assertEquals(2L, attributes.get((Attribute) L_2));
        assertEquals((Long) Long.MAX_VALUE, attributes.get((Attribute) L_3, Long.MAX_VALUE));

    }

    public static void checkShort(AttributeMap attributes, Map<ShortAttribute, Short> map) {
        ShortAttribute S_2 = new ShortAttribute("S_2", (short) 2) {};
        ShortAttribute S_3 = new ShortAttribute("S_3", (short) 3) {};

        for (Map.Entry<ShortAttribute, Short> e : map.entrySet()) {
            ShortAttribute a = e.getKey();
            short value = e.getValue();
            assertEquals(value, attributes.get(a));
            assertEquals(value, attributes.get(a, value));
            assertEquals(value, attributes.get(a, (short) -56));

            assertEquals(value, attributes.get((Attribute) a));
            assertEquals((Short) value, attributes.get((Attribute) a, value));
            assertEquals((Short) value, attributes.get((Attribute) a, (short) -56));
        }
        assertEquals((short) 3, attributes.get(S_3));
        assertEquals(Short.MIN_VALUE, attributes.get(S_3, Short.MIN_VALUE));
        assertEquals((short) 2, attributes.get(S_2, (short) 2));

        assertEquals((short) 2, attributes.get((Attribute) S_2));
        assertEquals((Short) Short.MAX_VALUE, attributes.get((Attribute) S_3, Short.MAX_VALUE));
    }

}
