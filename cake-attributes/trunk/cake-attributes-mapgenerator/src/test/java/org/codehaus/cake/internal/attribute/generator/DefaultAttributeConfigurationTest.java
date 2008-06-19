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
package org.codehaus.cake.internal.attribute.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.junit.Test;

public class DefaultAttributeConfigurationTest {

    private final static IntAttribute I_1 = new IntAttribute("L_1", 1) {};

    @Test(expected = NullPointerException.class)
    public void constructorNPE() {
        new DefaultAttributeConfiguration(null, false, false, false, false);
    }

    @Test(expected = NullPointerException.class)
    public void constructorNPE1() {
        new DefaultAttributeConfiguration(null);
    }

    @Test
    public void constructor1() {
        DefaultAttributeConfiguration dac = new DefaultAttributeConfiguration(I_1, true, false, false, false);
        DefaultAttributeConfiguration dac2 = new DefaultAttributeConfiguration(dac);
        assertSame(dac.getAttribute(), dac2.getAttribute());
        assertEquals(dac.allowGet(), dac2.allowGet());
        assertEquals(dac.allowPut(), dac2.allowPut());
    }

    @Test
    public void testToString() {
        new DefaultAttributeConfiguration(I_1, false, false, false, false).toString();
    }

    @Test
    public void hashCodeEquals() {
        DefaultAttributeConfiguration dac = new DefaultAttributeConfiguration(I_1, true, false, false, false);
        DefaultAttributeConfiguration dac2 = new DefaultAttributeConfiguration(dac);
        assertEquals(dac, dac2);
        assertEquals(dac.hashCode(), dac2.hashCode());
        assertFalse(dac.equals(new DefaultAttributeConfiguration(new BooleanAttribute("ff", false) {}, true, false,
                false, false)));
        assertFalse(dac.equals(new DefaultAttributeConfiguration(I_1, false, false, false, false)));
        assertFalse(dac.equals(new DefaultAttributeConfiguration(I_1, true, true, false, false)));
    }
}
