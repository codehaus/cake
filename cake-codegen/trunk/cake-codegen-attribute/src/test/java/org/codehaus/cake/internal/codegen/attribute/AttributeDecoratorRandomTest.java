package org.codehaus.cake.internal.codegen.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.cake.internal.codegen.ClassDefiner;
import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.collection.Lists;
import org.junit.Test;

public class AttributeDecoratorRandomTest extends AtrStubs {

    ClassDefiner def = new ClassDefiner();

    @Test
    public void test() throws Exception {
        run(S_2, (short) 3);
        run(S_3, (short) 3);
        run(S_2, (short) 3, B_FALSE, false);
        run(S_2, (short) 3, L_2, 4L);
    }

    static FieldDefinition newConf(Attribute attribute) {
        return new FieldDefinition(attribute.getType(), "f" + System.nanoTime()).setAttribute(attribute);
    }

    <T> void run(Attribute<T> attribute, T value) throws Exception {
        LinkedHashMap<FieldDefinition, Object> map = new LinkedHashMap<FieldDefinition, Object>();
        map.put(newConf(attribute), value);
        run(map);
    }

    <T1, T2> void run(Attribute<T1> attribute1, T1 value1, Attribute<T2> attribute2, T2 value2) throws Exception {
        LinkedHashMap<FieldDefinition, Object> map = new LinkedHashMap<FieldDefinition, Object>();
        map.put(newConf(attribute1), value1);
        map.put(newConf(attribute2), value2);
        run(map);
    }

    void run(LinkedHashMap<FieldDefinition, Object> m) throws Exception {
        Class<AttributeMap> c = AttributeDecorator.decorate(def, "any" + System.nanoTime(), SingleElement.class,
                new ArrayList<FieldDefinition>(m.keySet()));
        assertEquals(2, c.getConstructors().length);

        ArrayList<Class> constructor = new ArrayList<Class>();
        LinkedHashMap<Attribute, Object> map = new LinkedHashMap<Attribute, Object>();
        for (Map.Entry<FieldDefinition, Object> me : m.entrySet()) {
            constructor.add(me.getKey().getType());
            map.put(me.getKey().getAttribute(), me.getValue());
        }

        assertNotNull(c.getConstructor(constructor.toArray(new Class[0])));
        assertNotNull(c.getConstructor(Lists.join(Object.class, constructor).toArray(new Class[0])));

        for (Object o : m.values()) {

        }
        AttributeMap attributemap = c.getConstructor(constructor.toArray(new Class[0])).newInstance(
                map.values().toArray());

        AttributeMapChecker.assertAttributeMap(attributemap, map);
    }

}
