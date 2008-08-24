package org.codehaus.cake.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Various tests for {@link CharAttribute}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: CharAttributeTest.java 590 2008-03-14 08:16:12Z kasper $
 */
public class AttributesTest {

    @Test
    public void testValidateOk() {
        AttributeMap map = new DefaultAttributeMap();
        map.put(AtrStubs.B_TRUE, true);
        map.put(AtrStubs.I_POSITIVE, 1);
        AttributeMap other = Attributes.validatedAttributeMap(map);
        assertNotSame(map, other);
        assertEquals(map, other);
        try {
            other.put(AtrStubs.I_3, 4);
            fail();
        } catch (UnsupportedOperationException ok) {
        }
        try {
            other.remove(AtrStubs.I_2);
            fail();
        } catch (UnsupportedOperationException ok) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateFail() {
        AttributeMap map = new DefaultAttributeMap();
        map.put(AtrStubs.B_TRUE, true);
        map.put(AtrStubs.I_POSITIVE, 0);
        Attributes.validatedAttributeMap(map);
    }
}
