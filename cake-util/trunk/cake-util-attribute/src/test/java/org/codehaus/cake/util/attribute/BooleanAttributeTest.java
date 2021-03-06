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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Various tests for {@link BooleanAttribute}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class BooleanAttributeTest extends AtrStubs {
    static final BooleanAttribute ATR0 = new BooleanAttribute("a0", false) {};
    static final BooleanAttribute ATR1 = new BooleanAttribute("a1", true) {};

    static final BooleanAttribute NON_NEGATIVE = new BooleanAttribute("a50", true) {
        @Override
        public boolean isValid(boolean value) {
            return value;
        }
    };

    @Test
    public void _constructors() {
        assertEquals(false, new BooleanAttribute() {}.getDefault().booleanValue());
        assertEquals(false, new BooleanAttribute("a") {}.getDefaultValue());
        // assertFalse(new BooleanAttribute() {}.getName().equals(new BooleanAttribute() {}.getName()));
        // assertFalse(new BooleanAttribute(false) {}.getName().equals(new BooleanAttribute(false) {}.getName()));
        assertTrue(new BooleanAttribute("a") {}.getName().equals(new BooleanAttribute("a") {}.getName()));
        assertEquals(true, new BooleanAttribute(true) {}.getDefaultValue());
        assertEquals(false, ATR0.getDefaultValue());
        assertEquals(true, ATR1.getDefaultValue());
        assertEquals(true, ATR1.getDefault().booleanValue());
        assertEquals("a1", ATR1.getName());

        assertSame(Boolean.TYPE, ATR1.getType());
    }

    @Test
    public void checkValid() {
        ATR1.checkValid(false);
        ATR1.checkValid(true);
        ATR1.checkValid(new Boolean(false));
        ATR1.checkValid(new Boolean(true));

        NON_NEGATIVE.checkValid(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE() {
        NON_NEGATIVE.checkValid(false);
    }

    @Test
    public void comparator() {
        AttributeMap wa1 = ATR1.singleton(false);
        AttributeMap wa2 = ATR1.singleton(true);
        AttributeMap wa22 = ATR1.singleton(true);
        assertEquals(0, ATR1.compare(wa2, wa2));
        assertEquals(0, ATR1.compare(wa2, wa22));
        assertEquals(0, ATR1.compare(wa22, wa2));
        assertTrue(ATR1.compare(wa1, wa2) > 0);
        assertTrue(ATR1.compare(wa2, wa1) < 0);
    }

    @Test
    public void fromString() {
        assertEquals(false, ATR1.fromString("false"));
        assertEquals(true, ATR1.fromString("true"));
    }

    @Test
    public void isValid() {
        assertTrue(ATR1.isValid(false));
        assertTrue(ATR1.isValid(true));
        assertTrue(ATR1.isValid(Boolean.valueOf(true)));
        assertTrue(ATR1.isValid(Boolean.valueOf(false)));

        assertTrue(NON_NEGATIVE.isValid(true));
        assertFalse(NON_NEGATIVE.isValid(false));
    }

    @Test
    public void toSingleton() {
        assertTrue(ATR1.singleton(true).get(ATR1));
        assertFalse(ATR1.singleton(false).get(ATR1));
        assertTrue(ATR1.singletonTrue().get(ATR1));
        assertFalse(ATR1.singletonFalse().get(ATR1));

        assertTrue(NON_NEGATIVE.singleton(true).get(NON_NEGATIVE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSingletonIAE() {
        NON_NEGATIVE.singleton(false);
    }

}
