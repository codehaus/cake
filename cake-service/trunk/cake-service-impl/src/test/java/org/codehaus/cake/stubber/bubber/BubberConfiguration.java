/*
 * Copyright 2008, 2009 Kasper Nielsen.
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
package org.codehaus.cake.stubber.bubber;

import org.codehaus.cake.util.attribute.AttributeMap;
import org.codehaus.cake.util.attribute.DefaultAttributeMap;
import org.codehaus.cake.util.attribute.MutableAttributeMap;
import org.codehaus.cake.util.attribute.WithAttributes;

public class BubberConfiguration implements WithAttributes {

    private final MutableAttributeMap attributes = new DefaultAttributeMap();

    public AttributeMap getAttributes() {
        return attributes;
    }

    public BubberConfiguration setFooFoo(int value) {
        attributes.put(BubberService.FOOFOO, value);
        return this;
    }
}
