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
package org.codehaus.cake.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class AttributeTest {
    static final Attribute<String> ATR = new DefaultAttribute("name", String.class, "default");
    static final Attribute<String> ATR_VALIDATE = new ValidateAttribute("fooignore");

    // ValidateAttribute
    MutableAttributeMap am1 = new DefaultAttributeMap();

    AttributeMap am2 = Attributes.singleton(ATR, "value");

    @Test(expected = NullPointerException.class)
    public void abstractAttributeNPE() {
        new DefaultAttribute(null, String.class, "default");
    }

    @Test(expected = NullPointerException.class)
    public void abstractAttributeNPE1() {
        new DefaultAttribute("asd", null, "default");
    }

    @Test
    public void checkValid() {
        ATR_VALIDATE.checkValid("foowerwer");
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE() {
        ATR_VALIDATE.checkValid("werwer");
    }


    // @Test
    // public void get() {
    // assertEquals("default", ATR.get(am1));
    // assertEquals("value", ATR.get(am2));
    // assertEquals("foo", ATR.get(am1, "foo"));
    // assertEquals("value", ATR.get(am2, "foo"));
    //
    // assertEquals("default", ATR.get(withAtr(am1)));
    // assertEquals("value", ATR.get(withAtr(am2)));
    // assertEquals("foo", ATR.get(withAtr(am1), "foo"));
    // assertEquals("value", ATR.get(withAtr(am2), "foo"));
    //
    // }
    //
    @Test(expected = IllegalArgumentException.class)
    public void illegalDefaultValue() {
        new ValidateAttribute("erer");
    }

 
    //
    protected MutableAttributeMap newMap() {
        return new DefaultAttributeMap();
    }

    //
    @Test
    public void nullDefaultValue() {
        assertNull(new DefaultAttribute("name", String.class, null).getDefault());
    }

    // @Test(expected = NullPointerException.class)
    // public void setNPE() {
    // ATR.set((AttributeMap) null, "value");
    // }
    //
    // @Test
    // public void setValidate() {
    // Attribute a = new ValidateAttribute("fooignore");
    // a.set(new DefaultAttributeMap(), "fooasd");
    // }
    //
    // @Test(expected = UnsupportedOperationException.class)
    // public void fromStringUOE() {
    // Attribute a = new ValidateAttribute("fooignore");
    // a.fromString("foo");
    // }
    //
    @Test
    public void test() {
        assertEquals("name", ATR.getName());
        assertEquals(String.class, ATR.getType());
        assertEquals("default", ATR.getDefault());
    }

    //
    // @Test
    // public void set() {
    // AttributeMap am = new DefaultAttributeMap();
    // assertEquals(0, am.size());
    //        
    // assertSame(am, ATR.set(am, "a"));
    // assertEquals(1, am.size());
    // assertEquals("a", am.get(ATR));
    //        
    // assertSame(am, ATR.set(am, null));
    // assertEquals(1, am.size());
    // assertNull(am.get(ATR));
    // }
    // @Test
    // public void setWith() {
    // AttributeMap am = new DefaultAttributeMap();
    // assertEquals(0, am.size());
    // assertSame(am, ATR.set(withAtr(am), "a"));
    // assertEquals(1, am.size());
    // assertEquals("a", am.get(ATR));
    // }
    // @Test(expected = IllegalArgumentException.class)
    // public void setIllegal() {
    // Attribute a = new ValidateAttribute("fooignore");
    // a.set(new DefaultAttributeMap(), "asd");
    // }

    @Test
    public void testConstructor() {
        Attribute<String> a = new Attribute(String.class, "default") {
            public void checkValid(Object value) {
                
            }

            public boolean isValid(Object value) {
                return true;
            }
        };
        assertEquals(String.class, a.getType());
        assertEquals("default", a.getDefault());
        assertNotNull(a.getName());
        a.checkValid(null);
        a.checkValid("default");
        assertTrue(a.isValid(null));
        assertTrue(a.isValid("default"));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNPE() {
        new Attribute(null, "default") {
            public void checkValid(Object value) {
                throw new UnsupportedOperationException();
            }

            public boolean isValid(Object value) {
                throw new UnsupportedOperationException();
            }
        };
    }

    //
    // @Test
    // public void toSingleton() {
    // AttributeMap map = ATR.singleton("singleton");
    // assertEquals(1, map.size());
    // assertTrue(map.contains(ATR));
    // assertEquals("singleton", map.get(ATR));
    //
    // map = ATR.singleton(null);
    // assertEquals(1, map.size());
    // assertTrue(map.contains(ATR));
    // assertNull(map.get(ATR));
    // }
    //
    // @Test(expected = IllegalArgumentException.class)
    // public void toSingletonIAE() {
    // ATR_VALIDATE.singleton("sdfsdf");
    // }
    //
    // @Test
    // public void toStrng() {
    // assertEquals("name", ATR.toString());
    // }
    //
    // @Test
    // public void unSet() {
    // AttributeMap am1 = new DefaultAttributeMap();
    // assertEquals(0, am1.size());
    // ATR.remove(am1);
    // assertEquals(0, am1.size());
    // }
    //
    @Test
    public void valid() {
        assertTrue(ATR.isValid(""));
        assertTrue(ATR.isValid(null));
        ATR.checkValid("");
        ATR.checkValid(null);
    }

    // @Test
    // public void map() {
    // TestUtil.assertIsSerializable(ATR.map());
    // AttributeMap am = Attributes.singleton(ATR, "abc");
    // assertEquals("abc", ATR.map().op(withAtr(am)));
    // assertEquals("default", ATR.map().op(withAtr(Attributes.EMPTY_ATTRIBUTE_MAP)));
    // }
    //
    // @Test(expected = NullPointerException.class)
    // public void mapper() {
    // ATR.map().op(null);
    // }
    //
    // @Test(expected = NullPointerException.class)
    // public void filterNPE() {
    // ATR.filter(null);
    // }
    //
    // @Test
    // public void filter() {
    // Predicate<WithAttributes> filter = ATR.filter(StringOps.startsWith("A"));
    // assertTrue(filter.op(withAtr(Attributes.singleton(ATR, "Adf"))));
    // assertFalse(filter.op(withAtr(Attributes.singleton(ATR, "Bdf"))));
    // assertFalse(filter.op(withAtr(Attributes.singleton(ATR, "adf"))));
    // }


    static class DefaultAttribute extends ObjectAttribute<String> {
        public DefaultAttribute(String name, Class<String> clazz, String defaultValue) {
            super(name, clazz, defaultValue);
        }

        // public String fromString(String str) {
        // return str;
        // }
    }

    static class TestAttribute extends Attribute {

        public TestAttribute(Class clazz, Object defaultValue) {
            super(clazz, defaultValue);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void checkValid(Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isValid(Object value) {
            throw new UnsupportedOperationException();
        }

    }

    static class ValidateAttribute extends DefaultAttribute {
        public ValidateAttribute(String defaultValue) {
            super("validate", String.class, defaultValue);
        }

        @Override
        public boolean isValid(String value) {
            return value.startsWith("foo");
        }
    }
}
