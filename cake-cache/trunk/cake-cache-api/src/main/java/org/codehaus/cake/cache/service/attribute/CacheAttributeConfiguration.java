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

/**
 * This configurer is used to determind which attributes the cache will keep for each entry.
 * For example, the following
 *
 * <p>
 * It is also possible to add custom attributes
 */
public class CacheAttributeConfiguration {

    private List<Attribute<?>> attributes = new ArrayList<Attribute<?>>();

    /**
     * @return a list of all the attributes that has been added
     */
    public List<Attribute<?>> getAllAttributes() {
        return new ArrayList<Attribute<?>>(attributes);
    }

    /**
     * Adds the specified attributes.
     * 
     * @param a
     *            the attribute(s) to add
     * @return this configuration
     */
    public CacheAttributeConfiguration add(Attribute<?>... a) {
        for (Attribute<?> aa : a) {
            if (attributes.contains(aa)) {
                throw new IllegalArgumentException("Attribute has already been added [Attribute =" + aa + "");
            }
        }
        for (Attribute<?> aa : a) {
            attributes.add(aa);
        }
        return this;
    }
}
