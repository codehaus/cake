package org.codehaus.cake.cache.test.tck.selection;

import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.cache.Cache;
import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;
import org.codehaus.cake.cache.test.util.AtrStubs;
import org.codehaus.cake.ops.Predicates;
import org.junit.Test;

public class SelectedCrud extends AbstractCacheTCKTest {

    @Test
    public void get() {
        c.crud().write().put(1, "A");
        c.crud().write().put(2, "B");
        Cache<Integer, String> s = c.select().onKey(Predicates.equalsToAny(1, 3));
        assertEquals("A", s.crud().value().get(1));
        assertEquals(1, s.crud().entry().get(1).getKey().intValue());
        assertEquals("A", s.crud().entry().get(1).getValue());
        assertEquals(0, c.crud().entry().get(1).getAttributes().size());
        
        assertNotNull(c.crud().value().get(2));
        assertNull(s.crud().value().get(2));
        
        assertNotNull(c.crud().entry().get(2));
        assertNull(s.crud().entry().get(2));

        assertEquals(3, c.crud().attribute(AtrStubs.I_3).get(2).intValue());
        assertEquals(3, s.crud().attribute(AtrStubs.I_3).get(2).intValue());
    }
    
    @Test
    public void getAttributes() {
        conf.withAttributes().add(AtrStubs.I_2);
        newCache();
        c.crud().value().get(1, Attributes.from(AtrStubs.I_2, 1, AtrStubs.L_3, 4L));
        c.crud().value().get(2, Attributes.from(AtrStubs.I_2, 1, AtrStubs.L_3, 4L));
        Cache<Integer, String> s = c.select().onValue(Predicates.equalsToAny("A", "C"));
        
        assertNotNull(c.crud().value().get(2));
        assertNull(s.crud().value().get(2));
        
        assertNotNull(c.crud().entry().get(2));
        assertNull(s.crud().entry().get(2));
        
        assertEquals(1, c.crud().attribute(AtrStubs.I_2).get(1).intValue());
        assertEquals(1, c.crud().attribute(AtrStubs.I_2).get(2).intValue());
        
        assertEquals(1, s.crud().attribute(AtrStubs.I_2).get(1).intValue());
        assertEquals(2, s.crud().attribute(AtrStubs.I_2).get(2).intValue());//Filtered
    }
}
