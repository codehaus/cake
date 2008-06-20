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
package org.codehaus.cake.cache.test.attributes;

import org.codehaus.cake.attribute.ObjectAttribute;

public class StringAttribute1 extends ObjectAttribute<String> {

    public static final StringAttribute1 INSTANCE = new StringAttribute1();

    /** serialVersionUID. */
    private static final long serialVersionUID = 1821856356464961171L;

    private StringAttribute1() {
        super("StringAttribute1", String.class, "string1");
    }
}
