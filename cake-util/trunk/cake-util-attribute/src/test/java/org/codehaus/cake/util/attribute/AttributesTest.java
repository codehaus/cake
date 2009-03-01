package org.codehaus.cake.util.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.attribute.CharAttribute;
import org.codehaus.cake.util.attribute.DefaultAttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;
import org.junit.Test;

/**
 * Various tests for {@link CharAttribute}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class AttributesTest {

    @Test
    public void testValidateOk() {
        MutableAttributeMap map = new DefaultAttributeMap();
        map.put(AtrStubs.B_TRUE, true);
        map.put(AtrStubs.I_POSITIVE, 1);
        AttributeMap other = Attributes.validatedAttributeMap(map);
        assertNotSame(map, other);
        assertEquals(map, other);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateFail() {
        MutableAttributeMap map = new DefaultAttributeMap();
        map.put(AtrStubs.B_TRUE, true);
        map.put(AtrStubs.I_POSITIVE, 0);
        Attributes.validatedAttributeMap(map);
    }
    
    @Test
    public void from2() {
        AttributeMap map=Attributes.from(AtrStubs.B_TRUE, false, AtrStubs.B_FALSE, false);
        assertEquals(2,map.size());
        assertTrue(map.contains(AtrStubs.B_TRUE));
        assertTrue(map.contains(AtrStubs.B_FALSE));
        assertFalse(map.get(AtrStubs.B_TRUE));
        assertFalse(map.get(AtrStubs.B_FALSE));
    }
}
