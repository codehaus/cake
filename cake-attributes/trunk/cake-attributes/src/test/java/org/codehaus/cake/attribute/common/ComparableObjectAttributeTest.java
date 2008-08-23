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
package org.codehaus.cake.attribute.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.codehaus.cake.attribute.AtrStubs;
import org.codehaus.cake.attribute.AttributeMap;
import org.codehaus.cake.attribute.Attributes;
import org.codehaus.cake.test.util.ComparatorTestUtil.Dummy;
import org.codehaus.cake.test.util.ComparatorTestUtil.DummyComparator;
import org.junit.Test;

public class ComparableObjectAttributeTest extends AtrStubs {
 
  @Test
  public void nameType() {
    ComparableObjectAttribute coa = new ComparableObjectAttribute<Integer>("foo", Integer.class, false) {
    };
    assertEquals("foo", coa.getName());

    AttributeMap am1 = Attributes.singleton(coa, 1);
    AttributeMap am1_ = Attributes.singleton(coa, 1);
    AttributeMap am2 = Attributes.singleton(coa, 2);

    assertNull(coa.getDefault());
    assertEquals(0, coa.compare(withAtr(am1), withAtr(am1)));
    assertEquals(0, coa.compare(withAtr(am1), withAtr(am1_)));
    assertTrue(coa.compare(withAtr(am1), withAtr(am2)) < 0);
    assertTrue(coa.compare(withAtr(am2), withAtr(am1)) > 0);
  }

  static class DD extends ComparableObjectAttribute<Object> {

    public DD(String name, Class clazz, boolean nullIsLeast) {
      super(name, clazz, nullIsLeast);
      // TODO Auto-generated constructor stub
    }


  }

  @Test
  public void nameTypeNull() {
    ComparableObjectAttribute coaf = new ComparableObjectAttribute<Integer>("foo", Integer.class, false) {
    };
    ComparableObjectAttribute coat = new ComparableObjectAttribute<Integer>("foo", Integer.class, true) {
    };

    assertEquals(0, coaf.compare(withAtr(coaf.singleton(null)),
      withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)));
    assertEquals(0, coaf.compare(withAtr(coaf.singleton(null)), withAtr(coaf.singleton(null))));
    assertTrue(coaf.compare(withAtr(coaf.singleton(null)), withAtr(coaf.singleton(-1))) > 0);
    assertTrue(coaf.compare(withAtr(coaf.singleton(3)), withAtr(coaf.singleton(null))) < 0);

    assertEquals(0, coat.compare(withAtr(coat.singleton(null)),
      withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)));
    assertEquals(0, coat.compare(withAtr(coat.singleton(null)), withAtr(coat.singleton(null))));
    assertTrue(coat.compare(withAtr(coat.singleton(null)), withAtr(coat.singleton(-1))) < 0);
    assertTrue(coat.compare(withAtr(coat.singleton(3)), withAtr(coat.singleton(null))) > 0);

  }

  @Test
  public void nameTypeComparator() {
    ComparableObjectAttribute coa = new ComparableObjectAttribute<Dummy>("foo", Dummy.class,
      new DummyComparator()) {
    };
    assertEquals("foo", coa.getName());

    AttributeMap am1 = Attributes.singleton(coa, Dummy.D1);
    AttributeMap am1_ = Attributes.singleton(coa, Dummy.D1);
    AttributeMap am2 = Attributes.singleton(coa, Dummy.D2);

    assertNull(coa.getDefault());
    assertEquals(0, coa.compare(withAtr(am1), withAtr(am1)));
    assertEquals(0, coa.compare(withAtr(am1), withAtr(am1_)));
    assertTrue(coa.compare(withAtr(am1), withAtr(am2)) < 0);
    assertTrue(coa.compare(withAtr(am2), withAtr(am1)) > 0);
  }

  @Test(expected = NullPointerException.class)
  public void nameTypeComparator_NPE() {
    new ComparableObjectAttribute<Dummy>("foo", Dummy.class, null) {
    };
  }

  @Test(expected = IllegalArgumentException.class)
  public void nameTypeComparator_IAE() {
    new ComparableObjectAttribute<Dummy>("foo", Dummy.class, false) {
    };
  }

  @Test(expected = IllegalArgumentException.class)
  public void nameTypeComparator_IAE2() {
    new ComparableObjectAttribute<Dummy>("foo", Dummy.class, Dummy.D1, false) {
    };
  }

  @Test
  public void nameTypeDefault() {
    ComparableObjectAttribute coa = new ComparableObjectAttribute<Integer>("foo", Integer.class, 55, false) {
    };
    assertEquals("foo", coa.getName());
    assertEquals(55, coa.getDefault());
    AttributeMap am1 = Attributes.singleton(coa, 1);
    AttributeMap am1_ = Attributes.singleton(coa, 1);

    assertEquals(0, coa.compare(withAtr(Attributes.EMPTY_ATTRIBUTE_MAP),
      withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)));
    assertEquals(0, coa.compare(withAtr(am1), withAtr(am1_)));
    assertTrue(coa.compare(withAtr(am1), withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)) < 0);
    assertTrue(coa.compare(withAtr(Attributes.EMPTY_ATTRIBUTE_MAP), withAtr(am1)) > 0);
  }

  @Test
  public void nameTypeDefaultComparator() {
    //Don't remove casts, needed by some silly old sun compilers
    ComparableObjectAttribute coa = new ComparableObjectAttribute<Object>("foo", (Class) Integer.class,
     (Comparator) new DummyComparator(), Dummy.D4) {
    };
    assertEquals("foo", coa.getName());
    assertEquals(Dummy.D4, coa.getDefault());
    AttributeMap am1 = Attributes.singleton(coa, Dummy.D1);
    AttributeMap am1_ = Attributes.singleton(coa, Dummy.D1);
    AttributeMap am2 = Attributes.singleton(coa, Dummy.D2);

    assertEquals(0, coa.compare(withAtr(Attributes.EMPTY_ATTRIBUTE_MAP),
      withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)));
    assertEquals(0, coa.compare(withAtr(am1), withAtr(am1_)));
    assertTrue(coa.compare(withAtr(am1), withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)) < 0);
    assertTrue(coa.compare(withAtr(Attributes.EMPTY_ATTRIBUTE_MAP), withAtr(am1)) > 0);
  }

  @Test(expected = NullPointerException.class)
  public void nameTypeDefaultComparator_NPE() {
    new ComparableObjectAttribute<Dummy>("foo", Dummy.class, null, Dummy.D4) {
    };
  }

}
