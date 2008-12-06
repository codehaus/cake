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
package org.codehaus.cake.internal.cache.service.memorystore;

import org.codehaus.cake.attribute.BooleanAttribute;
import org.codehaus.cake.attribute.IntAttribute;
import org.codehaus.cake.attribute.LongAttribute;

public class MemoryStoreAttributes {

    public static final BooleanAttribute IS_DISABLED = new IsDisabledAttribute();
    public static final IntAttribute MAX_SIZE = new MaximumSizeAttribute();
    public static final LongAttribute MAX_VOLUME = new MaximumVolumeAttribute();

    static class IsDisabledAttribute extends BooleanAttribute {
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        /** @return Preserves singleton property */
        private Object readResolve() {
            return IS_DISABLED;
        }
    }

    static class MaximumSizeAttribute extends IntAttribute {

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        MaximumSizeAttribute() {
            super(Integer.MAX_VALUE);
        }

        @Override
        protected String checkValidFailureMessage(Integer value) {
            return "Size must be positive, was " + value;
        }

        @Override
        public boolean isValid(int value) {
            return value >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return MAX_SIZE;
        }
    }

    static class MaximumVolumeAttribute extends LongAttribute {
        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        MaximumVolumeAttribute() {
            super(Long.MAX_VALUE);
        }

        protected String checkValidFailureMessage(Integer value) {
            return "Volume must be positive, was " + value;
        }
        @Override
        public boolean isValid(long value) {
            return value >= 0;
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return MAX_VOLUME;
        }
    }

}
