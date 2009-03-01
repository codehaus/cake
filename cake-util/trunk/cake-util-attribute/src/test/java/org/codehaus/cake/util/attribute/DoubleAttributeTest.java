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
package org.codehaus.cake.util.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.DoubleAttribute;
import org.junit.Test;
/**
 * Various tests for {@link DoubleAttribute}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class DoubleAttributeTest extends AtrStubs {
    static final DoubleAttribute ATR0 = new DoubleAttribute("a0",0D) {};
    static final DoubleAttribute ATR1 = new DoubleAttribute("a1",1D) {};
    static final DoubleAttribute ATR100 = new DoubleAttribute("a100", 100D) {};

    static final DoubleAttribute NON_NEGATIVE = new DoubleAttribute("a50", 50D) {
        @Override
        public boolean isValid(double value) {
            return value >= 5D;
        }
    };

    @Test
    public void _constructors() {
        assertEquals(0D, new DoubleAttribute() {}.getDefault().doubleValue(),0);
        assertEquals(0D, new DoubleAttribute("a") {}.getDefaultValue(),0);
//        assertFalse(new DoubleAttribute() {}.getName().equals(new DoubleAttribute() {}.getName()));
//        assertFalse(new DoubleAttribute(3D) {}.getName().equals(new DoubleAttribute(3D) {}.getName()));
        assertTrue(new DoubleAttribute("a") {}.getName().equals(new DoubleAttribute("a") {}.getName()));
        assertEquals(3D, new DoubleAttribute(3D) {}.getDefaultValue(),0);
        assertEquals(0D, ATR0.getDefaultValue(),0);
        assertEquals(100D, ATR100.getDefaultValue(),0);
        assertEquals(100D, ATR100.getDefault().doubleValue(),0);
        assertEquals("a100", ATR100.getName());
        
        assertSame(Double.TYPE, ATR100.getType());
    }
    
    @Test
    public void checkValid() {
        ATR100.checkValid(Double.MIN_VALUE);
        ATR100.checkValid(Double.MAX_VALUE);
        ATR100.checkValid(new Double(Double.MIN_VALUE));
        ATR100.checkValid(new Double(Double.MIN_VALUE));

        NON_NEGATIVE.checkValid(5D);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE() {
        NON_NEGATIVE.checkValid(4D);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE2() {
      ATR0.checkValid(Double.NEGATIVE_INFINITY); 
    }

    @Test
    public void comparator() {
        AttributeMap wa1 = ATR1.singleton(1D);
        AttributeMap wa2 = ATR1.singleton(2D);
        AttributeMap wa22 = ATR1.singleton(2D);
        AttributeMap wa3 = ATR1.singleton(3D);
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
        assertEquals(-1D, ATR100.fromString(Integer.valueOf(-1).toString()),0);
        assertEquals(Double.MIN_VALUE, ATR100.fromString(new Double(Double.MIN_VALUE).toString()),0);
        assertEquals(Double.MAX_VALUE, ATR100.fromString(new Double(Double.MAX_VALUE).toString()),0);
    }

    @Test
    public void isValid() {
        assertTrue(ATR100.isValid(Double.MIN_VALUE));
        assertTrue(ATR100.isValid(Double.MAX_VALUE));
        assertTrue(ATR100.isValid(Double.valueOf(Double.MIN_VALUE)));
        assertTrue(ATR100.isValid(Double.valueOf(Double.MAX_VALUE)));

        assertTrue(NON_NEGATIVE.isValid(5D));
        assertFalse(NON_NEGATIVE.isValid(4D));
    }

    @Test
    public void toSingleton() {
        assertEquals(-10D, ATR100.singleton(-10D).get(ATR100),0);
        assertEquals(10D, ATR100.singleton(10D).get(ATR100),0);
        assertEquals(Double.MAX_VALUE, ATR100.singleton(Double.MAX_VALUE).get(ATR100),0);

        assertEquals(10D, NON_NEGATIVE.singleton(10D).get(NON_NEGATIVE),0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSingletonIAE() {
        NON_NEGATIVE.singleton(3D);
    }
}
