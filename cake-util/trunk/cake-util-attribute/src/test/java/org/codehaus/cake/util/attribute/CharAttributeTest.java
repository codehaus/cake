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

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
/**
 * Various tests for {@link CharAttribute}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class CharAttributeTest extends AtrStubs {
    static final CharAttribute ATR0 = new CharAttribute("a0",(char) 0) {};
    static final CharAttribute ATR1 = new CharAttribute("a1",(char) 1) {};
    static final CharAttribute ATR100 = new CharAttribute("a100", (char) 100) {};

    static final CharAttribute NON_NEGATIVE = new CharAttribute("a50", (char) 50) {
        @Override
        public boolean isValid(char value) {
            return value >= (char) 5;
        }
    };

    @Test
    public void _constructors() {
        assertEquals((char) 0, new CharAttribute() {}.getDefault().charValue());
        assertEquals((char) 0, new CharAttribute("a") {}.getDefaultValue());
//        assertFalse(new CharAttribute() {}.getName().equals(new CharAttribute() {}.getName()));
//        assertFalse(new CharAttribute((char) 3) {}.getName().equals(new CharAttribute((char) 3) {}.getName()));
        assertTrue(new CharAttribute("a") {}.getName().equals(new CharAttribute("a") {}.getName()));
        assertEquals((char) 3, new CharAttribute((char) 3) {}.getDefaultValue());
        assertEquals((char) 0, ATR0.getDefaultValue());
        assertEquals((char) 100, ATR100.getDefaultValue());
        assertEquals((char) 100, ATR100.getDefault().charValue());
        assertEquals("a100", ATR100.getName());
        
        assertSame(Character.TYPE, ATR100.getType());
    }
    
    @Test
    public void checkValid() {
        ATR100.checkValid(Character.MIN_VALUE);
        ATR100.checkValid(Character.MAX_VALUE);
        ATR100.checkValid(new Character(Character.MIN_VALUE));
        ATR100.checkValid(new Character(Character.MIN_VALUE));

        NON_NEGATIVE.checkValid((char) 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE() {
        NON_NEGATIVE.checkValid((char) 4);
    }
    
    @Test
    public void comparator() {
        AttributeMap wa1 = ATR1.singleton((char) 1);
        AttributeMap wa2 = ATR1.singleton((char) 2);
        AttributeMap wa22 = ATR1.singleton((char) 2);
        AttributeMap wa3 = ATR1.singleton((char) 3);
        assertEquals(0, ATR1.compare(wa2, wa2));
        assertEquals(0, ATR1.compare(wa2, wa22));
        assertEquals(0, ATR1.compare(wa22, wa2));
        assertTrue(ATR1.compare(wa1, wa2) < 0);
        assertTrue(ATR1.compare(wa2, wa1) > 0);
        assertTrue(ATR1.compare(wa1, wa3) < 0);
        assertTrue(ATR1.compare(wa3, wa2) > 0);
        assertTrue(ATR1.compare(wa2, wa3) < 0);
        
        ArrayList<AttributeMap> al = new ArrayList<AttributeMap>();
        al.add(wa2);
        al.add(wa1);
        Collections.sort(al, ATR1);
        assertSame(wa1, al.get(0));
        assertSame(wa2, al.get(1));
    }
    
    @Test
    public void fromString() {
        assertEquals('f', ATR100.fromString("f"));
        assertEquals('F', ATR100.fromString("F"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromStringIAE() {
        ATR100.fromString("foo");
    }

    @Test
    public void isValid() {
        assertTrue(ATR100.isValid(Character.MIN_VALUE));
        assertTrue(ATR100.isValid(Character.MAX_VALUE));
        assertTrue(ATR100.isValid(Character.valueOf(Character.MIN_VALUE)));
        assertTrue(ATR100.isValid(Character.valueOf(Character.MAX_VALUE)));

        assertTrue(NON_NEGATIVE.isValid((char) 5));
        assertFalse(NON_NEGATIVE.isValid((char) 4));
    }

    @Test
    public void toSingleton() {
        assertEquals((char) -10, ATR100.singleton((char) -10).get(ATR100));
        assertEquals((char) 10, ATR100.singleton((char) 10).get(ATR100));
        assertEquals(Character.MAX_VALUE, ATR100.singleton(Character.MAX_VALUE).get(ATR100));

        assertEquals((char) 10, NON_NEGATIVE.singleton((char) 10).get(NON_NEGATIVE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSingletonIAE() {
        NON_NEGATIVE.singleton((char) 3);
    }
}
