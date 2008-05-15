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
        new DefaultAttributeConfiguration(null, false, false);
    }

    @Test(expected = NullPointerException.class)
    public void constructorNPE1() {
        new DefaultAttributeConfiguration(null);
    }

    @Test
    public void constructor1() {
        DefaultAttributeConfiguration dac = new DefaultAttributeConfiguration(I_1, true, false);
        DefaultAttributeConfiguration dac2 = new DefaultAttributeConfiguration(dac);
        assertSame(dac.getAttribute(), dac2.getAttribute());
        assertEquals(dac.isHidden(), dac2.isHidden());
        assertEquals(dac.isMutable(), dac2.isMutable());
    }

    @Test
    public void testToString() {
        new DefaultAttributeConfiguration(I_1, false, false).toString();
    }

    @Test
    public void hashCodeEquals() {
        DefaultAttributeConfiguration dac = new DefaultAttributeConfiguration(I_1, true, false);
        DefaultAttributeConfiguration dac2 = new DefaultAttributeConfiguration(dac);
        assertEquals(dac, dac2);
        assertEquals(dac.hashCode(), dac2.hashCode());
        assertFalse(dac.equals(new DefaultAttributeConfiguration(new BooleanAttribute("ff", false) {}, true, false)));
        assertFalse(dac.equals(new DefaultAttributeConfiguration(I_1, false, false)));
        assertFalse(dac.equals(new DefaultAttributeConfiguration(I_1, true, true)));
    }
}
