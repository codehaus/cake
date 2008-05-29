/* Copyright 2004 - 2008 Kasper Nielsen <kasper@codehaus.org> 
 * Licensed under the Apache 2.0 License. */
package org.codehaus.cake.attribute.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.attribute.AtrStubs;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.test.util.ComparatorTestUtil.Dummy;
import org.codehaus.cake.test.util.ComparatorTestUtil.DummyComparator;
import org.junit.Test;

public class ComparableObjectAttributeTest extends AtrStubs {

    @Test
    public void nameType() {
        ComparableObjectAttribute coa = new ComparableObjectAttribute("foo", Integer.class);
        assertEquals("foo", coa.getName());

        AttributeMap am1 = Attributes.singleton(coa, 1);
        AttributeMap am1_ = Attributes.singleton(coa, 1);
        AttributeMap am2 = Attributes.singleton(coa, 2);

        assertNull(coa.getDefaultValue());
        assertEquals(0, coa.compare(withAtr(am1), withAtr(am1)));
        assertEquals(0, coa.compare(withAtr(am1), withAtr(am1_)));
        assertTrue(coa.compare(withAtr(am1), withAtr(am2)) < 0);
        assertTrue(coa.compare(withAtr(am2), withAtr(am1)) > 0);
    }

    @Test
    public void nameTypeComparator() {
        ComparableObjectAttribute coa = new ComparableObjectAttribute("foo", Dummy.class, new DummyComparator());
        assertEquals("foo", coa.getName());

        AttributeMap am1 = Attributes.singleton(coa, Dummy.D1);
        AttributeMap am1_ = Attributes.singleton(coa, Dummy.D1);
        AttributeMap am2 = Attributes.singleton(coa, Dummy.D2);

        assertNull(coa.getDefaultValue());
        assertEquals(0, coa.compare(withAtr(am1), withAtr(am1)));
        assertEquals(0, coa.compare(withAtr(am1), withAtr(am1_)));
        assertTrue(coa.compare(withAtr(am1), withAtr(am2)) < 0);
        assertTrue(coa.compare(withAtr(am2), withAtr(am1)) > 0);
    }

    @Test(expected = NullPointerException.class)
    public void nameTypeComparator_NPE() {
        new ComparableObjectAttribute("foo", Dummy.class, null);
    }

    @Test
    public void nameTypeDefault() {
        ComparableObjectAttribute coa = new ComparableObjectAttribute("foo", Integer.class, 55);
        assertEquals("foo", coa.getName());
        assertEquals(55, coa.getDefaultValue());
        AttributeMap am1 = Attributes.singleton(coa, 1);
        AttributeMap am1_ = Attributes.singleton(coa, 1);

        assertEquals(0, coa.compare(withAtr(Attributes.EMPTY_ATTRIBUTE_MAP), withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)));
        assertEquals(0, coa.compare(withAtr(am1), withAtr(am1_)));
        assertTrue(coa.compare(withAtr(am1), withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)) < 0);
        assertTrue(coa.compare(withAtr(Attributes.EMPTY_ATTRIBUTE_MAP), withAtr(am1)) > 0);
    }

    @Test
    public void nameTypeDefaultComparator() {
        ComparableObjectAttribute coa = new ComparableObjectAttribute("foo", Integer.class, new DummyComparator(),
                Dummy.D4);
        assertEquals("foo", coa.getName());
        assertEquals(Dummy.D4, coa.getDefaultValue());
        AttributeMap am1 = Attributes.singleton(coa, Dummy.D1);
        AttributeMap am1_ = Attributes.singleton(coa, Dummy.D1);
        AttributeMap am2 = Attributes.singleton(coa, Dummy.D2);

        assertEquals(0, coa.compare(withAtr(Attributes.EMPTY_ATTRIBUTE_MAP), withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)));
        assertEquals(0, coa.compare(withAtr(am1), withAtr(am1_)));
        assertTrue(coa.compare(withAtr(am1), withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)) < 0);
        assertTrue(coa.compare(withAtr(Attributes.EMPTY_ATTRIBUTE_MAP), withAtr(am1)) > 0);
    }
    
    @Test(expected = NullPointerException.class)
    public void nameTypeDefaultComparator_NPE() {
        new ComparableObjectAttribute("foo", Dummy.class, null,Dummy.D4);
    }

}
