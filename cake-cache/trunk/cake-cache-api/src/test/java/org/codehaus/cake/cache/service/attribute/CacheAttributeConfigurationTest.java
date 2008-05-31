package org.codehaus.cake.cache.service.attribute;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

import org.codehaus.cake.attribute.Attribute;
import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.LongAttribute;
import org.junit.Test;

public class CacheAttributeConfigurationTest {

    static final Attribute A1 = new LongAttribute() {};
    static final Attribute A2 = new BooleanAttribute() {};

    @Test
    public void addAttribute() {
        CacheAttributeConfiguration c = new CacheAttributeConfiguration();
        assertSame(c, c.add(A1));
        assertSame(c, c.add(A2));
        assertEquals(2, c.getAllAttributes().size());
        assertSame(A1, c.getAllAttributes().get(0));
        assertSame(A2, c.getAllAttributes().get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addSame_IAE() {
        new CacheAttributeConfiguration().add(A1).add(A1);
    }
}
