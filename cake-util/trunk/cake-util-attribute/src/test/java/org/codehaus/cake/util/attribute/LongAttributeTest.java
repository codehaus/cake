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
 * Various tests for {@link LongAttribute}.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class LongAttributeTest extends AtrStubs {
    static final LongAttribute ATR0 = new LongAttribute("a0",0L) {};
    static final LongAttribute ATR1 = new LongAttribute("a1",1L) {};
    static final LongAttribute ATR100 = new LongAttribute("a100", 100L) {};

    static final LongAttribute NON_NEGATIVE = new LongAttribute("a50", 50L) {
        @Override
        public boolean isValid(long value) {
            return value >= 5L;
        }
    };

    @Test
    public void _constructors() {
        assertEquals(0L, new LongAttribute() {}.getDefault().longValue());
        assertEquals(0L, new LongAttribute("a") {}.getDefaultValue());
//        assertFalse(new LongAttribute() {}.getName().equals(new LongAttribute() {}.getName()));
//        assertFalse(new LongAttribute(3L) {}.getName().equals(new LongAttribute(3L) {}.getName()));
        assertTrue(new LongAttribute("a") {}.getName().equals(new LongAttribute("a") {}.getName()));
        assertEquals(3L, new LongAttribute(3L) {}.getDefaultValue());
        assertEquals(0L, ATR0.getDefaultValue());
        assertEquals(100L, ATR100.getDefaultValue());
        assertEquals(100L, ATR100.getDefault().longValue());
        assertEquals("a100", ATR100.getName());
        
        assertSame(Long.TYPE, ATR100.getType());
    }
    
    @Test
    public void checkValid() {
        ATR100.checkValid(Long.MIN_VALUE);
        ATR100.checkValid(Long.MAX_VALUE);
        ATR100.checkValid(new Long(Long.MIN_VALUE));
        ATR100.checkValid(new Long(Long.MIN_VALUE));

        NON_NEGATIVE.checkValid(5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkValidIAE() {
        NON_NEGATIVE.checkValid(4L);
    }
    
    @Test
    public void comparator() {
        AttributeMap wa1 = ATR1.singleton(1L);
        AttributeMap wa2 = ATR1.singleton(2L);
        AttributeMap wa22 = ATR1.singleton(2L);
        AttributeMap wa3 = ATR1.singleton(3L);
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
        assertEquals(-1L, ATR100.fromString(Integer.valueOf(-1).toString()));
        assertEquals(Long.MIN_VALUE, ATR100.fromString(new Long(Long.MIN_VALUE).toString()));
        assertEquals(Long.MAX_VALUE, ATR100.fromString(new Long(Long.MAX_VALUE).toString()));
    }

    @Test
    public void isValid() {
        assertTrue(ATR100.isValid(Long.MIN_VALUE));
        assertTrue(ATR100.isValid(Long.MAX_VALUE));
        assertTrue(ATR100.isValid(Long.valueOf(Long.MIN_VALUE)));
        assertTrue(ATR100.isValid(Long.valueOf(Long.MAX_VALUE)));

        assertTrue(NON_NEGATIVE.isValid(5L));
        assertFalse(NON_NEGATIVE.isValid(4L));
    }

    @Test
    public void toSingleton() {
        assertEquals(-10L, ATR100.singleton(-10L).get(ATR100));
        assertEquals(10L, ATR100.singleton(10L).get(ATR100));
        assertEquals(Long.MAX_VALUE, ATR100.singleton(Long.MAX_VALUE).get(ATR100));

        assertEquals(10L, NON_NEGATIVE.singleton(10L).get(NON_NEGATIVE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSingletonIAE() {
        NON_NEGATIVE.singleton(3L);
    }
}
