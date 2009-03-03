package org.codehaus.cake.cache.test.tck.attributes;

import static org.codehaus.cake.util.attribute.AtrStubs.*;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.ops.Predicates;
import org.junit.Ignore;
import org.junit.Test;
public class ObjectAttributes extends AbstractCacheTCKTest {

    @Test
    public void objectAttribute() {
        conf.addEntryAttributes(O_4);
        newCache();
        assertNull(c.getEntry(5).get(O_4));
        c.withCrud().write().put(5, "A", O_4.singleton("foo"));
        assertEquals("foo", c.getEntry(5).get(O_4));
        c.put(5, "value");
        assertNull(c.getEntry(5).get(O_4));
    }

    @Test
    public void objectAttributeDefault() {
        conf.addEntryAttributes(O_1);
        newCache();
        assertEquals("1.5", c.getEntry(5).get(O_1));
        c.withCrud().write().put(5, "A", O_1.singleton("foo"));
        assertEquals("foo", c.getEntry(5).get(O_1));
    }

    @Test
    public void objectAttributeDefaultPreExisting() {
        conf.addEntryAttributes(O_1);
        newCache();
        assertEquals("1.5", c.getEntry(1).get(O_1));
        c.withCrud().write().put(1, "A", O_1.singleton("foo"));
        assertEquals("foo", c.getEntry(1).get(O_1));
    }

    @Test @Ignore
    public void noLoader() {
        newConfigurationClean();
        conf.addEntryAttributes(O_1, O_4);
        newCache();
        assertNull(c.filter().on(Predicates.FALSE).getEntry(10).get(O_4));
        assertEquals("1.5", c.filter().on(Predicates.FALSE).getEntry(10).get(O_1));
        assertEquals("1.5", c.filter().on(O_1, Predicates.notEqualsTo("1.5")).getEntry(10).get(O_1));

    }
    @Test @Ignore
    public void objectAttributeWithSelector() {
        conf.addEntryAttributes(O_1, O_4);
        newCache();
        assertNull(c.filter().on(Predicates.FALSE).getEntry(10).get(O_4));
        assertEquals("1.5", c.filter().on(Predicates.FALSE).getEntry(10).get(O_1));
        assertEquals("1.5", c.filter().on(O_1, Predicates.notEqualsTo("1.5")).getEntry(10).get(O_1));

        c.withCrud().write().put(10, "A", Attributes.from(O_1, "foo", O_4, "foo2"));
        assertNull(c.filter().on(Predicates.FALSE).getEntry(10).get(O_4));
        assertEquals("1.5", c.filter().on(Predicates.FALSE).getEntry(10).get(O_1));
        assertEquals("foo", c.filter().on(Predicates.TRUE).getEntry(10).get(O_1));
        assertEquals("foo2", c.filter().on(Predicates.TRUE).getEntry(10).get(O_4));

        assertEquals("1.5", c.filter().on(O_1, Predicates.notEqualsTo("foo")).getEntry(10).get(O_1));
    }
}
