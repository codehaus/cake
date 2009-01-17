package org.codehaus.cake.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.MutableAttributeMap;
import org.codehaus.cake.attribute.DefaultAttributeMap;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.LongAttribute;
import org.junit.Test;

public class Caches_newEntryTest {
    Attribute a = new LongAttribute("A") {};
    Attribute a1 = new LongAttribute("B") {};
    private final AttributeMap am = a.singleton(3L);
    private final AttributeMap am1 = a1.singleton(4L);

    @Test
    public void newEntry() {
        CacheEntry<Integer, Integer> me = Caches.newEntry(0, 1);
        assertEquals(0, me.getKey().intValue());
        assertEquals(1, me.getValue().intValue());
        assertEquals(0, me.size());
        assertTrue(me.toString().contains("0=1"));
    }

    @Test
    public void newEntryAttributes() {
        CacheEntry<Integer, Integer> me = Caches.newEntry(0, 1, am);
        assertEquals(0, me.getKey().intValue());
        assertEquals(1, me.getValue().intValue());
        //assertSame(am, me.getAttributes());
        assertEquals("0=1 [A=3]", me.toString());
        MutableAttributeMap map = new DefaultAttributeMap();
        map.put(a, 5);
        map.put(a1, 6);
        me = Caches.newEntry(0, 1, map);
        assertTrue(me.toString().equals("0=1 [A=5, B=6]") || me.toString().equals("0=1 [B=6, A=5]"));
    }

    @Test(expected = NullPointerException.class)
    public void newEntryAttributesNPE() {
        Caches.newEntry("key", "value", null);
    }

    @Test(expected = NullPointerException.class)
    public void newEntryKeyNPE() {
        Caches.newEntry(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void newEntryValueNPE() {
        Caches.newEntry("key", null);
    }

    @Test
    public void simpleImmutableEntryEquals() {
        CacheEntry<Integer, Integer> me = Caches.newEntry(0, 1);
        assertFalse(Caches.newEntry(0, 1).equals(null));
        assertFalse(Caches.newEntry(0, 1).equals(new Object()));
        assertTrue(me.equals(me));
        assertFalse(Caches.newEntry(0, 1).equals(Caches.newEntry(0, 0)));
        assertFalse(Caches.newEntry(0, 1).equals(Caches.newEntry(1, 1)));
        assertTrue(Caches.newEntry(0, 1).equals(Caches.newEntry(0, 1)));
        // assertFalse(Caches.newEntry(0, 1).equals(Caches.newEntry(0, 1, am1)));
        assertTrue(Caches.newEntry(0, 1, am1).equals(Caches.newEntry(0, 1, am1)));
        // assertFalse(Caches.newEntry(0, 1, am).equals(Caches.newEntry(0, 1, am1)));
    }

    @Test
    public void simpleImmutableEntryHashcode() {
        assertEquals(100 ^ 200, Caches.newEntry(100, 200).hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void simpleImmutableEntrySetValueUOE() {
        Caches.newEntry(0, 1).setValue(2);
    }


}
