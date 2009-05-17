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
package org.codehaus.cake.management;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used to mark methods that should be exposed via JMX. Should only be used on JavaBean getters or
 * setters. If bean has both a setter and a getter for a specific attribute, only the getter or the setter should be
 * annotated with this annotation, not both.
 * 
 * It is possible to expose attributes as both Read-only, Write-only or as Read-Writeable by using these simple rules.
 * 
 * Read-Write: If this annotation is used on the setter and the attribute has a valid getter for the attribute
 * Read-Only: If this annotation is used on the getter, the attribute will be exposed as read only even if a valid
 * setter is available for the attribute. Write-only, if this annotation is used on the setter, and no valid getter
 * exists. If a getter exists {@link #isWriteOnly()} must be set to true
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ManagedAttribute.java 225 2008-11-30 20:53:08Z kasper $
 */
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagedAttribute {

    /** The name of the attribute. */
    String defaultValue() default "";

    /** The description of the atttribute (optionally). */
    String description() default "";

    /** Whether or not this attribute is write only. Should only be used on a setter. */
    boolean isWriteOnly() default false;
}
