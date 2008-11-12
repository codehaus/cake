package org.codehaus.cake.cache.test.tck.attributes;

import static org.codehaus.cake.cache.test.util.AtrStubs.O_1;
import static org.codehaus.cake.cache.test.util.AtrStubs.O_4;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.ops.Predicates;
import org.junit.Test;

public class ObjectAttributes extends AbstractCacheTCKTest {

    @Test
    public void objectAttribute() {
        conf.withAttributes().add(O_4);
        newCache();
        assertNull(c.crud().attribute(O_4).get(10));
        c.crud().write().put(10, "A", O_4.singleton("foo"));
        assertEquals("foo", c.crud().attribute(O_4).get(10));
        c.put(10, "value");
        assertNull(c.crud().attribute(O_4).get(10));
    }

    @Test
    public void objectAttributeDefault() {
        conf.withAttributes().add(O_1);
        newCache();
        assertEquals("1.5", c.crud().attribute(O_1).get(10));
        c.crud().write().put(10, "A", O_1.singleton("foo"));
        assertEquals("foo", c.crud().attribute(O_1).get(10));
    }

    @Test
    public void objectAttributeDefaultPreExisting() {
        conf.withAttributes().add(O_1);
        newCache();
        assertEquals("1.5", c.crud().attribute(O_1).get(1));
        c.crud().write().put(1, "A", O_1.singleton("foo"));
        assertEquals("foo", c.crud().attribute(O_1).get(1));
    }

    @Test
    public void noLoader() {
        newConfigurationClean();
        conf.withAttributes().add(O_1, O_4);
        newCache();
        assertNull(c.select().on(Predicates.FALSE).crud().attribute(O_4).get(10));
        assertEquals("1.5", c.select().on(Predicates.FALSE).crud().attribute(O_1).get(10));
        assertEquals("1.5", c.select().on(O_1, Predicates.notEqualsTo("1.5")).crud().attribute(O_1).get(10));

    }
    @Test
    public void objectAttributeWithSelector() {
        conf.withAttributes().add(O_1, O_4);
        newCache();
        assertNull(c.select().on(Predicates.FALSE).crud().attribute(O_4).get(10));
        assertEquals("1.5", c.select().on(Predicates.FALSE).crud().attribute(O_1).get(10));
        assertEquals("1.5", c.select().on(O_1, Predicates.notEqualsTo("1.5")).crud().attribute(O_1).get(10));

        c.crud().write().put(10, "A", Attributes.from(O_1, "foo", O_4, "foo2"));
        assertNull(c.select().on(Predicates.FALSE).crud().attribute(O_4).get(10));
        assertEquals("1.5", c.select().on(Predicates.FALSE).crud().attribute(O_1).get(10));
        assertEquals("foo", c.select().on(Predicates.TRUE).crud().attribute(O_1).get(10));
        assertEquals("foo2", c.select().on(Predicates.TRUE).crud().attribute(O_4).get(10));

        assertEquals("1.5", c.select().on(O_1, Predicates.notEqualsTo("foo")).crud().attribute(O_1).get(10));
    }
}
