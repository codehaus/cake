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
 * Various tests for {@link ByteAttribute}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class ByteAttributeTest extends AtrStubs {
    static final ByteAttribute ATR0 = new ByteAttribute("a0",(byte) 0) {};
    static final ByteAttribute ATR1 = new ByteAttribute("a1",(byte) 1) {};
    static final ByteAttribute ATR100 = new ByteAttribute("a100", (byte) 100) {};

    static final ByteAttribute NON_NEGATIVE = new ByteAttribute("a50", (byte) 50) {
        @Override
        public boolean isValid(byte value) {
            return value >= (byte) 5;
        }
    };

    @Test
    public void _constructors() {
        assertEquals((byte) 0, new ByteAttribute() {}.getDefault().byteValue());
        assertEquals((byte) 0, new ByteAttribute("a") {}.getDefaultValue());
//        assertFalse(new ByteAttribute() {}.getName().equals(new ByteAttribute() {}.getName()));
//        assertFalse(new ByteAttribute((byte) 3) {}.getName().equals(new ByteAttribute((byte) 3) {}.getName()));
        assertTrue(new ByteAttribute("a") {}.getName().equals(new ByteAttribute("a") {}.getName()));
        assertEquals((byte) 3, new ByteAttribute((byte) 3) {}.getDefaultValue());
        assertEquals((byte) 0, ATR0.getDefaultValue());
        assertEquals((byte) 100, ATR100.getDefaultValue());
        assertEquals((byte) 100, ATR100.getDefault().byteValue());
        assertEquals("a100", ATR100.getName());
        
        assertSame(Byte.TYPE, ATR100.getType());
    }
    
    @Test
    public void checkValid() {
        ATR100.checkValid(Byte.MIN_VALUE);
        ATR100.checkValid(Byte.MAX_VALUE);
        ATR100.checkValid(new Byte(Byte.MIN_VALUE));
        ATR100.checkValid(new Byte(Byte.MIN_VALUE));

        NON_NEGATIVE.checkValid((byte) 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE() {
        NON_NEGATIVE.checkValid((byte) 4);
    }
    
    @Test
    public void comparator() {
        AttributeMap wa1 = ATR1.singleton((byte) 1);
        AttributeMap wa2 = ATR1.singleton((byte) 2);
        AttributeMap wa22 = ATR1.singleton((byte) 2);
        AttributeMap wa3 = ATR1.singleton((byte) 3);
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
        assertEquals((byte) -1, ATR100.fromString(Integer.valueOf(-1).toString()));
        assertEquals(Byte.MIN_VALUE, ATR100.fromString(new Byte(Byte.MIN_VALUE).toString()));
        assertEquals(Byte.MAX_VALUE, ATR100.fromString(new Byte(Byte.MAX_VALUE).toString()));
    }

    @Test
    public void isValid() {
        assertTrue(ATR100.isValid(Byte.MIN_VALUE));
        assertTrue(ATR100.isValid(Byte.MAX_VALUE));
        assertTrue(ATR100.isValid(Byte.valueOf(Byte.MIN_VALUE)));
        assertTrue(ATR100.isValid(Byte.valueOf(Byte.MAX_VALUE)));

        assertTrue(NON_NEGATIVE.isValid((byte) 5));
        assertFalse(NON_NEGATIVE.isValid((byte) 4));
    }

    @Test
    public void toSingleton() {
        assertEquals((byte) -10, ATR100.singleton((byte) -10).get(ATR100));
        assertEquals((byte) 10, ATR100.singleton((byte) 10).get(ATR100));
        assertEquals(Byte.MAX_VALUE, ATR100.singleton(Byte.MAX_VALUE).get(ATR100));

        assertEquals((byte) 10, NON_NEGATIVE.singleton((byte) 10).get(NON_NEGATIVE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSingletonIAE() {
        NON_NEGATIVE.singleton((byte) 3);
    }
}
