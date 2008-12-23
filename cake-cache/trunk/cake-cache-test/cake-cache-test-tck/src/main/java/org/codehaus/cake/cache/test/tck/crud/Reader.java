package org.codehaus.cake.cache.test.tck.crud;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.cache.test.util.AtrStubs;
import org.junit.Test;

public class Reader extends AbstractCacheTCKTest {

    @Test
    public void get() {
        c.withCrud().write().put(1, "A");
        assertEquals("A", c.withCrud().value().get(1));
        assertEquals(1, c.withCrud().entry().get(1).getKey().intValue());
        assertEquals("A", c.withCrud().entry().get(1).getValue());
        assertEquals(0, c.withCrud().entry().get(1).size());
    }

    @Test
    public void getWithAttributes() {
        c.withCrud().write().put(1, "A");
        assertEquals("A", c.withCrud().value().get(1, AtrStubs.I_1.singleton(4)));
        assertEquals(1, c.withCrud().entry().get(1).getKey().intValue());
        assertEquals("A", c.withCrud().entry().get(1).getValue());
        assertEquals(0, c.withCrud().entry().get(1).size());
    }

    @Test
    public void getLoading() {
        assertGet(M1, M2);
        assertEquals(0, loader.get(1).getParameters().size());
        assertEquals(0, loader.get(2).getParameters().size());
        assertNotSame(loader.get(1).getParameters(), loader.get(2).getParameters());
    }

    @Test
    public void getAttributesKeepnone() {
        c.withCrud().value().get(1, Attributes.from(AtrStubs.I_2, 1, AtrStubs.L_3, 4L));
        assertEquals(2, loader.get(1).getParameters().size());
        assertEquals(1, loader.get(1).getParameters().get(AtrStubs.I_2));
        assertEquals(4L, loader.get(1).getParameters().get(AtrStubs.L_3));
        assertEquals(0, c.withCrud().entry().get(1).size());
    }
    
    @Test
    public void getAttributesKeepOne() {
        conf.addEntryAttributes(AtrStubs.I_2);
        newCache();
        c.withCrud().value().get(1, Attributes.from(AtrStubs.I_2, 1, AtrStubs.L_3, 4L));
        assertEquals(2, loader.get(1).getParameters().size());
        assertEquals(1, loader.get(1).getParameters().get(AtrStubs.I_2));
        assertEquals(4L, loader.get(1).getParameters().get(AtrStubs.L_3));
        assertEquals(1, c.withCrud().entry().get(1).size());
        assertEquals(1, c.withCrud().attribute(AtrStubs.I_2).get(1).intValue());
    }
}
