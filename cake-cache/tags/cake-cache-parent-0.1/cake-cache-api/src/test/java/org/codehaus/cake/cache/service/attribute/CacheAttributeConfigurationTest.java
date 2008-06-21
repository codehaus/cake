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
