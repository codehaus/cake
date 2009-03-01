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

import org.codehaus.cake.util.attribute.Attribute;
import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.Attributes;
import org.codehaus.cake.util.attribute.ObjectAttribute;
import org.junit.Test;
/**
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public class AttributeMapTest {

    @Test
    public void singleton() {
        Attribute a = new ObjectAttribute("key", Integer.class, 5) {};
        AttributeMap am = Attributes.singleton(a, 1);
        assertEquals(1, am.size());
        assertEquals(1, am.get(a));
    }
    // @Test
    // @Ignore
    // public void toMapEmpty() {
    // Map<Integer, AttributeMap> m = Attributes.toMap(Arrays.asList(1, 2, 3, 4));
    // assertEquals(4, m.size());
    // assertEquals(Attributes.EMPTY_ATTRIBUTE_MAP, m.get(1));
    // assertEquals(Attributes.EMPTY_ATTRIBUTE_MAP, m.get(2));
    // assertEquals(Attributes.EMPTY_ATTRIBUTE_MAP, m.get(3));
    // assertEquals(Attributes.EMPTY_ATTRIBUTE_MAP, m.get(4));
    // assertNull(m.get(5));
    // }
    // // @Test
    // public void toMap() {
    // AttributeMap am = TestUtil.dummy(AttributeMap.class);
    // Map<Integer, AttributeMap> m = Attributes.toMap(Arrays.asList(1, 2, 3, 4), am);
    // assertEquals(4, m.size());
    // assertEquals(am, m.get(1));
    // assertEquals(am, m.get(2));
    // assertEquals(am, m.get(3));
    // assertEquals(am, m.get(4));
    // assertNull(m.get(5));
    // }

}
