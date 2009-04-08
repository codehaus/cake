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
package org.codehaus.cake.cache;

import org.codehaus.cake.util.attribute.BooleanAttribute;

class CacheAttributes {

    final static BooleanAttribute READ_ONLY = new ReadOnlyAttribute();

    /**
     * The <tt>Hits</tt> attribute indicates the number of hits for a cache element. The mapped value must be of a
     * type <tt>long</tt> between 0 and {@link Long#MAX_VALUE}.
     */
    static final class ReadOnlyAttribute extends BooleanAttribute {

        /** serialVersionUID. */
        private static final long serialVersionUID = -2353351535602223603L;

        /** Creates a new SizeAttribute. */
        ReadOnlyAttribute() {
            super("ReadOnly");
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return CacheAttributes.READ_ONLY;
        }
    }
}
