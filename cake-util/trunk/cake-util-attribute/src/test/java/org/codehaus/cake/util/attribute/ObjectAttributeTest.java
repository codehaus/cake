/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.util.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Various tests for {@link ObjectAttribute}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class ObjectAttributeTest extends AtrStubs {

    static final ObjectAttribute<Object> NON_NULL = new ObjectAttribute<Object>("a50", Object.class, true) {
        @Override
        public boolean isValid(Object value) {
            return value != null;
        }
    };
    static final ObjectAttribute<Object> NULL = new ObjectAttribute<Object>("a50", Object.class, null) {
        @Override
        public boolean isValid(Object value) {
            return value == null;
        }
    };

    @Test
    public void _constructors() {
        assertNull(new ObjectAttribute<Object>(Object.class) {}.getDefault());
//        assertFalse(new ObjectAttribute<Object>(Object.class) {}.getName().equals(
//                new ObjectAttribute<Object>(Object.class) {}.getName()));
        assertSame(String.class, new ObjectAttribute<String>(String.class) {}.getType());

        assertEquals("foo", new ObjectAttribute<Object>(Object.class, "foo") {}.getDefault());
//        assertFalse(new ObjectAttribute<Object>(Object.class, "foo") {}.getName().equals(
//                new ObjectAttribute<Object>(Object.class) {}.getName()));
        assertSame(String.class, new ObjectAttribute<String>(String.class, "foo") {}.getType());

        assertNull(new ObjectAttribute<Object>("A", Object.class) {}.getDefault());
        assertEquals("B", new ObjectAttribute<Object>("B", Object.class) {}.getName());
        assertSame(String.class, new ObjectAttribute<String>("C", String.class) {}.getType());

        assertEquals("foo", new ObjectAttribute<Object>("A", Object.class, "foo") {}.getDefault());
        assertEquals("B", new ObjectAttribute<Object>("B", Object.class, "foo") {}.getName());
        assertSame(String.class, new ObjectAttribute<String>("C", String.class, "foo") {}.getType());
    }

    @Test
    public void checkValid() {
        NULL.checkValid(null);
        NON_NULL.checkValid(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE() {
        NULL.checkValid("null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE1() {
        NON_NULL.checkValid(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void fromString() {
        NON_NULL.fromString("");
    }

    @Test
    public void isValid() {
        assertTrue(NULL.isValid(null));
        assertTrue(NON_NULL.isValid(true));

        assertFalse(NULL.isValid("null"));
        assertFalse(NON_NULL.isValid(null));
    }

    @Test
    public void set() {
        MutableAttributeMap am = new DefaultAttributeMap();
        NON_NULL.set(am, true);
        assertTrue((Boolean) am.get(NON_NULL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setIAE() {
        NON_NULL.set(new DefaultAttributeMap(), null);
    }

    @Test(expected = NullPointerException.class)
    public void setNPE() {
        NON_NULL.set((MutableAttributeMap) null, false);
    }

    @Test
    public void toSingleton() {
        assertEquals("foo", NON_NULL.singleton("foo").get(NON_NULL));
        assertEquals(1, NON_NULL.singleton(1).get(NON_NULL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSingletonIAE() {
        NON_NULL.singleton(null);
    }
}
