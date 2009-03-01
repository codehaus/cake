/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.util.attribute.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.codehaus.cake.test.util.ComparatorTestUtil.Dummy;
import org.codehaus.cake.test.util.ComparatorTestUtil.DummyComparator;
import org.codehaus.cake.util.attribute.AtrStubs;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.attribute.common.ComparableObjectAttribute;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class ComparableObjectAttributeTest extends AtrStubs {

    @Test
    public void nameType() {
        ComparableObjectAttribute<Integer> coa = new ComparableObjectAttribute<Integer>("foo", Integer.class, false) {};
        assertEquals("foo", coa.getName());

        AttributeMap am1 = Attributes.singleton(coa, 1);
        AttributeMap am1_ = Attributes.singleton(coa, 1);
        AttributeMap am2 = Attributes.singleton(coa, 2);

        assertNull(coa.getDefault());
        assertEquals(0, coa.compare(am1, am1));
        assertEquals(0, coa.compare(am1, am1_));
        assertTrue(coa.compare(am1, am2) < 0);
        assertTrue(coa.compare(am2, am1) > 0);
    }

    static class DD extends ComparableObjectAttribute<Object> {

        public DD(String name, Class clazz, boolean nullIsLeast) {
            super(name, clazz, nullIsLeast);
            // TODO Auto-generated constructor stub
        }

    }

    @Test
    public void nameTypeNull() {
        ComparableObjectAttribute coaf = new ComparableObjectAttribute<Integer>("foo", Integer.class, false) {};
        ComparableObjectAttribute coat = new ComparableObjectAttribute<Integer>("foo", Integer.class, true) {};

        assertEquals(0, coaf.compare(coaf.singleton(null), Attributes.EMPTY_ATTRIBUTE_MAP));
        assertEquals(0, coaf.compare(coaf.singleton(null), coaf.singleton(null)));
        assertTrue(coaf.compare(coaf.singleton(null), coaf.singleton(-1)) > 0);
        assertTrue(coaf.compare(coaf.singleton(3), coaf.singleton(null)) < 0);

        assertEquals(0, coat.compare(coat.singleton(null), Attributes.EMPTY_ATTRIBUTE_MAP));
        assertEquals(0, coat.compare(coat.singleton(null), coat.singleton(null)));
        assertTrue(coat.compare(coat.singleton(null), coat.singleton(-1)) < 0);
        assertTrue(coat.compare(coat.singleton(3), coat.singleton(null)) > 0);

    }

    @Test
    public void nameTypeComparator() {
        ComparableObjectAttribute<Dummy> coa = new ComparableObjectAttribute<Dummy>("foo", Dummy.class,
                new DummyComparator()) {};
        assertEquals("foo", coa.getName());

        AttributeMap am1 = Attributes.singleton(coa, Dummy.D1);
        AttributeMap am1_ = Attributes.singleton(coa, Dummy.D1);
        AttributeMap am2 = Attributes.singleton(coa, Dummy.D2);

        assertNull(coa.getDefault());
        assertEquals(0, coa.compare(am1, am1));
        assertEquals(0, coa.compare(am1, am1_));
        assertTrue(coa.compare(am1, am2) < 0);
        assertTrue(coa.compare(am2, am1) > 0);
    }

    @Test(expected = NullPointerException.class)
    public void nameTypeComparator_NPE() {
        new ComparableObjectAttribute<Dummy>("foo", Dummy.class, null) {};
    }

    @Test(expected = IllegalArgumentException.class)
    public void nameTypeComparator_IAE() {
        new ComparableObjectAttribute<Dummy>("foo", Dummy.class, false) {};
    }

    @Test(expected = IllegalArgumentException.class)
    public void nameTypeComparator_IAE2() {
        new ComparableObjectAttribute<Dummy>("foo", Dummy.class, Dummy.D1, false) {};
    }

    @Test
    public void nameTypeDefault() {
        ComparableObjectAttribute coa = new ComparableObjectAttribute<Integer>("foo", Integer.class, 55, false) {};
        assertEquals("foo", coa.getName());
        assertEquals(55, coa.getDefault());
        AttributeMap am1 = Attributes.singleton(coa, 1);
        AttributeMap am1_ = Attributes.singleton(coa, 1);

        assertEquals(0, coa.compare(Attributes.EMPTY_ATTRIBUTE_MAP, Attributes.EMPTY_ATTRIBUTE_MAP));
        assertEquals(0, coa.compare(am1, am1_));
        assertTrue(coa.compare(am1, Attributes.EMPTY_ATTRIBUTE_MAP) < 0);
        assertTrue(coa.compare(Attributes.EMPTY_ATTRIBUTE_MAP, am1) > 0);
    }

    @Test
    public void nameTypeDefaultComparator() {
        // Don't remove casts, needed by some silly old sun compilers
        ComparableObjectAttribute coa = new ComparableObjectAttribute<Object>("foo", (Class) Integer.class,
                (Comparator) new DummyComparator(), Dummy.D4) {};
        assertEquals("foo", coa.getName());
        assertEquals(Dummy.D4, coa.getDefault());
        AttributeMap am1 = Attributes.singleton(coa, Dummy.D1);
        AttributeMap am1_ = Attributes.singleton(coa, Dummy.D1);
        AttributeMap am2 = Attributes.singleton(coa, Dummy.D2);

        assertEquals(0, coa.compare(Attributes.EMPTY_ATTRIBUTE_MAP, Attributes.EMPTY_ATTRIBUTE_MAP));
        assertEquals(0, coa.compare(am1, am1_));
        assertTrue(coa.compare(am1, Attributes.EMPTY_ATTRIBUTE_MAP) < 0);
        assertTrue(coa.compare(Attributes.EMPTY_ATTRIBUTE_MAP, am1) > 0);
    }

    @Test(expected = NullPointerException.class)
    public void nameTypeDefaultComparator_NPE() {
        new ComparableObjectAttribute<Dummy>("foo", Dummy.class, null, Dummy.D4) {};
    }

}
