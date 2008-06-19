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
package org.codehaus.cake.internal.attribute.generator;

import org.codehaus.cake.attribute.Attribute;

public class DefaultAttributeConfiguration implements AttributeConfiguration {
    private final Attribute a;

    private boolean allowGet;
    private boolean allowPut;
    private boolean isPrivate;
    private boolean isFinal;

    // public DefaultAttributeConfiguration(Attribute a, boolean isMutable, boolean isHidden) {
    // this(a, !isHidden, !isHidden, !isMutable, true);
    // }

    public DefaultAttributeConfiguration(Attribute a, boolean allowGet, boolean allowPut, boolean isFinal,
            boolean isPrivate) {
        if (a == null) {
            throw new NullPointerException("a is null");
        }
        this.a = a;
        this.allowGet = allowGet;
        this.allowPut = allowPut;
        this.isFinal = isFinal;
        this.isPrivate = isPrivate;
    }

    public DefaultAttributeConfiguration(DefaultAttributeConfiguration other) {
        this(other.a, other.allowGet, other.allowPut, other.isFinal, other.isPrivate);
    }

    @Override
    public boolean equals(Object obj) {
        DefaultAttributeConfiguration c = (DefaultAttributeConfiguration) obj;
        return c.a == a && c.allowGet == allowGet && c.allowPut == allowPut && c.isFinal == isFinal
                && c.isPrivate == isPrivate;
    }

    public Attribute getAttribute() {
        return a;
    }

    @Override
    public int hashCode() {
        return a.hashCode();
    }

    public boolean allowGet() {
        return allowGet;
    }

    public String toString() {
        return a + ", allowGet=" + allowGet + ", allowPut=" + allowPut + ", isPrivate=" + isPrivate + ", isFinal="
                + isFinal;
    }

    public boolean allowPut() {
        return allowPut;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isFinal() {
        return isFinal;
    }
}
