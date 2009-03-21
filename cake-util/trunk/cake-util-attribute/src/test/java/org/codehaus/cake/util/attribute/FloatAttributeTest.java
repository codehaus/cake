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
 * Various tests for {@link FloatAttribute}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class FloatAttributeTest extends AtrStubs {
    static final FloatAttribute ATR0 = new FloatAttribute("a0",0F) {};
    static final FloatAttribute ATR1 = new FloatAttribute("a1",1F) {};
    static final FloatAttribute ATR100 = new FloatAttribute("a100", 100F) {};

    static final FloatAttribute NON_NEGATIVE = new FloatAttribute("a50", 50F) {
        @Override
        public boolean isValid(float value) {
            return value >= 5F;
        }
    };

    @Test
    public void _constructors() {
        assertEquals(0F, new FloatAttribute() {}.getDefault().floatValue(),0);
        assertEquals(0F, new FloatAttribute("a") {}.getDefaultValue(),0);
//        assertFalse(new FloatAttribute() {}.getName().equals(new FloatAttribute() {}.getName()));
//        assertFalse(new FloatAttribute(3F) {}.getName().equals(new FloatAttribute(3F) {}.getName()));
        assertTrue(new FloatAttribute("a") {}.getName().equals(new FloatAttribute("a") {}.getName()));
        assertEquals(3F, new FloatAttribute(3F) {}.getDefaultValue(),0);
        assertEquals(0F, ATR0.getDefaultValue(),0);
        assertEquals(100F, ATR100.getDefaultValue(),0);
        assertEquals(100F, ATR100.getDefault().floatValue(),0);
        assertEquals("a100", ATR100.getName());
        
        assertSame(Float.TYPE, ATR100.getType());
    }
    
    @Test
    public void checkValid() {
        ATR100.checkValid(Float.MIN_VALUE);
        ATR100.checkValid(Float.MAX_VALUE);
        ATR100.checkValid(new Float(Float.MIN_VALUE));
        ATR100.checkValid(new Float(Float.MIN_VALUE));

        NON_NEGATIVE.checkValid(5F);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE() {
        NON_NEGATIVE.checkValid(4F);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE2() {
      ATR0.checkValid(Float.NEGATIVE_INFINITY); 
    }

    @Test
    public void comparator() {
        AttributeMap wa1 = ATR1.singleton(1F);
        AttributeMap wa2 = ATR1.singleton(2F);
        AttributeMap wa22 = ATR1.singleton(2F);
        AttributeMap wa3 = ATR1.singleton(3F);
        assertEquals(0, ATR1.compare(wa2, wa2),0);
        assertEquals(0, ATR1.compare(wa2, wa22),0);
        assertEquals(0, ATR1.compare(wa22, wa2),0);
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
        assertEquals(-1F, ATR100.fromString(Integer.valueOf(-1).toString()),0);
        assertEquals(Float.MIN_VALUE, ATR100.fromString(new Float(Float.MIN_VALUE).toString()),0);
        assertEquals(Float.MAX_VALUE, ATR100.fromString(new Float(Float.MAX_VALUE).toString()),0);
    }

    @Test
    public void isValid() {
        assertTrue(ATR100.isValid(Float.MIN_VALUE));
        assertTrue(ATR100.isValid(Float.MAX_VALUE));
        assertTrue(ATR100.isValid(Float.valueOf(Float.MIN_VALUE)));
        assertTrue(ATR100.isValid(Float.valueOf(Float.MAX_VALUE)));

        assertTrue(NON_NEGATIVE.isValid(5F));
        assertFalse(NON_NEGATIVE.isValid(4F));
    }

    @Test
    public void toSingleton() {
        assertEquals(-10F, ATR100.singleton(-10F).get(ATR100),0);
        assertEquals(10F, ATR100.singleton(10F).get(ATR100),0);
        assertEquals(Float.MAX_VALUE, ATR100.singleton(Float.MAX_VALUE).get(ATR100),0);

        assertEquals(10F, NON_NEGATIVE.singleton(10F).get(NON_NEGATIVE),0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSingletonIAE() {
        NON_NEGATIVE.singleton(3F);
    }
}
