package org.codehaus.cake.cache.test.tck.selection;

import org.codehaus.cake.cache.test.tck.AbstractCacheTCKTest;

public class SelectedCrud extends AbstractCacheTCKTest {

//    @Test
//    public void get() {
//        c.withCrud().write().put(1, "A");
//        c.withCrud().write().put(2, "B");
//        Cache<Integer, String> s = c.filter().onKey(Predicates.equalsToAny(1, 3));
//        assertEquals("A", s.withCrud().value().get(1));
//        assertEquals(1, s.withCrud().entry().get(1).getKey().intValue());
//        assertEquals("A", s.withCrud().entry().get(1).getValue());
//        assertEquals(0, c.withCrud().entry().get(1).size());
//        
//        assertNotNull(c.withCrud().value().get(2));
//        assertNull(s.withCrud().value().get(2));
//        
//        assertNotNull(c.withCrud().entry().get(2));
//        assertNull(s.withCrud().entry().get(2));
//
//        assertEquals(3, c.withCrud().attribute(AtrStubs.I_3).get(2).intValue());
//        assertEquals(3, s.withCrud().attribute(AtrStubs.I_3).get(2).intValue());
//    }
//    
//    @Test
//    public void getAttributes() {
//        conf.addEntryAttributes(AtrStubs.I_2);
//        newCache();
//        c.withCrud().value().get(1, Attributes.from(AtrStubs.I_2, 1, AtrStubs.L_3, 4L));
//        c.withCrud().value().get(2, Attributes.from(AtrStubs.I_2, 1, AtrStubs.L_3, 4L));
//        Cache<Integer, String> s = c.filter().onValue(Predicates.equalsToAny("A", "C"));
//        
//        assertNotNull(c.withCrud().value().get(2));
//        assertNull(s.withCrud().value().get(2));
//        
//        assertNotNull(c.withCrud().entry().get(2));
//        assertNull(s.withCrud().entry().get(2));
//        
//        assertEquals(1, c.withCrud().attribute(AtrStubs.I_2).get(1).intValue());
//        assertEquals(1, c.withCrud().attribute(AtrStubs.I_2).get(2).intValue());
//        
//        assertEquals(1, s.withCrud().attribute(AtrStubs.I_2).get(1).intValue());
//        assertEquals(2, s.withCrud().attribute(AtrStubs.I_2).get(2).intValue());//Filtered
//    }
}
