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

import static org.codehaus.cake.test.util.TestUtil.assertIsSerializable;
import static org.codehaus.cake.test.util.TestUtil.serializeAndUnserialize;
import static org.codehaus.cake.util.attribute.Attributes.EMPTY_ATTRIBUTE_MAP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

/**
 * Tests {@link Attributes#EMPTY_ATTRIBUTE_MAP}.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class AttributeMaps_EmptyMapTest extends AbstractAttributeMapTest {

    @Test
    public void getters() {
        assertEquals("15", EMPTY_ATTRIBUTE_MAP.get(O_2));
        assertEquals("goo", EMPTY_ATTRIBUTE_MAP.get(O_2, "goo"));
        assertFalse(EMPTY_ATTRIBUTE_MAP.get(B_FALSE));
        assertTrue(EMPTY_ATTRIBUTE_MAP.get(B_FALSE, true));
        assertEquals((byte) 1, EMPTY_ATTRIBUTE_MAP.get(B_1));
        assertEquals((byte) 2, EMPTY_ATTRIBUTE_MAP.get(B_2));
        assertEquals((byte) 100, EMPTY_ATTRIBUTE_MAP.get(B_1, (byte) 100));
        assertEquals('\u0001', EMPTY_ATTRIBUTE_MAP.get(C_1));
        assertEquals('\u0002', EMPTY_ATTRIBUTE_MAP.get(C_2));
        assertEquals('f', EMPTY_ATTRIBUTE_MAP.get(C_1, 'f'));
        assertEquals(1.5d, EMPTY_ATTRIBUTE_MAP.get(D_1), 0);
        assertEquals(2.5d, EMPTY_ATTRIBUTE_MAP.get(D_2), 0);
        assertEquals(100.5d, EMPTY_ATTRIBUTE_MAP.get(D_1, 100.5d), 0);
        assertEquals(1.5f, EMPTY_ATTRIBUTE_MAP.get(F_1), 0);
        assertEquals(2.5f, EMPTY_ATTRIBUTE_MAP.get(F_2), 0);
        assertEquals(100.5f, EMPTY_ATTRIBUTE_MAP.get(F_2, 100.5f), 0);
        assertEquals(1, EMPTY_ATTRIBUTE_MAP.get(I_1));
        assertEquals(2, EMPTY_ATTRIBUTE_MAP.get(I_2));
        assertEquals(100, EMPTY_ATTRIBUTE_MAP.get(I_1, 100));
        assertEquals(1l, EMPTY_ATTRIBUTE_MAP.get(L_1));
        assertEquals(100l, EMPTY_ATTRIBUTE_MAP.get(L_2, 100l));
        assertEquals((short) 1, EMPTY_ATTRIBUTE_MAP.get(S_1));
        assertEquals((short) 2, EMPTY_ATTRIBUTE_MAP.get(S_2));
        assertEquals((short) 100, EMPTY_ATTRIBUTE_MAP.get(S_1, (short) 100));

    }

    /**
     * Tests that EMPTY_MAP is serializable and maintains the singleton property.
     * 
     * @throws Exception
     *             something went wrong
     */
    @Test
    public void serialization() throws Exception {
        assertIsSerializable(EMPTY_ATTRIBUTE_MAP);
        assertSame(EMPTY_ATTRIBUTE_MAP, serializeAndUnserialize(EMPTY_ATTRIBUTE_MAP));
    }

    @Test
    public void various() {
        assertFalse(EMPTY_ATTRIBUTE_MAP.contains(O_2));
        // assertFalse(EMPTY_ATTRIBUTE_MAP.containsValue(KEY));
        assertEquals(0, EMPTY_ATTRIBUTE_MAP.entrySet().size());
        assertEquals(new DefaultAttributeMap(), EMPTY_ATTRIBUTE_MAP);
        assertEquals(new DefaultAttributeMap().hashCode(), EMPTY_ATTRIBUTE_MAP.hashCode());
        assertEquals(0, EMPTY_ATTRIBUTE_MAP.size());
        assertTrue(EMPTY_ATTRIBUTE_MAP.isEmpty());
        assertEquals(0, EMPTY_ATTRIBUTE_MAP.attributes().size());
        assertEquals(0, EMPTY_ATTRIBUTE_MAP.values().size());
        assertFalse(EMPTY_ATTRIBUTE_MAP.equals(new HashSet()));
        map = EMPTY_ATTRIBUTE_MAP;
        super.noPut();
        assertEquals(B_TRUE.getDefaultValue(), map.remove(B_TRUE));
        assertEquals(B_2.getDefaultValue(), map.remove(B_2));
        assertEquals(C_2.getDefaultValue(), map.remove(C_2));
        assertEquals(D_2.getDefaultValue(), map.remove(D_2), 0);
        assertEquals(F_2.getDefaultValue(), map.remove(F_2), 0);
        assertEquals(I_2.getDefaultValue(), map.remove(I_2));
        assertEquals(O_2.getDefault(), map.remove(O_2));
        assertEquals(L_2.getDefaultValue(), map.remove(L_2));
        assertEquals(S_2.getDefaultValue(), map.remove(S_2));
        assertEquals("{}", map.toString());
        map.clear();// no exceptions
    }
}
