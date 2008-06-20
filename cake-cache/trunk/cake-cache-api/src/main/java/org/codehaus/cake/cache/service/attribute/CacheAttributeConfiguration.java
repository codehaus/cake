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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.cake.attribute.Attribute;

public class CacheAttributeConfiguration {

    private List attributes = new ArrayList();

    public List getAllAttributes() {
        return new ArrayList(attributes);
    }

    public CacheAttributeConfiguration add(Attribute... a) {
        for (Attribute aa : a) {
            if (attributes.contains(aa)) {
                throw new IllegalArgumentException("Attribute has already been added [Attribute =" + aa + "");
            }
        }
        for (Attribute aa : a) {
            attributes.add(aa);
        }
        return this;
    }
}
